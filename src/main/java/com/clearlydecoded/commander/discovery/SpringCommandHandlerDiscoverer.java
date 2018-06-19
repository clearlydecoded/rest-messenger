package com.clearlydecoded.commander.discovery;

import com.clearlydecoded.commander.Command;
import com.clearlydecoded.commander.CommandHandler;
import com.clearlydecoded.commander.CommandResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * {@link SpringCommandHandlerDiscoverer} class is a Spring Framework implementation of the
 * {@link CommandHandlerDiscoverer} interface. It uses only the Spring context to look for
 * concrete implementations of {@link CommandHandler} interface.
 */
@Component
public class SpringCommandHandlerDiscoverer implements CommandHandlerDiscoverer {

  /**
   * Spring Framework application context.
   */
  @Autowired
  @Setter
  private ApplicationContext springContext;

  @Override
  public List<? extends CommandHandler<? extends Command<? extends CommandResponse>,
      ? extends CommandResponse>> discoverHandlers() {

    // Retrieve all spring beans that implement CommandHandler interface
    Map<String, CommandHandler> beansMap = springContext.getBeansOfType(CommandHandler.class);

    // Return list of command handlers
    return new ArrayList<>(
        (List<? extends CommandHandler<? extends Command<? extends CommandResponse>,
            ? extends CommandResponse>>) beansMap.values());
  }
}
