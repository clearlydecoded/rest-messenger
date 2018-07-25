/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package test.com.clearlydecoded.messenger.rest.basic;

import com.clearlydecoded.messenger.MessageProcessor;
import org.springframework.stereotype.Service;

/**
 * {@link Message5Processor} class is used for testing the rest controller.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
@Service
public class Message5Processor implements MessageProcessor<Message5, Message5Response> {

  @Override
  public Message5Response process(Message5 message) {

    String response;
    if (message.getGreeting().equals("Hello")) {
      response = "Hi!";
    } else {
      response = "Bye!";
    }

    return new Message5Response(response);
  }

  @Override
  public String getCompatibleMessageType() {
    return Message5.TYPE;
  }

  @Override
  public Class<Message5> getCompatibleMessageClassType() {
    return Message5.class;
  }

  @Override
  public Class<Message5Response> getCompatibleMessageResponseClassType() {
    return Message5Response.class;
  }
}
