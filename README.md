# REST Messenger
Easily create and expose a Spring REST-based API implementation, without losing strong Java message typing, improving simplicity and robustness of your implementations.

It's so easy to use, you can start implementing your APIs in under 5 minutes. 

Yes, really. Just see the [How](#how) section.

## Why?

**If you don't care about philosophical blah blah, just skip this section. You've been warned. :-)**

It is very often the case that the standard REST conventions of providing endpoints for CRUD (**C**reate with `POST`, **R**ead with `GET`, etc.) don't fit with some use cases. This is especially true when the API you are trying to create is a microservice.

In many such cases (or even if you feel like it's appropriate to break the REST convention), you'd need to implement an API that is robust, easy to understand, and easy to use for the clients of your API.

Going to full blown Enterprise Integration frameworks like Spring Integration or Apache Camel is overkill in a *lot* of scenarios. In the opinions of people a lot smarter than I, you should always strive for simple solutions to complex problems, not complicated solutions to simple problems. Use those frameworks when they make super complex things simpler, not straightforward use cases complicated.

In most cases, projects are looking for a very simple and straightforward way to expose their API to outside clients, be they truly external or simply a microservice which is part of an enterprise integration effort.

One of the fairly standard integration design patterns (i.e, approaches) for creating an API is some variable of the Request-Response Pattern. It has many advantages (and a minor disadvantage), but the main benefit (in my experience) is that the pattern, by *its nature of focus and simplicity*, pushes you to produce very clean and easy to use APIs. It forces you to focus on a single request/message (just an POJO) with specific properties that are needed to accomplish some outcome, represented by a response (another POJO).

However, when it comes to implementing this pattern in Java, especially when receiving these requests over HTTP as JSON, there are some challenges:
* We want Java's strong *compile-time* type verification benefits but a serialized generic message coming in as JSON doesn't contain any Java typing information. It's just a string. Creating a separate endpoint for each type of message creates more boilerplate code, increasing complexity and uncertainty of which endpoint to use when and how to use it, especially as the API grows in size.
* We have have a single endpoint accept all kinds of messages, but we don't want to have to update some giant `switch` statement every time we add a new message to our API. This is tedious and error-prone.
* We need an easy and robust mechanism to connect these messages to their classes that process them.
* We need a *compile-time* mechanism to ensure that our message and message response POJOs are compatible and that there is no way to make a mistake of which message is corresponds to which response.

The REST-MESSENGER framework takes care of all of these challenges in a lightweight manner with *zero* effort on your part.

Blah blah blah... Just tell me how to use it already!

Ok, ok. If you are that impatient, just jump to the How section.

## Features
* Single (and simple!) line of code to configure
* (*at startup*) Automatic discovery of message processors in your system 
* (*at startup*) Automatic validation that message processors are properly wired to support correct message routing to their strongly typed message processors 
* Automatic conversion of JSON-based messages to a concrete message class defined in your system
* Automatic routing of messages to their specific, strongly typed message processors
* (*at startup*) Automatic validation of message and response POJOs' ability to correctly deserialize into JSON, letting you find out right away that something is wrong instead of much later into the application use when that particular message is received

## Dependecies
* Java 8 and above
* Spring Boot 1.x and above

## How
Set your stopwatch. This will take 5 minutes.

2 simple steps to configure the framework.

**Step 1**: Start with a simple Spring Boot configured `pom.xml` and add `rest-messenger` dependency to it.

For example:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.3.RELEASE</version>
    <relativePath/>
  </parent>

  <groupId>com.clearlydecoded</groupId>
  <artifactId>rest-messenger-demo</artifactId>
  <packaging>war</packaging>
  <version>1.0.0</version>

  <properties>
    <java.version>1.8</java.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- ADD THIS DEPENDENCY -->
    <dependency>
      <groupId>com.clearlydecoded</groupId>
      <artifactId>rest-messenger</artifactId>
      <version>1.0.0</version>
    </dependency>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>

</project>
```

**Step 2**: Create `SpringRestMessenger` as a bean in a `@Configuration` class. (You can do this directly in the class annotated with `@SpringBootApplication` instead if you choose.)

For example:
```java
package com.clearlydecoded.messenger.demo;

import com.clearlydecoded.messenger.rest.SpringRestMessenger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  protected SpringRestMessenger createSpringRestMessenger(ApplicationContext springContext) {
    return new SpringRestMessenger(springContext);
  }
}
```

✅ That's it! Configuration is done!

Now, we can start creating message, message response, and message processor classes.

**Step 1**: Define a POJO you expect as a message response (you'll see why response is first in a second).

It must implement `com.clearlydecoded.messenger.MessageResponse` interface.

For example:
```java
package com.clearlydecoded.messenger.demo.message;

import com.clearlydecoded.messenger.MessageResponse;
import java.util.Objects;

public class GreetMeMessageResponse implements MessageResponse {

  /**
   * The actual data we want as the response.
   */
  private String greeting;

  /**
   * MUST at least have a no-arg constructor.
   **/
  public GreetMeMessageResponse() { }

  public GreetMeMessageResponse(String greeting) { this.greeting = greeting; }

  public String getGreeting() { return greeting; }

  public void setGreeting(String greeting) { this.greeting = greeting; }

  @Override
  public boolean equals(Object o) { // cut out for brevity }

  @Override
  public int hashCode() { // cut out for brevity }

  @Override
  public String toString() { // cut out for brevity }
}
```

**Step 2**: Define a POJO you expect to receive as a message request.

It must implement `com.clearlydecoded.messenger.Message` interface that is typed with the
previously defined message response POJO. In this case, `GreetMeMessageResponse`.

For example:
```java
package com.clearlydecoded.messenger.demo.message;

import com.clearlydecoded.messenger.Message;
import java.util.Objects;

public class GreetMeMessage implements Message<GreetMeMessageResponse> {

  /**
   * Set up the required message type identifier like so.
   * There are other ways to define it, but this approach is nice.
   */
  public static final String TYPE = "GreetMe";
  private final String type = TYPE;

  /**
   * The actual data we want to pass to the processor.
   */
  private String myName;

  /**
   * MUST at least have a no-arg constructor.
   */
  public GreetMeMessage() { }

  public GreetMeMessage(String myName) { this.myName = myName; }

  public String getMyName() { return myName; }

  public void setMyName(String myName) { this.myName = myName; }

  @Override // This is a required interface method
  public String getType() { return type; }

  @Override
  public boolean equals(Object o) { // cut out for brevity }

  @Override
  public int hashCode() { // cut out for brevity }

  @Override
  public String toString() { // cut out for brevity }
}
```

**Step 3**: Define message processor class.

It must implement `com.clearlydecoded.messenger.MessageProcessor` interface, be annotated with either `@Service`, `@Component`, etc. for Spring to discover it, and be typed in terms of the previously defined message and message response POJOs. In this case, `GreetMeMessage` and `GreetMeMessageResponse`.

For example:
```java
package com.clearlydecoded.messenger.demo.processor;

import com.clearlydecoded.messenger.MessageProcessor;
import com.clearlydecoded.messenger.demo.message.GreetMeMessage;
import com.clearlydecoded.messenger.demo.message.GreetMeMessageResponse;
import org.springframework.stereotype.Service;

@Service // Must have this annotation for Spring to discover the class
public class GreetMeMessageProcessor implements
    MessageProcessor<GreetMeMessage, GreetMeMessageResponse> {

  @Override
  public GreetMeMessageResponse process(GreetMeMessage message) {

    // This is where you write the actual business logic
    return new GreetMeMessageResponse("Hello " + message.getMyName());
  }

  @Override
  public String getCompatibleMessageType() {
    // SEE! Defining that TYPE as public static final paid off!
    // You certainly CAN just hardcode a string here, but this is more robust.
    // Don't worry, if you mess this up, the validation at startup will catch it
    return GreetMeMessage.TYPE;
  }

  @Override
  public Class<GreetMeMessage> getCompatibleMessage() {
    return GreetMeMessage.class; // not possible to mess up here
  }

  @Override
  public Class<GreetMeMessageResponse> getCompatibleMessageResponseClassType() {
    return GreetMeMessageResponse.class; // not possible to mess up here
  }
}
```
Now, *"rinse and repeat"* as you expand your API.

✅ That's it! 2 simple steps to configure. 3 simple classes for a very clearly defined endpoint API.

Now, start up the application with a simple maven command:
```bash
mvn spring-boot:run
```
Look closely at the logs. Depending on the configured logging level, at the very least, you should see something like this:

```bash
...DefaultMessageProcessorRegistry: Registered [GreetMeMessageProcessor] to process
 messages of type [GreetMeMessage] identified by [GreetMe]
...
...SpringRestMessenger: REST-MESSENGER endpoint configured for URI: /process. To 
configure custom URI, supply 'com.clearlydecoded.messenger.endpoint.uri' property.
```

To test this message/response with a browser, download a REST client plugin (e.g., [Restlet Client for Chrome](https://chrome.google.com/webstore/detail/restlet-client-rest-api-t/aejoelaoggembcahagimdiliamlcdmfm)) and send the following in the body of the `POST` request to the `/process` URI:

```json
{
  "type": "GreetMe", 
  "myName": "Yaakov"
}
```

The server will respond with:
```json
{"greeting":"Hello Yaakov"}
```

## Fully Functional Demo Application
To see full source code of an application using rest-messenger for demo purposes, see the [rest-messenger-demo](https://github.com/clearlydecoded/rest-messenger-demo) repository.

## Additional Info

* Want to contribute or have a question? File a new issue or comment on an existing one.
* Want to make your POJOs even cleaner, check out [Project Lombok](https://projectlombok.org/). You'll never write another getter/setter by hand again. The code is SO much cleaner looking. For an example, take a look at [`MaxSugarOrder.java`](https://github.com/clearlydecoded/rest-messenger-demo/blob/master/src/main/java/com/clearlydecoded/messenger/demo/message/MaxSugarOrder.java) and [`MaxSugerOrderResponse.java`](https://github.com/clearlydecoded/rest-messenger-demo/blob/master/src/main/java/com/clearlydecoded/messenger/demo/message/MaxSugarOrderResponse.java) in the [rest-messenger-demo](https://github.com/clearlydecoded/rest-messenger-demo) repository.

