package frc.robot.states;

import frc.robot.subsystems.RobotState.State;

import java.util.Map;

import frc.robot.subsystems.RobotState;
import frc.team9410.lib.StateConditionHandler;

public class ClimbingReadyState implements StateConditionHandler {
    public boolean matches(RobotState state) {
        return state.getController().povLeft().getAsBoolean()
        || state.getController().povRight().getAsBoolean();
    }
    
    public void execute(RobotState state) {
        state.setState(State.CLIMBING_READY);
        final Map<String, Double> stageDestination;

        if (state.getController().povLeft().getAsBoolean()) {
            stageDestination = StateHelpers.getStageDestination(state.getAllianceColor(),
                state.getAllianceColor().equals("blue") ? "high" : "low");
        } else {
            stageDestination = StateHelpers.getStageDestination(state.getAllianceColor(),
                state.getAllianceColor().equals("blue") ? "low" : "high");
        }
        
        state.setTargetX(stageDestination.get("x"));
        state.setTargetY(stageDestination.get("y"));
    }
}