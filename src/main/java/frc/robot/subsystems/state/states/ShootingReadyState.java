package frc.robot.subsystems.state.states;

import frc.robot.subsystems.StateMachine.State;
import frc.robot.subsystems.state.helpers.PositionHelpers;

import java.util.List;
import java.util.Map;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.StateMachine;
import frc.team9410.lib.StateHandler;

public class ShootingReadyState implements StateHandler {
    public boolean matches(State state) {
        return state.equals(State.SHOOTING_READY);
    }

    public void execute(StateMachine state) {
        state.removeMultipleKeys(List.of(
            "targetX",
            "targetY",
            "targetRotation",
            "intakeRollerVelocity",
            "intakeRollerFeedForward",
            "intakeWristSetpoint"));

        double locationX = (double) state.getSubsystemData("locationX");
        double locationY = (double) state.getSubsystemData("locationY");
        boolean isWithinRange = PositionHelpers.isWithinSpeakerRange(state.getAllianceColor(), locationX, locationY);

        state.updateCommandData("targetRotation",
            new Rotation2d(PositionHelpers.getShootingAngle(
                    state.getAllianceColor(),
                    locationX,
                    locationY)).getDegrees());

        Map<String, Double> shootingDestination = PositionHelpers.getSpeakerDestination(
            state.getAllianceColor(), locationX, locationY);
        state.updateCommandData("targetX", shootingDestination.get("x"));
        state.updateCommandData("targetY", shootingDestination.get("y"));

        if (isWithinRange) {
            state.updateCommandData("shooterFeederVelocity", -45.0);
            state.updateCommandData("shooterWheelsVelocity", 70.0);
        } else {
            state.updateCommandData("shooterFeederVelocity", -25.0);
            state.updateCommandData("shooterWheelsVelocity", 40.0);
        }
    }
}