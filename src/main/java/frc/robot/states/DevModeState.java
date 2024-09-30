package frc.robot.states;

import frc.robot.subsystems.RobotState.State;
import frc.robot.subsystems.RobotState;
import frc.team9410.lib.StateConditionHandler;

public class DevModeState implements StateConditionHandler {
    public boolean matches(RobotState state) {
        return state.getState().equals(State.DEV_MODE);
    }
    
    public void execute(RobotState state) {}
}