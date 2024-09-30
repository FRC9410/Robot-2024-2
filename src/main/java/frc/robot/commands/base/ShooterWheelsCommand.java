// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.base;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterWheels;

public class ShooterWheelsCommand extends Command {
  private ShooterWheels shooterWheels;
  private double speed;

  public ShooterWheelsCommand(ShooterWheels shooterWheels, double speed) {
    this.shooterWheels = shooterWheels;
    this.speed = speed;
    addRequirements(shooterWheels);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    shooterWheels.setVelocity(speed);
  }

  @Override
  public void end(boolean interrupted) {
    shooterWheels.setOff();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
