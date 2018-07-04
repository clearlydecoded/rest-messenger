/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package test.com.clearlydecoded.messenger.discovery.validator;

import com.clearlydecoded.messenger.MessageProcessor;

public class MockMessageMessageWithNoPublicConstructorProcessor implements
    MessageProcessor<MockMessageWithNoPublicConstructor, MockMessageResponse> {

  @Override
  public MockMessageResponse process(MockMessageWithNoPublicConstructor message) {
    return null;
  }

  @Override
  public String getCompatibleMessageType() {
    return MockMessageWithNoPublicConstructor.TYPE;
  }

  @Override
  public Class<MockMessageWithNoPublicConstructor> getCompatibleMessage() {
    return MockMessageWithNoPublicConstructor.class;
  }

  @Override
  public Class<MockMessageResponse> getCompatibleMessageResponseClassType() {
    return MockMessageResponse.class;
  }
}
