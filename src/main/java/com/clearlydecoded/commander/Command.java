package com.clearlydecoded.commander;

import java.io.Serializable;

/**
 * {@link Command} interface represents a command the client can execute. It should contain the
 * necessary data for the command handler to execute this command.
 */
public interface Command<T extends CommandResponse> extends Serializable {

}
