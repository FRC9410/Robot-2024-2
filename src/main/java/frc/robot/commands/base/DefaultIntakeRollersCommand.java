// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.base;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeRollers;
import frc.robot.subsystems.StateMachine;
import frc.robot.subsystems.StateMachine.State;

public class DefaultIntakeRollersCommand extends Command {
  private IntakeRollers intakeRollers;
  private StateMachine robotState;

  public DefaultIntakeRollersCommand(IntakeRollers intakeRollers, StateMachine robotState) {
    this.intakeRollers = intakeRollers;
    this.robotState = robotState;
    addRequirements(intakeRollers);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (robotState.getCommandData("intakeRollerVelocity") != null
        && robotState.getCommandData("intakeRollerFeedForward") != null) {
      if (robotState.getState().equals(State.INTAKING)) {
        intakeRollers.setTorque((double) robotState.getCommandData("intakeRollerVelocity"),
          (double) robotState.getCommandData("intakeRollerFeedForward"));
      } else {
        intakeRollers.setVoltage((double) robotState.getCommandData("intakeRollerVelocity"),
          (double) robotState.getCommandData("intakeRollerFeedForward")); 
      }
    } else {
      intakeRollers.setOff();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
