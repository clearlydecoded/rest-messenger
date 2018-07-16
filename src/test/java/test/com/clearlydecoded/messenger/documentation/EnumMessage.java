/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package test.com.clearlydecoded.messenger.documentation;

import com.clearlydecoded.messenger.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {@link EnumMessage} class is an enum containing message. This class is used to test generation of
 * docs for a pojo that contains an enum, i.e., showing the options of the enum.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnumMessage implements Message<EnumMessageResponse> {

  public static final String TYPE = "EnumMessage";

  private final String type = TYPE;

  /**
   * Enum to test for choice generation.
   */
  private Status status;

  private SingleEnumValueType singleValue;

  private String regularString;

  @Override
  public String getType() {
    return type;
  }
}
