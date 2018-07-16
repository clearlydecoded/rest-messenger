package test.com.clearlydecoded.messenger.discovery.noboilerplate;

import com.clearlydecoded.messenger.Message;

/**
 * {@link NoBoilerPlateMessage} class is a message for testing {@link
 * com.clearlydecoded.messenger.AbstractMessageProcessor}.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public class NoBoilerPlateMessage implements Message<NoBoilerPlateMessageResponse> {

  public static final String TYPE = "NoBoilerPlateMessage";

  private final String type = TYPE;

  @Override
  public String getType() {
    return type;
  }
}
