package test.com.clearlydecoded.commander.documentation;

import com.clearlydecoded.commander.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonCommand implements Command<PersonCommandResponse> {

  public static final String TYPE = "PersonCommand";

  private final String type = TYPE;

  private Person person;

  private int age;

  @Override
  public String getType() {
    return type;
  }
}
