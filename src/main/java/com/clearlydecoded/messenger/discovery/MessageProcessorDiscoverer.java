/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.messenger.discovery;

import com.clearlydecoded.messenger.Message;
import com.clearlydecoded.messenger.MessageProcessor;
import com.clearlydecoded.messenger.MessageResponse;
import java.util.List;

/**
 * {@link MessageProcessorDiscoverer} interface defines a method that discovers all message
 * processors within the system (however that's done), which implement {@link MessageProcessor}
 * interface.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public interface MessageProcessorDiscoverer {

  /**
   * This method scans the system for concrete message processor implementations of
   * {@link MessageProcessor} interface. Empty list is returned if nothing is discovered.
   *
   * @return List of discovered concrete classes within the system that implement
   * {@link MessageProcessor} interface. Empty list is returned if nothing is discovered.
   */
  List<? extends MessageProcessor<? extends Message<? extends MessageResponse>,
      ? extends MessageResponse>> discoverProcessors();
}
