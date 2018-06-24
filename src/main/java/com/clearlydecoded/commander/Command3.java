package com.clearlydecoded.commander;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command3 implements Command<Command3Response> {

  public static final String TYPE = "Command-3";

  private final String type = TYPE;

  private String echoGreeting;

  @Override
  public String getType() {
    return TYPE;
  }
}
