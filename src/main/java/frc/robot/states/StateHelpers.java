
package frc.robot.states;

import frc.robot.subsystems.Vision.VisionType;
import frc.team9410.lib.LimelightHelpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Constants.FieldConstants;
import frc.robot.subsystems.RobotState;
import frc.robot.subsystems.Vision;

public class StateHelpers {
    
  public static boolean hasGamePiece(int laserDistance) {
    return !(laserDistance > 450 && laserDistance < 1000);
  }

  public static boolean hasTarget(Vision vision) {
    double ty = vision.getTx(VisionType.GAME_PIECE);
    return vision.hasTarget(VisionType.GAME_PIECE) && ty != 0 && ty > 1 && ty < 10;
  }

  public static boolean isInZone(String allianceColor, double locationX) {
    if (allianceColor.equals("blue") && locationX < FieldConstants.centerLine) {
      return true;
    } else if (allianceColor.equals("red") && locationX > FieldConstants.centerLine) {
      return true;
    } else {
      return false;
    }
  }

  public static boolean isWithinRange(String allianceColor, double locationX, double locationY, boolean rightTrigger) {
    if (rightTrigger) {
      if (allianceColor.equals("blue") && locationX < FieldConstants.centerLine) {
        double distanceToTarget = Math.sqrt(Math.pow(locationX - FieldConstants.blueSpeakerX, 2)
            + Math.pow(locationY - FieldConstants.speakerY, 2));
        return distanceToTarget < FieldConstants.shooterRange;
      } else if (allianceColor.equals("red") && locationX > FieldConstants.centerLine) {
        double distanceToTarget = Math.sqrt(Math.pow(locationX - FieldConstants.redSpeakerX, 2)
            + Math.pow(locationY - FieldConstants.speakerY, 2));
        return distanceToTarget < FieldConstants.shooterRange;
      } else {
        return false;
      }
    } else {
      if (allianceColor.equals("blue") && locationX < FieldConstants.centerLine) {
        double distanceToTarget = Math.sqrt(Math.pow(locationX - FieldConstants.blueAmpX, 2)
            + Math.pow(locationY - FieldConstants.ampY, 2));
        return distanceToTarget < FieldConstants.ampRange;
      } else if (allianceColor.equals("red") && locationX > FieldConstants.centerLine) {
        double distanceToTarget = Math.sqrt(Math.pow(locationX - FieldConstants.redAmpX, 2)
            + Math.pow(locationY - FieldConstants.ampY, 2));
        return distanceToTarget < FieldConstants.ampRange;
      } else {
        return false;
      }
    }
  }

  public static double getShootingAngle(String allianceColor, double locationX, double locationY, boolean rightTrigger) {
    if (!StateHelpers.isWithinRange(allianceColor, locationX, locationY, rightTrigger)) {
      return allianceColor.equals("blue") ? 0 : 180;
    }

    if (allianceColor.equals("blue") && locationX < FieldConstants.centerLine) {
      return Math.atan2(FieldConstants.speakerY - locationY, FieldConstants.blueSpeakerX - locationX);
    } else if (allianceColor.equals("red") && locationX > FieldConstants.centerLine) {
      return Math.atan2(FieldConstants.speakerY - locationY, FieldConstants.redSpeakerX - locationX);
    } else {
      return 0;
    }
  }

  public static Optional<Rotation2d> getAmpAngle(String allianceColor, double locationX) {
    if (allianceColor.equals("blue") && locationX > FieldConstants.centerLine) {
      return Optional.of(Rotation2d.fromDegrees(0));
    } else if (allianceColor.equals("red") && locationX > FieldConstants.centerLine) {
      return Optional.of(Rotation2d.fromDegrees(180));
    } else {
      return Optional.of(Rotation2d.fromDegrees(90));
    }
  }

  public static boolean shooterIsReady(double shooterPrimaryWheelVelocity, double shooterSecondaryWheelVelocity, double feederVelocity) {
    return feederVelocity > 60
        && shooterPrimaryWheelVelocity > 75
        && shooterSecondaryWheelVelocity > 75;
  }

  public static Map<String, Double> getAmpDestination(String allianceColor) {
    Map<String, Double> coordinates = new HashMap<>();

    coordinates.put("x", allianceColor.equals("blue") ? FieldConstants.blueAmpX : FieldConstants.redAmpX);
    coordinates.put("y", FieldConstants.ampY);

    return coordinates;
  }

  public static Map<String, Double> getSpeakerDestination(String allianceColor, double locationX, double locationY) {
    Map<String, Double> coordinates = new HashMap<>();
    double speakerX = allianceColor.equals("blue") ? FieldConstants.blueSpeakerX : FieldConstants.redSpeakerX;
    double speakerY = FieldConstants.speakerY;

    // Calculate direction vector
    double directionX = speakerX - locationX;
    double directionY = speakerY - locationY;

    // Calculate the magnitude of the direction vector
    double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);

    // Normalize the direction vector
    double unitDirectionX = directionX / magnitude;
    double unitDirectionY = directionY / magnitude;

    // Calculate the intersection point (the point on the circle's boundary)
    double intersectX = speakerX - FieldConstants.subwooferRadius * unitDirectionX;
    double intersectY = speakerY - FieldConstants.subwooferRadius * unitDirectionY;

    coordinates.put("x", intersectX);
    coordinates.put("y", intersectY);

    return coordinates;
  }

  public static Map<String, Double> getStageDestination(String allianceColor, String location) {

    Map<String, Double> coordinates = new HashMap<>();

    coordinates.put("x", allianceColor.equals("blue") ? FieldConstants.blueStageX : FieldConstants.redStageX);
    coordinates.put("y", location.equals("high") ? FieldConstants.stageHighY : FieldConstants.stageLowY);

    return coordinates;
  }
}