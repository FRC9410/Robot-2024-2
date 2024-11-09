package frc.robot;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.subsystems.Subsystems;
import frc.robot.subsystems.Telemetry;
import frc.robot.subsystems.StateMachine.State;
import frc.robot.commands.base.DefaultDriveCommand;
import frc.robot.commands.base.DefaultIntakeRollersCommand;
import frc.robot.commands.base.DefaultIntakeWristCommand;
import frc.robot.commands.base.StateChangeRequestCommand;
import frc.robot.commands.base.VoltageIntakeCommand;
import frc.robot.commands.group.EjectNoteCommand;
import frc.robot.commands.group.IntakeNoteCommand;

public class RobotContainer {
  private final CommandXboxController driverController = new CommandXboxController(0);
  private final CommandXboxController copilotController = new CommandXboxController(1);
  private Subsystems subsystems = new Subsystems(driverController);
  private final Telemetry logger = new Telemetry(5.24);

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

    driverController.back().onTrue(subsystems.getVision().runOnce(() -> {
      subsystems.getVision().setYaw(false);
    }));

    driverController.leftTrigger(0.5).whileTrue(new StateChangeRequestCommand(subsystems.getStateMachine(), State.INTAKING));
    driverController.leftBumper().whileTrue(new IntakeNoteCommand(subsystems));
  }
  
  private void configureCopilotBindings() {
    copilotController.x().whileTrue(new VoltageIntakeCommand(subsystems.getIntakeRollers(), -10, -6,100));
    copilotController.b().onTrue(new EjectNoteCommand(subsystems));
    // copilotController.rightTrigger(0.5).whileTrue(new ElevatorCommand(subsystems.getElevator(), 1));
    // copilotController.leftTrigger(0.5).whileTrue(new ElevatorCommand(subsystems.getElevator(), -1));
    
    /* Bindings for drivetrain characterization */
    /* These bindings require multiple buttons pushed to swap between quastatic and dynamic */
    /* Back/Start select dynamic/quasistatic, Y/X select forward/reverse direction */
    copilotController.back().and(copilotController.y()).whileTrue(subsystems.getDrivetrain().sysIdDynamic(Direction.kForward));
    copilotController.back().and(copilotController.x()).whileTrue(subsystems.getDrivetrain().sysIdDynamic(Direction.kReverse));
    copilotController.start().and(copilotController.y()).whileTrue(subsystems.getDrivetrain().sysIdQuasistatic(Direction.kForward));
    copilotController.start().and(copilotController.x()).whileTrue(subsystems.getDrivetrain().sysIdQuasistatic(Direction.kReverse));
    subsystems.getDrivetrain().registerTelemetry(logger::telemeterize);
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

    subsystems.getStateMachine().setDefaultCommand(
      new StateChangeRequestCommand(
        subsystems.getStateMachine(), State.IDLE));
  }

  public Subsystems getSubsystems() {
    return this.subsystems;
  }

  public CommandXboxController getDriverController() {
    return driverController;
  }
}
