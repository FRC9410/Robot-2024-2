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
    private Leds leds;
    private Music music;
    private Vision vision;
    private RobotState robotState;
    private IntakeWrist intakeWrist;
    private IntakeRollers intakeRollers;
    private ShooterWrist shooterWrist;
    private ShooterFeeder shooterFeeder;
    private ShooterWheels shooterWheels;

    public Subsystems(CommandXboxController controller) {
        this.drivetrain = TunerConstants.DriveTrain;
        this.leds = new Leds();
        this.music = new Music(drivetrain);
        this.vision = new Vision();
        this.intakeWrist = new IntakeWrist();
        this.intakeRollers = new IntakeRollers();
        this.shooterWrist = new ShooterWrist();
        this.shooterFeeder = new ShooterFeeder();
        this.shooterWheels = new ShooterWheels();
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