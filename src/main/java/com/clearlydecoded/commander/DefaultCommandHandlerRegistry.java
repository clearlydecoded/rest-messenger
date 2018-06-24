package com.clearlydecoded.commander;

import static com.clearlydecoded.commander.discovery.CommandHandlerVerifier.verifyCommandHandlerCompatibility;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import lombok.extern.java.Log;

/**
 * {@link DefaultCommandHandlerRegistry} class is the default implementation of the
 * {@link CommandHandlerRegistry} interface.
 */
@Log
public class DefaultCommandHandlerRegistry implements CommandHandlerRegistry {

  /**
   * Map of {@link CommandHandler} instances keyed by their type they are able to process.
   */
  private Map<String,
      CommandHandler<? extends Command<? extends CommandResponse>,
          ? extends CommandResponse>> handlerMap = new HashMap<>();

  @Override
  public void addHandler(CommandHandler
      <? extends Command<? extends CommandResponse>, ? extends CommandResponse> commandHandler) {

    // Verify string and Java-based types are compatible in the commandHandler
    verifyCommandHandlerCompatibility(commandHandler);

    // Place commandHandler into map, keyed by string-based command type identifier
    String handlerCommandStringType = commandHandler.getCompatibleCommandType();
    handlerMap.put(handlerCommandStringType, commandHandler);

    // Log registration
    Class<?> handledCommand = commandHandler.getCompatibleCommandClassType();
    String messageTemplate =
        "Registered [{0}] to handle commands of type [{1}] identified by [{2}]";

    if (log.isLoggable(Level.INFO)) {
      String infoMessage = MessageFormat.format(
          messageTemplate,
          commandHandler.getClass().getSimpleName(),
          handledCommand.getSimpleName(),
          handlerCommandStringType);
      log.info(infoMessage);
    } else if (log.isLoggable(Level.FINE)) {
      String debugMessage = MessageFormat.format(
          messageTemplate,
          commandHandler.getClass().getName(),
          handledCommand.getName(),
          handlerCommandStringType);
      log.info(debugMessage);
    }
  }

  @Override
  public void addHandlers(List<? extends CommandHandler
      <? extends Command<? extends CommandResponse>, ? extends CommandResponse>> commandHandlers) {

    // If handlers is null, do nothing
    if (commandHandlers == null) {
      log.info("No CommandHandlers provided. No CommandHandlers will be registered or available.");
      return;
    }

    // Add each handler to the map
    commandHandlers.forEach(this::addHandler);
  }

  @Override
  public CommandHandler
      <? extends Command<? extends CommandResponse>, ? extends CommandResponse> getHandlerFor
      (String commandType) {

    log.info("Retrieving handler for Command identifier type [" + commandType + "].");

    return handlerMap.get(commandType);
  }

  @Override
  public void removeHandler(String commandType) {

    log.info("Removing handler for Command identifier type [" + commandType + "].");

    handlerMap.remove(commandType);
  }

  @Override
  public List<CommandHandler<? extends Command<? extends CommandResponse>,
      ? extends CommandResponse>> getHandlers() {
    return new ArrayList<>(handlerMap.values());
  }
}
