package frc.robot.subsystems.state.states;

import frc.robot.subsystems.StateMachine.State;
import frc.robot.subsystems.state.helpers.PositionHelpers;

import java.util.List;
import java.util.Map;

import frc.robot.subsystems.StateMachine;
import frc.team9410.lib.StateHandler;

public class ClimbingRightReadyState implements StateHandler {
    public boolean matches(State state) {
        return state.equals(State.CLIMBING_RIGHT_READY);
    }
    
    public void execute(StateMachine state) {
            state.removeMultipleKeys(List.of(
              "targetRotation",
              "intakeRollerVelocity",
              "intakeRollerFeedForward",
              "intakeWristSetpoint",
              "shooterFeederVelocity",
              "shooterWheelsVelocity"));

        final Map<String, Double> stageDestination = PositionHelpers.getStageDestination(state.getAllianceColor(),
                state.getAllianceColor().equals("blue") ? "low" : "high");

        state.updateCommandData("targetX", stageDestination.get("x"));
        state.updateCommandData("targetY", stageDestination.get("y"));
    }
}