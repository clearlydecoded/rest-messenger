package test.com.clearlydecoded.commander.discovery.rest;

import com.clearlydecoded.RestCommandExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

  /**
   * Use the Spring-based command handler registry factory to create the registry with automatically
   * discovered command handlers and expose it as a Bean into the Spring Context.
   */
  @Bean
  public RestCommandExecutor configureCommandHandlerRegistry(ApplicationContext springContext) {
    return new RestCommandExecutor(springContext);
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
