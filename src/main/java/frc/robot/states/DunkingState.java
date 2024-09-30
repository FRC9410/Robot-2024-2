package frc.robot.states;

import frc.robot.subsystems.RobotState.State;
import frc.robot.subsystems.RobotState;
import frc.team9410.lib.StateConditionHandler;

public class DunkingState implements StateConditionHandler {
    public boolean matches(RobotState state) {
        return state.getController().rightBumper().getAsBoolean()
        && state.getDebouncer().calculate(StateHelpers.hasGamePiece(state.getIntakeLaser().getMeasurement().distance_mm))
        && StateHelpers.isWithinRange(
          state.getAllianceColor(),
          state.getLocationX(),
          state.getLocationY(),
          state.getController().getRightTriggerAxis() > 0.5);
    }
    
    public void execute(RobotState state) {
      state.setState(State.DUNKING);
      state.setCommandExecuting(true);
      state.setTargetRotation(StateHelpers.getAmpAngle(state.getAllianceColor(), state.getLocationX()));
    }
}