package frc.robot.subsystems.state.helpers;

public class ShooterHelpers {
    public static boolean shooterIsReady(double shooterPrimaryWheelVelocity, double shooterSecondaryWheelVelocity, double feederVelocity) {
    if(Math.abs(feederVelocity) > 40
          && Math.abs(shooterPrimaryWheelVelocity) > 75
          && Math.abs(shooterSecondaryWheelVelocity) > 75) {
        return true;
          }
      return false;
    }
}
