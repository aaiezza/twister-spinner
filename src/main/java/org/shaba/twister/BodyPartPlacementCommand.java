package org.shaba.twister;

import static org.shaba.twister.BodyPartPlacementCommand.SidedBodyPart.Side.LEFT;
import static org.shaba.twister.BodyPartPlacementCommand.SidedBodyPart.Side.RIGHT;

import java.util.List;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;

@lombok.Value
public class BodyPartPlacementCommand implements Command {
  private final BodyPart bodyPart;
  private final Placement placement;

  @Override
  public String getCommandMessage() {
    return StringUtils.capitalize(
        String.format("%s %s", bodyPart.getBodyPartName(), placement.getPlacementName()).trim());
  }

  public static interface BodyPart {
    public String getBodyPartName();
  }

  public static interface EnumeratedBodyPart extends BodyPart {
    public String name();

    @Override
    public default String getBodyPartName() {
      return name().toLowerCase().replaceAll("_", " ").trim();
    }
  }

  public enum LoneBodyPart implements EnumeratedBodyPart {
    HEAD;
  }

  @lombok.RequiredArgsConstructor
  @lombok.Getter
  public enum SidedBodyPart implements EnumeratedBodyPart {
    LEFT_HAND(LEFT),
    RIGHT_HAND(RIGHT),
    LEFT_FOOT(LEFT),
    RIGHT_FOOT(RIGHT);

    private final Side side;

    public enum Side {
      LEFT,
      RIGHT;
    }
  }

  public static interface Placement {
    public String getPlacementName();

    public static List<Placement> classicPlacements() {
      return StreamEx.<Placement>of(ClassicPlacements.values()).toList();
    }

    @lombok.RequiredArgsConstructor
    @lombok.Getter
    public enum ClassicPlacements implements Placement {
      // AIR("💨"),
      // BLUE("🔵"),
      // RED("🔴"),
      // GREEN("🟢"),
      // YELLOW("🟡");
      AIR("in the air"),
      BLUE("blue"),
      RED("red"),
      GREEN("green"),
      YELLOW("yellow");

      private final String placementName;
    }
  }
}
