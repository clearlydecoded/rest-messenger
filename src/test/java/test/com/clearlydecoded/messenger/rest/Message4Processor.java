/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package test.com.clearlydecoded.messenger.rest;

import com.clearlydecoded.messenger.MessageProcessor;
import org.springframework.stereotype.Service;

/**
 * {@link Message4Processor} class is used for testing the rest processor.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
@Service
public class Message4Processor implements MessageProcessor<Message4, Message4Response> {

  @Override
  public Message4Response process(Message4 message) {
    return new Message4Response("Echo of " + message.getGreeting());
  }

  @Override
  public String getCompatibleMessageType() {
    return Message4.TYPE;
  }

  @Override
  public Class<Message4> getCompatibleMessageClassType() {
    return Message4.class;
  }

  @Override
  public Class<Message4Response> getCompatibleMessageResponseClassType() {
    return Message4Response.class;
  }
}
