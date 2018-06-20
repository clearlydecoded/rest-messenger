package com.clearlydecoded.commander.discovery;

import com.clearlydecoded.commander.CommandHandler;
import com.clearlydecoded.commander.CommandHandlerException;

public class MockCommandWithNoPublicConstructorHandler implements
    CommandHandler<MockCommandWithNoPublicConstructor, MockCommandResponse> {

  @Override
  public MockCommandResponse execute(MockCommandWithNoPublicConstructor command)
      throws CommandHandlerException {
    return null;
  }

  @Override
  public String getCompatibleCommandType() {
    return MockCommandWithNoPublicConstructor.TYPE;
  }

  @Override
  public Class<MockCommandWithNoPublicConstructor> getCompatibleCommandClassType() {
    return MockCommandWithNoPublicConstructor.class;
  }
}
