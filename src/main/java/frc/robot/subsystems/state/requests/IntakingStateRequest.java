package frc.robot.subsystems.state.requests;

import frc.robot.subsystems.StateMachine.State;
import frc.robot.subsystems.state.helpers.IntakeHelpers;

import java.util.List;

import frc.robot.subsystems.StateMachine;
import frc.team9410.lib.StateRequestHandler;

public class IntakingStateRequest implements StateRequestHandler {
    public boolean matches(StateMachine state, State request) {
        boolean hasGamePiece = IntakeHelpers.hasGamePiece(state.getIntakeLaser().getMeasurement().distance_mm);
        return request.equals(State.INTAKING)
        && !hasGamePiece
        && state.getSubsystemData("hasGamePieceTarget") != null
        && (boolean) state.getSubsystemData("hasGamePieceTarget");
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
        state.updateCommandData("targetRotation", gamePieceTx);

        if (gamePieceTy > -16.0 && gamePieceTy < 8.0) {

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