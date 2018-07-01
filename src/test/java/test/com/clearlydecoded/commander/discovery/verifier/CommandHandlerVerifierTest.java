/*
 * Copyright 2018 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package test.com.clearlydecoded.commander.discovery.verifier;

import com.clearlydecoded.commander.discovery.CommandHandlerVerifier;
import org.junit.Test;

/**
 * {@link CommandHandlerVerifierTest} class is a test class for {@link CommandHandlerVerifier}.
 */
public class CommandHandlerVerifierTest {

  @Test
  public void testVerifyCommandHandlerCompatibility() {
    CommandHandlerVerifier.verifyCommandHandlerCompatibility(new MockCommandHandler());
  }

  @Test(expected = IllegalStateException.class)
  public void testVerifyCommandHandlerCompatibilityNoDefaultConstructor() {
    CommandHandlerVerifier
        .verifyCommandHandlerCompatibility(new MockCommandWithNoDefaultConstructorHandler());
  }

  @Test(expected = IllegalStateException.class)
  public void testVerifyCommandHandlerCompatibilityNoPublicConstructor() {
    CommandHandlerVerifier
        .verifyCommandHandlerCompatibility(new MockCommandWithNoPublicConstructorHandler());
  }

  @Test(expected = IllegalStateException.class)
  public void testVerifyCommandHandlerCompatibilityBadCommand() {
    CommandHandlerVerifier
        .verifyCommandHandlerCompatibility(new BadMockCommandHandler());
  }
}
