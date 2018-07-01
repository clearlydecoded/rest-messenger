/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package test.com.clearlydecoded.commander.discovery.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.clearlydecoded.commander.CommandHandlerRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ApplicationConfig.class)
@RunWith(SpringRunner.class)
public class SpringCommandHandlerRegistryFactoryTest {

  @Autowired
  private ApplicationContext springContext;

  @Autowired
  private CommandHandlerRegistry commandHandlerRegistry;

  @Test
  public void testEverythingIsWiredCorrectly() {
    assertNotNull("Non-null Spring Context exists and is wired correctly.", springContext);
  }

  @Test
  public void testSuccessfulHandlerRegistration() {
    assertNotNull("Command handler registry is not null.", commandHandlerRegistry);
    assertEquals("3 command handlers should have been automatically discovered and registered.", 3,
        commandHandlerRegistry.getHandlers().size());
  }
}
