package test.com.clearlydecoded.commander.rest;

import com.clearlydecoded.commander.rest.RestCommandExecutor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class ApplicationConfig {

  /**
   * Use the Spring-based command handler registry factory to create the registry with automatically
   * discovered command handlers and expose it as a Bean into the Spring Context.
   */
  @Bean
  public RestCommandExecutor configureCommandHandlerRegistry(ApplicationContext springContext) {
    return new RestCommandExecutor(springContext);
  }
}
