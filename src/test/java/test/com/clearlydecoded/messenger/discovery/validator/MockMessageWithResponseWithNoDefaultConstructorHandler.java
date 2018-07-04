package test.com.clearlydecoded.messenger.discovery.validator;

import com.clearlydecoded.messenger.MessageProcessor;

public class MockMessageWithResponseWithNoDefaultConstructorHandler implements
    MessageProcessor<MockMessageWithResponseWithNoDefaultConstructor,
            MockResponseWithNoDefaultConstructor> {

  @Override
  public MockResponseWithNoDefaultConstructor process(
      MockMessageWithResponseWithNoDefaultConstructor message) {
    return null;
  }

  @Override
  public String getCompatibleMessageType() {
    return MockMessageWithResponseWithNoDefaultConstructor.TYPE;
  }

  @Override
  public Class<MockMessageWithResponseWithNoDefaultConstructor> getCompatibleMessage() {
    return MockMessageWithResponseWithNoDefaultConstructor.class;
  }

  @Override
  public Class<MockResponseWithNoDefaultConstructor> getCompatibleMessageResponseClassType() {
    return MockResponseWithNoDefaultConstructor.class;
  }
}
