/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package test.com.clearlydecoded.messenger.discovery.samemessagetype;

import com.clearlydecoded.messenger.DefaultMessageProcessorRegistry;
import com.clearlydecoded.messenger.MessageProcessor;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * {@link SpringMessageProcessorRegistrySameMessageTypeIdTest} class is used to test registration of
 * a processors which process messages with the same string-based type ID.
 *
 * @author Yaakov Chaikin (yaakov@ClearlyDecoded.com)
 */
@SuppressWarnings("unchecked")
public class SpringMessageProcessorRegistrySameMessageTypeIdTest {

  @Test(expected = IllegalStateException.class)
  public void testSuccessfulProcessorRegistration() {

    DefaultMessageProcessorRegistry registry = new DefaultMessageProcessorRegistry();

    MyMessage1Processor processor1 = new MyMessage1Processor();
    MyMessage2Processor processor2 = new MyMessage2Processor();
    List processors = new ArrayList<MessageProcessor>();
    processors.add(processor1);
    processors.add(processor2);

    registry.addProcessors(processors);
  }
}
