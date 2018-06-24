package test.com.clearlydecoded.commander.discovery;

import com.clearlydecoded.commander.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Command with no no-args constructor defined
 */
@Data
@AllArgsConstructor
public class MockCommandWithNoDefaultConstructor implements Command<MockCommandResponse> {

  private final String type = "MockCommandWithNoDefaultConstructor";

  private String someProp;

  @Override
  public String getType() {
    return type;
  }
}
