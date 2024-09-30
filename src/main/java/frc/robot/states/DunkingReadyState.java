package frc.robot.states;

import frc.robot.subsystems.RobotState.State;

import java.util.Map;

import frc.robot.subsystems.RobotState;
import frc.team9410.lib.StateConditionHandler;

public class DunkingReadyState implements StateConditionHandler {
    public boolean matches(RobotState state) {
        return state.getController().rightBumper().getAsBoolean()
        && StateHelpers.hasGamePiece(state.getIntakeLaser().getMeasurement().distance_mm)
        && StateHelpers.isInZone(state.getAllianceColor(), state.getLocationX());
    }
    
    public void execute(RobotState state) {
        state.setState(State.DUNKING_READY);
        state.setTargetRotation(StateHelpers.getAmpAngle(state.getAllianceColor(), state.getLocationX()));
        Map<String, Double> ampDestination = StateHelpers.getAmpDestination(state.getAllianceColor());
        state.setTargetX(ampDestination.get("x"));
        state.setTargetY(ampDestination.get("y"));
    }
}