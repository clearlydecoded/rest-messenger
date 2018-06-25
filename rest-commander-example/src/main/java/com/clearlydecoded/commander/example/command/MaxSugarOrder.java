package com.clearlydecoded.commander.example.command;

import com.clearlydecoded.commander.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaxSugarOrder implements Command<MaxSugarOrderResponse> {

  public static final String TYPE = "MaxSugarOrder";

  private final String type = TYPE;

  private int maxSugarInGrams;

  @Override
  public String getType() {
    return type;
  }
}
