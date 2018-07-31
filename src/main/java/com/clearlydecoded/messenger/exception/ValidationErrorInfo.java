package com.clearlydecoded.messenger.exception;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {@link ValidationErrorInfo} class contains information about the validation error message
 * together with the name of the field that the validation failed on.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorInfo implements Serializable {

  /**
   * Field name the validation failed on.
   */
  private String field;

  /**
   * Validation error message for the field.
   */
  private String defaultMessage;
}
