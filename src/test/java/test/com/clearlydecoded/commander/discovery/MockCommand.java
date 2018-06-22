package test.com.clearlydecoded.commander.discovery;

import com.clearlydecoded.commander.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MockCommand implements Command<MockCommandResponse> {

  public static final String TYPE = "MockCommand";

  private String command;

  @Override
  public String getType() {
    return MockCommand.TYPE;
  }
}
