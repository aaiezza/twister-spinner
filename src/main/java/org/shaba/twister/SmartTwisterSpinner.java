package org.shaba.twister;

import one.util.streamex.StreamEx;

@lombok.Value
@lombok.EqualsAndHashCode(callSuper = true)
public class SmartTwisterSpinner extends AbstractTwisterSpinner {
  private final CommandOptions allPossibleCommandOptions;
  private final SpinHistory spinHistory;

  @lombok.experimental.Tolerate
  public SmartTwisterSpinner(final CommandOptions allPossibleCommandOptions) {
    this(allPossibleCommandOptions, new SpinHistory());
  }

  @Override
  public CommandOptions getCommandOptions() {
    return StreamEx.of(allPossibleCommandOptions.iterator())
        .remove(spinHistory::commandThatWouldChangeNothing)
        .remove(this::commandForTheSameBodyPart)
        .toSetAndThen(CommandOptions::new);
  }

  @Override
  public AbstractTwisterSpinner getSpinnerForResult(final Command command) {
    return new SmartTwisterSpinner(allPossibleCommandOptions, spinHistory.add(command));
  }

  private boolean commandForTheSameBodyPart(final Command command) {
    if (command instanceof BodyPartPlacementCommand) {
      return spinHistory
          .lastSpin()
          .filter(c -> c instanceof BodyPartPlacementCommand)
          .map(c -> ((BodyPartPlacementCommand) c).getBodyPart())
          .map(((BodyPartPlacementCommand) command).getBodyPart()::equals)
          .orElse(false);
    } else return false;
  }
}
