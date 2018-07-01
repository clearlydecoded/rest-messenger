package test.com.clearlydecoded.commander.documentation;

import com.clearlydecoded.commander.CommandHandler;

public class GetPersonCommandHandler implements
    CommandHandler<GetPersonCommand, GetPersonCommandResponse> {

  @Override
  public GetPersonCommandResponse execute(GetPersonCommand command) {
    return null;
  }

  @Override
  public String getCompatibleCommandType() {
    return GetPersonCommand.TYPE;
  }

  @Override
  public Class<GetPersonCommand> getCompatibleCommandClassType() {
    return GetPersonCommand.class;
  }

  @Override
  public Class<GetPersonCommandResponse> getCompatibleCommandResponseClassType() {
    return GetPersonCommandResponse.class;
  }
}
