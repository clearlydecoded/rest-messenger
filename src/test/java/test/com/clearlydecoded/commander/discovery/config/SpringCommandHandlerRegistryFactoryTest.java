package test.com.clearlydecoded.commander.discovery.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.clearlydecoded.commander.CommandHandlerRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
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
    assertEquals("2 command handlers were automatically discovered and registered.", 2,
        commandHandlerRegistry.getHandlers().size());
  }
}
