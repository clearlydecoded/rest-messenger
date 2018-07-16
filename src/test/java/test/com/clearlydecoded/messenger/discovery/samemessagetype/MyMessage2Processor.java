/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package test.com.clearlydecoded.messenger.discovery.samemessagetype;

import com.clearlydecoded.messenger.AbstractMessageProcessor;
import org.springframework.stereotype.Service;

/**
 * Class for testing multiple messages with the same type id.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
@Service
public class MyMessage2Processor extends AbstractMessageProcessor<MyMessage2, MyResponse> {

  @Override
  public MyResponse process(MyMessage2 message) {
    return null;
  }
}
