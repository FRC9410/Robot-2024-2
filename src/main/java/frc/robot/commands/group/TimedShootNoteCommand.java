// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.group;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.base.IntakeCommand;
import frc.robot.commands.base.ShooterFeederCommand;
import frc.robot.commands.base.ShooterWheelsCommand;
import frc.robot.subsystems.Subsystems;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class TimedShootNoteCommand extends SequentialCommandGroup {
  /** Creates a new ShootNoteCommand. */
  public TimedShootNoteCommand(Subsystems subsystems) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new ParallelRaceGroup(
        new WaitCommand(0.2),
        new ShooterWheelsCommand(subsystems.getShooterWheels(), 70),
        new ShooterFeederCommand(subsystems.getShooterFeeder(), -45)
      ),
      new ParallelRaceGroup(
        new WaitCommand(0.2),
        new ShooterWheelsCommand(subsystems.getShooterWheels(), 70),
        new ShooterFeederCommand(subsystems.getShooterFeeder(), -45),
        new IntakeCommand(subsystems.getIntakeRollers(), 85, 8, 0)
      )
    );
  }
}
