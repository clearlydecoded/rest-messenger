package com.clearlydecoded.commander.example.command;

import com.clearlydecoded.commander.Command;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SugarComaCookieOrder implements Command<SugarComaCookieOrderResponse> {

  public static final String TYPE = "SugarComaCookieOrder";

  private final String type = TYPE;

  @Override
  public String getType() {
    return type;
  }
}
