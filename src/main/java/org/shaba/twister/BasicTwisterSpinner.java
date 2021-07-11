package org.shaba.twister;

@lombok.Value
@lombok.EqualsAndHashCode(callSuper = true)
public class BasicTwisterSpinner extends AbstractTwisterSpinner {
  private final CommandOptions commandOptions;
}
