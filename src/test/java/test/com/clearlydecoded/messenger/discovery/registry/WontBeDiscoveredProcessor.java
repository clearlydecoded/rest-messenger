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

/**
 * {@link WontBeDiscoveredProcessor} class is used for testing a condition where it won't be
 * automatically discovered because it's not marked with @Service.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public class WontBeDiscoveredProcessor implements MessageProcessor<Message2, Message2Response> {

  @Override
  public Message2Response process(Message2 message) {
    return null;
  }

  @Override
  public String getCompatibleMessageType() {
    return null;
  }

  @Override
  public Class<Message2> getCompatibleMessageClassType() {
    return null;
  }

  @Override
  public Class<Message2Response> getCompatibleMessageResponseClassType() {
    return null;
  }
}
