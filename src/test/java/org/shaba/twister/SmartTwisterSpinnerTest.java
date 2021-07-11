package org.shaba.twister;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.shaba.twister.BodyPartPlacementCommand.Placement.ClassicPlacements.RED;
import static org.shaba.twister.BodyPartPlacementCommand.SidedBodyPart.RIGHT_HAND;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.shaba.twister.AbstractTwisterSpinner.SpinResult;

class SmartTwisterSpinnerTest {
  private SmartTwisterSpinner subject;

  @BeforeEach
  void setUp() {}

  @Test
  void shouldWork() {
    subject =
        new SmartTwisterSpinner(
            CommandOptions.classicCommands(),
            new SpinHistory(new BodyPartPlacementCommand(RIGHT_HAND, RED)));

    final SpinResult spinResult = subject.spin();

    assertThat(spinResult.getTwisterSpinner())
        .asInstanceOf(type(SmartTwisterSpinner.class))
        .satisfies(
            spinner -> {
              assertThat(spinner.getSpinHistory().lastSpin()).hasValue(spinResult.getCommand());
            });

    assertThat(subject.getCommandOptions())
        .doesNotContain(subject.getSpinHistory().lastSpin().get());
  }

  @Test
  void shouldWorkToo() {
    subject = new SmartTwisterSpinner(CommandOptions.classicCommands());

    final AtomicInteger counter = new AtomicInteger();
    while (counter.get() > 20) {

      final SpinResult spinResult = subject.spin();

      subject = (SmartTwisterSpinner) spinResult.getTwisterSpinner();

      counter.incrementAndGet();
    }
  }
}
