package com.clearlydecoded.commander.rest;

import com.clearlydecoded.commander.Command;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * {@link CommandWithJustType} class represents a command to be used to deserialize the command
 * string coming as payload of the REST request. It is then used to determine which real concrete
 * {@link Command} implementation to deserialize the command string to.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommandWithJustType implements Command {

  /**
   * Command type identifier.
   */
  private String type;

  @Override
  public String getType() {
    return type;
  }
}
