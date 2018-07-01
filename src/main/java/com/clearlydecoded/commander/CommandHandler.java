/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.commander;

/**
 * {@link CommandHandler} interface defines methods which allow its implementations to do the actual
 * work of processing a concrete type of {@link Command} implementation and produce a response that
 * is a concerete implementation of {@link CommandResponse}.
 * <p>
 * Clients of this framework should implement a concrete implementation of this interface for each
 * distinct type of {@link Command}/{@link CommandResponse} pair.
 * </p>
 * <p>
 * <b>Warning! Implementations of this interface must account for the fact that the
 * execution of its code might happen in a multi-threaded environment. Do NOT assume that
 * a new instance of this handler will be created for each thread. In other words,
 * multiple threads might share the same instance of the handler.
 * </b>
 * </p>
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public interface CommandHandler
    <CommandT extends Command<CommandResponseT>, CommandResponseT extends CommandResponse> {

  /**
   * Executes the <code>command</code>, producing command response of class type which is embedded
   * in the type of <code>command</code>.
   *
   * @param command Command object containing data which is needed for execution this command.
   * @return Command response object that represents a processed response to the
   * <code>command</code>.
   */
  CommandResponseT execute(CommandT command);

  /**
   * Retrieves command type identifier that this handler is able to process. The type identifier is
   * system-wide unique.
   *
   * @return Command type identifier that is unique system-wide which is associated with a concrete
   * class that implements {@link Command} interface.
   */
  String getCompatibleCommandType();

  /**
   * Retrieves class type of the command this handler is to able to process.
   *
   * @return Class type of the command this handler is able to process.
   */
  Class<CommandT> getCompatibleCommandClassType();
}
