package com.clearlydecoded.commander.rest;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response implements Serializable {

  private String text;

  private long number;

}
