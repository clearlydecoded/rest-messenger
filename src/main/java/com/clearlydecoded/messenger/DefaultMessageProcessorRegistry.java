/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.messenger;

import static com.clearlydecoded.messenger.discovery.MessageProcessorValidator.validateMessageProcessor;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import lombok.extern.java.Log;

/**
 * {@link DefaultMessageProcessorRegistry} class is the default implementation of the
 * {@link MessageProcessorRegistry} interface.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
@Log
public class DefaultMessageProcessorRegistry implements MessageProcessorRegistry {

  /**
   * Map of {@link MessageProcessor} instances keyed by the message type they are able to process.
   */
  private Map<String, MessageProcessor<? extends Message<? extends MessageResponse>,
      ? extends MessageResponse>> processorMap = new HashMap<>();

  @Override
  public void addProcessor(MessageProcessor<? extends Message<? extends MessageResponse>,
      ? extends MessageResponse> processor) {

    // Verify string and Java-based types are compatible in the message processor
    validateMessageProcessor(processor);

    // Verify no processor for the same string-based type ID is already registered
    validateNoDuplicateTypeIdProcessor(processor);

    // Place message processor into map, keyed by string-based message type identifier
    String processorStringType = processor.getCompatibleMessageType();
    processorMap.put(processorStringType, processor);

    // Log registration
    Class<?> processedMessage = processor.getCompatibleMessageClassType();
    String logTemplate = "Registered [{0}] to process messages of type [{1}] identified by [{2}]";

    if (log.isLoggable(Level.INFO)) {
      String infoLogMessage = MessageFormat.format(
          logTemplate,
          processor.getClass().getSimpleName(),
          processedMessage.getSimpleName(),
          processorStringType);
      log.info(infoLogMessage);
    } else if (log.isLoggable(Level.FINE)) {
      String debugLogMessage = MessageFormat.format(
          logTemplate,
          processor.getClass().getName(),
          processedMessage.getName(),
          processorStringType);
      log.info(debugLogMessage);
    }
  }

  @Override
  public void addProcessors(List<? extends MessageProcessor<? extends
      Message<? extends MessageResponse>, ? extends MessageResponse>> processors) {

    // If processors is null, do nothing
    if (processors == null) {
      log.warning(
          "No Message Processors provided. No Message Processors will be registered or available.");
      return;
    }

    // Add each processor to the map
    processors.forEach(this::addProcessor);
  }

  @Override
  public MessageProcessor<? extends Message<? extends MessageResponse>,
      ? extends MessageResponse> getProcessorFor(String messageType) {

    log.fine("Retrieving processor for Message identifier type [" + messageType + "].");

    return processorMap.get(messageType);
  }

  @Override
  public void removeProcessor(String messageType) {

    log.fine("Removing processor for Message identifier type [" + messageType + "].");

    processorMap.remove(messageType);
  }

  @Override
  public List<MessageProcessor<? extends Message<? extends MessageResponse>,
      ? extends MessageResponse>> getProcessors() {
    return new ArrayList<>(processorMap.values());
  }

  /**
   * Validates that the <code>processor</code> about to be added has a unique string-based type ID.
   * If a processor with the same string-based type ID already exists in the map of processors,
   * throw {@link IllegalStateException}.
   *
   * @param processor Processor to validate.
   * @throws IllegalStateException If a processor with the same string-based type ID already exists
   * in the map of processors
   */
  private void validateNoDuplicateTypeIdProcessor(
      MessageProcessor<? extends Message<? extends MessageResponse>,
          ? extends MessageResponse> processor) throws IllegalStateException {

    // Retrieve possibly existing processor
    MessageProcessor<? extends Message<? extends MessageResponse>, ? extends MessageResponse>
        existingProcessor = getProcessorFor(processor.getCompatibleMessageType());

    // If processor for that type already exists, throw exception
    if (existingProcessor != null) {
      String logMessage = "Unable to register [{0}] to process messages of type [{1}] identified"
          + " by [{2}]. Another message processor [{3}] already identifies itself as the processor"
          + " of the same message type with string identifier [{4}].";
      String message = MessageFormat.format(logMessage, processor.getClass().getName(),
          processor.getCompatibleMessageClassType().getName(),
          processor.getCompatibleMessageType(), existingProcessor.getClass().getName(),
          existingProcessor.getCompatibleMessageType());

      throw new IllegalStateException(message);
    }
  }
}
