package com.clearlydecoded.commander.example.command;

import com.clearlydecoded.commander.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GingerCookieOrder implements Command<GingerCookieOrderResponse> {

  public static final String TYPE = "FirstAvailableGingerCookieOrder";

  private final String type = TYPE;

  @Override
  public String getType() {
    return type;
  }
}
