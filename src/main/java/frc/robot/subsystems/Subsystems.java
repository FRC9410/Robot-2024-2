package frc.robot.subsystems;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.TunerConstants;

import java.util.HashMap;
import java.util.Map;

/** Add your docs here. */
public class Subsystems {
    private Map<String, Object> subsystemData = new HashMap<>();;
    private CommandSwerveDrivetrain drivetrain;
    // private Leds leds = new Leds();
    // private Music music;
    private Vision vision = new Vision((key, value) -> updateSubsystemData(key, value));
    private IntakeWrist intakeWrist = new IntakeWrist((key, value) -> updateSubsystemData(key, value));
    private IntakeRollers intakeRollers = new IntakeRollers((key, value) -> updateSubsystemData(key, value));
    private ShooterWrist shooterWrist = new ShooterWrist((key, value) -> updateSubsystemData(key, value));
    private ShooterFeeder shooterFeeder = new ShooterFeeder((key, value) -> updateSubsystemData(key, value));
    private ShooterWheels shooterWheels = new ShooterWheels((key, value) -> updateSubsystemData(key, value));
    private StateMachine stateMachine;

    public Subsystems(CommandXboxController controller) {
        this.drivetrain = TunerConstants.DriveTrain;
        // this.music = new Music(drivetrain);
        this.stateMachine = new StateMachine(drivetrain, subsystemData);
    }

    public CommandSwerveDrivetrain getDrivetrain() {
        return drivetrain;
    }

    public StateMachine getRobotState() {
        return stateMachine;
    }

    // public Leds getLeds() {
    //     return leds;
    // }

    // public Music getMusic() {
    //     return music;
    // }

    public Vision getVision() {
        return vision;
    }

    public IntakeWrist getIntakeWrist() {
        return intakeWrist;
    }

    public IntakeRollers getIntakeRollers() {
        return intakeRollers;
    }

    public ShooterWrist getShooterWrist() {
        return shooterWrist;
    }

    public ShooterFeeder getShooterFeeder() {
        return shooterFeeder;
    }

    public ShooterWheels getShooterWheels() {
        return shooterWheels;
    }

    public StateMachine getStateMachine() {
        return stateMachine;
    }

    public void updateSubsystemData(String key, Object value) {
        subsystemData.put(key, value);
    }

    public void setDisabledIdleMode() {

    }

    public void setEnabledIdleMode() {

    }

    public void updatePosition() {
        Map<String, Object> poseWitTimeEstimate = vision.getPoseEstimate(drivetrain.getPose().getRotation().getDegrees());
        if (poseWitTimeEstimate != null) {
            Pose2d pose;
            if(poseWitTimeEstimate.get("2dpose") != null) {
                pose = (Pose2d) poseWitTimeEstimate.get("2dpose");
            } else {
                Pose3d pose3d = (Pose3d) poseWitTimeEstimate.get("3dpose");
                pose =  pose3d.toPose2d();
            }
            drivetrain.setVisionMeasurementStdDevs(VecBuilder.fill(.7, .7, 9999999));
            drivetrain.addVisionMeasurement(
                pose,
                (double) poseWitTimeEstimate.get("timestamp")
            );
            if (!vision.isYawSet()) {
                drivetrain.seedFieldRelative(pose);
                vision.setYaw();
            }
        }
    }
}