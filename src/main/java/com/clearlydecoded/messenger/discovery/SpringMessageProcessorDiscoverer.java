/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.clearlydecoded.messenger.discovery;

import com.clearlydecoded.messenger.Message;
import com.clearlydecoded.messenger.MessageProcessor;
import com.clearlydecoded.messenger.MessageResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.java.Log;
import org.springframework.context.ApplicationContext;

/**
 * {@link SpringMessageProcessorDiscoverer} class is a Spring Framework implementation of the
 * {@link MessageProcessorDiscoverer} interface. It uses only the Spring context to look for
 * concrete implementations of {@link MessageProcessor} interface.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
@Log
public class SpringMessageProcessorDiscoverer implements MessageProcessorDiscoverer {

  /**
   * Spring Framework application context.
   */
  private ApplicationContext springContext;

  /**
   * Constructor.
   *
   * @param springContext Spring Framework application context.
   */
  public SpringMessageProcessorDiscoverer(ApplicationContext springContext) {
    this.springContext = springContext;
  }

  @Override
  public List<? extends MessageProcessor<? extends Message<? extends MessageResponse>,
      ? extends MessageResponse>> discoverProcessors() {

    // Retrieve all spring beans that implement MessageProcessor interface
    Map<String, MessageProcessor> beanMap = springContext.getBeansOfType(MessageProcessor.class);

    // Build a list of message processors from the bean map
    Set<String> beanNames = beanMap.keySet();
    List<MessageProcessor<? extends Message<? extends MessageResponse>,
        ? extends MessageResponse>> processors = new ArrayList<>();

    for (String beanName : beanNames) {
      MessageProcessor<? extends Message<? extends MessageResponse>,
          ? extends MessageResponse> processor;
      processor = (MessageProcessor<? extends Message<? extends MessageResponse>,
          ? extends MessageResponse>) beanMap.get(beanName);

      processors.add(processor);
    }

    // Check that at least 1 message processor is discovered; log warning if not
    if (processors.size() == 0) {
      String logMessage =
          "No message processors were discovered in the Spring Context. If you think you have"
              + " MessageProcessor classes implemented, check that your message processor classes"
              + " are injected into the Spring Context either by manually injecting them (in an"
              + " @Configuration class) or by having the message processor classes annotated with"
              + " either @Service, @Component, etc.";
      log.warning(logMessage);
    }

    return processors;
  }
}
