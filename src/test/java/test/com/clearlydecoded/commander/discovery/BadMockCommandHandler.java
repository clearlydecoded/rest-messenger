package test.com.clearlydecoded.commander.discovery;

import com.clearlydecoded.commander.CommandHandler;
import com.clearlydecoded.commander.CommandHandlerException;

public class BadMockCommandHandler implements CommandHandler<BadMockCommand, MockCommandResponse> {

  @Override
  public MockCommandResponse execute(BadMockCommand command) throws CommandHandlerException {
    return null;
  }

  @Override
  public String getCompatibleCommandType() {
    return "Update";
  }

  @Override
  public Class<BadMockCommand> getCompatibleCommandClassType() {
    return BadMockCommand.class;
  }
}
