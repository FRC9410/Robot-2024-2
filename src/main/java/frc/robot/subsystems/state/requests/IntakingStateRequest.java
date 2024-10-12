package frc.robot.subsystems.state.requests;

import frc.robot.subsystems.StateMachine.State;
import frc.robot.subsystems.state.helpers.IntakeHelpers;

import frc.robot.subsystems.StateMachine;
import frc.team9410.lib.StateRequestHandler;

public class IntakingStateRequest implements StateRequestHandler {
    public boolean matches(StateMachine state, State request) {
        boolean hasGamePiece = IntakeHelpers.hasGamePiece(state.getIntakeLaser().getMeasurement().distance_mm);
        return request.equals(State.INTAKING)
        && !hasGamePiece
        && state.getSubsystemData("hasGamePieceTarget") != null
        && (boolean) state.getSubsystemData("hasGamePieceTarget");
    }
    
    public void execute(StateMachine state) {
        state.setState(State.INTAKING);
    }
}