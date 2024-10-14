package frc.robot.subsystems;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.FieldConstants;
import frc.robot.subsystems.Vision.VisionType;
import frc.team9410.lib.LimelightHelpers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class Vision extends SubsystemBase {
    public final String GAME_PIECE_TABLE_NAME = "limelight-front";
    public final String TAG_TABLE_LEFT_NAME = "limelight-bl";
    public final String TAG_TABLE_RIGHT_NAME = "limelight-br";

    private final NetworkTable gamePieceTable = NetworkTableInstance.getDefault().getTable(GAME_PIECE_TABLE_NAME);
    private final NetworkTable tagTableLeft = NetworkTableInstance.getDefault().getTable(TAG_TABLE_LEFT_NAME);
    private final NetworkTable tagTableRight = NetworkTableInstance.getDefault().getTable(TAG_TABLE_RIGHT_NAME);

    private boolean yawSet = false;

    private final BiConsumer<String, Object> updateData;

    /** Creates a new Vision. */
    public Vision(BiConsumer<String, Object> updateData) {
        gamePieceTable.getEntry("ledMode").setNumber(1);
        // tagTable.getEntry("ledMode").setNumber(1);
        this.updateData = updateData;
    }

    @Override
    public void periodic() {
    // This method will be called once per scheduler run
        updateData.accept("hasGamePieceTarget", hasTarget(VisionType.GAME_PIECE));
        
        if (hasTarget(VisionType.GAME_PIECE)) {
            updateData.accept("gamePieceTx", getTx(VisionType.GAME_PIECE));
            updateData.accept("gamePieceTy", getTy(VisionType.GAME_PIECE));
        } else {
            updateData.accept("gamePieceTx", 100.0);
            updateData.accept("gamePieceTy", 100.0);
        }
    }

    public double getTx(VisionType type) {
        return getTable(type).getEntry("tx").getDouble(0);
    }

    public double getTy(VisionType type) {
        return  getTable(type).getEntry("ty").getDouble(0);
    }

    public double getTa(VisionType type) {
        return getTable(type).getEntry("ta").getDouble(0);
    }

    public int getTagId(VisionType type) {
        double tagId = getTable(type).getEntry("tid").getDouble(0);
        return (int) tagId;
    }

    public boolean hasTarget(VisionType type) {
        return getTable(type).getEntry("tv").getDouble(0) == 1;
    }

    public void setPipeline(VisionType type, int pipeline) {
        getTable(type).getEntry("pipeline").setNumber(pipeline);
    }
  
    public int getPipeline(VisionType type) {
        return (int) getTable(type).getEntry("getpipe").getDouble(-1);
    }

    public boolean isYawSet(){
        return yawSet;
    }

    public void setYaw(boolean isSet){
        yawSet = isSet;
    }

    public Map<String, Object> getPoseEstimate (double yaw) {
        final Map<String, Object> result = new HashMap<>();
        final double leftTa = getTa(VisionType.TAG_LEFT);
        final boolean leftTagIsIncludedTag = Arrays.stream(FieldConstants.includedTags).anyMatch(tag -> tag == getTagId(VisionType.TAG_LEFT));
        final double rightTa = getTa(VisionType.TAG_RIGHT);
        final boolean rightTagIsIncludedTag = Arrays.stream(FieldConstants.includedTags).anyMatch(tag -> tag == getTagId(VisionType.TAG_LEFT));
        final String bestCamera;
        if (leftTa > rightTa && leftTagIsIncludedTag) {
            bestCamera = TAG_TABLE_LEFT_NAME;
        } else if (rightTa > leftTa && rightTagIsIncludedTag) {
            bestCamera = TAG_TABLE_RIGHT_NAME;
        } else {
            return null;
        }

        LimelightHelpers.SetRobotOrientation(bestCamera, yaw, 0, 0, 0, 0, 0);
        LimelightHelpers.PoseEstimate limelight2dMeasurement = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(bestCamera);

        if(limelight2dMeasurement == null || limelight2dMeasurement.tagCount < 1 || limelight2dMeasurement.avgTagArea < 0.2) {
            return null;
        }

        Pose2d pose = limelight2dMeasurement.pose;
        result.put("2dpose", pose);
        result.put("timestamp", limelight2dMeasurement.timestampSeconds);

        Pose3d pose3d = LimelightHelpers.getBotPose3d_wpiBlue(bestCamera);
        result.put("3dpose", pose3d);

        return result;
    }

    public Optional<Rotation2d> getGamePieceRotation() {
        if(!hasTarget(VisionType.GAME_PIECE)) {
            return Optional.empty();
        }
        return Optional.of(Rotation2d.fromDegrees(getTx(VisionType.GAME_PIECE)));
    }

    private NetworkTable getTable(VisionType type) {
        switch (type) {
            case GAME_PIECE:
                return gamePieceTable;
            case TAG_LEFT:
                return tagTableLeft;
            case TAG_RIGHT:
                return tagTableRight;
            default:
                return null;
        }
    }

    public enum VisionType {
        GAME_PIECE, TAG_LEFT, TAG_RIGHT
    }
}