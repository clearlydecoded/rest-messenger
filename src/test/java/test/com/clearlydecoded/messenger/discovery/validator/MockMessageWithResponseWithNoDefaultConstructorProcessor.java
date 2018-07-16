package test.com.clearlydecoded.messenger.discovery.validator;

import com.clearlydecoded.messenger.MessageProcessor;

/**
 * {@link MockMessageWithResponseWithNoDefaultConstructorProcessor} class is used for testing
 * processor validation.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public class MockMessageWithResponseWithNoDefaultConstructorProcessor implements
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
  public Class<MockMessageWithResponseWithNoDefaultConstructor> getCompatibleMessageClassType() {
    return MockMessageWithResponseWithNoDefaultConstructor.class;
  }

  @Override
  public Class<MockResponseWithNoDefaultConstructor> getCompatibleMessageResponseClassType() {
    return MockResponseWithNoDefaultConstructor.class;
  }
}
