package test.com.clearlydecoded.commander.discovery;

import com.clearlydecoded.commander.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BadMockCommand implements Command<MockCommandResponse> {

  private final String type = "MockCommand";

  private String command;

  @Override
  public String getType() {
    return type;
  }
}
