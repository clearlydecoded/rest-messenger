/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.commander.discovery;

import com.clearlydecoded.commander.Command;
import com.clearlydecoded.commander.CommandHandler;
import com.clearlydecoded.commander.CommandResponse;
import java.util.List;

/**
 * {@link CommandHandlerDiscoverer} interface defines a method that discovers all command handlers
 * within the system (however that's done), which implement {@link CommandHandler} interface.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public interface CommandHandlerDiscoverer {

  /**
   * This method scans the system for concrete command handler implementations of
   * {@link CommandHandler} interface. Empty list is returned if nothing is discovered.
   *
   * @return List of discovered concrete classes within the system that implement
   * {@link CommandHandler} interface.. Empty list is returned if nothing is discovered.
   */
  List<? extends CommandHandler<? extends Command<? extends CommandResponse>,
      ? extends CommandResponse>> discoverHandlers();
}
