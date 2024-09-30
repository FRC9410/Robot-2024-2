package frc.robot.states;

import frc.robot.subsystems.RobotState.State;

import java.util.Optional;

import frc.robot.subsystems.RobotState;
import frc.team9410.lib.StateConditionHandler;

public class IntakingState implements StateConditionHandler {
    public boolean matches(RobotState state) {
        return state.getController().getLeftTriggerAxis() > 0.5
        && state.getDebouncer().calculate(!StateHelpers.hasGamePiece(state.getIntakeLaser().getMeasurement().distance_mm))
        && state.getDebouncer().calculate(StateHelpers.hasTarget(state.getVision()));
    }
    
    public void execute(RobotState state) {
        if (state.getTargetX() != -1) {
          state.setTargetX(-1);
        }
    
        if (state.getTargetY() != -1) {
          state.setTargetY(-1);
        }
        
        state.setState(State.INTAKING);
  
        if (state.getTargetRotation().isPresent()) {
            state.setTargetRotation(Optional.empty());
        }
    }
}