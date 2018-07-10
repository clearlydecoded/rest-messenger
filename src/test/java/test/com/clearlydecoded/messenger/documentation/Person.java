/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
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

/**
 * {@link Person} class is used to test generation of documentation.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
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
