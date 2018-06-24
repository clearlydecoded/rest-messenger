package test.com.clearlydecoded.commander.discovery;

import com.clearlydecoded.commander.CommandHandler;

public class MockCommandWithNoDefaultConstructorHandler implements
    CommandHandler<MockCommandWithNoDefaultConstructor, MockCommandResponse> {

  @Override
  public MockCommandResponse execute(MockCommandWithNoDefaultConstructor command) {
    return null;
  }

  @Override
  public String getCompatibleCommandType() {
    return new MockCommandWithNoDefaultConstructor("test").getType();
  }

  @Override
  public Class<MockCommandWithNoDefaultConstructor> getCompatibleCommandClassType() {
    return MockCommandWithNoDefaultConstructor.class;
  }
}
