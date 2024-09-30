// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.base;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterWrist;

public class ShooterWristCommand extends Command {
  private ShooterWrist shooterWrist;
  private double angle;
  
  public ShooterWristCommand(ShooterWrist shooterWrist, double angle) {
    this.shooterWrist = shooterWrist;
    this.angle = angle;
    addRequirements(shooterWrist);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    shooterWrist.setWristAngle(angle);
  }

  @Override
  public void end(boolean interrupted) {
    shooterWrist.setWristAngle(0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
