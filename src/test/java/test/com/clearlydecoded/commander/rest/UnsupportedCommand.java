package test.com.clearlydecoded.commander.rest;

import com.clearlydecoded.commander.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnsupportedCommand implements Command {

  private final String type = "Unknown-Command";

  @Override
  public String getType() {
    return type;
  }
}
