// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.base;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterWrist;
import frc.robot.subsystems.RobotState;
import frc.robot.subsystems.RobotState.State;

public class DefaultShooterWristCommand extends Command {
  private ShooterWrist shooterWrist;
  private RobotState robotState;
  private State state;
  private Timer timer;

  public DefaultShooterWristCommand(ShooterWrist shooterWrist, RobotState robotState) {
    this.shooterWrist = shooterWrist;
    this.robotState = robotState;
    this.state = robotState.getState();
    this.timer = new Timer();
    addRequirements(shooterWrist);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (robotState.getState() == State.DUNKING
          && state != State.DUNKING) {
        state = robotState.getState();
        timer.start();
    } else if (robotState.getState() == State.DUNKING
        && timer.get() > 1) {
        shooterWrist.setWristAngle(5);
    } else if (robotState.getState() != State.DUNKING
        && state == State.DUNKING && timer.get() > 4) {
        shooterWrist.setWristAngle(0.4);
        state = robotState.getState();
        timer.stop();
        timer.reset();
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
