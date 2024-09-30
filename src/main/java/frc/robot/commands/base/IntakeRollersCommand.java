// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.base;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeRollers;

public class IntakeRollersCommand extends Command {
  private final IntakeRollers intakeRollers;
  private final double speed;

  public IntakeRollersCommand(IntakeRollers intakeRollers, double speed) {
    this.intakeRollers = intakeRollers;
    this.speed = speed;
    addRequirements(intakeRollers);
  }

  @Override
  public void initialize() {
    intakeRollers.setVoltage(speed);
  }

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {
    intakeRollers.setOff();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
