package frc.robot.subsystems;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.TunerConstants;

import java.util.Map;

/** Add your docs here. */
public class Subsystems {
    private CommandSwerveDrivetrain drivetrain;
    private Leds leds = new Leds();
    private Music music;
    private Vision vision = new Vision();
    private IntakeWrist intakeWrist = new IntakeWrist();
    private IntakeRollers intakeRollers = new IntakeRollers();
    private ShooterWrist shooterWrist = new ShooterWrist();
    private ShooterFeeder shooterFeeder = new ShooterFeeder();
    private ShooterWheels shooterWheels = new ShooterWheels();
    private RobotState robotState;

    public Subsystems(CommandXboxController controller) {
        this.drivetrain = TunerConstants.DriveTrain;
        this.music = new Music(drivetrain);
        this.robotState = new RobotState(drivetrain, vision, shooterFeeder, shooterWheels, controller);
    }

    public CommandSwerveDrivetrain getDrivetrain() {
        return drivetrain;
    }

    public RobotState getRobotState() {
        return robotState;
    }

    public Leds getLeds() {
        return leds;
    }

    public Music getMusic() {
        return music;
    }

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

    public void setDisabledIdleMode() {

    }

    public void setEnabledIdleMode() {

    }

    public void updatePosition() {
        Map<String, Object> poseWitTimeEstimate = vision.getPoseEstimate(drivetrain.getPose().getRotation().getDegrees());
        if (poseWitTimeEstimate != null) {
            Pose2d pose = (Pose2d) poseWitTimeEstimate.get("pose");
            pose.rotateBy(Rotation2d.fromDegrees(180));
            drivetrain.setVisionMeasurementStdDevs(VecBuilder.fill(.7, .7, 9999999));
            drivetrain.addVisionMeasurement(
                pose,
                (double) poseWitTimeEstimate.get("timestamp")
            );
            drivetrain.seedFieldRelative(pose);
        }
    }
}