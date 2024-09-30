package frc.robot.subsystems.state.helpers;

import java.util.HashMap;
import java.util.Map;

import frc.robot.Constants.FieldConstants;

public class PositionHelpers {

    public static boolean isInZone(String allianceColor, double locationX) {
      if (allianceColor.equals("blue") && locationX < FieldConstants.centerLine) {
        return true;
      } else if (allianceColor.equals("red") && locationX > FieldConstants.centerLine) {
        return true;
      } else {
        return false;
      }
    }
  
    public static boolean isWithinSpeakerRange(String allianceColor, double locationX, double locationY) {
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
    }

  public static double getShootingAngle(String allianceColor, double locationX, double locationY) {
    if (allianceColor.equals("blue") && locationX < FieldConstants.centerLine) {
      return Math.atan2(FieldConstants.speakerY - locationY, FieldConstants.blueSpeakerX - locationX);
    } else if (allianceColor.equals("red") && locationX > FieldConstants.centerLine) {
      return Math.atan2(FieldConstants.speakerY - locationY, FieldConstants.redSpeakerX - locationX);
    } else {
      return 0;
    }
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

  public static double getAmpAngle(String allianceColor, double locationX) {
    if (allianceColor.equals("blue") && locationX > FieldConstants.centerLine) {
      return 0;
    } else if (allianceColor.equals("red") && locationX > FieldConstants.centerLine) {
      return 180;
    } else {
      return 90;
    }
  }
  
    public static boolean isWithinAmpRange(String allianceColor, double locationX, double locationY) {
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

  public static Map<String, Double> getAmpDestination(String allianceColor) {
    Map<String, Double> coordinates = new HashMap<>();

    coordinates.put("x", allianceColor.equals("blue") ? FieldConstants.blueAmpX : FieldConstants.redAmpX);
    coordinates.put("y", FieldConstants.ampY);

    return coordinates;
  }

  public static Map<String, Double> getStageDestination(String allianceColor, String location) {

    Map<String, Double> coordinates = new HashMap<>();

    coordinates.put("x", allianceColor.equals("blue") ? FieldConstants.blueStageX : FieldConstants.redStageX);
    coordinates.put("y", location.equals("high") ? FieldConstants.stageHighY : FieldConstants.stageLowY);

    return coordinates;
  }
}
