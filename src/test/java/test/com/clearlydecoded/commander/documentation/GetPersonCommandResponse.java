package test.com.clearlydecoded.commander.documentation;

import com.clearlydecoded.commander.CommandResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPersonCommandResponse implements CommandResponse {

  private Person person;
}
