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
 * {@link RestMessageProcessorDocumentationGenerator} is a utility class that produces JSON-based
 * documentation for {@link MessageProcessor}s.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
public class RestMessageProcessorDocumentationGenerator {

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
  public static RestMessageProcessorDocumentation generateDocumentation(
      MessageProcessor processor)
      throws Exception {
    RestMessageProcessorDocumentation documentation = new RestMessageProcessorDocumentation();

    // Extract message & message response classes
    Class<? extends Message> messageClass = processor.getCompatibleMessage();
    Class<? extends MessageResponse> messageResponseClass = processor
        .getCompatibleMessageResponseClassType();

    // Generate documentation for message and message response
    String messageDocs = generateMessageDocumentation(messageClass);
    String messageResponseDocs = generateMessageResponseDocumentation(messageResponseClass);

    // Set up the message handler documentation object
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
      return getJsonEntry(propName, propSchema, spacePadding);

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

    // If any schema, i.e., empty, skip the rest of generation
    JsonSchema schema = schemaGenerator.generateSchema(messageResponseClass);
    if (!(schema instanceof AnySchema)) {
      // Generate message response object schema
      ObjectSchema messageResponseSchema = schemaGenerator.generateSchema(messageResponseClass)
          .asObjectSchema();

      // Loop over all properties of message response object
      Map<String, JsonSchema> propertiesMap = messageResponseSchema.getProperties();
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















