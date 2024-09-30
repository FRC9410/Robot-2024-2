package frc.team9410.lib;

import frc.robot.subsystems.StateMachine;
import frc.robot.subsystems.StateMachine.State;

public interface StateHandler {
    boolean matches(State state);
    void execute(StateMachine state);
}
