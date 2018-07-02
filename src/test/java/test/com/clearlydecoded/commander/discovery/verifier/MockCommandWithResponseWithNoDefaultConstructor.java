package test.com.clearlydecoded.commander.discovery.verifier;

import com.clearlydecoded.commander.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MockCommandWithResponseWithNoDefaultConstructor implements
    Command<MockResponseWithNoDefaultConstructor> {

  public static final String TYPE = "MockCommandWithResponseNoDefaultConstructor";

  private final String type = TYPE;

  @Override
  public String getType() {
    return type;
  }
}
