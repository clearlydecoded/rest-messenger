/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.commander.rest;

import com.clearlydecoded.commander.Command;
import com.clearlydecoded.commander.CommandHandler;
import com.clearlydecoded.commander.CommandHandlerRegistry;
import com.clearlydecoded.commander.CommandResponse;
import com.clearlydecoded.commander.discovery.SpringCommandHandlerRegistryFactory;
import com.clearlydecoded.commander.documentation.RestCommandHandlerDocumentation;
import com.clearlydecoded.commander.documentation.RestCommandHandlerDocumentationGenerator;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * {@link SpringRestCommander} class is a REST controller which wires itself to listen on
 * <code>'/execute'</code> URI, or a custom configured URI (through
 * <code>'com.clearlydecoded.commander.endpoint.uri'</code> property) and execute auto-discovered
 * command handlers which implemente the {@link CommandHandler} interface and are injected into
 * the Spring Context either manually or by using Spring annotations such as {@link
 * org.springframework.stereotype.Service}, {@link org.springframework.stereotype.Component}, etc.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
@Controller
@Log
public class SpringRestCommander {

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
   * List of documentation classes that can be used to describe inputs/outputs of all the commands
   * registered in the system.
   */
  private List<RestCommandHandlerDocumentation> handlerDocs;

  /**
   * Constructor.
   * <p>Use this constructor unless your application needs direct access to the {@link
   * CommandHandlerRegistry}. (Hint: in majority of cases, you don't need {@link
   * CommandHandlerRegistry} to be available directly in Spring Context.</p>
   *
   * @param springContext Spring Application Context.
   */
  public SpringRestCommander(ApplicationContext springContext) {
    this(SpringCommandHandlerRegistryFactory
        .discoverCommandHandlersAndCreateRegistry(springContext));
  }

  /**
   * Constructor.
   * <p>Use this constructor if you manually created {@link CommandHandlerRegistry}. Usually, you
   * would want to do this if you manually injected {@link CommandHandlerRegistry} into Spring
   * Context for some reason (e.g., you want to look up registered command handlers in your own
   * code, outside of {@link SpringRestCommander}). To create {@link CommandHandlerRegistry}
   * populated with automatically discovered command handlers, use {@link
   * SpringCommandHandlerRegistryFactory} class.</p>
   *
   * @param commandHandlerRegistry Command handler registry used to look up command handlers based
   * on the type identifier of a command received through the REST call.
   */
  public SpringRestCommander(CommandHandlerRegistry commandHandlerRegistry) {
    this.commandHandlerRegistry = commandHandlerRegistry;

    handlerDocs = new ArrayList<>();

    try {
      // Generate docs for command handlers
      for (CommandHandler commandHandler : commandHandlerRegistry.getHandlers()) {
        RestCommandHandlerDocumentation documentation = RestCommandHandlerDocumentationGenerator
            .generateDocumentation(commandHandler);
        handlerDocs.add(documentation);
      }
    } catch (Exception e) {
      String message = "Generating command handler documentation failed. However, this will NOT" +
          "affect the functionality of the rest of the application. While it is most probably a " +
          "bug, it can be safely ignored because it only affects the documentation of the REST "
          + "API.";
      log.severe(message);
    }
  }

  /**
   * Creates mapping for the executeCommand method with the <code>endpointUri</code>.
   *
   * @throws Exception If Java reflection can't find the appropriate method on the controller.
   */
  @PostConstruct
  private void createRequestMapping() throws Exception {

    String message = "REST-COMMANDER endpoint configured for URI: /" + endpointUri;
    message += ". To configure custom URI, supply 'com.clearlydecoded.commander.endpoint.uri'";
    message += " property.";
    log.info(message);

    // Wire up request mapping for command execution
    RequestMappingInfo commandExecutionRequestMappingInfo = RequestMappingInfo
        .paths(endpointUri)
        .methods(RequestMethod.POST)
        .consumes(MediaType.APPLICATION_JSON_VALUE)
        .produces(MediaType.APPLICATION_JSON_VALUE)
        .build();
    requestMappingHandlerMapping.registerMapping(commandExecutionRequestMappingInfo, this,
        SpringRestCommander.class.getDeclaredMethod("execute", String.class));

    // Wire up request mapping for output of available commands in the system
    RequestMappingInfo getAvailableCommandsRequestMappingInfo = RequestMappingInfo
        .paths(endpointUri)
        .methods(RequestMethod.GET)
        .produces(MediaType.APPLICATION_JSON_VALUE)
        .build();
    requestMappingHandlerMapping.registerMapping(getAvailableCommandsRequestMappingInfo, this,
        SpringRestCommander.class.getDeclaredMethod("getAvailableCommands"));
  }

  /**
   * Executes commands that are sent as part of the request body.
   *
   * @param command JSON string representing the command to execute.
   * @return Command response, serialized as a JSON string.
   */
  @ResponseBody
  @SuppressWarnings("unchecked")
  private <CommandT extends Command<CommandResponseT>, CommandResponseT extends CommandResponse>
  String execute(@RequestBody String command) {

    log.fine("Full command string received: " + command);

    // Create Jackson JSON/Object mapper
    ObjectMapper mapper = new ObjectMapper();

    // Deserialize command just to find out the type identifier
    CommandWithJustType typedCommand;
    try {
      typedCommand = mapper.readValue(command, CommandWithJustType.class);
    } catch (IOException e) {
      String message = "Error deserializing command type identifier from JSON: " + command;
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

    // Retrieve command handler's command class type
    Class<CommandT> handlersCommandClassType = commandHandler
        .getCompatibleCommandClassType();

    // Attempt to deserialize string command using command handler's command class type
    CommandT javaTypedCommand;
    try {
      javaTypedCommand = mapper.readValue(command, handlersCommandClassType);
    } catch (IOException e) {
      String message = "Error deserializing " + command + " to " + handlersCommandClassType;
      log.severe(message);
      throw new IllegalArgumentException(message, e);
    }

    log.fine("Command about to be executed: " + javaTypedCommand);

    // Execute de-serialized command
    CommandResponseT commandResponse = commandHandler.execute(javaTypedCommand);

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

  /**
   * @return TODO: not done yet.
   */
  private String getAvailableCommands() throws Exception {

    //    // Retrieve all command handlers
    //    List<CommandHandler<? extends Command<? extends CommandResponse>,
    //        ? extends CommandResponse>> commandHandlers = commandHandlerRegistry.getHandlers();
    //
    //    // Retrieve all command handlers' command class types
    //    List<Class<? extends Command<? extends CommandResponse>>> commandClassTypes = commandHandlers
    //        .stream()
    //        .map(CommandHandler::getCompatibleCommandClassType)
    //        .collect(Collectors.toList());
    //
    //    ObjectMapper mapper = new ObjectMapper();
    //    JsonSchemaGenerator schemaGenerator = new JsonSchemaGenerator(mapper);
    //
    //    // Instantiate each command so it can be serialized to JSON
    //    List<Command<? extends CommandResponse>> commands = new ArrayList<>();
    //    for (Class<? extends Command<? extends CommandResponse>> commandClass : commandClassTypes) {
    //
    //      System.out.println("************** " + commandClass + "**************");
    //      JsonSchema schema = schemaGenerator.generateSchema(commandClass);
    //
    //      String prettyPrint = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
    //      System.out.println(prettyPrint);
    //
    //      Command<? extends CommandResponse> command = commandClass.newInstance();
    //      System.out.println(mapper.writeValueAsString(command));
    //      commands.add(command);
    //    }
    //
    //    String commandsJSON = mapper.writeValueAsString(commands);
    return "Not implemented yet";
  }
}
