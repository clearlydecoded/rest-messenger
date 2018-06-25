package com.clearlydecoded.commander.rest;

import com.clearlydecoded.commander.Command;
import com.clearlydecoded.commander.CommandHandler;
import com.clearlydecoded.commander.CommandHandlerRegistry;
import com.clearlydecoded.commander.CommandResponse;
import com.clearlydecoded.commander.discovery.SpringCommandHandlerRegistryFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@RestController
@Log
public class RestCommandExecutor {

  /**
   * URI of the endpoint. Defaults to '/execute' unless specified in Spring-based properties.
   */
  @Value("${com.clearlydecoded.commander.endpoint.uri:execute}")
  @Setter
  private String endpointUri;

  /**
   * Spring request mapping instance to use for wiring up request mapping for this endpoint.
   */
  @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
  @Autowired
  @Setter
  private RequestMappingHandlerMapping requestMappingHandlerMapping;

  /**
   * Command handler registry used to look up command handlers based on the type identifier of a
   * command received through the REST call.
   */
  private CommandHandlerRegistry commandHandlerRegistry;

  /**
   * Constructor.
   * <p>Use this constructor unless your application needs direct access to the {@link
   * CommandHandlerRegistry}. (Hint: in majority of cases, you don't need {@link
   * CommandHandlerRegistry} to be available directly in Spring Context.</p>
   *
   * @param springContext Spring Application Context.
   */
  public RestCommandExecutor(ApplicationContext springContext) {
    this.commandHandlerRegistry = SpringCommandHandlerRegistryFactory
        .discoverCommandHandlersAndCreateRegistry(springContext);
  }

  /**
   * Constructor.
   * <p>Use this constructor if you manually created {@link CommandHandlerRegistry}. Usually, you
   * would want to do this if you manually injected {@link CommandHandlerRegistry} into Spring
   * Context for some reason (e.g., you want to look up registered command handlers in your own
   * code, outside of {@link RestCommandExecutor}). To create {@link CommandHandlerRegistry}
   * populated with automatically discovered command handlers, use {@link
   * SpringCommandHandlerRegistryFactory} class.</p>
   *
   * @param commandHandlerRegistry Command handler registry used to look up command handlers based
   * on the type identifier of a command received through the REST call.
   */
  public RestCommandExecutor(CommandHandlerRegistry commandHandlerRegistry) {
    this.commandHandlerRegistry = commandHandlerRegistry;
  }

  /**
   * Creates mapping for the executeCommand method with the <code>endpointUri</code>.
   *
   * @throws Exception TODO: decide what to do here.
   */
  @PostConstruct
  private void createRequestMapping() throws Exception {

    log.info("REST-COMMANDER endpoint configured for URI: /" + endpointUri);

    // Wire up the request mapping
    RequestMappingInfo requestMappingInfo = RequestMappingInfo
        .paths(endpointUri)
        .methods(RequestMethod.POST)
        .consumes(MediaType.APPLICATION_JSON_VALUE)
        .produces(MediaType.APPLICATION_JSON_VALUE)
        .build();
    requestMappingHandlerMapping.registerMapping(requestMappingInfo, this,
        RestCommandExecutor.class.getDeclaredMethod("executeCommand", String.class));
  }

  /**
   * Executes commands that are sent as part of the request body.
   *
   * @param commandString JSON string representing the command to execute.
   * @return Command response, serialized as a JSON string.
   */
  @SuppressWarnings("unchecked")
  private <CommandT extends Command<CommandResponseT>, CommandResponseT extends CommandResponse>
  String executeCommand(@RequestBody String commandString) {

    log.fine("Full command string received: " + commandString);

    // Create Jackson JSON/Object mapper
    ObjectMapper mapper = new ObjectMapper();

    // Deserialize command just to find out the type identifier
    CommandWithJustType typedCommand;
    try {
      typedCommand = mapper.readValue(commandString, CommandWithJustType.class);
    } catch (IOException e) {
      String message = "Error deserializing command type identifier from JSON: " + commandString;
      log.severe(message);
      throw new IllegalArgumentException(message, e);
    }
    String commandType = typedCommand.getType();
    log.fine("Identified command with type identifier: [" + commandType + "].");

    // Look up command handler for this command type identifier
    CommandHandler<CommandT, CommandResponseT> commandHandler =
        (CommandHandler<CommandT, CommandResponseT>) commandHandlerRegistry
            .getHandlerFor(commandType);

    // Throw exception if no such command handler is found
    if (commandHandler == null) {
      String message = "No registered command handler found for command type [" + commandType + "]";
      message += ". If you think you have a CommandHandler class implemented for this command type";
      message += ", check that your command handler class is injected into Spring Context either";
      message += " by manually injecting it (in an @Configuration class) or by having the command";
      message += " handler class annotated with either @Service, @Component, etc.";
      log.severe(message);

      throw new IllegalArgumentException(
          "Command with type [" + commandType + "] is not supported.");
    }

    // Attempt to deserialize commandString using command handler's command class
    Class<CommandT> commandClassType = commandHandler
        .getCompatibleCommandClassType();
    CommandT command;
    try {
      command = mapper.readValue(commandString, commandClassType);
    } catch (IOException e) {
      String message = "Error deserializing " + commandString + " to " + commandClassType;
      log.severe(message);
      throw new IllegalArgumentException(message, e);
    }

    log.fine("Command about to be executed: " + command);

    // Execute de-serialized command
    CommandResponseT commandResponse = commandHandler.execute(command);

    // Generate raw JSON from command response
    String response;
    try {
      response = mapper.writeValueAsString(commandResponse);
    } catch (JsonProcessingException e) {
      String message = "Error serializing " + commandResponse + " to JSON";
      log.severe(message);
      throw new IllegalArgumentException(message, e);
    }

    log.fine("Full command response string to be sent: " + response);

    return response;
  }
}
