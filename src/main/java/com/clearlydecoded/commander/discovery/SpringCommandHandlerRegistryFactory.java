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
