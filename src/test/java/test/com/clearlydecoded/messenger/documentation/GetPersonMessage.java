package test.com.clearlydecoded.messenger.documentation;

import com.clearlydecoded.messenger.Message;

public class GetPersonMessage implements Message<GetPersonMessageResponse> {

  public static final String TYPE = "GetPersonMessage";

  private final String type = TYPE;

  @Override
  public String getType() {
    return type;
  }
}
