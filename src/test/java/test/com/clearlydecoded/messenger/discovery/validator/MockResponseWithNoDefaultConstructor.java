package test.com.clearlydecoded.messenger.discovery.validator;

import com.clearlydecoded.messenger.MessageResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * {@link MockResponseWithNoDefaultConstructor} class is used for testing processor validation.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
@Data
@AllArgsConstructor
public class MockResponseWithNoDefaultConstructor implements MessageResponse {

  private String greeting;

}
