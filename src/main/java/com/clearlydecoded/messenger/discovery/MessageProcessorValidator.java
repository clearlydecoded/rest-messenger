/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.messenger.discovery;

import com.clearlydecoded.messenger.Message;
import com.clearlydecoded.messenger.MessageProcessor;
import com.clearlydecoded.messenger.MessageResponse;
import java.text.MessageFormat;
import java.util.logging.Logger;

/**
 * {@link MessageProcessorValidator} class is a helper class that makes sure that a
 * {@link MessageProcessor} is coded properly such that its string-based message type matches the
 * string-based type of the Java {@link Message} class that the processor is typed with.
 *
 * <p>In addition, the {@link MessageResponse} class that the processor is typed with is validated
 * for existence of proper constructor definitions.</p>
 *
 * <p>The reason for this validation is to cause the failure to happen at startup of the application
 * instead of discovering during runtime at some point later that a particular message can not be
 * properly processed.</p>
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public class MessageProcessorValidator {

  /**
   * Logger for this class.
   */
  private static final Logger log = Logger.getLogger(MessageProcessorValidator.class.getName());

  /**
   * Validates that the <code>processor</code> is associated with a concrete {@link Message} class
   * that is implemented with the exact same string message type identifier as the processor's type
   * safe concrete {@link Message} implementation.
   *
   * <p>
   * If incompatibility is found, {@link IllegalStateException} is thrown.
   * </p>
   *
   * <p>
   * Calling this method prevents {@link MessageProcessor}/{@link Message} pair that are liked by
   * Java type to produce inconsistent string-based type identifiers.
   * </p>
   *
   * @param processor {@link MessageProcessor} to verify for correct {@link Message} type
   * compatibility.
   * @throws IllegalStateException If <code>processor</code> or its associated {@link Message} and
   * {@link MessageResponse} classes are found to be invalid.
   */
  @SuppressWarnings("unused")
  public static void validateMessageProcessor(
      MessageProcessor<? extends Message<? extends MessageResponse>,
          ? extends MessageResponse> processor)
      throws IllegalStateException {

    // Retrieve Java class message type and string-based message type
    Class<? extends Message<? extends MessageResponse>> processorMessageClassType =
        processor.getCompatibleMessageClassType();
    String processorMessageStringType = processor.getCompatibleMessageType();

    try {
      // Instantiate concrete message based on the processor message class type
      Message<? extends MessageResponse> message = processorMessageClassType.newInstance();

      //  Compare message's string-based type with processor's string-based type
      if (processorMessageStringType == null || !processorMessageStringType
          .equals(message.getType())) {

        String logTemplate = "Message processor of type [{0}] is NOT valid! It declares to process"
            + " messages of type [{1}]. Processor''s getCompatibleMessageType() returns [{2}], but"
            + " message''s getType() returns [{3}]. These must return identical string-based"
            + " identifiers.";
        String logMessage = MessageFormat.format(
            logTemplate,
            processor.getClass(),
            processorMessageClassType,
            processorMessageStringType,
            message.getType());
        log.severe(logMessage);

        throw new IllegalStateException(logMessage);
      }

    } catch (InstantiationException | IllegalAccessException e) {

      String logTemplate = "Error trying to verify message processor [{0}]. Did not find public"
          + " no-argument constructor on the message class of type [{1}]. Message implementations"
          + " must have public no-argument constructor.";
      String logMessage = MessageFormat
          .format(logTemplate, processor.getClass(), processorMessageClassType);

      // Rethrow as IllegalStateException
      log.severe(logMessage);
      throw new IllegalStateException(logMessage, e);
    }

    // Verify message response has a public no-argument constructor
    Class<? extends MessageResponse> processorMessageResponseClassType = processor
        .getCompatibleMessageResponseClassType();
    try {
      MessageResponse messageResponse = processorMessageResponseClassType.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      String logTemplate = "Error trying to verify message processor [{0}]. Did not find public"
          + " no-argument constructor on the message response class of type [{1}]. Message"
          + " response implementations must have public no-argument constructor.";
      String logMessage = MessageFormat
          .format(logTemplate, processor.getClass(), processorMessageResponseClassType);

      // Rethrow as IllegalStateException
      log.severe(logMessage);
      throw new IllegalStateException(logMessage, e);
    }
  }
}
