package test.com.clearlydecoded.messenger.documentation;

import com.clearlydecoded.messenger.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonMessage implements Message<PersonMessageResponse> {

  public static final String TYPE = "PersonMessage";

  private final String type = TYPE;

  private Person person;

  private int age;

  @Override
  public String getType() {
    return type;
  }
}
