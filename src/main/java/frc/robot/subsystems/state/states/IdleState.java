package frc.robot.subsystems.state.states;

import frc.robot.subsystems.StateMachine.State;

import java.util.List;

import frc.robot.subsystems.StateMachine;
import frc.team9410.lib.StateHandler;

public class IdleState implements StateHandler {
    public boolean matches(State state) {
        return state.equals(State.IDLE);
    }
    
    public void execute(StateMachine state) {
            state.removeMultipleKeys(List.of(
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