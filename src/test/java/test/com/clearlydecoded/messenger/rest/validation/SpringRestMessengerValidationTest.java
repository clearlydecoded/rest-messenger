/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package test.com.clearlydecoded.messenger.rest.validation;

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

/**
 * {@link SpringRestMessengerValidationTest} class tests the validation of the rest controller.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class SpringRestMessengerValidationTest {

  @Autowired
  private MockMvc mvc;

  @Test
  public void testValidMessage1() throws Exception {
    ValidMessage1 validMessage1 = new ValidMessage1("hello", "yaakov");
    Response expectedResponse = new Response("dummy response");

    ObjectMapper mapper = new ObjectMapper();
    String validMessage1String = mapper.writeValueAsString(validMessage1);
    String expectedResponseString = mapper.writeValueAsString(expectedResponse);

    mvc.perform(post("/process").accept(MediaType.APPLICATION_JSON).content(validMessage1String)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().string(expectedResponseString));
  }

  @Test(expected = NestedServletException.class)
  public void testInvalidMessage1() throws Exception {
    ValidMessage1 validMessage1 = new ValidMessage1("", "");

    ObjectMapper mapper = new ObjectMapper();
    String validMessage1String = mapper.writeValueAsString(validMessage1);

    mvc.perform(post("/process").accept(MediaType.APPLICATION_JSON).content(validMessage1String)
        .contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void testInvalidMessage2() throws Exception {
    // Construct an invalid message
    ValidMessage2 validMessage2 = new ValidMessage2("", "");
    Response expectedResponse = new Response("dummy response");

    ObjectMapper mapper = new ObjectMapper();
    String validMessage2String = mapper.writeValueAsString(validMessage2);
    String expectedResponseString = mapper.writeValueAsString(expectedResponse);

    // Since not @Valid on processor, this should pass without issues
    mvc.perform(post("/process").accept(MediaType.APPLICATION_JSON).content(validMessage2String)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().string(expectedResponseString));
  }
}

