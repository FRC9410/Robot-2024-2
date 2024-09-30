// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.base;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterFeeder;
import frc.robot.subsystems.StateMachine;

public class DefaultShooterFeederCommand extends Command {
  private ShooterFeeder shooterFeeder;
  private StateMachine robotState;

  public DefaultShooterFeederCommand(ShooterFeeder shooterFeeder, StateMachine robotState) {
    this.shooterFeeder = shooterFeeder;
    this.robotState = robotState;
    addRequirements(shooterFeeder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (robotState.getCommandData("shooterFeederVelocity") != null) {
      shooterFeeder.setVelocity((double) robotState.getCommandData("shooterFeederVelocity"));
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
