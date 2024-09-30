package frc.robot.states;

import frc.robot.subsystems.RobotState.State;
import frc.robot.subsystems.RobotState;
import frc.team9410.lib.StateConditionHandler;

public class IdleState implements StateConditionHandler {
    public boolean matches(RobotState state) {
        return true;
    }
    
    public void execute(RobotState state) {
        state.setState(State.IDLE);
    }
}