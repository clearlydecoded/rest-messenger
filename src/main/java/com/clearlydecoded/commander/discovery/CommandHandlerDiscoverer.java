package com.clearlydecoded.commander.discovery;

import com.clearlydecoded.commander.Command;
import com.clearlydecoded.commander.CommandHandler;
import com.clearlydecoded.commander.CommandResponse;
import java.util.List;

/**
 * {@link CommandHandlerDiscoverer} interface defines a method that discovers all command handlers
 * within the system (however that's done), which are annotated with {@link DiscoverCommandHandler}.
 */
public interface CommandHandlerDiscoverer {

  /**
   * This method scans the system for command handlers with {@link DiscoverCommandHandler}
   * annotation and returns ready to use concrete implementations of {@link CommandHandler}
   * interface. Empty list is returned if nothing is discovered.
   *
   * @return List of discovered classes within the system that implement {@link CommandHandler}
   * interface and are also annotated with {@link DiscoverCommandHandler}. Empty list is returned if
   * nothing is discovered.
   */
  List<? extends CommandHandler<? extends Command<? extends CommandResponse>,
      ? extends CommandResponse>> discoverHandlers();
}
