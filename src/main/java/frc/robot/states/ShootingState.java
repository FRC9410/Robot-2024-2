package frc.robot.states;

import frc.robot.subsystems.RobotState.State;

import java.util.Optional;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.RobotState;
import frc.team9410.lib.StateConditionHandler;

public class ShootingState implements StateConditionHandler {
    public boolean matches(RobotState state) {
        return state.getController().getRightTriggerAxis() > 0.5
        && state.getDebouncer().calculate(StateHelpers.hasGamePiece(state.getIntakeLaser().getMeasurement().distance_mm))
        && StateHelpers.isWithinRange(
            state.getAllianceColor(),
            state.getLocationX(),
            state.getLocationY(),
            state.getController().getRightTriggerAxis() > 0.5)
        && state.getDebouncer().calculate(StateHelpers.shooterIsReady(
            state.getShooterWheels().getPrimaryWheelVelocity(),
            state.getShooterWheels().getSecondaryWheelVelocity(),
            state.getShooterFeeder().getVelocity()
        ));
    }
    
    public void execute(RobotState state) {
        state.setState(State.SHOOTING);
        state.setTargetRotation(Optional.of(Rotation2d.fromRadians(StateHelpers.getShootingAngle(
            state.getAllianceColor(),
            state.getLocationX(),
            state.getLocationY(),
            state.getController().getRightTriggerAxis() > 0.5))));
    }
}