package test.com.clearlydecoded.commander.discovery.config;

import com.clearlydecoded.commander.Command;

public class Command2 implements Command<Command2Response> {

  public static final String TYPE = "Command-2";

  @Override
  public String getType() {
    return TYPE;
  }
}
