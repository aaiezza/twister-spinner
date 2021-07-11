package org.shaba.twister;

import static java.util.function.UnaryOperator.identity;
import static org.shaba.twister.BodyPartPlacementCommand.Placement.ClassicPlacements.AIR;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.shaba.twister.BodyPartPlacementCommand.BodyPart;
import org.shaba.twister.BodyPartPlacementCommand.Placement;
import org.shaba.twister.BodyPartPlacementCommand.SidedBodyPart;

@lombok.Value
public class SpinHistory implements Iterable<Command> {
  private final List<Command> value;

  public SpinHistory(final Command... commands) {
    value =
        StreamEx.of(commands).toCollectionAndThen(LinkedList::new, Collections::unmodifiableList);
  }

  public SpinHistory add(final Command command) {
    return new SpinHistory(StreamEx.of(value).append(command).toArray(Command[]::new));
  }

  public boolean isEmpty() {
    return 0 >= value.size();
  }

  public Optional<Command> lastSpin() {
    return isEmpty() ? Optional.empty() : Optional.of(value.get(value.size() - 1));
  }

  public boolean commandThatWouldChangeNothing(final Command command) {
    return EntryStream.of(getCurrentBodyPartPlacements())
        .mapKeyValue(BodyPartPlacementCommand::new)
        .anyMatch(command::equals);
  }

  @Override
  public Iterator<Command> iterator() {
    return value.iterator();
  }

  public Map<BodyPart, BodyPartPlacementCommand> getCurrentBodyPartPlacementCommands() {
    final Map<BodyPart, BodyPartPlacementCommand> placements = new HashMap<>();

    StreamEx.of(SidedBodyPart.values())
        .mapToEntry(identity(), bp -> new BodyPartPlacementCommand(bp, AIR))
        .forKeyValue(placements::put);

    StreamEx.of(iterator())
        .filter(c -> c instanceof BodyPartPlacementCommand)
        .<BodyPartPlacementCommand>map(c -> (BodyPartPlacementCommand) c)
        .mapToEntry(BodyPartPlacementCommand::getBodyPart, identity())
        .forKeyValue(placements::put);

    return Collections.unmodifiableMap(placements);
  }

  public Map<BodyPart, Placement> getCurrentBodyPartPlacements() {
    final Map<BodyPart, Placement> placements = new HashMap<>();

    StreamEx.of(SidedBodyPart.values())
        .mapToEntry(identity(), bp -> AIR)
        .forKeyValue(placements::put);

    EntryStream.of(getCurrentBodyPartPlacementCommands())
        .mapValues(BodyPartPlacementCommand::getPlacement)
        .forKeyValue(placements::put);

    return Collections.unmodifiableMap(placements);
  }
}
