package test.com.clearlydecoded.commander.discovery.verifier;

import com.clearlydecoded.commander.CommandHandler;

public class MockCommandWithResponseWithNoDefaultConstructorHandler implements
    CommandHandler<MockCommandWithResponseWithNoDefaultConstructor,
        MockResponseWithNoDefaultConstructor> {

  @Override
  public MockResponseWithNoDefaultConstructor execute(
      MockCommandWithResponseWithNoDefaultConstructor command) {
    return null;
  }

  @Override
  public String getCompatibleCommandType() {
    return MockCommandWithResponseWithNoDefaultConstructor.TYPE;
  }

  @Override
  public Class<MockCommandWithResponseWithNoDefaultConstructor> getCompatibleCommandClassType() {
    return MockCommandWithResponseWithNoDefaultConstructor.class;
  }

  @Override
  public Class<MockResponseWithNoDefaultConstructor> getCompatibleCommandResponseClassType() {
    return MockResponseWithNoDefaultConstructor.class;
  }
}
