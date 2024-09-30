package frc.robot.subsystems.state.helpers;

public class IntakeHelpers {
    public static boolean hasGamePiece(int laserDistance) {
      return !(laserDistance > 450 && laserDistance < 1000);
    }
}
