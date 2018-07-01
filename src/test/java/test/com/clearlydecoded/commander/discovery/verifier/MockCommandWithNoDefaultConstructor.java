/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package test.com.clearlydecoded.commander.discovery.verifier;

import com.clearlydecoded.commander.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Command with no no-args constructor defined
 */
@Data
@AllArgsConstructor
public class MockCommandWithNoDefaultConstructor implements Command<MockCommandResponse> {

  public static final String TYPE = "MockCommandWithNoDefaultConstructor";

  private final String type = TYPE;

  private String someProp;

  @Override
  public String getType() {
    return type;
  }
}
