package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import frc.robot.commands.base.VoltageIntakeCommand;
import frc.robot.commands.group.IntakeNoteCommand;
import frc.robot.commands.group.TimedShootNoteCommand;
import frc.robot.subsystems.Subsystems;

/** Add your docs here. */
public class Dashboard {
    private boolean devMode;
    private NetworkTableInstance inst;
    private NetworkTable dashboardTable;
    private SendableChooser<Command> autoChooser;

    public Dashboard() {
        inst = NetworkTableInstance.getDefault();
        dashboardTable = inst.getTable("Dashboard");
        devMode = false;
    }

    public void loadDashboard(Subsystems subsystems) {
        loadAutoChooser(subsystems);
        dashboardTable.getEntry("Development Mode").setBoolean(devMode);
        // dashboardTable.getEntry("Subsystems").setString(subsystems.toString());
    }

    public void updateDashboard(Subsystems subsystems) {
        // devMode = dashboardTable.getEntry("Development Mode").getBoolean(false);
        // dashboardTable.getEntry("Subsystems").setString(subsystems.toString());
    }

    public void loadAutoChooser(Subsystems subsystems) {
        registerNamedCommands(subsystems);
        autoChooser = AutoBuilder.buildAutoChooser("deploy/autos");
        SmartDashboard.putData("Auto Chooser", autoChooser);
    }

    public void registerNamedCommands(Subsystems subsystems) {
        NamedCommands.registerCommand("TimedShootNoteCommand", new TimedShootNoteCommand(subsystems));
        NamedCommands.registerCommand("IntakeNoteCommand", new IntakeNoteCommand(subsystems));
        NamedCommands.registerCommand("IntakeCommand", new VoltageIntakeCommand(subsystems.getIntakeRollers(), -10, -6,100));
    }

    public Command getAutonomousCommand() {
      return autoChooser.getSelected();
    }
}
