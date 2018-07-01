/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.commander.documentation;

import com.clearlydecoded.commander.Command;
import com.clearlydecoded.commander.CommandHandler;
import com.clearlydecoded.commander.CommandResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.types.AnySchema;
import com.fasterxml.jackson.module.jsonSchema.types.ArraySchema;
import com.fasterxml.jackson.module.jsonSchema.types.BooleanSchema;
import com.fasterxml.jackson.module.jsonSchema.types.IntegerSchema;
import com.fasterxml.jackson.module.jsonSchema.types.NumberSchema;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import com.fasterxml.jackson.module.jsonSchema.types.ReferenceSchema;
import com.fasterxml.jackson.module.jsonSchema.types.StringSchema;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@link RestCommandHandlerDocumentationGenerator} is a utility class that produces JSON-based
 * documentation for {@link com.clearlydecoded.commander.CommandHandler}s.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public class RestCommandHandlerDocumentationGenerator {

  /**
   * Jackson object mapper to use for JSON schema generation.
   */
  private static ObjectMapper mapper = new ObjectMapper();

  /**
   * JSON module schema parser.
   */
  private static JsonSchemaGenerator schemaGenerator = new JsonSchemaGenerator(mapper);

  /**
   * Space padding to use for indentation to make it pretty print.
   */
  private static String spacePadding = "  ";

  /**
   * Generates REST friendly documentation for the provided <code>commandHandler</code>.
   *
   * @param commandHandler {@link CommandHandler} to generate documentation for.
   * @return Object that hold the documentation for the provided <code>commandHandler</code>.
   * @throws Exception If anything goes wrong with generating command handler documentation.
   */
  @SuppressWarnings("unchecked")
  public static RestCommandHandlerDocumentation generateDocumentation(
      CommandHandler commandHandler)
      throws Exception {
    RestCommandHandlerDocumentation documentation = new RestCommandHandlerDocumentation();

    // Extract command & command response classes
    Class<? extends Command> commandClass = commandHandler.getCompatibleCommandClassType();
    Class<? extends CommandResponse> commandResponseClass = commandHandler
        .getCompatibleCommandResponseClassType();

    // Generate documentation for command and command response
    String commandDocs = generateCommandDocumentation(commandClass);
    String commandResponseDocs = generateCommandResponseDocumentation(commandResponseClass);

    // Set up the command handler documentation object
    documentation.setCommandModel(commandDocs);
    documentation.setCommandShortClassName(commandClass.getSimpleName());
    documentation.setCommandResponseModel(commandResponseDocs);
    documentation.setCommandResponseShortClassName(commandResponseClass.getSimpleName());

    return documentation;
  }

  /**
   * Generates rest command documentation for the provided <code>commandClass</code>.
   *
   * @param commandClass Command class that implements {@link Command} interface.
   * @return Documentation for the <code>commandClass</code> in various formats.
   * @throws Exception If anything goes wrong during schema generation.
   */
  private static String generateCommandDocumentation(Class<? extends Command> commandClass)
      throws Exception {

    StringBuilder model = new StringBuilder();

    // Start with {
    model.append("{\n").append(spacePadding);

    // Append type identifier property
    model.append(getCommandType(commandClass));

    // Generate command object schema
    ObjectSchema commandSchema = schemaGenerator.generateSchema(commandClass).asObjectSchema();

    // Loop over all properties of command object, skipping 'type' property
    Map<String, JsonSchema> propertiesMap = commandSchema.getProperties();
    Set<String> propNames = propertiesMap.keySet();
    propNames.remove("type");
    List<String> jsonEntries = propNames.stream().map(propName -> {

      JsonSchema propSchema = propertiesMap.get(propName);
      return getJsonEntry(propName, propSchema, spacePadding);

    }).collect(Collectors.toList());

    jsonEntries.forEach(model::append);
    model.append("\n}");

    return model.toString();
  }

  /**
   * Generates rest command response documentation for the provided
   * <code>commandResponseClass</code> class.
   *
   * @param commandResponseClass Command response class that implements {@link CommandResponse}
   * interface.
   * @return Documentation for the <code>commandResponseClass</code> in various formats.
   * @throws Exception If anything goes wrong during schema generation.
   */
  private static String generateCommandResponseDocumentation(
      Class<? extends CommandResponse> commandResponseClass) throws Exception {

    StringBuilder model = new StringBuilder();

    // Start with {
    model.append("{");

    // If any schema, i.e., empty, skip the rest of generation
    JsonSchema schema = schemaGenerator.generateSchema(commandResponseClass);
    if (!(schema instanceof AnySchema)) {
      // Generate command response object schema
      ObjectSchema commandSchema = schemaGenerator.generateSchema(commandResponseClass)
          .asObjectSchema();

      // Loop over all properties of command response object
      Map<String, JsonSchema> propertiesMap = commandSchema.getProperties();
      Set<String> propNames = propertiesMap.keySet();
      List<String> jsonEntries = propNames.stream().map(propName -> {

        JsonSchema propSchema = propertiesMap.get(propName);
        return getJsonEntry(propName, propSchema, spacePadding);

      }).collect(Collectors.toList());

      jsonEntries.forEach(model::append);
    }

    model.append("\n}");

    return model.toString();
  }

  /**
   * Retrieves JSON-formatted string type identifier property of <code>commandClass</code>.
   *
   * @param commandClass Command class that implements {@link Command} interface.
   * @return JSON-based string type property of <code>commandClass</code>
   * @throws Exception If anything goes wrong with dynamically instantiating
   * <code>commandClass</code> in order to retrieve the type identifier.
   */
  private static String getCommandType(Class<? extends Command> commandClass)
      throws Exception {

    // Instantiate command to retrieve type property
    Command command = commandClass.newInstance();
    String type = command.getType();

    return "\"type\": " + "\"" + type + "\"";
  }


  /**
   * Retrieves JSON-formatted string that represents the model for the <code>propName</code> based
   * on a generic {@link JsonSchema}.
   *
   * @param propName Name of the property which <code>jsonSchema</code> describes.
   * @param jsonSchema JSON schema object for the <code>propName</code>.
   * @param currentPadding Current space padding to use for indenting the output.
   * @return JSON-formatted string that represents the model for the <code>propName</code>.
   */
  private static String getJsonEntry(String propName, JsonSchema jsonSchema,
      String currentPadding) {

    // Identify self-referencing
    if (jsonSchema instanceof ReferenceSchema) {
      ReferenceSchema refSchema = (ReferenceSchema) jsonSchema;
      String refObject = refSchema.get$ref();
      int lastColonIndex = refObject.lastIndexOf(":");
      String objectReference = refObject.substring(lastColonIndex + 1);

      // If propName is null, assume inside of an array - no propName needed
      if (propName == null) {
        return "\n" + currentPadding + objectReference + " self reference";
      } else {
        return "\n" + currentPadding + "\"" + propName + "\": " + objectReference
            + " self reference";
      }
    }

    // Handle model based on type of schema it is
    JsonFormatTypes schemaType = jsonSchema.getType();
    switch (schemaType) {
      case ARRAY:
        return getJsonEntry(propName, jsonSchema.asArraySchema(), currentPadding);
      case OBJECT:
        return getJsonEntry(propName, jsonSchema.asObjectSchema(), currentPadding);
      case BOOLEAN:
        return getJsonEntry(propName, jsonSchema.asBooleanSchema(), currentPadding);
      case INTEGER:
        return getJsonEntry(propName, jsonSchema.asIntegerSchema(), currentPadding);
      case NUMBER:
        return getJsonEntry(propName, jsonSchema.asNumberSchema(), currentPadding);
      case STRING:
        return getJsonEntry(propName, jsonSchema.asStringSchema(), currentPadding);
      case ANY:
        return null;
      default:
        return "could not parse: unsupported schema";
    }
  }

  /**
   * Retrieves JSON-formatted string that represents the model for the <code>propName</code> based
   * on the {@link ArraySchema}.
   *
   * @param propName Name of the property which <code>jsonSchema</code> describes.
   * @param arraySchema Array type schema object for the <code>propName</code>.
   * @param currentPadding Current space padding to use for indenting the output.
   * @return JSON-formatted string that represents the model for the <code>propName</code>.
   */
  private static String getJsonEntry(String propName, ArraySchema arraySchema,
      String currentPadding) {

    String output;

    // If propName is null, assume another array inside of this one - no propName needed
    if (propName == null) {
      output = "\n" + currentPadding + "[";
    } else {
      output = "\n" + currentPadding + "\"" + propName + "\": [";
    }

    // Double padding for subsequent properties
    String newSpacePadding = currentPadding + spacePadding;

    // Extract json entry from array items
    JsonSchema arrayItemsSchema = arraySchema.getItems().asSingleItems().getSchema();
    output += getJsonEntry(null, arrayItemsSchema, newSpacePadding);

    // Append ] at the same level as the array property name
    output += "\n" + currentPadding + "]";

    return output;
  }

  /**
   * Retrieves JSON-formatted string that represents the model for the <code>propName</code> based
   * on the {@link ObjectSchema}.
   *
   * @param propName Name of the property which <code>jsonSchema</code> describes.
   * @param objectSchema Object type schema object for the <code>propName</code>.
   * @param currentPadding Current space padding to use for indenting the output.
   * @return JSON-formatted string that represents the model for the <code>propName</code>.
   */
  private static String getJsonEntry(String propName, ObjectSchema objectSchema,
      String currentPadding) {

    String output;

    // If propName is null, assume schema inside of an array - no propName needed
    if (propName == null) {
      output = "\n" + currentPadding + "{";
    } else {
      output = "\n" + currentPadding + "\"" + propName + "\": {";
    }

    // Double spacing for subsequent properties
    String newSpacePadding = currentPadding + spacePadding;

    // Loop over all properties object and append to output
    Map<String, JsonSchema> propertiesMap = objectSchema.getProperties();
    StringBuilder outputBuilder = new StringBuilder();
    for (String objectPropName : propertiesMap.keySet()) {
      outputBuilder
          .append(getJsonEntry(objectPropName, propertiesMap.get(objectPropName), newSpacePadding));
    }
    output += outputBuilder.toString();

    // Append } at the same level as the object property name
    output += "\n" + currentPadding + "}";

    return output;
  }

  /**
   * Retrieves JSON-formatted string that represents the model for the <code>propName</code> based
   * on the {@link BooleanSchema}.
   *
   * @param propName Name of the property which <code>jsonSchema</code> describes.
   * @param booleanSchema Boolean type schema object for the <code>propName</code>.
   * @param currentPadding Current space padding to use for indenting the output.
   * @return JSON-formatted string that represents the model for the <code>propName</code>.
   */
  @SuppressWarnings("unused")
  private static String getJsonEntry(String propName, BooleanSchema booleanSchema,
      String currentPadding) {

    // If propName is null, assume schema inside of an array - no propName needed
    if (propName == null) {
      return "\n" + currentPadding + "boolean";
    } else {
      return "\n" + currentPadding + "\"" + propName + "\": boolean";
    }
  }

  /**
   * Retrieves JSON-formatted string that represents the model for the <code>propName</code> based
   * on the {@link IntegerSchema}.
   *
   * @param propName Name of the property which <code>jsonSchema</code> describes.
   * @param integerSchema Integer type schema object for the <code>propName</code>.
   * @param currentPadding Current space padding to use for indenting the output.
   * @return JSON-formatted string that represents the model for the <code>propName</code>.
   */
  @SuppressWarnings("unused")
  private static String getJsonEntry(String propName, IntegerSchema integerSchema,
      String currentPadding) {

    // If propName is null, assume schema inside of an array - no propName needed
    if (propName == null) {
      return "\n" + currentPadding + "number";
    } else {
      return "\n" + currentPadding + "\"" + propName + "\": number";
    }
  }

  /**
   * Retrieves JSON-formatted string that represents the model for the <code>propName</code> based
   * on the {@link IntegerSchema}.
   *
   * @param propName Name of the property which <code>jsonSchema</code> describes.
   * @param numberSchema Number type schema object for the <code>propName</code>.
   * @param currentPadding Current space padding to use for indenting the output.
   * @return JSON-formatted string that represents the model for the <code>propName</code>.
   */
  @SuppressWarnings("unused")
  private static String getJsonEntry(String propName, NumberSchema numberSchema,
      String currentPadding) {

    // If propName is null, assume schema inside of an array - no propName needed
    if (propName == null) {
      return "\n" + currentPadding + "number";
    } else {
      return "\n" + currentPadding + "\"" + propName + "\": number";
    }
  }

  /**
   * Retrieves JSON-formatted string that represents the model for the <code>propName</code> based
   * on the {@link StringSchema}.
   *
   * @param propName Name of the property which <code>jsonSchema</code> describes.
   * @param stringSchema String type schema object for the <code>propName</code>.
   * @param currentPadding Current space padding to use for indenting the output.
   * @return JSON-formatted string that represents the model for the <code>propName</code>.
   */
  @SuppressWarnings("unused")
  private static String getJsonEntry(String propName, StringSchema stringSchema,
      String currentPadding) {

    // If propName is null, assume schema inside of an array - no propName needed
    if (propName == null) {
      return "\n" + currentPadding + "\"string\"";
    } else {
      return "\n" + currentPadding + "\"" + propName + "\": \"string\"";
    }
  }
}















