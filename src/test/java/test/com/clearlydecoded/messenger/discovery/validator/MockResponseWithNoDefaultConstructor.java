package test.com.clearlydecoded.messenger.discovery.validator;

import com.clearlydecoded.messenger.MessageResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MockResponseWithNoDefaultConstructor implements MessageResponse {

  private String greeting;

}
