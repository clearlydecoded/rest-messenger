/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package test.com.clearlydecoded.messenger.rest.basic;

import com.clearlydecoded.messenger.rest.SpringRestMessenger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * {@link ApplicationConfig} class is a Spring Context configuration class for testing the rest
 * controller.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class ApplicationConfig {

  /**
   * Use the Spring-based message processor registry factory to create the registry with
   * automatically discovered message handlers and expose it as a Bean into the Spring Context.
   */
  @Bean
  public SpringRestMessenger createSpringRestCommander(ApplicationContext springContext) {
    return new SpringRestMessenger(springContext);
  }
}
