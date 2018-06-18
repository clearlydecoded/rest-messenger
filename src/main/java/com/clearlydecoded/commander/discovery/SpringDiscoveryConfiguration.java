package com.clearlydecoded.commander.discovery;

import com.clearlydecoded.commander.CommandHandlerRegistry;
import com.clearlydecoded.commander.DefaultCommandHandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDiscoveryConfiguration {



  /**
   * @return Instance of default command handler registry.
   */
  @Bean
  public CommandHandlerRegistry commandHandlerRegistry() {
    return new DefaultCommandHandlerRegistry();
  }
}
