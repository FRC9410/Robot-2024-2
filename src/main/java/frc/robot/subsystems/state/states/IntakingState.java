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
        double gamePieceTx = (double) state.getSubsystemData("gamePieceTx");
        double gamePieceTy = (double) state.getSubsystemData("gamePieceTy");
        double rotation = (double) state.getSubsystemData("rotation");

        state.removeMultipleKeys(List.of(
          "targetX",
          "targetY"));
        state.updateCommandData("targetRotation", rotation - gamePieceTx);

        if (gamePieceTy > -16 && gamePieceTy < 0) {
            state.updateCommandData("intakeRollerVelocity", -85);
            state.updateCommandData("intakeRollerFeedForward", -6);
            state.updateCommandData("intakeWristSetpoint", 0.64);
        } else {
            state.removeMultipleKeys(List.of(
              "intakeRollerVelocity",
              "intakeRollerFeedForward",
              "intakeWristSetpoint"));

        }
    }
}