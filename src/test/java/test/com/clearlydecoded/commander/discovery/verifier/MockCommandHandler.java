package test.com.clearlydecoded.commander.discovery.verifier;

import com.clearlydecoded.commander.CommandHandler;
import org.springframework.stereotype.Service;

@Service
public class MockCommandHandler implements CommandHandler<MockCommand, MockCommandResponse> {

  @Override
  public MockCommandResponse execute(MockCommand command) {
    return null;
  }

  @Override
  public String getCompatibleCommandType() {
    return MockCommand.TYPE;
  }

  @Override
  public Class<MockCommand> getCompatibleCommandClassType() {
    return MockCommand.class;
  }
}
