/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.commander;

import java.util.List;

/**
 * {@link CommandHandlerRegistry} interface defines methods for adding, removing, and retrieving
 * {@link CommandHandler}s based on the type of the {@link Command}.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public interface CommandHandlerRegistry {

  /**
   * Adds <code>commandHandler</code> to the commandHandler registry.
   *
   * @param commandHandler Handler to add to the commandHandler registry.
   */
  void addHandler(CommandHandler
      <? extends Command<? extends CommandResponse>, ? extends CommandResponse> commandHandler);

  /**
   * Adds <code>commandHandlers</code> to the handler registry.
   *
   * @param commandHandlers List of commandHandlers to add to the handler registry.
   */
  void addHandlers(List<? extends CommandHandler
      <? extends Command<? extends CommandResponse>, ? extends CommandResponse>> commandHandlers);

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
