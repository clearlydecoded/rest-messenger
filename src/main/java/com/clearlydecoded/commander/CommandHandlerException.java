package com.clearlydecoded.commander;

/**
 * {@link CommandHandlerException} class is an exception that the {@link CommandHandler} can throw
 * if something goes wrong during the processing of a command.
 */
public class CommandHandlerException extends RuntimeException {

  public CommandHandlerException() {
  }

  public CommandHandlerException(String message) {
    super(message);
  }

  public CommandHandlerException(String message, Throwable cause) {
    super(message, cause);
  }

  public CommandHandlerException(Throwable cause) {
    super(cause);
  }

  public CommandHandlerException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
