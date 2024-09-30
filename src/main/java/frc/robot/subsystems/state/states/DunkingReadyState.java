package frc.robot.subsystems.state.states;

import frc.robot.subsystems.StateMachine.State;
import frc.robot.subsystems.state.helpers.PositionHelpers;

import java.util.List;
import java.util.Map;

import frc.robot.subsystems.StateMachine;
import frc.team9410.lib.StateHandler;

public class DunkingReadyState implements StateHandler {
    public boolean matches(State state) {
        return state.equals(State.DUNKING_READY);
    }
    
    public void execute(StateMachine state) {
            state.removeMultipleKeys(List.of(
              "intakeRollerVelocity",
              "intakeRollerFeedForward",
              "intakeWristSetpoint",
              "shooterFeederVelocity",
              "shooterWheelsVelocity"));

        state.updateCommandData("targetRotation",
        PositionHelpers.getAmpAngle(state.getAllianceColor(), (double) state.getSubsystemData("locationX")));
        Map<String, Double> ampDestination = PositionHelpers.getAmpDestination(state.getAllianceColor());

        state.updateCommandData("targetX", ampDestination.get("x"));
        state.updateCommandData("targetY", ampDestination.get("y"));
    }
}