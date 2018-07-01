package test.com.clearlydecoded.commander.documentation;

import static org.junit.Assert.assertEquals;

import com.clearlydecoded.commander.documentation.RestCommandHandlerDocumentation;
import com.clearlydecoded.commander.documentation.RestCommandHandlerDocumentationGenerator;
import org.junit.Test;

public class RestCommandHandlerDocumentationGeneratorTest {

  @Test
  public void testGenerateCommandDocumentation() throws Exception {

    String expectedCommandModel = "{\n"
        + "  \"type\": \"PersonCommand\"\n"
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

    String expectedCommandResponseModel = "{\n" + "}";

    PersonCommandHandler personCommandHandler = new PersonCommandHandler();
    RestCommandHandlerDocumentation docs = RestCommandHandlerDocumentationGenerator
        .generateDocumentation(personCommandHandler);

    assertEquals("Short command class name should be 'PersonCommand'", "PersonCommand",
        docs.getCommandShortClassName());
    assertEquals("Short command response class name should be 'PersonCommandResponse'",
        "PersonCommandResponse", docs.getCommandResponseShortClassName());
    assertEquals("Command model should be correct.", expectedCommandModel, docs.getCommandModel());
    assertEquals("Command response model should be correct.", expectedCommandResponseModel,
        docs.getCommandResponseModel());
  }

  @Test
  public void testGenerateCommandDocumentationLargeResponse() throws Exception {

    String expectedCommandModel = "{\n"
        + "  \"type\": \"GetPersonCommand\"\n"
        + "}";

    String expectedCommandResponseModel = "{\n"
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

    GetPersonCommandHandler getPersonCommandHandler = new GetPersonCommandHandler();
    RestCommandHandlerDocumentation docs = RestCommandHandlerDocumentationGenerator
        .generateDocumentation(getPersonCommandHandler);

    assertEquals("Short command class name should be 'GetPersonCommand'", "GetPersonCommand",
        docs.getCommandShortClassName());
    assertEquals("Short command response class name should be 'GetPersonCommandResponse'",
        "GetPersonCommandResponse", docs.getCommandResponseShortClassName());
    assertEquals("Command model should be correct.", expectedCommandModel, docs.getCommandModel());
    assertEquals("Command response model should be correct.", expectedCommandResponseModel,
        docs.getCommandResponseModel());
  }
}
