// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.base;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.StateMachine;
import frc.robot.subsystems.ShooterWheels;

public class DefaultShooterWheelsCommand extends Command {
  private ShooterWheels shooterWheels;
  private StateMachine robotState;
  
  public DefaultShooterWheelsCommand(ShooterWheels shooterWheels, StateMachine robotState) {
    this.shooterWheels = shooterWheels;
    this.robotState = robotState;
    addRequirements(shooterWheels);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (robotState.getCommandData("shooterWheelsVelocity") != null) {
      shooterWheels.setVelocity((double) robotState.getCommandData("shooterWheelsVelocity"));
    } else {
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
