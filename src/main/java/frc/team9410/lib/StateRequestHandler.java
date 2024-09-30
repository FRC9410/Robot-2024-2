package frc.team9410.lib;

import frc.robot.subsystems.StateMachine;
import frc.robot.subsystems.StateMachine.State;

public interface StateRequestHandler {
    boolean matches(StateMachine state, State request);
    void execute(StateMachine state);
}
