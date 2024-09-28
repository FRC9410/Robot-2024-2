package frc.robot;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import frc.robot.subsystems.Subsystems;
import frc.robot.commands.base.DefaultDriveCommand;
import frc.robot.commands.base.DefaultIntakeRollersCommand;
import frc.robot.commands.base.DefaultIntakeWristCommand;
import frc.robot.commands.base.DefaultShooterFeederCommand;
import frc.robot.commands.base.DefaultShooterWheelsCommand;
import frc.robot.commands.base.DefaultShooterWristCommand;

public class RobotContainer {
  private final CommandXboxController driverController = new CommandXboxController(0);
  private final CommandXboxController copilotController = new CommandXboxController(1);
  private Subsystems subsystems = new Subsystems(driverController);
  // private final Telemetry logger = new Telemetry(DriveConstants.MaxSpeed);

  public RobotContainer() {
    // subsystems.getDrivetrain().registerTelemetry(logger::telemeterize);
    configurePilotBindings();
    configureCopilotBindings();
    configureDefaultBindings();
  }

  private void configurePilotBindings() {
    driverController.start().onTrue(subsystems.getDrivetrain().runOnce(() -> {
      subsystems.getDrivetrain().resetPose();
      subsystems.getDrivetrain().seedFieldRelative();
    }));
  }
  
  private void configureCopilotBindings() {
    
    /* Bindings for drivetrain characterization */
    /* These bindings require multiple buttons pushed to swap between quastatic and dynamic */
    /* Back/Start select dynamic/quasistatic, Y/X select forward/reverse direction */
    // copilotController.back().and(copilotController.y()).whileTrue(subsystems.getDrivetrain().sysIdDynamic(Direction.kForward));
    // copilotController.back().and(copilotController.x()).whileTrue(subsystems.getDrivetrain().sysIdDynamic(Direction.kReverse));
    // copilotController.start().and(copilotController.y()).whileTrue(subsystems.getDrivetrain().sysIdQuasistatic(Direction.kForward));
    // copilotController.start().and(copilotController.x()).whileTrue(subsystems.getDrivetrain().sysIdQuasistatic(Direction.kReverse));
  }

  private void configureDefaultBindings() {
    subsystems.getDrivetrain().setDefaultCommand(
      new DefaultDriveCommand(
        subsystems.getDrivetrain(),
        driverController,
        subsystems.getRobotState()));

    subsystems.getIntakeWrist().setDefaultCommand(
      new DefaultIntakeWristCommand(
        subsystems.getIntakeWrist(),
        subsystems.getRobotState()));

    subsystems.getIntakeRollers().setDefaultCommand(
      new DefaultIntakeRollersCommand(
        subsystems.getIntakeRollers(),
        subsystems.getRobotState()));

    subsystems.getShooterFeeder().setDefaultCommand(
      new DefaultShooterFeederCommand(
        subsystems.getShooterFeeder(),
        subsystems.getRobotState()));

    subsystems.getShooterWrist().setDefaultCommand(
      new DefaultShooterWristCommand(
        subsystems.getShooterWrist(),
        subsystems.getRobotState()));

    subsystems.getShooterWheels().setDefaultCommand(
      new DefaultShooterWheelsCommand(
        subsystems.getShooterWheels(),
        subsystems.getRobotState()));
  }

  public Subsystems getSubsystems() {
    return this.subsystems;
  }

  public CommandXboxController getDriverController() {
    return driverController;
  }
}
