package frc.robot.subsystems.state.helpers;

public class ShooterHelpers {
    public static boolean shooterIsReady(double shooterPrimaryWheelVelocity, double shooterSecondaryWheelVelocity, double feederVelocity) {
      return feederVelocity > 60
          && shooterPrimaryWheelVelocity > 75
          && shooterSecondaryWheelVelocity > 75;
    }
}
