package test.com.clearlydecoded.commander.discovery.registry;

import com.clearlydecoded.commander.CommandResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Command3Response implements CommandResponse {

  private String greeting;

  private String originalGreeting;

  private Long randomNumber;
}
