package test.com.clearlydecoded.messenger.documentation;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempSimplePerson {

  private int age;

  private List<Integer> altAges;

  private List<String> altNames;

  private String firstName;
  private String lastName;

  private boolean flag;

}
