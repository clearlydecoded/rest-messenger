package test.com.clearlydecoded.commander.discovery.config;

import com.clearlydecoded.commander.Command;

public class Command1 implements Command<Command1Response> {

  public static final String TYPE = "Command-1";

  @Override
  public String getType() {
    return TYPE;
  }
}
