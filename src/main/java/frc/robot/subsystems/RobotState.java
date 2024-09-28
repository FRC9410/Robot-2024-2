// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.FieldConstants;
import frc.robot.subsystems.Vision.VisionType;
import frc.team9410.lib.LimelightHelpers;
import frc.team9410.lib.Utility;
import au.grapplerobotics.ConfigurationFailedException;
import au.grapplerobotics.LaserCan;

import java.util.Optional;

public class RobotState extends SubsystemBase {

  private final CommandSwerveDrivetrain drivetrain;
  private final Vision vision;
  private final ShooterFeeder feeder;
  private final ShooterWheels shooterWheels;
  private final CommandXboxController controller;

  private final LaserCan intakeLaser;

  private State state;
  private String allianceColor;
  private double locationX;
  private double locationY;
  private double rotation;

  private double targetX;
  private double targetY;
  private Optional<Rotation2d> targetRotation;

  private boolean isFollowingPath;

  public RobotState(CommandSwerveDrivetrain drivetrain, Vision vision, ShooterFeeder shooterFeeder, ShooterWheels shooterWheels, CommandXboxController controller) {
    this.drivetrain = drivetrain;
    this.vision = vision;
    this.feeder = shooterFeeder;
    this.shooterWheels = shooterWheels;
    this.controller = controller;

    intakeLaser = new LaserCan(13);

    state = State.IDLE;
    allianceColor = Utility.getAllianceColor();
    locationX = -1;
    locationY = -1;
    rotation = 0;
    targetX = -1;
    targetY = -1;
    targetRotation = Optional.empty();
    isFollowingPath = false;

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
    isFollowingPath = controller.a().getAsBoolean();

    if (controller.getLeftTriggerAxis() > 0.5
      && !hasGamePiece()
      && hasTarget()) {
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
      targetRotation = Optional.of(Rotation2d.fromDegrees(90));
    } else if (controller.rightBumper().getAsBoolean()
      && hasGamePiece()
      && hasTarget()
      && isWithinRange()
      && shooterIsReady()) {
      state = State.DUNKING;
      targetRotation = Optional.of(Rotation2d.fromDegrees(90));
    }  else {
      state = State.IDLE;
      
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
      DUNKING_READY
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
      return allianceColor.equals("blue") ? 180 : 0;
    }

    if (allianceColor.equals("blue") && locationX < FieldConstants.centerLine) {
      return Math.atan2(FieldConstants.speakerY - locationY, FieldConstants.blueSpeakerX - locationX);
    } else if (allianceColor.equals("red") && locationX > FieldConstants.centerLine) {
      return Math.atan2(FieldConstants.speakerY - locationY, FieldConstants.redSpeakerX - locationX);
    } else {
      return 0;
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
}
