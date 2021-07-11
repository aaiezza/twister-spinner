package org.shaba.twister;

import java.util.Collections;
import java.util.List;
import one.util.streamex.StreamEx;

public abstract class AbstractTwisterSpinner {
  public abstract CommandOptions getCommandOptions();

  public AbstractTwisterSpinner getSpinnerForResult(final Command command) {
    return this;
  }

  public final SpinResult spin() {
    final Command command =
        StreamEx.of(getCommandOptions().iterator())
            .toListAndThen(AbstractTwisterSpinner::chooseRandomly);

    return new SpinResult(command, getSpinnerForResult(command));
  }

  private static <T> T chooseRandomly(final List<T> collection) {
    Collections.shuffle(collection);
    return collection.get(0);
  }

  @lombok.Value
  public static class SpinResult {
    private final Command command;
    private final AbstractTwisterSpinner twisterSpinner;
  }
}
