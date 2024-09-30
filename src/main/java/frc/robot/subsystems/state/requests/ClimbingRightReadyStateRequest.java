package frc.robot.subsystems.state.requests;

import frc.robot.subsystems.StateMachine.State;

import frc.robot.subsystems.StateMachine;
import frc.team9410.lib.StateRequestHandler;

public class ClimbingRightReadyStateRequest implements StateRequestHandler {
    public boolean matches(StateMachine state, State request) {
        return request.equals(State.CLIMBING_RIGHT_READY);
    }
    
    public void execute(StateMachine state) {
        state.setState(State.CLIMBING_RIGHT_READY);
    }
}