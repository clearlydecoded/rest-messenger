package com.clearlydecoded.commander;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Command3Response implements CommandResponse {

  private String greeting;

  private String originalGreeting;

  private Long randomNumber;
}
