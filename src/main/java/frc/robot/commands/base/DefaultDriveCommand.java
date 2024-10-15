package frc.robot.commands.base;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;

import frc.robot.Constants.DriveConstants;
import frc.robot.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.CommandSwerveDrivetrain.DriveMode;
import frc.robot.subsystems.StateMachine.State;
import frc.robot.subsystems.StateMachine;

import frc.team9410.lib.Utility;

import java.util.List;

public class DefaultDriveCommand extends Command {
  CommandSwerveDrivetrain drivetrain;
  CommandXboxController controller;
  StateMachine robotState;
  Command followPathCommand;

  private HolonomicDriveController holonomicController;

  public DefaultDriveCommand(CommandSwerveDrivetrain drivetrain, CommandXboxController controller, StateMachine robotState) {
    this.drivetrain = drivetrain;
    this.controller = controller;
    this.robotState = robotState;
    this.holonomicController = new HolonomicDriveController(new PIDController(1, 0, 0), new PIDController(1.0, 0, 0),
        new ProfiledPIDController(9.0, 0, 0.03,
            new TrapezoidProfile.Constraints(TunerConstants.kSpeedAt12VoltsMps, 3.8)));
    addRequirements(drivetrain);
  }

  @Override
  public void execute() {
    // if (controller.a().getAsBoolean()
    //   && robotState.getCommandData("targetX") != null
    //   && robotState.getCommandData("targetY") != null) {
    //   followTrajectory();
    // }
    if (robotState.getCommandData("targetRotation") != null && robotState.getState().equals(State.SHOOTING_READY)){
      // if (followPathCommand != null) {
      //   followPathCommand.cancel();
      // }

      double allianceRotation = robotState.getAllianceColor() == "red" ? Math.PI : 0;
      double rotation = (double) robotState.getSubsystemData("rotation");
      rotation += allianceRotation;
      double targetRotation = (double) robotState.getCommandData("targetRotation");
      double rotationDiff = normalizeAngle(targetRotation - rotation);
      double rps = getRpsDistance(rotationDiff);
      

      drivetrain.drive(
        // robotState.getState() == State.INTAKING ? -0.5 * DriveConstants.MaxSpeed : Utility.getSpeed(controller.getLeftY() * getDirection()) * DriveConstants.MaxSpeed,
        Utility.getSpeed(controller.getLeftY()) * DriveConstants.MaxSpeed,
        Utility.getSpeed(controller.getLeftX()) * DriveConstants.MaxSpeed,
        rps,
        // robotState.getState() == State.INTAKING ? DriveMode.ROBOT_RELATIVE : DriveMode.FIELD_RELATIVE);
        DriveMode.FIELD_RELATIVE);
    }
    else if (robotState.getCommandData("targetRotation") != null && robotState.getState().equals(State.INTAKING)){
      // if (followPathCommand != null) {
      //   followPathCommand.cancel();
      // }
      double targetRotation = (double) robotState.getCommandData("targetRotation");
      double rps = getRpsDistance(-targetRotation);
      

      drivetrain.drive(
        // -0.5 * DriveConstants.MaxSpeed,
        Utility.getSpeed(controller.getLeftY()) * DriveConstants.MaxSpeed,
        Utility.getSpeed(controller.getLeftX()) * DriveConstants.MaxSpeed,
        rps,
        // DriveMode.ROBOT_RELATIVE);
        DriveMode.FIELD_RELATIVE);
    }
    else {
    //   if (followPathCommand != null) {
    //     followPathCommand.cancel();
    //   }
      
      drivetrain.drive(
        Utility.getSpeed(controller.getLeftY()) * DriveConstants.MaxSpeed,
        Utility.getSpeed(controller.getLeftX()) * DriveConstants.MaxSpeed,
        Utility.getSpeed(controller.getRightX()) * DriveConstants.MaxSpeed,
        DriveMode.FIELD_RELATIVE);
    }
  } 

  private void followTrajectory() {
    if (!followPathCommand.isScheduled()) {
      Rotation2d targetRotation = robotState.getCommandData("targetRotation") != null
      ? Rotation2d.fromDegrees((double) robotState.getCommandData("targetRotation"))
      : Rotation2d.fromDegrees((double) robotState.getSubsystemData("rotation"));
      
      List<Translation2d> bezierPoints = PathPlannerPath.bezierFromPoses(
        new Pose2d((double) robotState.getCommandData("targetX"),
          (double) robotState.getCommandData("targetY"),
          Rotation2d.fromDegrees(90))
      );

      PathPlannerPath path = new PathPlannerPath(
            bezierPoints,
            new PathConstraints(3.0, 3.0, 2 * Math.PI, 4 * Math.PI),
            new GoalEndState(0.0, targetRotation)
      );
      
      followPathCommand = AutoBuilder.followPath(path);
    }
  }

  @Override
  public void end(boolean interrupted) {
    drivetrain.drive(0, 0, 0, DriveMode.FIELD_RELATIVE);
  }

  public double getRPS(Rotation2d setpoint) {
    // Increase kP based on horizontal velocity to reduce lag
    double vy = drivetrain.getChassisSpeeds().vyMetersPerSecond; // Horizontal velocity
    double kp = DriveConstants.rotationKP;
    kp *= Math.max(1, vy * 1.5);
    holonomicController.getThetaController().setP(kp);


    return holonomicController.calculate(drivetrain.getPose(),
        drivetrain.getPose(), 0.0, setpoint).omegaRadiansPerSecond;
  }

  public double getRpsDistance(double distance) {
    // Increase kP based on horizontal velocity to reduce lag
    double vy = drivetrain.getChassisSpeeds().vyMetersPerSecond; // Horizontal velocity
    double kp = DriveConstants.rotationKP;
    // kp *= vy * 1.5;

    return distance * kp;
  }
  public double normalizeAngle(double angle) {
    return (angle + 180) % 360 - 180;
  }
}