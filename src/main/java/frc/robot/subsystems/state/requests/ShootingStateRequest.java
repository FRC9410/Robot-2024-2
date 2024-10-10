package frc.robot.subsystems.state.requests;

import frc.robot.subsystems.StateMachine.State;
import frc.robot.subsystems.state.helpers.IntakeHelpers;
import frc.robot.subsystems.state.helpers.PositionHelpers;
import frc.robot.subsystems.state.helpers.ShooterHelpers;
import frc.robot.subsystems.StateMachine;
import frc.team9410.lib.StateRequestHandler;

public class ShootingStateRequest implements StateRequestHandler {
    public boolean matches(StateMachine state, State request) {
        return request.equals(State.SHOOTING_READY)
        && state.getDebouncer().calculate(IntakeHelpers.hasGamePiece(state.getIntakeLaser().getMeasurement().distance_mm))
        // && PositionHelpers.isWithinSpeakerRange(
        //     state.getAllianceColor(),
        //     (double) state.getSubsystemData("locationX"),
        //     (double) state.getSubsystemData("locationY"))
        && ShooterHelpers.shooterIsReady(
            state.getSubsystemData("primaryWheelVelocity") != null
            ? (double) state.getSubsystemData("primaryWheelVelocity")
            : 0.0,
            state.getSubsystemData("secondaryWheelVelocity") != null
            ? (double) state.getSubsystemData("secondaryWheelVelocity")
            : 0.0,
            state.getSubsystemData("feederVelocity") != null
            ? (double) state.getSubsystemData("feederVelocity")
            : 0.0
        );
    }
    
    public void execute(StateMachine state) {
        state.setState(State.SHOOTING);
    }
}