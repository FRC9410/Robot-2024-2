// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkLowLevel.MotorType;

import java.util.List;
import java.util.function.BiConsumer;

import frc.team9410.lib.subsystem.ControlledSubsystem;

import frc.robot.Constants.SubsystemConstants;

public class IntakeWrist extends ControlledSubsystem {


  private final CANSparkMax primaryWrist = new CANSparkMax(SubsystemConstants.IntakeWrist.kPrimaryWristCanId, MotorType.kBrushless);
  private final CANSparkMax secondaryWrist = new CANSparkMax(SubsystemConstants.IntakeWrist.kSecondaryWristCanId, MotorType.kBrushless);
  private final SparkPIDController pidController = primaryWrist.getPIDController();
  private final AbsoluteEncoder encoder = primaryWrist.getAbsoluteEncoder(SubsystemConstants.IntakeWrist.kAbsEncType);
  private final double setpoint = SubsystemConstants.IntakeWrist.kMinRotation;

  private final BiConsumer<String, Object> updateData;

  public IntakeWrist(BiConsumer<String, Object> updateData) {
    this.primaryWrist.restoreFactoryDefaults();
    this.secondaryWrist.restoreFactoryDefaults();
    this.secondaryWrist.follow(primaryWrist, true);
    this.primaryWrist.setIdleMode(IdleMode.kBrake);
    this.secondaryWrist.setIdleMode(IdleMode.kBrake);
    
    this.encoder.setZeroOffset(SubsystemConstants.IntakeWrist.kOffset);
    this.pidController.setFeedbackDevice(encoder);

    this.pidController.setP(SubsystemConstants.IntakeWrist.kP);
    this.pidController.setI(SubsystemConstants.IntakeWrist.kI);
    this.pidController.setD(SubsystemConstants.IntakeWrist.kD);
    this.pidController.setOutputRange(SubsystemConstants.IntakeWrist.kMinOutput, SubsystemConstants.IntakeWrist.kMaxOutput);
    
    pidController.setSmartMotionMaxAccel(SubsystemConstants.IntakeWrist.maxAcc, 0);
    pidController.setSmartMotionMaxVelocity(SubsystemConstants.IntakeWrist.maxVel, 0);
    pidController.setSmartMotionAllowedClosedLoopError(SubsystemConstants.IntakeWrist.allowedError, 0);

    this.pidController.setReference(setpoint, CANSparkMax.ControlType.kPosition);

    this.updateData = updateData;

    createSubsystemTable("Intake Wrist");
    addSparkMax(List.of(primaryWrist, secondaryWrist));
    addAbosultePIDController(pidController, encoder, setpoint);
  }

  @Override
  public void periodic() {
    super.periodic();
  }
  
  public void setAngle(double angle) {
    this.pidController.setReference(angle, CANSparkMax.ControlType.kPosition);
  }

  public void setMinAngle() {
    this.pidController.setReference(SubsystemConstants.IntakeWrist.kMinRotation, CANSparkMax.ControlType.kPosition);
  }

  public double getPosition() {
    return encoder.getPosition();
  }

  public void setEnableIdleMode() {
    setAllMotorsBrakeMode();
  }

  public void setDisabledIdleMode() {
    setAllMotorsCoastMode();
  }
}
