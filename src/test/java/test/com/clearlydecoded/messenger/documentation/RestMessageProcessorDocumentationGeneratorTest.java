package test.com.clearlydecoded.messenger.documentation;

import static org.junit.Assert.assertEquals;

import com.clearlydecoded.messenger.documentation.RestMessageProcessorDocumentation;
import com.clearlydecoded.messenger.documentation.RestMessageProcessorDocumentationGenerator;
import org.junit.Test;

public class RestMessageProcessorDocumentationGeneratorTest {

  @Test
  public void testGenerateProcessorDocumentation() throws Exception {

    String expectedMessageModel = "{\n"
        + "  \"type\": \"PersonMessage\"\n"
        + "  \"person\": {\n"
        + "    \"id\": number\n"
        + "    \"longTime\": number\n"
        + "    \"firstName\": \"string\"\n"
        + "    \"lastName\": \"string\"\n"
        + "    \"parent\": Person self reference\n"
        + "    \"preferences\": [\n"
        + "      \"string\"\n"
        + "    ]\n"
        + "    \"relatives\": [\n"
        + "      Person self reference\n"
        + "    ]\n"
        + "    \"nameToRelativeMap\": {\n"
        + "    }\n"
        + "    \"programmer\": boolean\n"
        + "    \"dob\": \"string\"\n"
        + "  }\n"
        + "  \"age\": number\n"
        + "}";

    String expectedMessageResponseModel = "{\n" + "}";

    PersonMessageProcessor personMessageProcessor = new PersonMessageProcessor();
    RestMessageProcessorDocumentation docs = RestMessageProcessorDocumentationGenerator
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
        + "  \"type\": \"GetPersonMessage\"\n"
        + "}";

    String expectedMessageResponseModel = "{\n"
        + "  \"person\": {\n"
        + "    \"id\": number\n"
        + "    \"longTime\": number\n"
        + "    \"firstName\": \"string\"\n"
        + "    \"lastName\": \"string\"\n"
        + "    \"parent\": Person self reference\n"
        + "    \"preferences\": [\n"
        + "      \"string\"\n"
        + "    ]\n"
        + "    \"relatives\": [\n"
        + "      Person self reference\n"
        + "    ]\n"
        + "    \"nameToRelativeMap\": {\n"
        + "    }\n"
        + "    \"programmer\": boolean\n"
        + "    \"dob\": \"string\"\n"
        + "  }\n"
        + "}";

    GetPersonMessageProcessor getPersonMessageProcessor = new GetPersonMessageProcessor();
    RestMessageProcessorDocumentation docs = RestMessageProcessorDocumentationGenerator
        .generateDocumentation(getPersonMessageProcessor);

    assertEquals("Short message class name should be 'GetPersonMessage'", "GetPersonMessage",
        docs.getMessageShortClassName());
    assertEquals("Short message response class name should be 'GetPersonMessageResponse'",
        "GetPersonMessageResponse", docs.getMessageResponseShortClassName());
    assertEquals("Message model should be correct.", expectedMessageModel, docs.getMessageModel());
    assertEquals("Message response model should be correct.", expectedMessageResponseModel,
        docs.getMessageResponseModel());
  }
}
