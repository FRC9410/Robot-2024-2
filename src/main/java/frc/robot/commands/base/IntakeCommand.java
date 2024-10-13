// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.base;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeRollers;

public class IntakeCommand extends Command {
  /** Creates a new IntakeIn. */
  private IntakeRollers intake;
  private double speed;
  private Timer timer;
  private double minCurrentDraw;
  private double feedforward;

  public IntakeCommand(IntakeRollers intake, double speed, double feedforward, double minCurrentDraw) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.intake = intake;
    this.speed = speed;
    this.timer = new Timer();
    this.minCurrentDraw = minCurrentDraw;
    this.feedforward = feedforward;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    this.intake.setVoltage(speed, feedforward);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    this.intake.setOff();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // if (this.speed < 0 && this.timer.hasElapsed(1) && Math.abs(this.intake.getVelocity()) < this.minCurrentDraw) {
    //   return true;
    // }
    return false;
  }
}
