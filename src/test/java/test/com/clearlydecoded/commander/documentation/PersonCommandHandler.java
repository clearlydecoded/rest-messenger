package test.com.clearlydecoded.commander.documentation;

import com.clearlydecoded.commander.CommandHandler;

public class PersonCommandHandler implements CommandHandler<PersonCommand, PersonCommandResponse> {

  @Override
  public PersonCommandResponse execute(PersonCommand command) {
    return null;
  }

  @Override
  public String getCompatibleCommandType() {
    return PersonCommand.TYPE;
  }

  @Override
  public Class<PersonCommand> getCompatibleCommandClassType() {
    return PersonCommand.class;
  }

  @Override
  public Class<PersonCommandResponse> getCompatibleCommandResponseClassType() {
    return PersonCommandResponse.class;
  }
}
