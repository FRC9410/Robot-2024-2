// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.base;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeRollers;
import frc.robot.subsystems.RobotState;
import frc.robot.subsystems.RobotState.State;

public class DefaultIntakeRollersCommand extends Command {
  private IntakeRollers intakeRollers;
  private RobotState robotState;
  private State state;
  private Timer timer;

  public DefaultIntakeRollersCommand(IntakeRollers intakeRollers, RobotState robotState) {
    this.intakeRollers = intakeRollers;
    this.robotState = robotState;
    this.timer = new Timer();
    addRequirements(intakeRollers);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (robotState.getState() == State.INTAKING) {
      state = robotState.getState();
      intakeRollers.setTorque(0, 0);
    } else if (robotState.getState() == State.SHOOTING) {
      state = robotState.getState();
      intakeRollers.setVoltage(0, 0); 
    } else if (robotState.getState() == State.DUNKING) {
      state = robotState.getState();
      intakeRollers.setVoltage(0, 0);
    } else if (robotState.getState() != State.DUNKING
        && state == State.DUNKING) {
      state = robotState.getState();
      intakeRollers.setOff();
    } else {
      if ((robotState.getState() != State.INTAKING
        && state == State.INTAKING)
        || (robotState.getState() != State.SHOOTING
        && state == State.SHOOTING)) {
        state = robotState.getState();
        timer.start();
      }
      
      if (timer.get() > 0.5) {
      intakeRollers.setOff();
        timer.stop();
        timer.reset();
      }
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
