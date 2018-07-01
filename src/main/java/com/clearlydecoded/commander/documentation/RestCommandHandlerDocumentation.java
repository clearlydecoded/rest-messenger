/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.commander.documentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {@link RestCommandHandlerDocumentation} class represents REST JSON documentation for a {@link
 * com.clearlydecoded.commander.CommandHandler}.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestCommandHandlerDocumentation {

  /**
   * Single word concrete command class name.
   */
  private String commandShortClassName;

  /**
   * Formatted JSON string representing the model of the command.
   */
  private String commandModel;

  /**
   * Single word concrete command response class name.
   */
  private String commandResponseShortClassName;

  /**
   * Formatted JSON string representing the model of the command response.
   */
  private String commandResponseModel;
}
