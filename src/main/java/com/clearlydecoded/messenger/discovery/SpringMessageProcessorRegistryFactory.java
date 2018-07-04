/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.messenger.discovery;

import com.clearlydecoded.messenger.DefaultMessageProcessorRegistry;
import com.clearlydecoded.messenger.MessageProcessor;
import com.clearlydecoded.messenger.MessageProcessorRegistry;
import org.springframework.context.ApplicationContext;

/**
 * {@link SpringMessageProcessorRegistryFactory} class creates a registry that contains
 * automatically discovered classes that implement {@link MessageProcessor} interface.
 *
 * <p>In order to be discovered,
 * these handlers have to be in the Application Context (i.e., Spring Context). This can be
 * accomplished by marking handler classes with {@link org.springframework.stereotype.Service},
 * or {@link org.springframework.stereotype.Component}, etc.</p>
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public class SpringMessageProcessorRegistryFactory {

  /**
   * Invokes automatic discovery of message processors in the system and registers them in the
   * message processor registry, which is what is returned to the caller of this method.
   *
   * @param springContext Spring Application Context.
   * @return Registry with already automatically discovered
   * {@link MessageProcessor}s.
   */
  public static MessageProcessorRegistry discoverMessageProcessorsAndCreateRegistry(
      ApplicationContext springContext) {

    // Create default registry
    MessageProcessorRegistry processorRegistry = new DefaultMessageProcessorRegistry();

    // Create spring-based automatic processor discoverer based on the provided Spring Context
    MessageProcessorDiscoverer discoverer = new SpringMessageProcessorDiscoverer(springContext);

    // Discover message processors in the provided Spring Context and add them to the registry
    processorRegistry.addProcessors(discoverer.discoverProcessors());

    return processorRegistry;
  }
}
