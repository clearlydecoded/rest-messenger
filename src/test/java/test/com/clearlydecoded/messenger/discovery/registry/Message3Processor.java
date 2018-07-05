/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package test.com.clearlydecoded.messenger.discovery.registry;

import com.clearlydecoded.messenger.MessageProcessor;
import org.springframework.stereotype.Service;

@Service
public class Message3Processor implements MessageProcessor<Message3, Message3Response> {

  @Override
  public Message3Response process(Message3 message) {
    return new Message3Response("Greeting echo: " + message.getEchoGreeting(),
        message.getEchoGreeting(), 34L);
  }

  @Override
  public String getCompatibleMessageType() {
    return Message3.TYPE;
  }

  @Override
  public Class<Message3> getCompatibleMessage() {
    return Message3.class;
  }

  @Override
  public Class<Message3Response> getCompatibleMessageResponseClassType() {
    return Message3Response.class;
  }
}