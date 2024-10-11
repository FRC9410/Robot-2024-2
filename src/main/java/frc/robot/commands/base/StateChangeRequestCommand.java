// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.base;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.StateMachine;
import frc.robot.subsystems.StateMachine.State;
import frc.robot.subsystems.state.helpers.IntakeHelpers;
import frc.robot.subsystems.state.helpers.PositionHelpers;
import frc.robot.subsystems.state.helpers.ShooterHelpers;

public class StateChangeRequestCommand extends Command {
  private final StateMachine stateMachine;
  private final State requestedState;
  
  public StateChangeRequestCommand(StateMachine stateMachine, State requestedState) {
    this.stateMachine = stateMachine;
    this.requestedState = requestedState;
    addRequirements(stateMachine);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    stateMachine.requestStateChange(requestedState);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    stateMachine.requestStateChange(State.IDLE);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (requestedState.equals(State.INTAKING) && IntakeHelpers.hasGamePiece(stateMachine.getIntakeLaser().getMeasurement().distance_mm)
      || (requestedState.equals(State.SHOOTING_READY)
        && (PositionHelpers.isInZone(stateMachine.getAllianceColor(), (double) stateMachine.getSubsystemData("locationX"))
          || !IntakeHelpers.hasGamePiece(stateMachine.getIntakeLaser().getMeasurement().distance_mm)))
      || (requestedState.equals(State.DUNKING_READY)
        && (PositionHelpers.isInZone(stateMachine.getAllianceColor(), (double) stateMachine.getSubsystemData("locationX"))
          || !IntakeHelpers.hasGamePiece(stateMachine.getIntakeLaser().getMeasurement().distance_mm)))
      || (requestedState.equals(State.SHOOTING)
        && (!IntakeHelpers.hasGamePiece(stateMachine.getIntakeLaser().getMeasurement().distance_mm)
          || ShooterHelpers.shooterIsReady(
            stateMachine.getSubsystemData("primaryWheelVelocity") != null
            ? (double) stateMachine.getSubsystemData("primaryWheelVelocity")
            : 0.0,
            stateMachine.getSubsystemData("secondaryWheelVelocity") != null
            ? (double) stateMachine.getSubsystemData("secondaryWheelVelocity")
            : 0.0,
            stateMachine.getSubsystemData("feederVelocity") != null
            ? (double) stateMachine.getSubsystemData("feederVelocity")
            : 0.0
          )
        )) ) {
      return true;
    }
    return false;
  }
}
