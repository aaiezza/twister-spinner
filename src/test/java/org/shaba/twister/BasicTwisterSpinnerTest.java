package org.shaba.twister;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BasicTwisterSpinnerTest {
  private BasicTwisterSpinner subject;

  @BeforeEach
  void setUp() {
    subject = new BasicTwisterSpinner(CommandOptions.classicCommands());
  }

  @Test
  void shouldWork() {
    assertThat(subject.spin().getCommand()).isIn(subject.getCommandOptions());
  }
}
