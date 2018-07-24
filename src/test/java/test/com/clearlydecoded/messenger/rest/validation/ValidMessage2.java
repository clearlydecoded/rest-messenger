/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package test.com.clearlydecoded.messenger.rest.validation;

import com.clearlydecoded.messenger.Message;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {@link ValidMessage2} class is used for testing rest controller validation.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidMessage2 implements Message<Response> {

  private final String type = "ValidMessage2";

  @NotNull(message = "'greeting' must not be blank")
  @Size(min = 1, message = "'greeting' must not be blank")
  private String greeting;

  @NotNull(message = "'name' must not be null")
  @Size(min = 2, message = "'name' must be at least 2 characters")
  private String name;

  @Override
  public String getType() {
    return type;
  }
}
