package test.com.clearlydecoded.messenger.discovery.noboilerplate;

import com.clearlydecoded.messenger.AbstractMessageProcessor;
import org.springframework.stereotype.Service;

/**
 * {@link NoBoilerPlateMessageProcessor} is a test class for testing {@link
 * AbstractMessageProcessor} class.
 */
@Service
public class NoBoilerPlateMessageProcessor extends
    AbstractMessageProcessor<NoBoilerPlateMessage, NoBoilerPlateMessageResponse> {

  @Override
  public String getCompatibleMessageType() {
    return NoBoilerPlateMessage.TYPE;
  }

  @Override
  public NoBoilerPlateMessageResponse process(NoBoilerPlateMessage message) {
    return null;
  }
}
