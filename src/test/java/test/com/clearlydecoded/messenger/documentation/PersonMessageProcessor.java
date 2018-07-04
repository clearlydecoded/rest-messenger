package test.com.clearlydecoded.messenger.documentation;

import com.clearlydecoded.messenger.MessageProcessor;

public class PersonMessageProcessor implements
    MessageProcessor<PersonMessage, PersonMessageResponse> {

  @Override
  public PersonMessageResponse process(PersonMessage message) {
    return null;
  }

  @Override
  public String getCompatibleMessageType() {
    return PersonMessage.TYPE;
  }

  @Override
  public Class<PersonMessage> getCompatibleMessage() {
    return PersonMessage.class;
  }

  @Override
  public Class<PersonMessageResponse> getCompatibleMessageResponseClassType() {
    return PersonMessageResponse.class;
  }
}
