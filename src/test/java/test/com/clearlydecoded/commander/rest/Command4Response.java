package test.com.clearlydecoded.commander.rest;

import com.clearlydecoded.commander.CommandResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command4Response implements CommandResponse {

  private String greetingEcho;

}
