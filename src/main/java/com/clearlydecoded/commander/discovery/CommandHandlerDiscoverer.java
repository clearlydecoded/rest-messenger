package com.clearlydecoded.commander.discovery;

import com.clearlydecoded.commander.Command;
import com.clearlydecoded.commander.CommandHandler;
import com.clearlydecoded.commander.CommandResponse;
import java.util.List;

/**
 * {@link CommandHandlerDiscoverer} interface defines a method that discovers all command handlers
 * within the system (however that's done), which implement {@link CommandHandler} interface.
 */
public interface CommandHandlerDiscoverer {

  /**
   * This method scans the system for concrete command handler implementations of
   * {@link CommandHandler} interface. Empty list is returned if nothing is discovered.
   *
   * @return List of discovered concrete classes within the system that implement
   * {@link CommandHandler} interface.. Empty list is returned if nothing is discovered.
   */
  List<? extends CommandHandler<? extends Command<? extends CommandResponse>,
      ? extends CommandResponse>> discoverHandlers();
}
