/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.messenger.rest;

import com.clearlydecoded.messenger.Message;
import com.clearlydecoded.messenger.MessageProcessor;
import com.clearlydecoded.messenger.MessageProcessorRegistry;
import com.clearlydecoded.messenger.MessageResponse;
import com.clearlydecoded.messenger.discovery.SpringMessageProcessorRegistryFactory;
import com.clearlydecoded.messenger.documentation.RestMessageProcessorDocumentation;
import com.clearlydecoded.messenger.documentation.RestMessageProcessorDocumentationGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * {@link SpringRestMessenger} class is a REST controller which wires itself to listen on
 * <code>'/process'</code> URI, or a custom configured URI (through
 * <code>'com.clearlydecoded.messenger.endpoint.uri'</code> property) and invokes the
 * auto-discovered message processors which implement the {@link MessageProcessor} interface and are
 * injected into the Spring Context either manually or by using Spring annotations such as {@link
 * org.springframework.stereotype.Service}, {@link org.springframework.stereotype.Component}, etc.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
@Log
public class SpringRestMessenger {

  /**
   * URI of the endpoint. Defaults to '/process' unless configured otherwise in Spring-based
   * properties.
   */
  @Value("${com.clearlydecoded.messenger.endpoint.uri:process}")
  @Setter
  private String endpointUri;

  /**
   * Spring request mapping instance to use for wiring up request mappings for this endpoint.
   */
  @SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection",
      "SpringJavaAutowiredMembersInspection"})
  @Autowired
  @Setter
  private RequestMappingHandlerMapping requestMappingHandlerMapping;

  /**
   * Message processor registry used to look up message processors based on the type identifier of a
   * message received through the REST call.
   */
  private MessageProcessorRegistry processorRegistry;

  /**
   * List of documentation classes that can be used to describe inputs/outputs of all the processors
   * registered in the system.
   */
  private List<RestMessageProcessorDocumentation> processorDocs;

  /**
   * Constructor.
   * <p>Use this constructor unless your application needs direct access to the {@link
   * MessageProcessorRegistry}. (Hint: in majority of cases, you don't need {@link
   * MessageProcessorRegistry} to be available directly in the Spring Context.</p>
   *
   * @param springContext Spring Application Context.
   */
  public SpringRestMessenger(ApplicationContext springContext) {
    this(SpringMessageProcessorRegistryFactory
        .discoverMessageProcessorsAndCreateRegistry(springContext));
  }

  /**
   * Constructor.
   * <p>Use this constructor if you manually created {@link MessageProcessorRegistry}. Usually, you
   * would want to do this if you manually injected {@link MessageProcessorRegistry} into Spring
   * Context for some reason (e.g., you want to look up registered message processors in your own
   * code, outside of {@link SpringRestMessenger}). To create a {@link MessageProcessorRegistry}
   * populated with automatically discovered message processors, use the {@link
   * SpringMessageProcessorRegistryFactory} class.</p>
   *
   * @param processorRegistry Message processor registry used to look up message processors based
   * on the type identifier of a message received through the REST call.
   */
  public SpringRestMessenger(MessageProcessorRegistry processorRegistry) {
    this.processorRegistry = processorRegistry;

    processorDocs = new ArrayList<>();

    try {
      // Generate docs for message processors
      for (MessageProcessor messageProcessor : processorRegistry.getProcessors()) {
        RestMessageProcessorDocumentation documentation = RestMessageProcessorDocumentationGenerator
            .generateDocumentation(messageProcessor);
        processorDocs.add(documentation);
      }
    } catch (Exception e) {
      String logMessage = "Generating message processor documentation failed. However, this will"
          + " NOT affect the functionality of the rest of the application. While it is most"
          + " probably a bug, the application will still function correctly because it only"
          + " affects the documentation of the REST API.";
      log.severe(logMessage);
    }
  }

  /**
   * Creates mapping for the <code>process</code> method with the <code>endpointUri</code>.
   *
   * @throws Exception If Java reflection can't find the appropriate method on the controller.
   */
  @PostConstruct
  private void createRequestMapping() throws Exception {

    String logMessage = "REST-MESSENGER endpoint configured for URI: /" + endpointUri + ". To"
        + " configure custom URI, supply 'com.clearlydecoded.messenger.endpoint.uri' property.";
    log.info(logMessage);

    // Wire up request mapping for message processing
    RequestMappingInfo messageProcessingRequestMappingInfo = RequestMappingInfo
        .paths(endpointUri)
        .methods(RequestMethod.POST)
        .consumes(MediaType.APPLICATION_JSON_VALUE)
        .produces(MediaType.APPLICATION_JSON_VALUE)
        .build();
    requestMappingHandlerMapping.registerMapping(messageProcessingRequestMappingInfo, this,
        SpringRestMessenger.class.getDeclaredMethod("process", String.class));

    // Wire up request mapping for output of processor docs
    //    RequestMappingInfo getProcessorDocsRequestMappingInfo = RequestMappingInfo
    //        .paths(endpointUri)
    //        .methods(RequestMethod.GET)
    //        .produces(MediaType.TEXT_HTML_VALUE)
    //        .build();
    //    requestMappingHandlerMapping.registerMapping(getProcessorDocsRequestMappingInfo, this,
    //        SpringRestMessenger.class.getDeclaredMethod("getProcessorDocs", Model.class));
  }

  /**
   * Processes messages that are sent as part of the request body.
   *
   * @param message JSON string representing the message to process.
   * @return Message response, serialized as a JSON string.
   */
  @ResponseBody
  @SuppressWarnings("unchecked")
  private <MessageT extends Message<MessageResponseT>, MessageResponseT extends MessageResponse>
  String process(@RequestBody String message) {

    log.fine("Full message string received: " + message);

    // Create Jackson JSON/Object mapper
    ObjectMapper mapper = new ObjectMapper();

    // Deserialize message just to find out the type identifier
    MessageWithJustType messageWithJustType;
    try {
      messageWithJustType = mapper.readValue(message, MessageWithJustType.class);
    } catch (IOException e) {
      String logMessage = "Error deserializing message type identifier from JSON: " + message
          + ". Please verify that the message being sent contains the [type] property and that"
          + " the message is valid JSON.";
      log.severe(logMessage);
      throw new IllegalArgumentException(logMessage, e);
    }
    String messageType = messageWithJustType.getType();
    log.fine("Identified message with type identifier: [" + messageType + "].");

    // Look up message processor for this message type identifier
    MessageProcessor<MessageT, MessageResponseT> messageProcessor =
        (MessageProcessor<MessageT, MessageResponseT>) processorRegistry
            .getProcessorFor(messageType);

    // Throw exception if no such message processor is found
    if (messageProcessor == null) {
      String logMessage = "No registered message processor found for message type [" + messageType
          + "]. If you think you have a MessageProcessor class implemented for this message type,"
          + " check that your message processor class is injected into the Spring Context either"
          + " by manually injecting it (in an @Configuration class) or by having the message"
          + " processor class annotated with either @Service, @Component, etc.";
      log.severe(logMessage);

      throw new IllegalArgumentException(
          "Message with type [" + messageType + "] is not supported.");
    }

    // Retrieve message processor's message class type
    Class<MessageT> processorMessageClassType = messageProcessor
        .getCompatibleMessage();

    // Attempt to deserialize string message using message processor's message class type
    MessageT javaTypedMessage;
    try {
      javaTypedMessage = mapper.readValue(message, processorMessageClassType);
    } catch (IOException e) {
      String errorMessage = "Error deserializing " + message + " to " + processorMessageClassType
          + ". Please verify that the message is valid JSON, no unrelated properties are included,"
          + " and that the required message properties are present.";

      // Add to the log message a hint for server-side developers
      String logMessage = " Also, verify that the [" + processorMessageClassType + "] contains"
          + " correct Jackson annotations for properties that can be ignored and properties that"
          + " are required.";
      log.severe(errorMessage + logMessage);

      throw new IllegalArgumentException(errorMessage, e);
    }

    log.fine("Message about to be processed: " + javaTypedMessage);

    // Execute de-serialized message
    MessageResponseT messageResponse = messageProcessor.process(javaTypedMessage);

    // Generate raw JSON from message response
    String response;
    try {
      response = mapper.writeValueAsString(messageResponse);
    } catch (JsonProcessingException e) {
      String logMessage = "Error serializing " + messageResponse + " to JSON";
      log.severe(logMessage);
      throw new IllegalArgumentException(logMessage, e);
    }

    log.fine("Full message response string to be sent: " + response);

    return response;
  }

  //  /**
  //   * Directs the request to the HTML page that displays all the documentation for the system
  //   * discovered message processors.
  //   *
  //   * @param model Shared model with the view.
  //   * @return ID of the page to serve to the client.
  //   */
  //  private String getProcessorDocs(Model model) {
  //
  //    // Store processor docs in the model and send control to documentation HTML page
  //    model.addAttribute("docs", processorDocs);
  //    return "SpringRestProcessorDocumentation";
  //  }
}
