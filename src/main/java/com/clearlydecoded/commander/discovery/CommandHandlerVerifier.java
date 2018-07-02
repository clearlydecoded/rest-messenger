/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.commander.discovery;

import com.clearlydecoded.commander.Command;
import com.clearlydecoded.commander.CommandHandler;
import com.clearlydecoded.commander.CommandResponse;
import java.text.MessageFormat;
import java.util.logging.Logger;

/**
 * {@link CommandHandlerVerifier} class is a helper class that makes sure that a
 * {@link CommandHandler} is coded properly such that its string-based command type matches the
 * string-based type of the Java {@link Command} class that the handler is typed with.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public class CommandHandlerVerifier {

  /**
   * Logger for this class.
   */
  private static final Logger log = Logger.getLogger(CommandHandlerVerifier.class.getName());

  /**
   * Verifies that the <code>commandHandler</code> is associated with a concrete {@link Command}
   * class that is implemented with the exact same string command type identifier as the
   * commandHandler type safe concrete {@link Command} implementation.
   * <p>
   * If incompatibility is found, {@link IllegalStateException} is thrown.
   * </p>
   * <p>
   * Calling this method prevents {@link CommandHandler}/{@link Command} pair that are liked by
   * Java type to produce inconsistent string-based type identifiers.
   * </p>
   *
   * @param commandHandler {@link CommandHandler} to verify for correct {@link Command} type
   * compatibility.
   * @throws IllegalStateException if <code>commandHandler</code> is found to command type
   * incompatibility.
   */
  @SuppressWarnings("unused")
  public static void verifyCommandHandlerCompatibility(
      CommandHandler<? extends Command<? extends CommandResponse>,
          ? extends CommandResponse> commandHandler)
      throws IllegalStateException {

    // Retrieve Java class command type and string-based command type
    Class<? extends Command<? extends CommandResponse>> handlerCommandClassType = commandHandler
        .getCompatibleCommandClassType();
    String handlerCommandStringType = commandHandler.getCompatibleCommandType();

    try {
      // Instantiate concrete command based on the Java type
      Command<? extends CommandResponse> command = handlerCommandClassType.newInstance();

      //  Compare command's string-based type with commandHandler's string-based type
      if (handlerCommandStringType == null || !handlerCommandStringType.equals(command.getType())) {

        String messageTemplate =
            "Command commandHandler of type [{0}] is NOT valid! It declares to handle " +
                "commands of type [{1}]. Handler''s getCompatibleCommandType() returns " +
                "[{2}], but command''s getType() returns [{3}]. These must return identical " +
                "string-based identifiers.";
        String message = MessageFormat.format(
            messageTemplate,
            commandHandler.getClass(),
            handlerCommandClassType,
            handlerCommandStringType,
            command.getType());
        log.severe(message);

        throw new IllegalStateException(message);
      }

    } catch (InstantiationException | IllegalAccessException e) {

      String messageTemplate =
          "Error trying to verify command commandHandler [{0}]. Did not find " +
              "public no-argument constructor on the command class of type [{1}]. Command " +
              "implementations must have public no-argument constructor.";
      String message = MessageFormat
          .format(messageTemplate, commandHandler.getClass(), handlerCommandClassType);

      // Rethrow as IllegalStateException
      log.severe(message);
      throw new IllegalStateException(message, e);
    }

    // Verify command response has a public no-argument constructor
    Class<? extends CommandResponse> commandResponseClass = commandHandler
        .getCompatibleCommandResponseClassType();
    try {
      CommandResponse commandResponse = commandResponseClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      String messageTemplate =
          "Error trying to verify command commandHandler [{0}]. Did not find " +
              "public no-argument constructor on the command response class of type [{1}]. " +
              "Command response implementations must have public no-argument constructor.";
      String message = MessageFormat
          .format(messageTemplate, commandHandler.getClass(), commandResponseClass);

      // Rethrow as IllegalStateException
      log.severe(message);
      throw new IllegalStateException(message, e);
    }
  }
}
