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
   * @param commandType Command type identifier that is unique system-wide.
   * @return An instance of a concrete implementation of {@link CommandHandler} based
   * on the <code>commandType</code>. If no handler is found for the command type,
   * returns <code>null</code>.
   */
  CommandHandler
      <? extends Command<? extends CommandResponse>, ? extends CommandResponse>
  getHandlerFor(String commandType);

  /**
   * Removes command handler which handles <code>commandtype</code> type of {@link Command}
   * from the handler registry.
   *
   * @param commandType Command type identifier that is unique system-wide.
   */
  void removeHandler(String commandType);

  /**
   * Retrieve all handlers successfully registered in the command handler registry.
   *
   * @return List of successfully discovered and registered command handlers. If none are
   * registered, returns an empty list.
   */
  List<CommandHandler<? extends Command<? extends CommandResponse>,
      ? extends CommandResponse>> getHandlers();
}
