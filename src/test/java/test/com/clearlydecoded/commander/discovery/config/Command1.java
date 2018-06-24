package test.com.clearlydecoded.commander.discovery.config;

import com.clearlydecoded.commander.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Command1 implements Command<Command1Response> {

  private final String type = "Command-1";

  @Override
  public String getType() {
    return type;
  }
}
