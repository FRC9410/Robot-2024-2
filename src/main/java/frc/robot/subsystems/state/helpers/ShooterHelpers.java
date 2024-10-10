package frc.robot.subsystems.state.helpers;

public class ShooterHelpers {
    public static boolean shooterIsReady(double shooterPrimaryWheelVelocity, double shooterSecondaryWheelVelocity, double feederVelocity) {
      return Math.abs(feederVelocity) > 60
          && Math.abs(shooterPrimaryWheelVelocity) > 75
          && Math.abs(shooterSecondaryWheelVelocity) > 75;
    }
}
