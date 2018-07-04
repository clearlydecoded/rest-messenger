package test.com.clearlydecoded.messenger.documentation;

import com.clearlydecoded.messenger.MessageResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPersonMessageResponse implements MessageResponse {

  private Person person;
}
