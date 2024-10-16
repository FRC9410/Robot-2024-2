package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;

import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.SparkMaxAlternateEncoder;
import com.revrobotics.CANSparkBase.IdleMode;

import java.util.List;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean constants. This
 * class should not be used for any other purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants
{
  public static final class RobotConstants {
    public static final String kCtreCanBusName = "canivore";
  }

  public static final class DriveConstants {
    // Driving Parameters - Note that these are not the maximum capable speeds of
    // the robot, rather the allowed maximum speeds
    public static final double kMaxSpeedMetersPerSecond = 4.8;
    public static final double kMaxAngularSpeed = 2 * Math.PI; // radians per second

    public static final double kDirectionSlewRate = 1.2; // radians per second
    public static final double kMagnitudeSlewRate = 1.8; // percent per second (1 = 100%)
    public static final double kRotationalSlewRate = 2.0; // percent per second (1 = 100%)

    // Chassis configuration
    public static final double kTrackWidth = Units.inchesToMeters(26.5);
    // Distance between centers of right and left wheels on robot
    public static final double kWheelBase = Units.inchesToMeters(26.5);
    // Distance between front and back wheels on robot
    public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
        new Translation2d(kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

    // Angular offsets of the modules relative to the chassis in radians
    public static final double kFrontLeftChassisAngularOffset = -Math.PI / 2;
    public static final double kFrontRightChassisAngularOffset = 0;
    public static final double kBackLeftChassisAngularOffset = Math.PI;
    public static final double kBackRightChassisAngularOffset = Math.PI / 2;

    // SPARK MAX CAN IDs
    public static final int kFrontLeftDrivingCanId = 11;
    public static final int kRearLeftDrivingCanId = 13;
    public static final int kFrontRightDrivingCanId = 15;
    public static final int kRearRightDrivingCanId = 17;

    public static final int kFrontLeftTurningCanId = 10;
    public static final int kRearLeftTurningCanId = 12;
    public static final int kFrontRightTurningCanId = 14;
    public static final int kRearRightTurningCanId = 16;

    public static final boolean kGyroReversed = false;

    public static final double MaxSpeed = 6; // 6 meters per second desired top speed
    public static final double MaxShootingSpeed = 3; // 6 meters per second desired top speed
    public static final double MaxIntakingSpeed = 1; // 6 meters per second desired top speed
    public static final double MaxAngularRate = 1.5 * Math.PI; // 3/4 of a rotation per second max angular velocity

    public static final double staticKFF = 0; // meters per second

    public static final double forwardKP = 5.5;
    public static final double forwardkI = 0.0;
    public static final double forwardkD = 0.0;

    public static final double strafeKP = 0.08;
    public static final double strafekI = 0.0;
    public static final double strafekD = 0.0;
    
    public static final double rotationKP = 0.09;
    public static final double rotationkI = 0.0;
    public static final double rotationkD = 0.0;

    public static final double targetLockKFF = 0.0;
    public static final double targetLockKTolerance = 0.25;

    public static final List<Integer> moveToTags = List.of(4, 5, 7, 15);
  }

  public static final class ModuleConstants {
    // The MAXSwerve module can be configured with one of three pinion gears: 12T, 13T, or 14T.
    // This changes the drive speed of the module (a pinion gear with more teeth will result in a
    // robot that drives faster).
    public static final int kDrivingMotorPinionTeeth = 14;

    // Invert the turning encoder, since the output shaft rotates in the opposite direction of
    // the steering motor in the MAXSwerve Module.
    public static final boolean kTurningEncoderInverted = true;

    // Calculations required for driving motor conversion factors and feed forward
    public static final double kDrivingMotorFreeSpeedRps = NeoMotorConstants.kFreeSpeedRpm / 60;
    public static final double kWheelDiameterMeters = 0.0762;
    public static final double kWheelCircumferenceMeters = kWheelDiameterMeters * Math.PI;
    // 45 teeth on the wheel's bevel gear, 22 teeth on the first-stage spur gear, 15 teeth on the bevel pinion
    public static final double kDrivingMotorReduction = (45.0 * 22) / (kDrivingMotorPinionTeeth * 15);
    public static final double kDriveWheelFreeSpeedRps = (kDrivingMotorFreeSpeedRps * kWheelCircumferenceMeters)
        / kDrivingMotorReduction;

    public static final double kDrivingEncoderPositionFactor = (kWheelDiameterMeters * Math.PI)
        / kDrivingMotorReduction; // meters
    public static final double kDrivingEncoderVelocityFactor = ((kWheelDiameterMeters * Math.PI)
        / kDrivingMotorReduction) / 60.0; // meters per second

    public static final double kTurningEncoderPositionFactor = (2 * Math.PI); // radians
    public static final double kTurningEncoderVelocityFactor = (2 * Math.PI) / 60.0; // radians per second

    public static final double kTurningEncoderPositionPIDMinInput = 0; // radians
    public static final double kTurningEncoderPositionPIDMaxInput = kTurningEncoderPositionFactor; // radians

    public static final double kDrivingP = 0.04;
    public static final double kDrivingI = 0;
    public static final double kDrivingD = 0;
    public static final double kDrivingFF = 1 / kDriveWheelFreeSpeedRps;
    public static final double kDrivingMinOutput = -1;
    public static final double kDrivingMaxOutput = 1;

    public static final double kTurningP = 0.8;
    public static final double kTurningI = 0;
    public static final double kTurningD = 0;
    public static final double kTurningFF = 0;
    public static final double kTurningMinOutput = -1;
    public static final double kTurningMaxOutput = 1;

    public static final IdleMode kDrivingMotorIdleMode = IdleMode.kBrake;
    public static final IdleMode kTurningMotorIdleMode = IdleMode.kBrake;

    public static final int kDrivingMotorCurrentLimit = 50; // amps
    public static final int kTurningMotorCurrentLimit = 20; // amps
  }

  public static final class OIConstants {
    public static final int kDriverControllerPort = 0;
    public static final double kDriveDeadband = 0.05;
    
    public static final double LEFT_X_DEADBAND  = 0.05;
    public static final double LEFT_Y_DEADBAND  = 0.05;
    public static final double RIGHT_X_DEADBAND = 0.05;
    public static final double TURN_CONSTANT    = 6;
  }

  public static final class AutoConstants {
    public static final double kMaxSpeedMetersPerSecond = 3;
    public static final double kMaxAccelerationMetersPerSecondSquared = 3;
    public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
    public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;

    public static final double kPXController = 1;
    public static final double kPYController = 1;
    public static final double kPThetaController = 1;

    // Constraint for the motion profiled robot angle controller
    public static final TrapezoidProfile.Constraints kThetaControllerConstraints = new TrapezoidProfile.Constraints(
        kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);
  }

  public static final class NeoMotorConstants {
    public static final double kFreeSpeedRpm = 5676;
  }

  public static final class FieldConstants {
    public static final double centerLine = 8.22;

    public static final double speakerY = 5.55;
    public static final double blueSpeakerX = -0.2;
    public static final double redSpeakerX = centerLine * 2 - blueSpeakerX;

    public static final double ampY = 7.4;
    public static final double blueAmpX = 1.9;
    public static final double redAmpX = centerLine * 2 - blueAmpX;

    public static final double subwooferRadius = 1.5;

    public static final double stageLowY = 3.3;
    public static final double stageHighY = 4.9;
    public static final double blueStageX = 4.4;
    public static final double redStageX = centerLine * 2 - blueStageX;

    public static final double shooterRange = 2.0;
    public static final double ampRange = 0.1;
  }

  public static final class SubsystemConstants {
    public static final class IntakeWrist
    {
      public static final int kPrimaryWristCanId = 11;
      public static final int kSecondaryWristCanId = 12;

      public static double kP = 5; 
      public static double kI = 0;
      public static double kD = 0; 
      public static double kIz = 0; 
      public static double kFF = 0; 
      public static double kMaxOutput = 1; 
      public static double kMinOutput = -1;
      public static double kOffset = 0.7;
      public static double kMaxRotation = 0.64;
      public static double kMinRotation = 0.11;

      public static final SparkAbsoluteEncoder.Type kAbsEncType = SparkAbsoluteEncoder.Type.kDutyCycle;
      public static final int kCPR = 8192;

      public static final double maxAcc = 25000;
      public static final double maxVel = 11000;
      public static final double allowedError = 0;

      //copy the above array and subtract 20.7 from every value
    }
    
    public static final class IntakeRollers
    {
      public static final int kIntakeCanId = 10;
    }
    
    public static final class ShooterWrist
    {
      public static final int kPrimaryWristCanId = 31;
      public static final int kSecondaryWristCanId = 32;
  
      public static double kP = 0.5; 
      public static double kI = 0;
      public static double kD = 0; 
      public static double kIz = 0; 
      public static double kFF = 0; 
      public static double kMaxOutput = 0.5; 
      public static double kMinOutput = -0.5;
      public static double kMinRotation = 0.0;
      public static double kMaxRotation = 16.0;
  
      public static final double maxAcc = 25000;
      public static final double maxVel = 11000;
      public static final double allowedError = 0;
  
      public static final SparkAbsoluteEncoder.Type kAbsEncType = SparkAbsoluteEncoder.Type.kDutyCycle;
      public static final int kCPR = 8192;
  
      public static final double [][] wristAngles = {
        {1.13, 0.2},
        {0.77, 0.7},
        {0.59, 1.0},
        {0.44, 1.3},
        {0.4, 1.5},
        {0.34, 1.6}
      };
    }
    
    public static final class ShooterFeeder
    {
      public static final int kFeederCanId = 20;
    }
    
    public static final class ShooterWheels
    {
      public static final int kPrimaryWheelCanId = 21;
      public static final int kSecondaryWheelCanId = 22;
      public static final double kP = 0.56;
      public static final double kFF = 4.75;
      public static final double kSpeakerShooterSpeed = 0;
      public static final double kSpeakerFeederSpeed = -45;
    }
    
    public static final class Elevator
    {
    public static final int kPrimaryElevatorCanId = 41;
    public static final int kSecondaryElevatorCanId = 42;
    public static double kP = 0.5; 
    public static double kI = 0;
    public static double kD = 0; 
    public static double kIz = 0; 
    public static double kFF = 0; 
    public static double kMaxOutput = 0.5; 
    public static double kMinOutput = -0.5;
    public static double kOffset = 0.7;
    public static double kMaxRotation = -80;
    public static double kMinRotation = 0;

    public static final double maxAcc = 25000;
    public static final double maxVel = 11000;
    public static final double allowedError = 0;

    public static final int kCPR = 8192;
    public static final SparkMaxAlternateEncoder.Type kAltEncType = SparkMaxAlternateEncoder.Type.kQuadrature;
    }
    
    public static final class Leds
    {
      public static final int kLedId = 23;
      public static final String kLedBus = "rio";
    }
  }
}