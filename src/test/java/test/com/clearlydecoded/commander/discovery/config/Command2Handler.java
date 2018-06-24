package test.com.clearlydecoded.commander.discovery.config;

import com.clearlydecoded.commander.CommandHandler;
import org.springframework.stereotype.Service;

@Service
public class Command2Handler implements CommandHandler<Command2, Command2Response> {

  @Override
  public Command2Response execute(Command2 command) {
    // do nothing. just for testing.
    return null;
  }

  @Override
  public String getCompatibleCommandType() {
    return Command2.TYPE;
  }

  @Override
  public Class<Command2> getCompatibleCommandClassType() {
    return Command2.class;
  }
}
