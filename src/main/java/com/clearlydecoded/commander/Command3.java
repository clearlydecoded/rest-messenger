package com.clearlydecoded.commander;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Command3 implements Command<Command3Response> {

  public static final String TYPE = "Command-3";

  private String echoGreeting;

  @Override
  public String getType() {
    return TYPE;
  }
}
