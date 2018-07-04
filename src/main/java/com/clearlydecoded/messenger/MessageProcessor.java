/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.messenger;

/**
 * {@link MessageProcessor} interface defines methods which allow its implementations to do the
 * actual work of processing a concrete type of {@link Message} implementation and produce a
 * response that is a concrete implementation of {@link MessageResponse}.
 * <p>
 * Clients of this framework should implement a concrete implementation of this interface for each
 * distinct type of {@link Message}/{@link MessageResponse} pair.
 * </p>
 * <p>
 * <b>Warning! Implementations of this interface must account for the fact that the
 * execution of its code might happen in a multi-threaded environment. Do NOT assume that
 * a new instance of this processor will be created for each thread. In other words,
 * multiple threads might share the same instance of the processor.
 * </b>
 * </p>
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public interface MessageProcessor
    <MessageT extends Message<MessageResponseT>, MessageResponseT extends MessageResponse> {

  /**
   * Executes the <code>message</code>, producing message response of class type which is embedded
   * in the type of <code>message</code>.
   *
   * @param message Message object containing data which is needed for processing this message.
   * @return Message response object that represents a processed response to the
   * <code>message</code>.
   */
  MessageResponseT process(MessageT message);

  /**
   * Retrieves message type identifier that this processor is able to process.
   *
   * <p>The type identifier must be unique system-wide and be identical to the value returned by
   * the {@link Message#getType()} method which is generically typed in the concrete implementation
   * of this processor.</p>
   *
   * @return Message type identifier that is unique system-wide and identical to the type identifier
   * of the concrete {@link Message} this processor is typed with.
   */
  String getCompatibleMessageType();

  /**
   * Retrieves class type of the message this processor is to able to process.
   *
   * @return Class type of the message this processor is able to process.
   */
  Class<MessageT> getCompatibleMessage();

  /**
   * Retrieves class type of the message response this processor is set to return.
   *
   * @return Class type of the message response this processor is set to return.
   */
  Class<MessageResponseT> getCompatibleMessageResponseClassType();
}
