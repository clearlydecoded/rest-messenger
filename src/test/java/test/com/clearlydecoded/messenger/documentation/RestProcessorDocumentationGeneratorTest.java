/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package test.com.clearlydecoded.messenger.documentation;

import static org.junit.Assert.assertEquals;

import com.clearlydecoded.messenger.documentation.RestProcessorDocumentation;
import com.clearlydecoded.messenger.documentation.RestProcessorDocumentationGenerator;
import org.junit.Test;

/**
 * {@link RestProcessorDocumentationGeneratorTest} is a test class for
 * {@link RestProcessorDocumentationGenerator} class.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public class RestProcessorDocumentationGeneratorTest {

  @Test
  public void testGenerateProcessorDocumentation() throws Exception {

    String expectedMessageModel = "{\n"
        + "  \"type\": \"PersonMessage\",\n"
        + "  \"person\": {\n"
        + "    \"id\": number,\n"
        + "    \"longTime\": number,\n"
        + "    \"firstName\": \"string\",\n"
        + "    \"lastName\": \"string\",\n"
        + "    \"parent\": Person self reference,\n"
        + "    \"preferences\": [\n"
        + "      \"string\"\n"
        + "    ],\n"
        + "    \"relatives\": [\n"
        + "      Person self reference\n"
        + "    ],\n"
        + "    \"nameToRelativeMap\": {\n"
        + "    },\n"
        + "    \"programmer\": boolean,\n"
        + "    \"dob\": \"string\"\n"
        + "  },\n"
        + "  \"age\": number\n"
        + "}";
    String expectedMessageResponseModel = "{"
        + "\n}";

    PersonMessageProcessor personMessageProcessor = new PersonMessageProcessor();
    RestProcessorDocumentation docs = RestProcessorDocumentationGenerator
        .generateDocumentation(personMessageProcessor);

    assertEquals("Short message class name should be 'PersonMessage'", "PersonMessage",
        docs.getMessageShortClassName());
    assertEquals("Short message response class name should be 'PersonMessageResponse'",
        "PersonMessageResponse", docs.getMessageResponseShortClassName());
    assertEquals("Message model should be correct.", expectedMessageModel, docs.getMessageModel());
    assertEquals("Message response model should be correct.", expectedMessageResponseModel,
        docs.getMessageResponseModel());
  }

  @Test
  public void testGenerateProcessorDocumentationLargeResponse() throws Exception {

    String expectedMessageModel = "{\n"
        + "  \"type\": \"GetPersonMessage\",\n"
        + "  \"personId\": \"string\"\n"
        + "}";

    String expectedMessageResponseModel = "{\n"
        + "  \"person\": {\n"
        + "    \"id\": number,\n"
        + "    \"longTime\": number,\n"
        + "    \"firstName\": \"string\",\n"
        + "    \"lastName\": \"string\",\n"
        + "    \"parent\": Person self reference,\n"
        + "    \"preferences\": [\n"
        + "      \"string\"\n"
        + "    ],\n"
        + "    \"relatives\": [\n"
        + "      Person self reference\n"
        + "    ],\n"
        + "    \"nameToRelativeMap\": {\n"
        + "    },\n"
        + "    \"programmer\": boolean,\n"
        + "    \"dob\": \"string\"\n"
        + "  }\n"
        + "}";

    GetPersonMessageProcessor getPersonMessageProcessor = new GetPersonMessageProcessor();
    RestProcessorDocumentation docs = RestProcessorDocumentationGenerator
        .generateDocumentation(getPersonMessageProcessor);

    assertEquals("Short message class name should be 'GetPersonMessage'", "GetPersonMessage",
        docs.getMessageShortClassName());
    assertEquals("Short message response class name should be 'GetPersonMessageResponse'",
        "GetPersonMessageResponse", docs.getMessageResponseShortClassName());
    assertEquals("Message model should be correct.", expectedMessageModel, docs.getMessageModel());
    assertEquals("Message response model should be correct.", expectedMessageResponseModel,
        docs.getMessageResponseModel());
  }

  @Test
  public void testGenerateProcessorDocumentationMessageInheritance() throws Exception {

    String expectedMessageModel = "{\n"
        + "  \"type\": \"EmployeeMessage\",\n"
        + "  \"personId\": \"string\",\n"
        + "  \"employeeNumber\": \"string\"\n"
        + "}";

    String expectedMessageResponseModel = "{\n"
        + "  \"person\": {\n"
        + "    \"id\": number,\n"
        + "    \"longTime\": number,\n"
        + "    \"firstName\": \"string\",\n"
        + "    \"lastName\": \"string\",\n"
        + "    \"parent\": Person self reference,\n"
        + "    \"preferences\": [\n"
        + "      \"string\"\n"
        + "    ],\n"
        + "    \"relatives\": [\n"
        + "      Person self reference\n"
        + "    ],\n"
        + "    \"nameToRelativeMap\": {\n"
        + "    },\n"
        + "    \"programmer\": boolean,\n"
        + "    \"dob\": \"string\"\n"
        + "  }\n"
        + "}";

    EmployeeMessageProcessor processor = new EmployeeMessageProcessor();
    RestProcessorDocumentation docs = RestProcessorDocumentationGenerator
        .generateDocumentation(processor);

    assertEquals("Short message class name should be 'EmployeeMessage'", "EmployeeMessage",
        docs.getMessageShortClassName());
    assertEquals("Short message response class name should be 'GetPersonMessageResponse'",
        "GetPersonMessageResponse", docs.getMessageResponseShortClassName());
    assertEquals("Message model should be correct.", expectedMessageModel, docs.getMessageModel());
    assertEquals("Message response model should be correct.", expectedMessageResponseModel,
        docs.getMessageResponseModel());
  }

  @Test
  public void testGenerateProcessorDocumentationMessageEnumContainingMessage() throws Exception {

    String expectedMessageModel = "{\n"
        + "  \"type\": \"EnumMessage\",\n"
        + "  \"status\": \"STARTED\" | \"FINISHED\",\n"
        + "  \"singleValue\": \"SINGLE_VALUE\",\n"
        + "  \"regularString\": \"string\"\n"
        + "}";

    EnumMessageProcessor processor = new EnumMessageProcessor();
    RestProcessorDocumentation docs = RestProcessorDocumentationGenerator
        .generateDocumentation(processor);

    assertEquals("Message model with enums should be correct.", expectedMessageModel,
        docs.getMessageModel());

  }
}
