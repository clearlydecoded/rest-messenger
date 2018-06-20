package com.clearlydecoded.commander.discovery;

import com.clearlydecoded.commander.CommandHandler;
import com.clearlydecoded.commander.CommandHandlerException;

public class MockCommandWithNoDefaultConstructorHandler implements
    CommandHandler<MockCommandWithNoDefaultConstructor, MockCommandResponse> {

  @Override
  public MockCommandResponse execute(MockCommandWithNoDefaultConstructor command)
      throws CommandHandlerException {
    return null;
  }

  @Override
  public String getCompatibleCommandType() {
    return MockCommandWithNoDefaultConstructor.TYPE;
  }

  @Override
  public Class<MockCommandWithNoDefaultConstructor> getCompatibleCommandClassType() {
    return MockCommandWithNoDefaultConstructor.class;
  }
}
