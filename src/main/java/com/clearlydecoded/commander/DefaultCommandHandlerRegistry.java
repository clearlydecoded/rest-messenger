package com.clearlydecoded.commander;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.java.Log;

/**
 * {@link DefaultCommandHandlerRegistry} class is the defautl implementation of the
 * {@link CommandHandlerRegistry} interface.
 */
@Log
public class DefaultCommandHandlerRegistry implements CommandHandlerRegistry {

  /**
   * Map of {@link CommandHandler} instances keyed by their type they are able to process.
   */
  private Map<Class<? extends Command<? extends CommandResponse>>,
      CommandHandler<? extends Command<? extends CommandResponse>,
          ? extends CommandResponse>> handlerMap = new HashMap<>();

  @Override
  public void addHandler(CommandHandler
      <? extends Command<? extends CommandResponse>, ? extends CommandResponse> handler) {

    log.info(
        "Registering handler instance [" + handler + "] for [" + handler.getCompatibleCommandType()
            + "] type.");

    handlerMap.put(handler.getCompatibleCommandType(), handler);
  }

  @Override
  public void addHandlers(List<? extends CommandHandler
      <? extends Command<? extends CommandResponse>, ? extends CommandResponse>> handlers) {

    // If handlers is null, do nothing
    if (handlers == null) {
      return;
    }

    // Add each handler to the map
    handlers.stream().forEach(handler -> addHandler(handler));
  }

  @Override
  public CommandHandler
      <? extends Command<? extends CommandResponse>, ? extends CommandResponse> getHandlerFor
      (Class<?> commandClassType) {

    log.info("Retrieving handler for Command type [" + commandClassType + "].");

    return handlerMap.get(commandClassType);
  }

  @Override
  public void removeHandler(Class<?> commandClassType) {

    log.info("Removing handler for Command type [" + commandClassType + "].");

    handlerMap.remove(commandClassType);
  }
}
