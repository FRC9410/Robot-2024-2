package frc.robot.subsystems.state.states;

import frc.robot.subsystems.StateMachine.State;
import frc.robot.subsystems.StateMachine;
import frc.team9410.lib.StateHandler;

public class DevModeState implements StateHandler {
    public boolean matches(State state) {
        return state.equals(State.DEV_MODE);
    }
    
    public void execute(StateMachine state) {}
}