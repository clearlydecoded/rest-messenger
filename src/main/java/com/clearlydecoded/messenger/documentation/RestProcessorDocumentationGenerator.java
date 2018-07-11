/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.messenger.documentation;

import com.clearlydecoded.messenger.Message;
import com.clearlydecoded.messenger.MessageProcessor;
import com.clearlydecoded.messenger.MessageResponse;
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
 * {@link RestProcessorDocumentationGenerator} is a utility class that produces JSON-based
 * documentation for {@link MessageProcessor}s.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public class RestProcessorDocumentationGenerator {

  /**
   * Jackson object mapper to use for JSON schema generation.
   */
  private static ObjectMapper mapper = new ObjectMapper();

  /**
   * JSON module schema parser.
   */
  private static JsonSchemaGenerator schemaGenerator = new JsonSchemaGenerator(mapper);

  /**
   * Space padding to use for indentation to make pretty print output.
   */
  private static String spacePadding = "  ";

  /**
   * Generates REST friendly documentation for the provided <code>processor</code>.
   *
   * @param processor {@link MessageProcessor} to generate documentation for.
   * @return Object that hold the documentation for the provided <code>processor</code>.
   * @throws Exception If anything goes wrong with generating message processor documentation.
   */
  @SuppressWarnings("unchecked")
  public static RestProcessorDocumentation generateDocumentation(MessageProcessor processor)
      throws Exception {
    RestProcessorDocumentation documentation = new RestProcessorDocumentation();

    // Extract message & message response classes
    Class<? extends Message> messageClass = processor.getCompatibleMessage();
    Class<? extends MessageResponse> messageResponseClass = processor
        .getCompatibleMessageResponseClassType();

    // Generate documentation for message and message response
    String messageDocs = generateMessageDocumentation(messageClass);
    String messageResponseDocs = generateMessageResponseDocumentation(messageResponseClass);

    // Set up the message processor documentation object
    documentation.setMessageModel(messageDocs);
    documentation.setMessageShortClassName(messageClass.getSimpleName());
    documentation.setMessageResponseModel(messageResponseDocs);
    documentation.setMessageResponseShortClassName(messageResponseClass.getSimpleName());

    return documentation;
  }

  /**
   * Generates rest message documentation for the provided <code>messageClass</code>.
   *
   * @param messageClass Message class that implements the {@link Message} interface.
   * @return Documentation for the <code>messageClass</code> in JSON schema-like format.
   * @throws Exception If anything goes wrong during schema generation.
   */
  private static String generateMessageDocumentation(Class<? extends Message> messageClass)
      throws Exception {

    StringBuilder model = new StringBuilder();

    // Start with {
    model.append("{\n").append(spacePadding);

    // Append type identifier property
    model.append(getMessageType(messageClass));

    // Generate message object schema
    ObjectSchema messageSchema = schemaGenerator.generateSchema(messageClass).asObjectSchema();

    // Loop over all properties of message object, skipping 'type' property
    Map<String, JsonSchema> propertiesMap = messageSchema.getProperties();
    Set<String> propNames = propertiesMap.keySet();
    propNames.remove("type");
    List<String> jsonEntries = propNames.stream().map(propName -> {

      JsonSchema propSchema = propertiesMap.get(propName);
      return getJsonEntry(propName, propSchema, spacePadding, true);

    }).collect(Collectors.toList());

    jsonEntries.forEach(model::append);
    model.append("\n}");

    return model.toString();
  }

  /**
   * Generates rest message response documentation for the provided
   * <code>messageResponseClass</code> class.
   *
   * @param messageResponseClass Message response class that implements {@link MessageResponse}
   * interface.
   * @return Documentation for the <code>messageResponseClass</code> in JSON schema-like format.
   * @throws Exception If anything goes wrong during schema generation.
   */
  private static String generateMessageResponseDocumentation(
      Class<? extends MessageResponse> messageResponseClass) throws Exception {

    StringBuilder model = new StringBuilder();

    // Start with {
    model.append("{");

    // If any schema, i.e., empty, skip the rest of model generation
    JsonSchema schema = schemaGenerator.generateSchema(messageResponseClass);
    if (!(schema instanceof AnySchema)) {

      // Generate message response object schema
      ObjectSchema messageResponseSchema = schemaGenerator.generateSchema(messageResponseClass)
          .asObjectSchema();

      // Generate object model
      Map<String, JsonSchema> propertiesMap = messageResponseSchema.getProperties();
      model.append(generateObjectModel(propertiesMap, false, spacePadding));
    }

    model.append("\n}");

    return model.toString();
  }

  /**
   * Generates model of the properties of an object represented by the <code>propertiesMap</code>.
   *
   * @param firstPropertyAlreadyGenerated Indicates the object being modeled already has 1 or more
   * properties inserted into its model. If false, that means that the first property inserted into
   * the model should not have a leading comma.
   * @param spacePadding Leading space to maintain for each property in the object.
   * @return Model of the object represented by the <code>propertiesMap</code>.
   */
  private static StringBuilder generateObjectModel(Map<String, JsonSchema> propertiesMap,
      boolean firstPropertyAlreadyGenerated, String spacePadding) {

    // Skip first property's leading comma if first properties has NOT been generated
    boolean skipLeadingComma = !firstPropertyAlreadyGenerated;

    // Loop over properties and generate model
    StringBuilder model = new StringBuilder();
    for (String propName : propertiesMap.keySet()) {

      // Do not insert leading comma for the first property
      if (skipLeadingComma) {
        model.append(getJsonEntry(propName, propertiesMap.get(propName), spacePadding, false));
        skipLeadingComma = false;
      } else {
        model.append(getJsonEntry(propName, propertiesMap.get(propName), spacePadding, true));
      }
    }

    return model;
  }

  /**
   * Retrieves JSON-formatted string type identifier property of <code>messageClass</code>.
   *
   * @param messageClass Message class that implements {@link Message} interface.
   * @return JSON-based string type property of <code>messageClass</code>
   * @throws Exception If anything goes wrong with dynamically instantiating
   * <code>messageClass</code> in order to retrieve the type identifier.
   */
  private static String getMessageType(Class<? extends Message> messageClass)
      throws Exception {

    // Instantiate message to retrieve type property
    Message message = messageClass.newInstance();
    String type = message.getType();

    return "\"type\": " + "\"" + type + "\"";
  }


  /**
   * Retrieves JSON-formatted string that represents the model for the <code>propName</code> based
   * on a generic {@link JsonSchema}.
   *
   * @param propName Name of the property which <code>jsonSchema</code> describes.
   * @param jsonSchema JSON schema object for the <code>propName</code>.
   * @param currentPadding Current space padding to use for indenting the output.
   * @param insertLeadingComma Flag to indicate if a leading comma should be inserted before the
   * property name (for an object property) or property value (for an array property). If the
   * property is first property of an object or an array, the comma should be skipped.
   * @return JSON-formatted string that represents the model for the <code>propName</code>.
   */
  private static String getJsonEntry(String propName, JsonSchema jsonSchema,
      String currentPadding, boolean insertLeadingComma) {

    // Identify self-referencing
    if (jsonSchema instanceof ReferenceSchema) {
      ReferenceSchema refSchema = (ReferenceSchema) jsonSchema;
      String refObject = refSchema.get$ref();
      int lastColonIndex = refObject.lastIndexOf(":");
      String objectReference = refObject.substring(lastColonIndex + 1);

      // Insert comma if indicated
      String possibleComma = insertLeadingComma ? "," : "";

      // If propName is null, assume inside of an array - no propName needed
      if (propName == null) {
        return possibleComma + "\n" + currentPadding + objectReference + " self reference";
      } else {
        return possibleComma + "\n" + currentPadding + "\"" + propName + "\": " + objectReference
            + " self reference";
      }
    }

    // Handle model based on type of schema it is
    JsonFormatTypes schemaType = jsonSchema.getType();
    switch (schemaType) {
      case ARRAY:
        return getJsonEntry(propName, jsonSchema.asArraySchema(), currentPadding,
            insertLeadingComma);
      case OBJECT:
        return getJsonEntry(propName, jsonSchema.asObjectSchema(), currentPadding,
            insertLeadingComma);
      case BOOLEAN:
        return getJsonEntry(propName, jsonSchema.asBooleanSchema(), currentPadding,
            insertLeadingComma);
      case INTEGER:
        return getJsonEntry(propName, jsonSchema.asIntegerSchema(), currentPadding,
            insertLeadingComma);
      case NUMBER:
        return getJsonEntry(propName, jsonSchema.asNumberSchema(), currentPadding,
            insertLeadingComma);
      case STRING:
        return getJsonEntry(propName, jsonSchema.asStringSchema(), currentPadding,
            insertLeadingComma);
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
   * @param insertLeadingComma Flag to indicate if a leading comma should be inserted before the
   * property name (for an object property) or property value (for an array property). If the
   * property is first property of an object or an array, the comma should be skipped.
   * @return JSON-formatted string that represents the model for the <code>propName</code>.
   */
  private static String getJsonEntry(String propName, ArraySchema arraySchema,
      String currentPadding, boolean insertLeadingComma) {

    String output;

    // Insert comma if indicated
    String possibleComma = insertLeadingComma ? "," : "";

    // If propName is null, assume another array inside of this one - no propName needed
    if (propName == null) {
      output = possibleComma + "\n" + currentPadding + "[";
    } else {
      output = possibleComma + "\n" + currentPadding + "\"" + propName + "\": [";
    }

    // Increase padding for subsequent properties
    String newSpacePadding = currentPadding + spacePadding;

    // Extract json entry from array items
    JsonSchema arrayItemsSchema = arraySchema.getItems().asSingleItems().getSchema();
    output += getJsonEntry(null, arrayItemsSchema, newSpacePadding, false);

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
   * @param insertLeadingComma Flag to indicate if a leading comma should be inserted before the
   * property name (for an object property) or property value (for an array property). If the
   * property is first property of an object or an array, the comma should be skipped.
   * @return JSON-formatted string that represents the model for the <code>propName</code>.
   */
  private static String getJsonEntry(String propName, ObjectSchema objectSchema,
      String currentPadding, boolean insertLeadingComma) {

    String output;

    // Insert comma if indicated
    String possibleComma = insertLeadingComma ? "," : "";

    // If propName is null, assume schema inside of an array - no propName needed
    if (propName == null) {
      output = possibleComma + "\n" + currentPadding + "{";
    } else {
      output = possibleComma + "\n" + currentPadding + "\"" + propName + "\": {";
    }

    // Increase spacing for subsequent properties
    String newSpacePadding = currentPadding + spacePadding;

    // Loop over all properties object and append to output
    Map<String, JsonSchema> propertiesMap = objectSchema.getProperties();
    StringBuilder model = generateObjectModel(propertiesMap, false, newSpacePadding);
    output += model.toString();

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
   * @param insertLeadingComma Flag to indicate if a leading comma should be inserted before the
   * property name (for an object property) or property value (for an array property). If the
   * property is first property of an object or an array, the comma should be skipped.
   * @return JSON-formatted string that represents the model for the <code>propName</code>.
   */
  @SuppressWarnings("unused")
  private static String getJsonEntry(String propName, BooleanSchema booleanSchema,
      String currentPadding, boolean insertLeadingComma) {

    // Insert comma if indicated
    String possibleComma = insertLeadingComma ? "," : "";

    // If propName is null, assume schema inside of an array - no propName needed
    if (propName == null) {
      return possibleComma + "\n" + currentPadding + "boolean";
    } else {
      return possibleComma + "\n" + currentPadding + "\"" + propName + "\": boolean";
    }
  }

  /**
   * Retrieves JSON-formatted string that represents the model for the <code>propName</code> based
   * on the {@link IntegerSchema}.
   *
   * @param propName Name of the property which <code>jsonSchema</code> describes.
   * @param integerSchema Integer type schema object for the <code>propName</code>.
   * @param currentPadding Current space padding to use for indenting the output.
   * @param insertLeadingComma Flag to indicate if a leading comma should be inserted before the
   * property name (for an object property) or property value (for an array property). If the
   * property is first property of an object or an array, the comma should be skipped.
   * @return JSON-formatted string that represents the model for the <code>propName</code>.
   */
  @SuppressWarnings("unused")
  private static String getJsonEntry(String propName, IntegerSchema integerSchema,
      String currentPadding, boolean insertLeadingComma) {

    // Insert comma if indicated
    String possibleComma = insertLeadingComma ? "," : "";

    // If propName is null, assume schema inside of an array - no propName needed
    if (propName == null) {
      return possibleComma + "\n" + currentPadding + "number";
    } else {
      return possibleComma + "\n" + currentPadding + "\"" + propName + "\": number";
    }
  }

  /**
   * Retrieves JSON-formatted string that represents the model for the <code>propName</code> based
   * on the {@link IntegerSchema}.
   *
   * @param propName Name of the property which <code>jsonSchema</code> describes.
   * @param numberSchema Number type schema object for the <code>propName</code>.
   * @param currentPadding Current space padding to use for indenting the output.
   * @param insertLeadingComma Flag to indicate if a leading comma should be inserted before the
   * property name (for an object property) or property value (for an array property). If the
   * property is first property of an object or an array, the comma should be skipped.
   * @return JSON-formatted string that represents the model for the <code>propName</code>.
   */
  @SuppressWarnings("unused")
  private static String getJsonEntry(String propName, NumberSchema numberSchema,
      String currentPadding, boolean insertLeadingComma) {

    // Insert comma if indicated
    String possibleComma = insertLeadingComma ? "," : "";

    // If propName is null, assume schema inside of an array - no propName needed
    if (propName == null) {
      return possibleComma + "\n" + currentPadding + "number";
    } else {
      return possibleComma + "\n" + currentPadding + "\"" + propName + "\": number";
    }
  }

  /**
   * Retrieves JSON-formatted string that represents the model for the <code>propName</code> based
   * on the {@link StringSchema}.
   *
   * @param propName Name of the property which <code>jsonSchema</code> describes.
   * @param stringSchema String type schema object for the <code>propName</code>.
   * @param currentPadding Current space padding to use for indenting the output.
   * @param insertLeadingComma Flag to indicate if a leading comma should be inserted before the
   * property name (for an object property) or property value (for an array property). If the
   * property is first property of an object or an array, the comma should be skipped.
   * @return JSON-formatted string that represents the model for the <code>propName</code>.
   */
  @SuppressWarnings("unused")
  private static String getJsonEntry(String propName, StringSchema stringSchema,
      String currentPadding, boolean insertLeadingComma) {

    // Insert comma if indicated
    String possibleComma = insertLeadingComma ? "," : "";

    // If propName is null, assume schema inside of an array - no propName needed
    if (propName == null) {
      return possibleComma + "\n" + currentPadding + "\"string\"";
    } else {
      return possibleComma + "\n" + currentPadding + "\"" + propName + "\": \"string\"";
    }
  }
}















