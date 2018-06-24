package test.com.clearlydecoded.commander.discovery.config;

import com.clearlydecoded.commander.CommandHandler;

/**
 * Won't be automatically discovered because it's not marked with @Service.
 */
public class WontBeDiscoveredHandler implements CommandHandler<Command2, Command2Response> {


  @Override
  public Command2Response execute(Command2 command) {
    return null;
  }

  @Override
  public String getCompatibleCommandType() {
    return null;
  }

  @Override
  public Class<Command2> getCompatibleCommandClassType() {
    return null;
  }
}
