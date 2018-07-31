package com.clearlydecoded.messenger.exception;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * {@link ValidationException} class is an exception that contains more detail about particular
 * fields that fails validation. It is intended to be used in the host app to return custom error
 * messages (if the app so chooses) and thus enabling the client of the host app to dynamically map
 * error messages to the particular fields that generated those validation errors.
 * <p>
 * This exception triggers HTTP 400 Bad Request.
 * </p>
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
@Setter
public class ValidationException extends RuntimeException {

  /**
   * List of error info. Each error info contains error message and the name of the field that was
   * validated.
   */
  private List<ValidationErrorInfo> errors;

  /**
   * General message that combines all the error messages from the <code>errors</code>.
   */
  private String message;

}
