package com.clearlydecoded.commander.discovery;

import com.clearlydecoded.commander.CommandHandlerRegistry;
import com.clearlydecoded.commander.DefaultCommandHandlerRegistry;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link SpringCommandHandlerDiscoveryConfiguration} class is a spring {@link Configuration} that
 * can be used to trigger automatic discovery of {@link com.clearlydecoded.commander.CommandHandler}
 * concrete implementations in the Spring context.
 */
@Configuration
public class SpringCommandHandlerDiscoveryConfiguration {

  /**
   * Spring-based discoverer of handler instances in Spring context.
   */
  @Autowired
  @Setter
  private SpringCommandHandlerDiscoverer springCommandHandlerDiscoverer;

  /**
   * @return Instance of command handler registry populated with available handlers.
   */
  @Bean
  public CommandHandlerRegistry commandHandlerRegistry() {
    CommandHandlerRegistry handlerRegistry = new DefaultCommandHandlerRegistry();
    handlerRegistry.addHandlers(springCommandHandlerDiscoverer.discoverHandlers());
    return handlerRegistry;
  }
}