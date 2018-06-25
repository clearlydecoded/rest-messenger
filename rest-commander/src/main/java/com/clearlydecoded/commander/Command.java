package com.clearlydecoded.commander;

import java.io.Serializable;

/**
 * {@link Command} interface represents a command the client can execute. It should contain the
 * necessary data for the command handler to execute this command. The only required property for
 * any command is <code>type</code>.
 */
public interface Command<T extends CommandResponse> extends Serializable {

  /**
   * Getter for string-based command type identifier.
   * <p>
   * <b>Warning!</b> Command type identifier should not be settable outside of this class. It should
   * be read-only, i.e., immutable. It's recommended that its internal declaration be marked
   * <code>final</code>.
   * </p>
   *
   * @return Command type identifier that is unique system-wide.
   */
  String getType();
}
