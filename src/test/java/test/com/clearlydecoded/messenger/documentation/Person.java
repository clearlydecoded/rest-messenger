package test.com.clearlydecoded.messenger.documentation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable {

  private int id;

  private Long longTime;

  private String firstName;

  private String lastName;

  private Person parent;

  private List<String> preferences;

  private Person[] relatives;

  private Map<String, Person> nameToRelativeMap;

  private boolean programmer;

  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
  private Date dob;
}
