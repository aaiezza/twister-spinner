package org.shaba.twister;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import one.util.streamex.StreamEx;
import org.shaba.twister.BodyPartPlacementCommand.Placement;
import org.shaba.twister.BodyPartPlacementCommand.SidedBodyPart;

@lombok.Value
public class CommandOptions implements Iterable<Command> {
  private final Set<Command> value;

  public CommandOptions(final Set<Command> options) {
    if (options.isEmpty()) throw new IllegalArgumentException("Command options cannot be empty");
    this.value = Collections.unmodifiableSet(options);
  }

  @Override
  public Iterator<Command> iterator() {
    return value.iterator();
  }

  public static CommandOptions classicCommands() {
    return StreamEx.of(SidedBodyPart.values())
        .cross(Placement.classicPlacements())
        .<Command>mapKeyValue(BodyPartPlacementCommand::new)
        .toSetAndThen(CommandOptions::new);
  }
}
