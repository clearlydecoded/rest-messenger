package com.clearlydecoded.commander.example;

import com.clearlydecoded.commander.rest.RestCommandExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link AppConfig} is a configuration class for the rest-commander-example web application.
 */
@Configuration
public class AppConfig {

  /**
   * Creates the REST endpoint controller that handles REST command requests.
   *
   * @param springContext Spring Application Context (automatically injected by Spring).
   * @return REST endpoint controller instance injected into Spring Context to handle REST command requests.
   */
  @Bean
  protected RestCommandExecutor createRestCommandExecutor(ApplicationContext springContext) {
    return new RestCommandExecutor(springContext);
  }
}
