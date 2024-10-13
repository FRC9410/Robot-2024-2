// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.base;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeWrist;

public class IntakeWristCommand extends Command {
  /** Creates a new WristUp. */
  private IntakeWrist intake;
  private double position;

  public IntakeWristCommand(IntakeWrist intake, double position) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.intake = intake;
    this.position = position;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    this.intake.setAngle(this.position);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    this.intake.setAngle(0.12);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
