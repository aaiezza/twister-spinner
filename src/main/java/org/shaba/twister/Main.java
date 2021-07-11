package org.shaba.twister;

import io.airlift.log.Logger;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import one.util.streamex.EntryStream;
import org.shaba.twister.AbstractTwisterSpinner.SpinResult;
import org.shaba.twister.BodyPartPlacementCommand.BodyPart;
import org.shaba.twister.BodyPartPlacementCommand.Placement;

public class Main {
  private static Logger logger = Logger.get(Main.class);

  public static void main(final String... args) {
    logger.info("Let's play Twister!");

    final AtomicReference<SmartTwisterSpinner> spinner =
        new AtomicReference<>(new SmartTwisterSpinner(CommandOptions.classicCommands()));
    final Scanner sc = new Scanner(System.in);

    while (sc.hasNextLine()) {
      sc.nextLine();

      final SpinResult spinResult = spinner.get().spin();

      spinner.set((SmartTwisterSpinner) spinResult.getTwisterSpinner());

      System.out.println(spinResult.getCommand().getCommandMessage());
      System.out.println(placements(spinner.get()));
      System.out.println();
    }

    sc.close();
  }

  private static Map<BodyPart, String> placements(SmartTwisterSpinner subject) {
    return EntryStream.of(subject.getSpinHistory().getCurrentBodyPartPlacements())
        .mapValues(Placement::getPlacementName)
        .toImmutableMap();
  }
}
