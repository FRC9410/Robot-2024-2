package frc.robot.subsystems.state.states;

import frc.robot.subsystems.StateMachine.State;
import frc.robot.subsystems.state.helpers.PositionHelpers;

import java.util.List;

import frc.robot.subsystems.StateMachine;
import frc.team9410.lib.StateHandler;

public class DunkingState implements StateHandler {
    public boolean matches(State state) {
        return state.equals(State.DUNKING);
    }
    
    public void execute(StateMachine state) {
            state.removeMultipleKeys(List.of(
              "intakeRollerVelocity",
              "intakeRollerFeedForward",
              "intakeWristSetpoint",
              "shooterFeederVelocity",
              "shooterWheelsVelocity"));

      state.setCommandExecuting(true);
      state.updateCommandData("targetRotation",
        PositionHelpers.getAmpAngle(state.getAllianceColor(), (double) state.getSubsystemData("locationX")));
    }
}