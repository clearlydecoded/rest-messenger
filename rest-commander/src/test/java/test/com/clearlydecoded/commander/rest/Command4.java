package test.com.clearlydecoded.commander.rest;

import com.clearlydecoded.commander.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command4 implements Command<Command4Response> {

  public static final String TYPE = "Command-4";

  private final String type = TYPE;

  private String greeting;

  @Override
  public String getType() {
    return type;
  }
}
