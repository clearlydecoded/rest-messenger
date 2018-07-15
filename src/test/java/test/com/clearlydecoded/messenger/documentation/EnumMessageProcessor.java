/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package test.com.clearlydecoded.messenger.documentation;

import com.clearlydecoded.messenger.MessageProcessor;

/**
 * {@link EnumMessageProcessor} class is a processor for an enum containing message. This class is
 * used to test generation of docs for a pojo that contains an enum, i.e., showing the options of
 * the enum.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public class EnumMessageProcessor implements MessageProcessor<EnumMessage, EnumMessageResponse> {

  @Override
  public EnumMessageResponse process(EnumMessage message) {
    return null;
  }

  @Override
  public String getCompatibleMessageType() {
    return EnumMessage.TYPE;
  }

  @Override
  public Class<EnumMessage> getCompatibleMessage() {
    return EnumMessage.class;
  }

  @Override
  public Class<EnumMessageResponse> getCompatibleMessageResponseClassType() {
    return EnumMessageResponse.class;
  }
}
