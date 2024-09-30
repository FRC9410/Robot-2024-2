// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.base;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeWrist;
import frc.robot.subsystems.RobotState;
import frc.robot.subsystems.RobotState.State;

public class DefaultIntakeWristCommand extends Command {
  private IntakeWrist intakeWrist;
  private RobotState robotState;
  private State state;
  private Timer timer = new Timer();

  public DefaultIntakeWristCommand(IntakeWrist intakeWrist, RobotState robotState) {
    this.intakeWrist = intakeWrist;
    this.robotState = robotState;
    this.state = robotState.getState();
    addRequirements(intakeWrist);
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
    } else {
      // do nothing
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
