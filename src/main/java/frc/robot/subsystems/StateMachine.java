package frc.robot.subsystems;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.state.states.*;
import frc.robot.subsystems.state.requests.*;
import frc.team9410.lib.StateHandler;
import frc.team9410.lib.StateRequestHandler;
import frc.team9410.lib.Utility;
import au.grapplerobotics.ConfigurationFailedException;
import au.grapplerobotics.LaserCan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateMachine extends SubsystemBase {

  private final CommandSwerveDrivetrain drivetrain;
  Map<String, Object> subsystemData;
  Map<String, Object> commandData = new HashMap<>();

  private final LaserCan intakeLaser = new LaserCan(13);

  private final Debouncer debouncer = new Debouncer(0.5, Debouncer.DebounceType.kBoth);

  private State state = State.IDLE;
  private String allianceColor;

  private boolean isFollowingPath = false;
  private boolean commandExecuting = false;
  private NetworkTableInstance inst = NetworkTableInstance.getDefault();
  private NetworkTable dashboardTable = inst.getTable("Dashboard");
  
  private final List<StateRequestHandler> requestHandlers = List.of(
    new DemoModeStateRequest(),
    new DevModeStateRequest(),
    new IntakingStateRequest(),
    new ShootingStateRequest(),
    new ShootingReadyStateRequest(),
    new DunkingStateRequest(),
    new DunkingReadyStateRequest(),
    new ClimbingLeftReadyStateRequest(),
    new ClimbingRightReadyStateRequest(),
    new IdleStateRequest()
  );
  
  private final List<StateHandler> stateHandlers = List.of(
    new DemoModeState(),
    new DevModeState(),
    new IntakingState(),
    new ShootingReadyState(),
    new ShootingState(),
    new DunkingReadyState(),
    new DunkingState(),
    new ClimbingLeftReadyState(),
    new ClimbingRightReadyState(),
    new IdleState()
  );

  public StateMachine(CommandSwerveDrivetrain drivetrain, Map<String, Object> subsystemData) {
    this.drivetrain = drivetrain;
    this.subsystemData = subsystemData;

    try {
      intakeLaser.setRangingMode(LaserCan.RangingMode.SHORT);
      intakeLaser.setRegionOfInterest(new LaserCan.RegionOfInterest(8, 8, 16, 16));
      intakeLaser.setTimingBudget(LaserCan.TimingBudget.TIMING_BUDGET_33MS);
    } catch (ConfigurationFailedException e) {
      System.out.println("Configuration failed! " + e);
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (allianceColor == null || allianceColor.isEmpty()) {
      allianceColor = Utility.getAllianceColor();
    }

    Pose2d pose = drivetrain.getPose();
    updateSubsystemData("locationX", pose.getX());
    updateSubsystemData("locationY", pose.getY());
    updateSubsystemData("rotation", pose.getRotation().getDegrees());
    updateSubsystemData("state", state);

    if (!commandExecuting) {
      for (StateHandler handler : stateHandlers) {
        if (handler.matches(state)) {
          handler.execute(this);
          break;
        }
      }
    }

    logMap(subsystemData, "Subsystem Data");
    logMap(commandData, "Command Data");
  }

  public State getState() {
    return state;
  }

  public String getAllianceColor() {
    return allianceColor;
  }

  public void updateCommandData(String key, Object value) {
    commandData.put(key, value);
  }

  public void removeCommandKey(String key) {
    commandData.remove(key);
  }

  public void removeMultipleKeys(List<String> keys) {
    for (String key : keys) {
      subsystemData.remove(key);
    }
  }

  public void removeMultipleCommandKeys(List<String> keys) {
    for (String key : keys) {
      commandData.remove(key);
    }
  }

  public Object getCommandData(String key) {
      Object value = commandData.get(key);

      if (value instanceof String) {
          return (String) value;
      } else if (value instanceof Integer) {
          return (Integer) value;
      } else if (value instanceof Double) {
          return (Double) value;
      } else {
          return null; // Handle cases where the value is not of expected type
      }
  }

  public void updateSubsystemData(String key, Object value) {
    subsystemData.put(key, value);
  }

  public Object getSubsystemData(String key) {
      Object value = subsystemData.get(key);

      if (value instanceof String) {
          return (String) value;
      } else if (value instanceof Integer) {
          return (Integer) value;
      } else if (value instanceof Double) {
          return (Double) value;
      } else if (value instanceof Boolean) {
          return (Boolean) value;
      }else {
          return null; // Handle cases where the value is not of expected type
      }
  }

  public boolean getIsFollowingPath() {
    return isFollowingPath;
  }

  public LaserCan getIntakeLaser() {
    return intakeLaser;
  }

  public Debouncer getDebouncer() {
    return debouncer;
  }

  public void setCommandExecuting(boolean commandExecuting) {
    this.commandExecuting = commandExecuting;
  }

  public void setState(State state) {
    this.state = state;
  }

  public void requestStateChange(State requestedState) {
    for (StateRequestHandler handler : requestHandlers) {
      if (handler.matches(this, requestedState)) {
        handler.execute(this);
        break;
      }
    }
  }

  public void logMap(Map<String, Object> map, String tableName) {
    NetworkTable table = inst.getTable(tableName);
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      if (entry.getValue() instanceof Double) {
        table.getEntry(entry.getKey()).setDouble((Double) entry.getValue());
      } else if (entry.getValue() instanceof Integer) {
        table.getEntry(entry.getKey()).setNumber((Integer) entry.getValue());
      } else if (entry.getValue() instanceof Boolean) {
        table.getEntry(entry.getKey()).setBoolean((Boolean) entry.getValue());
      } else {
      table.getEntry(entry.getKey()).setString(entry.getValue().toString());
      }
    }
  }

  public enum State {
    IDLE,
    INTAKING,
    SHOOTING,
    SHOOTING_READY,
    DUNKING,
    DUNKING_READY,
    CLIMBING_LEFT_READY,
    CLIMBING_RIGHT_READY,
    COMMAND_EXECUTING,
    DEV_MODE,
    DEMO_MODE
  }
}
