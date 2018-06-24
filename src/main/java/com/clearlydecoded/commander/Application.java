package com.clearlydecoded.commander;

import com.clearlydecoded.RestCommandExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

  @Bean
  public RestCommandExecutor createEndpoint(ApplicationContext springContext) {
    return new RestCommandExecutor(springContext);
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
