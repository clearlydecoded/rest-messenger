/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.messenger.documentation;

import com.clearlydecoded.messenger.MessageProcessor;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {@link RestProcessorDocumentation} class represents REST JSON documentation for a {@link
 * MessageProcessor}.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestProcessorDocumentation {

  /**
   * Unique id of this documentation.
   */
  private final String id = UUID.randomUUID().toString();

  /**
   * Unique id of the message of this documentation.
   */
  private final String messageId = UUID.randomUUID().toString();

  /**
   * Unique id of the message response of this documentation.
   */
  private final String messageResponseId = UUID.randomUUID().toString();

  /**
   * Single word concrete message class name.
   */
  private String messageShortClassName;

  /**
   * Formatted JSON string representing the model of the message.
   */
  private String messageModel;

  /**
   * Single word concrete message response class name.
   */
  private String messageResponseShortClassName;

  /**
   * Formatted JSON string representing the model of the message response.
   */
  private String messageResponseModel;
}
