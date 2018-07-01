/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.commander.discovery;

import com.clearlydecoded.commander.CommandHandlerRegistry;
import com.clearlydecoded.commander.DefaultCommandHandlerRegistry;
import org.springframework.context.ApplicationContext;

/**
 * {@link SpringCommandHandlerRegistryFactory} class creates a registry that contains automatically
 * discovered classes that implement {@link com.clearlydecoded.commander.CommandHandler}s. In order
 * to be discovered, these handlers have to be in the Application Context (i.e., Spring Context).
 * This can be accomplished by marking handler classes with
 * {@link org.springframework.stereotype.Service},
 * or {@link org.springframework.stereotype.Component}, etc.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public class SpringCommandHandlerRegistryFactory {

  /**
   * @param springContext Spring Application Context.
   * @return Registry with already automatically discovered
   * {@link com.clearlydecoded.commander.CommandHandler}s.
   */
  public static CommandHandlerRegistry discoverCommandHandlersAndCreateRegistry(
      ApplicationContext springContext) {

    // Create default registry
    CommandHandlerRegistry handlerRegistry = new DefaultCommandHandlerRegistry();

    // Create spring-based automatic handler discoverer based on the provided Spring Context
    CommandHandlerDiscoverer discoverer = new SpringCommandHandlerDiscoverer(springContext);

    // Discover command handlers in the provided Spring Context and add them to the registry
    handlerRegistry.addHandlers(discoverer.discoverHandlers());

    return handlerRegistry;
  }
}
