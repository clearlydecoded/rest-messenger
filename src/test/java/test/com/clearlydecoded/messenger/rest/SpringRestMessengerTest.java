/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package test.com.clearlydecoded.messenger.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

/**
 * {@link SpringRestMessengerTest} class tests the rest controller.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class SpringRestMessengerTest {

  @Autowired
  private MockMvc mvc;

  @Test
  public void testSendingMessage4() throws Exception {
    Message4 message4 = new Message4("Hello", new Person("Yaakov", "Chaikin"));
    Message4Response expectedResponse = new Message4Response("Echo of Hello");
    ObjectMapper mapper = new ObjectMapper();
    String message4String = mapper.writeValueAsString(message4);
    String expectedResponseString = mapper.writeValueAsString(expectedResponse);
    mvc.perform(post("/process").accept(MediaType.APPLICATION_JSON).content(message4String)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().string(expectedResponseString));
  }

  @Test
  public void testSendingMessage5Hello() throws Exception {
    Message5 message5 = new Message5("Hello");
    Message5Response expectedResponse = new Message5Response("Hi!");
    ObjectMapper mapper = new ObjectMapper();
    String message4String = mapper.writeValueAsString(message5);
    String expectedResponseString = mapper.writeValueAsString(expectedResponse);
    mvc.perform(post("/process").accept(MediaType.APPLICATION_JSON).content(message4String)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().string(expectedResponseString));
  }

  @Test
  public void testSendingMessage5Bye() throws Exception {
    Message5 message5 = new Message5("Good Bye");
    Message5Response expectedResponse = new Message5Response("Bye!");
    ObjectMapper mapper = new ObjectMapper();
    String message4String = mapper.writeValueAsString(message5);
    String expectedResponseString = mapper.writeValueAsString(expectedResponse);
    mvc.perform(post("/process").accept(MediaType.APPLICATION_JSON).content(message4String)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().string(expectedResponseString));
  }

  @Test(expected = NestedServletException.class)
  public void testSendingUnknownMessage() throws Exception {
    UnsupportedMessage unsupportedMessage = new UnsupportedMessage();
    ObjectMapper mapper = new ObjectMapper();
    String unsupportedMessageString = mapper.writeValueAsString(unsupportedMessage);
    mvc.perform(
        post("/process").accept(MediaType.APPLICATION_JSON).content(unsupportedMessageString)
            .contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void testGetAvailableMessages() throws Exception {
    mvc.perform(get("/process")).andDo(print());
    //        .contentType(MediaType.APPLICATION_JSON))
    //        .andExpect(status().isOk())
    //        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    //        .andExpect(content().string(expectedResponseString));
  }
}
