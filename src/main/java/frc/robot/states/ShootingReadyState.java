package frc.robot.states;

import frc.robot.subsystems.RobotState.State;

import java.util.Map;
import java.util.Optional;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.RobotState;
import frc.team9410.lib.StateConditionHandler;

public class ShootingReadyState implements StateConditionHandler {
    public boolean matches(RobotState state) {
        return state.getController().getRightTriggerAxis() > 0.5
        && StateHelpers.hasGamePiece(state.getIntakeLaser().getMeasurement().distance_mm)
        && StateHelpers.isInZone(state.getAllianceColor(), state.getLocationX());
    }

    public void execute(RobotState state) {
        state.setState(State.SHOOTING_READY);
  
        if (state.getTargetRotation().isPresent()) {
          state.setTargetRotation(Optional.of(Rotation2d.fromRadians(
            StateHelpers.getShootingAngle(
                state.getAllianceColor(),
                state.getLocationX(),
                state.getLocationY(),
                state.getController().getRightTriggerAxis() > 0.5))));
        }

        Map<String, Double> shootingDestination = StateHelpers.getSpeakerDestination(
            state.getAllianceColor(), state.getLocationX(), state.getLocationY());
        state.setTargetX(shootingDestination.get("x"));
        state.setTargetY(shootingDestination.get("y"));
    }
}