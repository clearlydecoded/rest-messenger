package test.com.clearlydecoded.commander.discovery.registry;

import com.clearlydecoded.commander.CommandHandler;
import org.springframework.stereotype.Service;

@Service
public class Command3Handler implements CommandHandler<Command3, Command3Response> {

  @Override
  public Command3Response execute(Command3 command) {
    return new Command3Response("Greeting echo: " + command.getEchoGreeting(),
        command.getEchoGreeting(), 34L);
  }

  @Override
  public String getCompatibleCommandType() {
    return Command3.TYPE;
  }

  @Override
  public Class<Command3> getCompatibleCommandClassType() {
    return Command3.class;
  }
}
