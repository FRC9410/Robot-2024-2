package frc.team9410.lib;

import frc.robot.subsystems.RobotState;

public interface StateConditionHandler {
    boolean matches(RobotState state);
    void execute(RobotState state);
}
