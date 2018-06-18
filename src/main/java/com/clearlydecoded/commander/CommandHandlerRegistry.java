package com.clearlydecoded.commander;

import java.util.List;

/**
 * {@link CommandHandlerRegistry} interface defines methods for adding, removing, and retrieving
 * {@link CommandHandler}s based on the type of the {@link Command}.
 */
public interface CommandHandlerRegistry {

  /**
   * Adds <code>handler</code> to the handler registry.
   *
   * @param handler Handler to add to the handler registry.
   */
  void addHandler(CommandHandler
      <? extends Command<? extends CommandResponse>, ? extends CommandResponse> handler);

  /**
   * Adds <code>handlers</code> to the handler registry.
   *
   * @param handlers List of handlers to add to the handler registry.
   */
  void addHandlers(List<? extends CommandHandler
      <? extends Command<? extends CommandResponse>, ? extends CommandResponse>> handlers);

  /**
   * Retrieves concrete {@link CommandHandler} for the type of the command. If not found, returns
   * <code>null</code>.
   *
   * @param commandClassType Command type.
   * @return An instance of a concrete implementation of {@link CommandHandler} based
   * on the <code>commandClassType</code>. If no handler is found for the command type,
   * returns <code>null</code>.
   */
  CommandHandler
      <? extends Command<? extends CommandResponse>, ? extends CommandResponse>
  getHandlerFor(Class<?> commandClassType);

  /**
   * Removes command handler which handles <code>commandClassType</code> type of {@link Command}
   * from the handler registry.
   *
   * @param commandClassType Command type.
   */
  void removeHandler(Class<?> commandClassType);

}
