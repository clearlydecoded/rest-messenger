package test.com.clearlydecoded.commander.documentation;

import com.clearlydecoded.commander.Command;

public class GetPersonCommand implements Command<GetPersonCommandResponse> {

  public static final String TYPE = "GetPersonCommand";

  private final String type = TYPE;

  @Override
  public String getType() {
    return type;
  }
}
