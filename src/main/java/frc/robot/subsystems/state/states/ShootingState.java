package frc.robot.subsystems.state.states;

import frc.robot.subsystems.StateMachine.State;

import java.util.List;

import frc.robot.subsystems.StateMachine;
import frc.team9410.lib.StateHandler;

public class ShootingState implements StateHandler {
    public boolean matches(State state) {
        return state.equals(State.SHOOTING);
    }
    
    public void execute(StateMachine state) {
        state.removeMultipleKeys(List.of(
            "targetX",
            "targetY",
            "targetRotation",
            "intakeWristSetpoint"));

        state.updateCommandData("intakeRollerFeedForward", 8);
        state.updateCommandData("intakeRollerVelocity", 60);
    }
}