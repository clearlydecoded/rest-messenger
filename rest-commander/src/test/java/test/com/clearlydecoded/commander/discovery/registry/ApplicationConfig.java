package test.com.clearlydecoded.commander.discovery.registry;

import com.clearlydecoded.commander.CommandHandlerRegistry;
import com.clearlydecoded.commander.discovery.SpringCommandHandlerRegistryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class ApplicationConfig {

  @Autowired
  private ApplicationContext springContext;

  /**
   * Use the Spring-based command handler registry factory to create the registry with automatically
   * discovered command handlers and expose it as a Bean into the Spring Context.
   */
  @Bean
  public CommandHandlerRegistry configureCommandHandlerRegistry() {
    return SpringCommandHandlerRegistryFactory
        .discoverCommandHandlersAndCreateRegistry(springContext);
  }
}
