// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.base;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.RobotState;
import frc.robot.subsystems.RobotState.State;
import frc.robot.subsystems.ShooterWheels;

public class DefaultShooterWheelsCommand extends Command {
  private ShooterWheels shooterWheels;
  private RobotState robotState;
  private State state;
  private Timer timer = new Timer();
  
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
    if (robotState.getState().equals(State.DEMO_MODE)) {
      // do nothing
    } else if (robotState.getState().equals(State.DEV_MODE)) {
      // do nothing
    } else if (robotState.getState() == State.DUNKING
      && state != State.DUNKING) {
      state = robotState.getState();
      timer.start();
    } else if (robotState.getState() == State.DUNKING
      && timer.get() > 1) {
      shooterWheels.setVelocity(20);
    } else if (robotState.getState() != State.DUNKING
      && state == State.DUNKING && timer.get() > 4) {
      shooterWheels.setOff();;
      state = robotState.getState();
      timer.stop();
      timer.reset();
    } else if (robotState.getState() == State.SHOOTING
      && state != State.SHOOTING) {
      state = robotState.getState();
    } else if (robotState.getState() != State.SHOOTING
        && state == State.SHOOTING) {
      state = robotState.getState();
      timer.start();
    } else if (robotState.getState() != State.SHOOTING
      && state == State.SHOOTING && timer.get() > 0.5) {
      shooterWheels.setOff();
      timer.stop();
      timer.reset();
    } else if (robotState.getState() == State.SHOOTING_READY
      && state == State.SHOOTING_READY
      && robotState.isWithinRange()) {
      state = robotState.getState();
      shooterWheels.setVelocity(75);
    } else if (robotState.getState() == State.SHOOTING_READY
      && state == State.SHOOTING_READY) {
      state = robotState.getState();
      shooterWheels.setVelocity(50);
    } else if (robotState.getState() != State.SHOOTING_READY
      && robotState.getState() != State.SHOOTING
      && state == State.SHOOTING_READY) {
      state = robotState.getState();
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
