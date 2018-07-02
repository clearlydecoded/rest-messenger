/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package test.com.clearlydecoded.commander.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class SpringRestCommanderTest {

  @Autowired
  private MockMvc mvc;

  @Test
  public void testSendingCommand4() throws Exception {
    Command4 command4 = new Command4("Hello", new Person("Yaakov", "Chaikin"));
    Command4Response expectedResponse = new Command4Response("Echo of Hello");
    ObjectMapper mapper = new ObjectMapper();
    String command4String = mapper.writeValueAsString(command4);
    String expectedResponseString = mapper.writeValueAsString(expectedResponse);
    mvc.perform(post("/execute").accept(MediaType.APPLICATION_JSON).content(command4String)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().string(expectedResponseString));
  }

  @Test
  public void testSendingCommand5Hello() throws Exception {
    Command5 command5 = new Command5("Hello");
    Command5Response expectedResponse = new Command5Response("Hi!");
    ObjectMapper mapper = new ObjectMapper();
    String command4String = mapper.writeValueAsString(command5);
    String expectedResponseString = mapper.writeValueAsString(expectedResponse);
    mvc.perform(post("/execute").accept(MediaType.APPLICATION_JSON).content(command4String)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().string(expectedResponseString));
  }

  @Test
  public void testSendingCommand5Bye() throws Exception {
    Command5 command5 = new Command5("Good Bye");
    Command5Response expectedResponse = new Command5Response("Bye!");
    ObjectMapper mapper = new ObjectMapper();
    String command4String = mapper.writeValueAsString(command5);
    String expectedResponseString = mapper.writeValueAsString(expectedResponse);
    mvc.perform(post("/execute").accept(MediaType.APPLICATION_JSON).content(command4String)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().string(expectedResponseString));
  }

  @Test(expected = NestedServletException.class)
  public void testSendingUnknownCommand() throws Exception {
    UnsupportedCommand unsupportedCommand = new UnsupportedCommand();
    ObjectMapper mapper = new ObjectMapper();
    String unsupportedCommandString = mapper.writeValueAsString(unsupportedCommand);
    mvc.perform(
        post("/execute").accept(MediaType.APPLICATION_JSON).content(unsupportedCommandString)
            .contentType(MediaType.APPLICATION_JSON));
  }

  //  @Test
  //  public void testGetAvailableCommands() throws Exception {
  //    mvc.perform(get("/execute")).andDo(print());
  ////        .contentType(MediaType.APPLICATION_JSON))
  ////        .andExpect(status().isOk())
  ////        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  ////        .andExpect(content().string(expectedResponseString));
  //  }
}
