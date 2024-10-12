package frc.robot.subsystems.state.states;

import frc.robot.subsystems.StateMachine.State;

import java.util.List;

import frc.robot.subsystems.StateMachine;
import frc.team9410.lib.StateHandler;

public class IntakingState implements StateHandler {
    public boolean matches(State state) {
        return state.equals(State.INTAKING);
    }
    
    public void execute(StateMachine state) {
        double gamePieceTx = state.getSubsystemData("gamePieceTx") != null
        ? (double) state.getSubsystemData("gamePieceTx")
        : 100.0;
        double gamePieceTy = state.getSubsystemData("gamePieceTy") != null
        ? (double) state.getSubsystemData("gamePieceTy")
        : 100.0;
        double rotation = (double) state.getSubsystemData("rotation");

        state.removeMultipleKeys(List.of(
          "targetX",
          "targetY"));
        state.updateCommandData("targetRotation", rotation - gamePieceTx);

        if (gamePieceTy > -16.0 && gamePieceTy < 6.0) {

            state.updateCommandData("intakeRollerVelocity", -85.0);
            state.updateCommandData("intakeRollerFeedForward", -6.0);
            state.updateCommandData("intakeWristSetpoint", 0.64);
        } else {
            state.removeMultipleKeys(List.of(
              "intakeRollerVelocity",
              "intakeRollerFeedForward",
              "intakeWristSetpoint"));

        }
    }
}