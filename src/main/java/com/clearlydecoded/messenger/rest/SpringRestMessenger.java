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
import com.clearlydecoded.messenger.documentation.RestProcessorDocumentation;
import com.clearlydecoded.messenger.documentation.RestProcessorDocumentationGenerator;
import com.clearlydecoded.messenger.exception.BadMessageFormatException;
import com.clearlydecoded.messenger.exception.MessageTypeNotSupportedException;
import com.clearlydecoded.messenger.exception.ValidationErrorInfo;
import com.clearlydecoded.messenger.exception.ValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
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
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
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
  private List<RestProcessorDocumentation> processorDocs;

  /**
   * String framework provided validator for JSR-380 validation.
   */
  @Autowired
  @Setter
  private Validator validator;

  /**
   * Application's context path as is registered by the servlet container.
   */
  @Value("#{servletContext.contextPath}")
  private String servletContextPath;

  /**
   * Spring application name, possibly wired in by the 'spring.application.name' property.
   * Defaults to an empty string.
   */
  @Value("${spring.application.name:}")
  private String springApplicationName;

  /**
   * Constructor.
   *
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
   *
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
        RestProcessorDocumentation documentation = RestProcessorDocumentationGenerator
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
        .consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .build();
    requestMappingHandlerMapping.registerMapping(messageProcessingRequestMappingInfo, this,
        SpringRestMessenger.class.getDeclaredMethod("process", String.class));

    // Wire up request mapping for output of processor docs through an HTML page
    RequestMappingInfo getProcessorDocsRequestMappingInfo = RequestMappingInfo
        .paths(endpointUri)
        .methods(RequestMethod.GET)
        .produces(MediaType.TEXT_HTML_VALUE)
        .build();
    requestMappingHandlerMapping.registerMapping(getProcessorDocsRequestMappingInfo, this,
        SpringRestMessenger.class.getDeclaredMethod("getProcessorDocs", Model.class));

    // Wire up request mapping for output of processor docs through REST endpoint
    RequestMappingInfo getJsonProcessorDocsRequestMappingInfo = RequestMappingInfo
        .paths(endpointUri)
        .methods(RequestMethod.GET)
        .consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .build();
    requestMappingHandlerMapping.registerMapping(getJsonProcessorDocsRequestMappingInfo, this,
        SpringRestMessenger.class.getDeclaredMethod("getJsonProcessorDocs"));

    // Wire up request mapping for output of processor docs through REST endpoint
    // Should be able to request directly in browser
    RequestMappingInfo getBrowserJsonProcessorDocsRequestMappingInfo = RequestMappingInfo
        .paths(endpointUri + ".json")
        .methods(RequestMethod.GET)
        .produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .build();
    requestMappingHandlerMapping.registerMapping(getBrowserJsonProcessorDocsRequestMappingInfo,
        this, SpringRestMessenger.class.getDeclaredMethod("getJsonProcessorDocs"));
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

    // Extract type ID of the message
    String messageType = extractMessageType(message);
    log.fine("Identified message with type identifier: [" + messageType + "].");

    // Look up message processor for this message type identifier
    MessageProcessor<MessageT, MessageResponseT> messageProcessor = lookupMessageProcessor(
        messageType);

    // Extract concretely typed message object
    MessageT javaTypedMessage = extractConcreteMessage(message, messageProcessor);
    log.fine("Message about to be processed: " + javaTypedMessage);

    // Validate message if declared with @Valid
    validateMessage(messageProcessor, javaTypedMessage);

    // Execute de-serialized message
    MessageResponseT messageResponse = messageProcessor.process(javaTypedMessage);

    // Generate JSON string as a response
    String response = generateStringResponse(messageResponse);
    log.fine("Full message response string to be sent: " + response);

    return response;
  }

  /**
   * Directs the request to the HTML page that displays all the documentation for the system
   * discovered message processors.
   *
   * @param model Shared model with the view.
   * @return ID of the page to serve to the client.
   */
  private String getProcessorDocs(Model model) {

    model.addAttribute("docs", processorDocs);
    model.addAttribute("endpointUri", endpointUri);
    model.addAttribute("servletContextPath", servletContextPath);
    model.addAttribute("messageMappedModels", generateMessageMappedModels());

    String appName = springApplicationName.trim();
    model.addAttribute("appName", appName.equals("") ? "unspecified" : appName);

    return "SpringRestProcessorDocumentation";
  }

  /**
   * @return Map where the key is the <code>compatibleMessageType</code> and the value is the
   * message model string.
   */
  private Map<String, String> generateMessageMappedModels() {
    return processorDocs.stream().collect(Collectors
        .toMap(RestProcessorDocumentation::getMessageId,
            RestProcessorDocumentation::getMessageModel));
  }

  /**
   * @return List of {@link RestProcessorDocumentation}s as a REST endpoint, i.e., returns docs as
   * JSON.
   */
  @ResponseBody
  private List<RestProcessorDocumentation> getJsonProcessorDocs() {
    return processorDocs;
  }

  /**
   * @param messageType String-based message type ID that uniquely identifies message processor.
   * @param <MessageT> Message type.
   * @param <MessageResponseT> Message response type.
   * @return Concretely typed message processor that handles messages with string-based type ID
   * provided by <code>messageType</code>.
   * @throws IllegalArgumentException If message processor for the provided <code>messageType</code>
   * is not found.
   */
  @SuppressWarnings("unchecked")
  private <MessageT extends Message<MessageResponseT>, MessageResponseT extends MessageResponse>
  MessageProcessor<MessageT, MessageResponseT> lookupMessageProcessor(String messageType) {

    // Look up processor by message type
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

      throw new MessageTypeNotSupportedException(
          "Message with type [" + messageType + "] is not supported.");
    }

    return messageProcessor;
  }

  /**
   * @param messageResponse Concretely typed response message to convert to JSON string.
   * @param <MessageResponseT> Message response type.
   * @return JSON string representing the response message.
   */
  private <MessageResponseT extends MessageResponse> String generateStringResponse(
      MessageResponseT messageResponse) {

    ObjectMapper mapper = new ObjectMapper();

    try {
      return mapper.writeValueAsString(messageResponse);
    } catch (JsonProcessingException e) {
      String logMessage = "Error serializing " + messageResponse + " to JSON";
      log.severe(logMessage);
      throw new IllegalArgumentException(logMessage, e);
    }
  }

  /**
   * @param message String-based message to extract message object with just the type property.
   * @return Message object with just the type property de-serialized.
   * @throws BadMessageFormatException If anything goes wrong during de-serialization.
   */
  private String extractMessageType(String message) {
    // Create Jackson JSON/Object mapper
    ObjectMapper mapper = new ObjectMapper();

    try {
      // Deserialize message just to find out the type identifier
      MessageWithJustType messageWithJustType = mapper
          .readValue(message, MessageWithJustType.class);

      return messageWithJustType.getType();

    } catch (IOException e) {
      String logMessage = "Error deserializing message type identifier from JSON: " + message
          + ". Please verify that the message being sent contains the [type] property and that"
          + " the message is valid JSON.";
      log.severe(logMessage);
      throw new BadMessageFormatException(logMessage, e);
    }
  }

  /**
   * @param message String-based message to extract the complete concrete object from based on
   * processor's compatible message type.
   * @param messageProcessor Message processor compatible with message (by its string type ID).
   * @param <MessageT> Concrete message type.
   * @param <MessageResponseT> Concrete message response type.
   * @return Object of concrete MessageT type, de-serialized from the string-based message.
   * @throws BadMessageFormatException If anything goes wrong during de-serialization.
   */
  private <MessageT extends Message<MessageResponseT>,
      MessageResponseT extends MessageResponse> MessageT extractConcreteMessage(
      String message,
      MessageProcessor<MessageT, MessageResponseT> messageProcessor) {

    ObjectMapper mapper = new ObjectMapper();

    // Retrieve message processor's message class type
    Class<MessageT> processorMessageClassType = messageProcessor.getCompatibleMessageClassType();

    try {

      // Attempt to deserialize string message using message processor's message class type
      return mapper.readValue(message, processorMessageClassType);

    } catch (IOException e) {
      String errorMessage = "Error deserializing " + message + " to " + processorMessageClassType
          + ". Please verify that the message is valid JSON, no unrelated properties are included,"
          + " and that the required message properties are present.";

      // Add to the log message a hint for server-side developers
      String logMessage = " Also, verify that the [" + processorMessageClassType + "] contains"
          + " correct Jackson annotations for properties that can be ignored and properties that"
          + " are required.";
      log.severe(errorMessage + logMessage);

      throw new BadMessageFormatException(errorMessage, e);
    }
  }

  /**
   * Validates processor received message according to the JSR-380 if the message is annotated with
   * {@link Valid}.
   *
   * @param messageProcessor Message processor whose message to potentially validate.
   * @param messageObject Message object to potentially validate.
   * @throws ValidationException If validation is triggered and fails. All of the validation
   * messages will be comma-separated in the exception message. Additional information about the
   * fields that failed validation is contained in this exception instance as well.
   */
  private void validateMessage(MessageProcessor messageProcessor, Object messageObject) {
    try {
      Method method = messageProcessor.getClass()
          .getDeclaredMethod("process", messageProcessor.getCompatibleMessageClassType());

      // Extract annotations for the 1st and only parameter
      Annotation[] messageAnnotations = method.getParameterAnnotations()[0];

      // Validate if @Valid is one of the annotations
      final List<ValidationErrorInfo> validationErrors = new ArrayList<>();
      Arrays.stream(messageAnnotations)
          .filter(Valid.class::isInstance)
          .findAny()
          .ifPresent(annotation -> {

            // Execute validation on the message object
            Set<ConstraintViolation<Object>> violations = validator.validate(messageObject);

            // Extract custom validation error info from each violation
            for (ConstraintViolation<Object> violation : violations) {
              ValidationErrorInfo validationError = new ValidationErrorInfo(
                  violation.getPropertyPath().toString(),
                  violation.getMessage());
              validationErrors.add(validationError);
            }

            // Create exception object and combine all validation error messages together
            final ValidationException validationException = new ValidationException();
            validationException.setErrors(validationErrors);
            validationErrors.stream()
                .map(ValidationErrorInfo::getDefaultMessage)
                .reduce((violationMessages, violationMessage) ->
                    String.format("%s, %s", violationMessages, violationMessage))
                .ifPresent(validationException::setMessage);

            // If at least 1 validation error, throw exception
            if (validationErrors.size() > 0) {
              throw validationException;
            }
          });
    } catch (NoSuchMethodException e) {
      String message = "Unable to execute validation.";
      log.severe(message + ": " + e);
      throw new RuntimeException(message, e);
    }
  }
}
