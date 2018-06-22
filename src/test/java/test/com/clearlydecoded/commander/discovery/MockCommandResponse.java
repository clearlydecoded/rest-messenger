package test.com.clearlydecoded.commander.discovery;

import com.clearlydecoded.commander.CommandResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MockCommandResponse implements CommandResponse {

  private String response;
}
