package test.com.clearlydecoded.commander.discovery.rest;

import com.clearlydecoded.commander.CommandHandler;
import org.springframework.stereotype.Service;

@Service
public class Command5Handler implements CommandHandler<Command5, Command5Response> {

  @Override
  public Command5Response execute(Command5 command) {

    String response;
    if (command.getGreeting().equals("Hello")) {
      response = "Hi!";
    } else {
      response = "Bye!";
    }

    return new Command5Response(response);
  }

  @Override
  public String getCompatibleCommandType() {
    return Command5.TYPE;
  }

  @Override
  public Class<Command5> getCompatibleCommandClassType() {
    return Command5.class;
  }
}
