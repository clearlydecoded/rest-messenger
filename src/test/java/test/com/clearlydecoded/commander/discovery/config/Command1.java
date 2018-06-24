package test.com.clearlydecoded.commander.discovery.config;

import com.clearlydecoded.commander.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Command1 implements Command<Command1Response> {

  public static final String TYPE = "Command-1";

  private final String type = TYPE;

  @Override
  public String getType() {
    return type;
  }
}
