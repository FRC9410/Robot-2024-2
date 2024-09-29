// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.FieldConstants;
import frc.robot.subsystems.Vision.VisionType;
import frc.team9410.lib.LimelightHelpers;
import frc.team9410.lib.Utility;
import au.grapplerobotics.ConfigurationFailedException;
import au.grapplerobotics.LaserCan;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RobotState extends SubsystemBase {

  private final CommandSwerveDrivetrain drivetrain;
  private final Vision vision;
  private final ShooterFeeder feeder;
  private final ShooterWheels shooterWheels;
  private final CommandXboxController controller;
  private final NetworkTableInstance inst = NetworkTableInstance.getDefault();
  private final NetworkTable dashboardTable = inst.getTable("Dashboard");

  private final LaserCan intakeLaser = new LaserCan(13);

  private State state = State.IDLE;
  private String allianceColor = Utility.getAllianceColor();
  private double locationX = -1;
  private double locationY = -1;
  private double rotation = 0;

  private double targetX = -1;
  private double targetY = -1;
  private Optional<Rotation2d> targetRotation = Optional.empty();

  private boolean isFollowingPath = false;

  public RobotState(CommandSwerveDrivetrain drivetrain, Vision vision, ShooterFeeder shooterFeeder, ShooterWheels shooterWheels, CommandXboxController controller) {
    this.drivetrain = drivetrain;
    this.vision = vision;
    this.feeder = shooterFeeder;
    this.shooterWheels = shooterWheels;
    this.controller = controller;

    try {
      intakeLaser.setRangingMode(LaserCan.RangingMode.SHORT);
      intakeLaser.setRegionOfInterest(new LaserCan.RegionOfInterest(8, 8, 16, 16));
      intakeLaser.setTimingBudget(LaserCan.TimingBudget.TIMING_BUDGET_33MS);
    } catch (ConfigurationFailedException e) {
      System.out.println("Configuration failed! " + e);
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    Pose2d pose = drivetrain.getPose();
    locationX = pose.getX();
    locationY = pose.getY();
    rotation = pose.getRotation().getDegrees();

    if (dashboardTable.getEntry("Development Mode").getBoolean(false)
      && state != State.DEV_MODE) {
      state = State.DEV_MODE;
    } else if (state == State.DEV_MODE) {
      state = State.IDLE;
    }

    if (!state.equals(State.DEV_MODE)
      && !state.equals(State.DEMO_MODE)
      && controller.a().getAsBoolean()) {
      isFollowingPath = controller.a().getAsBoolean();
    } else {
      isFollowingPath = false;
    }

    if (State.DEMO_MODE.equals(state)) {
      clearDestination();
    } else if (State.DEV_MODE.equals(state)) {
      clearDestination();
    } else if (controller.getLeftTriggerAxis() > 0.5
      && !hasGamePiece()
      && hasTarget()) {
      clearDestination();
      state = State.INTAKING;

      if (targetRotation.isPresent()) {
        targetRotation = Optional.empty();
      }
    } else if (controller.getLeftTriggerAxis() > 0.5
      && hasGamePiece()
      && hasTarget()
      && isInZone()) {
      state = State.SHOOTING_READY;
      targetRotation = Optional.of(Rotation2d.fromRadians(getShootingAngle()));
      setSpeakerDestination(allianceColor);
    } else if (controller.getLeftTriggerAxis() > 0.5
      && hasGamePiece()
      && hasTarget()
      && isWithinRange()
      && shooterIsReady()) {
      state = State.SHOOTING;
      targetRotation = Optional.of(Rotation2d.fromRadians(getShootingAngle()));
    } else if (controller.rightBumper().getAsBoolean()
      && hasGamePiece()
      && hasTarget()
      && isInZone()) {
      state = State.DUNKING_READY;
      setAmpAngle();
      setAmpDestination(allianceColor);
    } else if (controller.rightBumper().getAsBoolean()
      && hasGamePiece()
      && hasTarget()
      && isWithinRange()
      && shooterIsReady()) {
      state = State.DUNKING;
      setAmpAngle();
    } else if (controller.povLeft().getAsBoolean()) {
      state = State.CLIMBING_READY;
      setStageDestination(allianceColor, allianceColor.equals("blue") ? "high" : "low");
    } else if (controller.povRight().getAsBoolean()) {
      state = State.CLIMBING_READY;
      setStageDestination(allianceColor, allianceColor.equals("blue") ? "low" : "high");
    } else {
      state = State.IDLE;
      clearDestination();
      
      if (targetRotation.isPresent()) {
        targetRotation = Optional.empty();
      }
    }
  }

  public State getState() {
    return state;
  }

  public String getAllianceColor() {
    return allianceColor;
  }

  public double getLocationX() {
    return locationX;
  }

  public double getLocationY() {
    return locationY;
  }

  public double getRotation() {
    return rotation;
  }

  public double getTargetX() {
    return targetX;
  }

  public double getTargetY() {
    return targetY;
  }

  public Optional<Rotation2d> getTargetRotation() {
    return targetRotation;
  }

  public boolean getIsFollowingPath() {
    return isFollowingPath;
  }

  public enum State {
      IDLE,
      INTAKING,
      SHOOTING,
      SHOOTING_READY,
      DUNKING,
      DUNKING_READY,
      CLIMBING_READY,
      DEV_MODE,
      DEMO_MODE
  }

  public boolean hasGamePiece() {
    return !(intakeLaser.getMeasurement().distance_mm > 450
      && intakeLaser.getMeasurement().distance_mm < 1000);
  }

  public boolean hasTarget() {
    double tx = vision.getTx(VisionType.GAME_PIECE);
    return LimelightHelpers.getTargetCount(vision.GAME_PIECE_TABLE_NAME) > 0
      && tx != 0 && tx > 1 && tx < 10;
  }

  public boolean isInZone() {
    if (allianceColor.equals("blue") && locationX < FieldConstants.centerLine) {
      return true;
    } else if (allianceColor.equals("red") && locationX > FieldConstants.centerLine) {
      return true;
    } else {
      return false;
    }
  }

  public boolean isWithinRange() {
    if (controller.getRightTriggerAxis() > 0.5) {
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

  public double getShootingAngle() {
    if (!isWithinRange()) {
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

  public void setAmpAngle() {
    if (allianceColor.equals("blue") && locationX > FieldConstants.centerLine) {
      targetRotation = Optional.of(Rotation2d.fromDegrees(0));
    } else if (allianceColor.equals("red") && locationX > FieldConstants.centerLine) {
      targetRotation = Optional.of(Rotation2d.fromDegrees(180));
    } else {
      targetRotation = Optional.of(Rotation2d.fromDegrees(90));
    }
  }

  public boolean shooterIsReady() {
    double feederVelocity = feeder.getVelocity();
    double shooterPrimaryWheelVelocity = shooterWheels.getPrimaryWheelVelocity();
    double shooterSecondaryWheelVelocity = shooterWheels.getSecondaryWheelVelocity();

    return feederVelocity > 60
      && shooterPrimaryWheelVelocity > 75
      && shooterSecondaryWheelVelocity > 75;
  }

  public void setAmpDestination(String allianceColor) {
    targetX = allianceColor.equals("blue") ? FieldConstants.blueAmpX : FieldConstants.redAmpX;
    targetY = FieldConstants.ampY;
  }

  public void setSpeakerDestination(String allianceColor) {
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

    targetX = intersectX;
    targetY = intersectY;
  }

  public void setStageDestination(String allianceColor, String location) {
    targetX = allianceColor.equals("blue") ? FieldConstants.blueStageX : FieldConstants.redStageX;
    targetY = location.equals("high") ? FieldConstants.stageHighY : FieldConstants.stageLowY;
  }

  public void clearDestination() {
    if (targetX != -1) {
      targetX = -1;
    }

    if (targetY != -1) {
      targetY = -1;
    }
  }
}
