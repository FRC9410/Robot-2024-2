// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.base;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterFeeder;
import frc.robot.states.StateHelpers;
import frc.robot.subsystems.RobotState;
import frc.robot.subsystems.RobotState.State;

public class DefaultShooterFeederCommand extends Command {
  private ShooterFeeder shooterFeeder;
  private RobotState robotState;
  private State state;

  public DefaultShooterFeederCommand(ShooterFeeder shooterFeeder, RobotState robotState) {
    this.shooterFeeder = shooterFeeder;
    this.robotState = robotState;
    this.state = robotState.getState();
    addRequirements(shooterFeeder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooterFeeder.setOff();;

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
      shooterFeeder.setVelocity(30);
    } else if (state.equals(State.SHOOTING_READY)
      && StateHelpers.isWithinRange(
        robotState.getAllianceColor(),
        robotState.getLocationX(),
        robotState.getLocationY(),
        robotState.getController().getRightTriggerAxis() > 0.5)) {
      shooterFeeder.setVelocity(60);
    } else {
      shooterFeeder.setOff();
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
