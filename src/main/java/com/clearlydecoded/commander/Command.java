/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.commander;

import java.io.Serializable;

/**
 * {@link Command} interface represents a command the client can execute. It should contain the
 * necessary data for the command handler to execute this command. The only required property for
 * any command is <code>type</code>.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public interface Command<T extends CommandResponse> extends Serializable {

  /**
   * Getter for string-based command type identifier.
   * <p>
   * <b>Warning!</b> Command type identifier should not be settable outside of this class. It should
   * be read-only, i.e., immutable. It's recommended that its internal declaration be marked
   * <code>final</code>.
   * </p>
   *
   * @return Command type identifier that is unique system-wide.
   */
  String getType();
}
