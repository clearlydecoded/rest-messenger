/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.messenger;

import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import lombok.extern.java.Log;

/**
 * {@link AbstractMessageProcessor} abstract class implements boilerplate methods of the {@link
 * MessageProcessor} interface such that the user of this interface does not have to bother.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
@Log
public abstract class AbstractMessageProcessor
    <MessageT extends Message<MessageResponseT>, MessageResponseT extends MessageResponse>
    implements MessageProcessor<MessageT, MessageResponseT> {

  @Override
  public String getCompatibleMessageType() {

    // Retrieve concerete message class type
    Class<MessageT> concreteMessageClass = this.getCompatibleMessageClassType();

    try {

      // Instantiate the concrete message class and retrieve its spring-based type identifier
      Message<? extends MessageResponse> message = concreteMessageClass.newInstance();

      return message.getType();

    } catch (InstantiationException | IllegalAccessException e) {
      String logTemplate = "Error trying to register message processor [{0}]. Did not find public"
          + " no-argument constructor on the message class of type [{1}]. Message implementations"
          + " must have public no-argument constructor.";
      String logMessage = MessageFormat
          .format(logTemplate, this.getClass(), concreteMessageClass);

      // Rethrow as IllegalStateException
      log.severe(logMessage);
      throw new IllegalStateException(logMessage, e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public Class<MessageT> getCompatibleMessageClassType() {

    return (Class<MessageT>) ((ParameterizedType) getClass()
        .getGenericSuperclass())
        .getActualTypeArguments()[0];
  }

  @Override
  @SuppressWarnings("unchecked")
  public Class<MessageResponseT> getCompatibleMessageResponseClassType() {

    return (Class<MessageResponseT>) ((ParameterizedType) getClass()
        .getGenericSuperclass())
        .getActualTypeArguments()[1];
  }
}
