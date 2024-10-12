// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.base;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeWrist;
import frc.robot.subsystems.StateMachine;

public class DefaultIntakeWristCommand extends Command {
  private IntakeWrist intakeWrist;
  private StateMachine robotState;

  public DefaultIntakeWristCommand(IntakeWrist intakeWrist, StateMachine robotState) {
    this.intakeWrist = intakeWrist;
    this.robotState = robotState;
    addRequirements(intakeWrist);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (robotState.getCommandData("intakeWristSetpoint") != null) {
      intakeWrist.setAngle((double) robotState.getCommandData("intakeWristSetpoint"));
    } else {
      intakeWrist.setAngle(0.12);
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
