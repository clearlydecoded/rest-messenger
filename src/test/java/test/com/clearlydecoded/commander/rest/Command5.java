package test.com.clearlydecoded.commander.rest;

import com.clearlydecoded.commander.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command5 implements Command<Command5Response> {

  public static final String TYPE = "Command-5";

  private final String type = TYPE;

  private String greeting;

  @Override
  public String getType() {
    return type;
  }
}
