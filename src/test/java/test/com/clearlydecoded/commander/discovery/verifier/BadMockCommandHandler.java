package test.com.clearlydecoded.commander.discovery.verifier;

import com.clearlydecoded.commander.CommandHandler;

public class BadMockCommandHandler implements CommandHandler<BadMockCommand, MockCommandResponse> {

  @Override
  public MockCommandResponse execute(BadMockCommand command) {
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
