// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.base;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterFeeder;

public class ShooterFeederCommand extends Command {
  private ShooterFeeder shooterFeeder;
  private double speed;

  public ShooterFeederCommand(ShooterFeeder shooterFeeder, double speed) {
    this.shooterFeeder = shooterFeeder;
    this.speed = speed;
    addRequirements(shooterFeeder);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    shooterFeeder.setVelocity(speed);
  }

  @Override
  public void end(boolean interrupted) {
    shooterFeeder.setOff();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
