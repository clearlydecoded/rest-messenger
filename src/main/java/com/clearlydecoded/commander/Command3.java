package com.clearlydecoded.commander;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command3 implements Command<Command3Response> {

  private final String type = "Command-3";

  private String echoGreeting;

  @Override
  public String getType() {
    return type;
  }
}
