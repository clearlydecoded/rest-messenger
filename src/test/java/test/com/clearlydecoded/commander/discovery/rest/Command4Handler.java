package test.com.clearlydecoded.commander.discovery.rest;

import com.clearlydecoded.commander.CommandHandler;
import org.springframework.stereotype.Service;

@Service
public class Command4Handler implements CommandHandler<Command4, Command4Response> {

  @Override
  public Command4Response execute(Command4 command) {
    return new Command4Response("Echo of " + command.getGreeting());
  }

  @Override
  public String getCompatibleCommandType() {
    return Command4.TYPE;
  }

  @Override
  public Class<Command4> getCompatibleCommandClassType() {
    return Command4.class;
  }
}
