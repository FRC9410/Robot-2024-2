package frc.robot.commands.group;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.base.ShooterFeederCommand;
import frc.robot.commands.base.IntakeRollersCommand;
import frc.robot.commands.base.ShooterWheelsCommand;
import frc.robot.commands.base.ShooterWristCommand;
import frc.robot.subsystems.Subsystems;
import frc.team9410.lib.InstCmd;

public class DunkingCommand extends SequentialCommandGroup {
  public DunkingCommand(Subsystems subsystems) {
    addCommands(
      new ParallelRaceGroup(
        new WaitCommand(0.25),
        new ShooterFeederCommand(subsystems.getShooterFeeder(), -30),
        new IntakeRollersCommand(subsystems.getIntakeRollers(), 30),
        new ShooterWheelsCommand(subsystems.getShooterWheels(), -40)
      ),
      new ParallelRaceGroup(
        new ShooterWristCommand(subsystems.getShooterWrist(), 16),
        new WaitCommand(0.7)
      ),
      new ParallelRaceGroup(
        new WaitCommand(0.4),
        new ShooterWheelsCommand(subsystems.getShooterWheels(), 60),
        new ShooterFeederCommand(subsystems.getShooterFeeder(), -60)
      ),
      new ShooterWristCommand(subsystems.getShooterWrist(), 0)
    );
  }

}