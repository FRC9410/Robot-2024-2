package frc.robot.subsystems.state.requests;

import frc.robot.subsystems.StateMachine.State;
import frc.robot.subsystems.state.helpers.IntakeHelpers;
import frc.robot.subsystems.state.helpers.PositionHelpers;
import frc.robot.subsystems.StateMachine;
import frc.team9410.lib.StateRequestHandler;

public class DunkingStateRequest implements StateRequestHandler {
    public boolean matches(StateMachine state, State request) {
        return request.equals(State.DUNKING_READY)
        && IntakeHelpers.hasGamePiece(state.getIntakeLaser().getMeasurement().distance_mm)
        && PositionHelpers.isWithinAmpRange(
          state.getAllianceColor(),
          (double) state.getSubsystemData("locationX"),
          (double) state.getSubsystemData("locationY"));
    }
    
    public void execute(StateMachine state) {
      state.setState(State.DUNKING);
    }
}