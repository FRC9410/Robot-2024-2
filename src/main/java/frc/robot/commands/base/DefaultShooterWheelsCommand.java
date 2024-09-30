// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.base;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.states.StateHelpers;
import frc.robot.subsystems.RobotState;
import frc.robot.subsystems.RobotState.State;
import frc.robot.subsystems.ShooterWheels;

public class DefaultShooterWheelsCommand extends Command {
  private ShooterWheels shooterWheels;
  private RobotState robotState;
  private State state;
  
  public DefaultShooterWheelsCommand(ShooterWheels shooterWheels, RobotState robotState) {
    this.shooterWheels = shooterWheels;
    this.robotState = robotState;
    this.state = robotState.getState();
    addRequirements(shooterWheels);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    state = robotState.getState();

    if (state.equals(State.DEMO_MODE)) {
      // do nothing
    } else if (state.equals(State.DEV_MODE)) {
      // do nothing
    } else if (state.equals(State.SHOOTING_READY)
      && !StateHelpers.isWithinRange(
        robotState.getAllianceColor(),
        robotState.getLocationX(),
        robotState.getLocationY(),
        robotState.getController().getRightTriggerAxis() > 0.5)) {
      shooterWheels.setVelocity(50);
    } else if (state.equals(State.SHOOTING_READY)
      && StateHelpers.isWithinRange(
        robotState.getAllianceColor(),
        robotState.getLocationX(),
        robotState.getLocationY(),
        robotState.getController().getRightTriggerAxis() > 0.5)) {
      shooterWheels.setVelocity(75);
    } else if (!state.equals(State.SHOOTING)) {
      shooterWheels.setOff();
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
