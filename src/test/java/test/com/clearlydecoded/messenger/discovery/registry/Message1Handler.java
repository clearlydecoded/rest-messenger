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

/**
 * {@link Message1Handler} class is used for registry testing.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
@Service
public class Message1Handler implements MessageProcessor<Message1, Message1Response> {

  @Override
  public Message1Response process(Message1 message) {
    // do nothing. processor is just for wiring tests
    return null;
  }

  @Override
  public String getCompatibleMessageType() {
    return Message1.TYPE;
  }

  @Override
  public Class<Message1> getCompatibleMessageClassType() {
    return Message1.class;
  }

  @Override
  public Class<Message1Response> getCompatibleMessageResponseClassType() {
    return Message1Response.class;
  }
}
