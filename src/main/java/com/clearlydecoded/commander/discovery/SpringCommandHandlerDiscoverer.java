package com.clearlydecoded.commander.discovery;

import com.clearlydecoded.commander.Command;
import com.clearlydecoded.commander.CommandHandler;
import com.clearlydecoded.commander.CommandResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.context.ApplicationContext;

/**
 * {@link SpringCommandHandlerDiscoverer} class is a Spring Framework implementation of the
 * {@link CommandHandlerDiscoverer} interface. It uses only the Spring context to look for
 * concrete implementations of {@link CommandHandler} interface.
 */
public class SpringCommandHandlerDiscoverer implements CommandHandlerDiscoverer {

  /**
   * Spring Framework application context.
   */
  private ApplicationContext springContext;

  /**
   * Constructor.
   *
   * @param springContext Spring Framework application context.
   */
  public SpringCommandHandlerDiscoverer(ApplicationContext springContext) {
    this.springContext = springContext;
  }

  @Override
  public List<? extends CommandHandler<? extends Command<? extends CommandResponse>,
      ? extends CommandResponse>> discoverHandlers() {

    // Retrieve all spring beans that implement CommandHandler interface
    Map<String, CommandHandler> beanMap = springContext.getBeansOfType(CommandHandler.class);

    // Build a list of CommandHandlers from the bean map
    Set<String> beanNames = beanMap.keySet();
    List<CommandHandler<? extends Command<? extends CommandResponse>,
        ? extends CommandResponse>> commandHandlers;
    commandHandlers = new ArrayList<>();

    for (String beanName : beanNames) {
      CommandHandler<? extends Command<? extends CommandResponse>,
          ? extends CommandResponse> commandHandler;
      commandHandler = (CommandHandler<? extends Command<? extends CommandResponse>,
          ? extends CommandResponse>) beanMap.get(beanName);

      commandHandlers.add(commandHandler);
    }

    return commandHandlers;
  }
}
