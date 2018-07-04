/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.messenger;

import java.util.List;

/**
 * {@link MessageProcessorRegistry} interface defines methods for adding, removing, and retrieving
 * {@link MessageProcessor}s based on the type of the {@link Message}.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public interface MessageProcessorRegistry {

  /**
   * Adds <code>processor</code> to the message processor registry.
   *
   * @param processor Message processor to add to the message processor registry.
   */
  void addProcessor(MessageProcessor<? extends Message<? extends MessageResponse>,
      ? extends MessageResponse> processor);

  /**
   * Adds <code>processors</code> to the message processor registry.
   *
   * @param processors List of message processors to add to the message processor registry.
   */
  void addProcessors(List<? extends MessageProcessor<? extends Message<? extends MessageResponse>,
      ? extends MessageResponse>> processors);

  /**
   * Retrieves a concrete {@link MessageProcessor} for the type of the message. If not found,
   * returns <code>null</code>.
   *
   * @param messageType Message type identifier that is unique system-wide.
   * @return An instance of a concrete implementation of {@link MessageProcessor} based on the
   * <code>messageType</code>. If no processor is found for the message type, returns
   * <code>null</code>.
   */
  MessageProcessor<? extends Message<? extends MessageResponse>,
      ? extends MessageResponse> getProcessorFor(String messageType);

  /**
   * Removes message processor which is identified by the <code>messageType</code> from the message
   * processor registry.
   *
   * @param messageType Message type identifier that is unique system-wide.
   */
  void removeProcessor(String messageType);

  /**
   * Retrieve all message processors successfully registered in the message processor registry.
   *
   * @return List of successfully discovered and registered message processors. If none are
   * registered, returns an empty list.
   */
  List<MessageProcessor<? extends Message<? extends MessageResponse>,
      ? extends MessageResponse>> getProcessors();
}
