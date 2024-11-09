package frc.robot.subsystems.state.requests;

import frc.robot.subsystems.StateMachine.State;
import frc.robot.subsystems.state.helpers.IntakeHelpers;

import java.util.List;

import frc.robot.subsystems.StateMachine;
import frc.team9410.lib.StateRequestHandler;

public class IdleStateRequest implements StateRequestHandler {
    public boolean matches(StateMachine state, State request) {
        return request.equals(State.IDLE) || IntakeHelpers.hasGamePiece(state.getIntakeLaser().getMeasurement().distance_mm);
    }
    
    public void execute(StateMachine state) {
        state.removeMultipleCommandKeys(List.of(
            "targetX",
            "targetY",
            "targetRotation",
            "intakeRollerVelocity",
            "intakeRollerFeedForward",
            "intakeWristSetpoint",
            "shooterFeederVelocity",
            "shooterWheelsVelocity"));
    }
}