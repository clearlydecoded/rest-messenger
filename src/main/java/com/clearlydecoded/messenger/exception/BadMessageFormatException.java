package com.clearlydecoded.messenger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * {@link BadMessageFormatException} class is an exception that is thrown when the message is not in
 * the proper format, thus the system is unable to parse it.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadMessageFormatException extends RuntimeException {

  public BadMessageFormatException() {
  }

  public BadMessageFormatException(String message) {
    super(message);
  }

  public BadMessageFormatException(String message, Throwable cause) {
    super(message, cause);
  }

  public BadMessageFormatException(Throwable cause) {
    super(cause);
  }

  public BadMessageFormatException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
