package frc.robot.subsystems;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.states.*;
import frc.team9410.lib.StateConditionHandler;
import frc.team9410.lib.Utility;
import au.grapplerobotics.ConfigurationFailedException;
import au.grapplerobotics.LaserCan;

import java.util.List;
import java.util.Optional;

public class RobotState extends SubsystemBase {

  private final CommandSwerveDrivetrain drivetrain;
  private final Vision vision;
  private final ShooterFeeder feeder;
  private final ShooterWheels shooterWheels;
  private final CommandXboxController controller;
  private final NetworkTableInstance inst = NetworkTableInstance.getDefault();
  private final NetworkTable dashboardTable = inst.getTable("Dashboard");

  private final LaserCan intakeLaser = new LaserCan(13);

  private final Debouncer debouncer = new Debouncer(0.5, Debouncer.DebounceType.kBoth);

  private State state = State.IDLE;
  private String allianceColor;
  private double locationX = -1;
  private double locationY = -1;
  private double rotation = 0;

  private double targetX = -1;
  private double targetY = -1;
  private Optional<Rotation2d> targetRotation = Optional.empty();

  private boolean isFollowingPath = false;
  private boolean commandExecuting = false;
  
  private final List<StateConditionHandler> handlers = List.of(
    new DemoModeState(),
    new DevModeState(),
    new IntakingState(),
    new ShootingReadyState(),
    new ShootingState(),
    new DunkingReadyState(),
    new DunkingState(),
    new ClimbingReadyState(),
    new IdleState()
  );

  public RobotState(CommandSwerveDrivetrain drivetrain, Vision vision, ShooterFeeder shooterFeeder,
      ShooterWheels shooterWheels, CommandXboxController controller) {
    this.drivetrain = drivetrain;
    this.vision = vision;
    this.feeder = shooterFeeder;
    this.shooterWheels = shooterWheels;
    this.controller = controller;

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
    if (allianceColor.isEmpty() || allianceColor == null) {
      allianceColor = Utility.getAllianceColor();
    }

    Pose2d pose = drivetrain.getPose();
    locationX = pose.getX();
    locationY = pose.getY();
    rotation = pose.getRotation().getDegrees();

    if (dashboardTable.getEntry("Development Mode").getBoolean(false)
        && state != State.DEV_MODE) {
      state = State.DEV_MODE;
    } else if (state == State.DEV_MODE) {
      state = State.IDLE;
    }

    if (!state.equals(State.DEV_MODE)
        && !state.equals(State.DEMO_MODE)
        && controller.a().getAsBoolean()) {
      isFollowingPath = controller.a().getAsBoolean();
    } else {
      isFollowingPath = false;
    }

    if (!commandExecuting) {
      for (StateConditionHandler handler : handlers) {
          if (handler.matches(this)) {
              handler.execute(this);
              break;
          }
      }
    }
  }

  public State getState() {
    return state;
  }

  public String getAllianceColor() {
    return allianceColor;
  }

  public double getLocationX() {
    return locationX;
  }

  public double getLocationY() {
    return locationY;
  }

  public double getRotation() {
    return rotation;
  }

  public double getTargetX() {
    return targetX;
  }

  public double getTargetY() {
    return targetY;
  }

  public Optional<Rotation2d> getTargetRotation() {
    return targetRotation;
  }

  public boolean getIsFollowingPath() {
    return isFollowingPath;
  }

  public CommandXboxController getController() {
    return controller;
  }

  public ShooterFeeder getShooterFeeder() {
    return feeder;
  }

  public ShooterWheels getShooterWheels() {
    return shooterWheels;
  }

  public Vision getVision() {
    return vision;
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

  public void setTargetX(double targetX) {
    this.targetX = targetX;
  }

  public void setTargetY(double targetY) {
    this.targetY = targetY;
  }

  public void setTargetRotation(Optional<Rotation2d> targetRotation) {
    this.targetRotation = targetRotation;
  }

  public enum State {
    IDLE,
    INTAKING,
    SHOOTING,
    SHOOTING_READY,
    DUNKING,
    DUNKING_READY,
    CLIMBING_READY,
    COMMAND_EXECUTING,
    DEV_MODE,
    DEMO_MODE
  }
}
