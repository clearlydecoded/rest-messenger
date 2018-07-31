package com.clearlydecoded.messenger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * {@link MessageTypeNotSupportedException} class is an exception that is thrown when the client of
 * the host app request to process a message with a type ID that is unknown to the host app.
 * <p>
 * This exception triggers HTTP 404 Not Found.
 * </p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MessageTypeNotSupportedException extends RuntimeException {

  public MessageTypeNotSupportedException() {
  }

  public MessageTypeNotSupportedException(String message) {
    super(message);
  }

  public MessageTypeNotSupportedException(String message, Throwable cause) {
    super(message, cause);
  }

  public MessageTypeNotSupportedException(Throwable cause) {
    super(cause);
  }

  public MessageTypeNotSupportedException(String message, Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
