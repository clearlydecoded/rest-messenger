package com.clearlydecoded.commander.discovery;

import com.clearlydecoded.commander.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MockCommand implements Command<MockCommandResponse> {

  public static final String TYPE = "MockCommand";

  private String command;

  @Override
  public String getType() {
    return MockCommand.TYPE;
  }
}
