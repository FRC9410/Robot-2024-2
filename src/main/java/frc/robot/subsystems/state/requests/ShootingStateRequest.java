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
        && PositionHelpers.isWithinSpeakerRange(
            state.getAllianceColor(),
            (double) state.getSubsystemData("locationX"),
            (double) state.getSubsystemData("locationY"))
        && state.getDebouncer().calculate(ShooterHelpers.shooterIsReady(
            (double) state.getSubsystemData("primaryWheelVelocity"),
            (double) state.getSubsystemData("secondaryWheelVelocity"),
            (double) state.getSubsystemData("feederVelocity")
        ));
    }
    
    public void execute(StateMachine state) {
        state.setState(State.SHOOTING);
    }
}