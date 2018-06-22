package com.clearlydecoded.commander;

import static com.clearlydecoded.commander.discovery.CommandHandlerVerifier.verifyCommandHandlerCompatibility;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
      <? extends Command<? extends CommandResponse>, ? extends CommandResponse> handler) {

    // Verify string and Java-based types are compatible in the handler
    verifyCommandHandlerCompatibility(handler);

    // Place handler into map, keyed by string-based command type
    String handlerStringCommandType = handler.getCompatibleCommandType();
    handlerMap.put(handlerStringCommandType, handler);

    // Log registration
    Class<?> handlerClassCommandType = handler.getCompatibleCommandClassType();
    String messageTemplate = "Registered handler instance [{0}] for commands of type [{1}], " +
        "which handle commands with string-based type of [{2}]";
    String message = MessageFormat
        .format(messageTemplate, handler.getClass(), handlerClassCommandType,
            handlerStringCommandType);
    log.info(message);
  }

  @Override
  public void addHandlers(List<? extends CommandHandler
      <? extends Command<? extends CommandResponse>, ? extends CommandResponse>> handlers) {

    // If handlers is null, do nothing
    if (handlers == null) {
      log.info("No CommandHandlers provided. No CommandHandlers will be registered or available.");
      return;
    }

    // Add each handler to the map
    handlers.forEach(this::addHandler);
  }

  @Override
  public CommandHandler
      <? extends Command<? extends CommandResponse>, ? extends CommandResponse> getHandlerFor
      (String commandType) {

    log.info("Retrieving handler for Command type [" + commandType + "].");

    return handlerMap.get(commandType);
  }

  @Override
  public void removeHandler(String commandType) {

    log.info("Removing handler for Command type [" + commandType + "].");

    handlerMap.remove(commandType);
  }

  @Override
  public List<CommandHandler<? extends Command<? extends CommandResponse>,
      ? extends CommandResponse>> getHandlers() {
    return new ArrayList<>(handlerMap.values());
  }
}
