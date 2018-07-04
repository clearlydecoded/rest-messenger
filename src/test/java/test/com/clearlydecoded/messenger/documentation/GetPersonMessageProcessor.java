package test.com.clearlydecoded.messenger.documentation;

import com.clearlydecoded.messenger.MessageProcessor;

public class GetPersonMessageProcessor implements
    MessageProcessor<GetPersonMessage, GetPersonMessageResponse> {

  @Override
  public GetPersonMessageResponse process(GetPersonMessage message) {
    return null;
  }

  @Override
  public String getCompatibleMessageType() {
    return GetPersonMessage.TYPE;
  }

  @Override
  public Class<GetPersonMessage> getCompatibleMessage() {
    return GetPersonMessage.class;
  }

  @Override
  public Class<GetPersonMessageResponse> getCompatibleMessageResponseClassType() {
    return GetPersonMessageResponse.class;
  }
}
