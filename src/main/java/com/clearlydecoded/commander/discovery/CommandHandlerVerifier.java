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
 */
public class CommandHandlerVerifier {

  /**
   * Logger for this class.
   */
  private static final Logger log = Logger.getLogger(CommandHandlerVerifier.class.getName());

  /**
   * Verifies that the <code>handler</code> is associated with a concrete {@link Command} class
   * that is implemented with the exact same string command type identifier as the handler type safe
   * concrete {@link Command} implementation.
   * <p>
   * If incompatibility is found, {@link IllegalStateException} is thrown.
   * </p>
   * <p>
   * Calling this method prevents {@link CommandHandler}/{@link Command} pair that are liked by
   * Java type but produce inconsistent string-based identifiers.
   * </p>
   *
   * @param handler {@link CommandHandler} to verify for correct {@link Command} type compatibility.
   * @throws IllegalStateException if <code>handler</code> is found to command type incompatibility.
   */
  public static void verifyCommandHandlerCompatibility(
      CommandHandler<? extends Command<? extends CommandResponse>,
          ? extends CommandResponse> handler)
      throws IllegalStateException {

    // Retrieve Java class command type and string-based command type
    Class<? extends Command<? extends CommandResponse>> handlerCommandClassType = handler
        .getCompatibleCommandClassType();
    String handlerCommandStringType = handler.getCompatibleCommandType();

    try {
      // Instantiate concrete command based on the Java type
      Command<? extends CommandResponse> command = handlerCommandClassType.newInstance();

      //  Compare command's string-based type with handler's string-based type
      if (handlerCommandStringType == null || !handlerCommandStringType.equals(command.getType())) {

        String messageTemplate = "Can not register handler of type [{0}]. It declares to handle " +
            "commands of type [{1}]. Handler's String-based compatible command type is [{3}], " +
            "yes the String-based command type of [{1}] is [{4}]. These must be identically named.";
        String message = MessageFormat
            .format(messageTemplate, handler.getClass(), handlerCommandClassType,
                handlerCommandStringType, command.getType());
        log.severe(message);

        throw new IllegalStateException(message);
      }

    } catch (InstantiationException | IllegalAccessException e) {

      // Rethrow as IllegalStateException
      String message = "Error trying to verify command handler '" + handler.getClass() + "'";
      log.severe(message);
      throw new IllegalStateException(message, e);
    }
  }
}
