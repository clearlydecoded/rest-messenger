package com.clearlydecoded.commander.discovery;

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
