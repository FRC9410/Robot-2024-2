// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.group;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.Constants.SubsystemConstants.IntakeWrist;
import frc.robot.commands.base.IntakeCommand;
import frc.robot.commands.base.IntakeWristCommand;
import frc.robot.subsystems.Subsystems;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class IntakeNoteCommand extends ParallelRaceGroup {
  /** Creates a new IntakeNoteCommand. */
  public IntakeNoteCommand(Subsystems subsystems) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new IntakeWristCommand(subsystems.getIntakeWrist(), IntakeWrist.kMaxRotation, subsystems.getStateMachine().getIntakeLaser()),
      new IntakeCommand(subsystems.getIntakeRollers(), -85, -6, 15)
    );
  }
}

