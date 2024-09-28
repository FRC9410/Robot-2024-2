// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import java.util.List;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.Constants.SubsystemConstants;
import frc.team9410.lib.subsystem.ControlledSubsystem;

public class ShooterWrist extends ControlledSubsystem {
  final private CANSparkMax primaryWrist;
  final private CANSparkMax secondaryWrist;
  final private SparkPIDController pidController;
  final private RelativeEncoder encoder;

  private double setpoint;

  public ShooterWrist() {
    primaryWrist = new CANSparkMax(SubsystemConstants.ShooterWrist.kPrimaryWristCanId, MotorType.kBrushless);
    secondaryWrist = new CANSparkMax(SubsystemConstants.ShooterWrist.kSecondaryWristCanId, MotorType.kBrushless);
    this.primaryWrist.restoreFactoryDefaults();
    this.secondaryWrist.restoreFactoryDefaults();
    this.secondaryWrist.follow(primaryWrist, true);
    this.primaryWrist.setIdleMode(IdleMode.kBrake);
    this.secondaryWrist.setIdleMode(IdleMode.kBrake);


    this.pidController = primaryWrist.getPIDController();

    this.encoder = primaryWrist.getEncoder();
    this.encoder.setPosition(-0.2);
    this.pidController.setFeedbackDevice(encoder);

    this.pidController.setP(SubsystemConstants.ShooterWrist.kP);
    this.pidController.setI(SubsystemConstants.ShooterWrist.kI);
    this.pidController.setD(SubsystemConstants.ShooterWrist.kD);
    this.pidController.setIZone(SubsystemConstants.ShooterWrist.kIz);
    this.pidController.setFF(SubsystemConstants.ShooterWrist.kFF);
    this.pidController.setOutputRange(SubsystemConstants.ShooterWrist.kMinOutput, SubsystemConstants.ShooterWrist.kMaxOutput);
    
    pidController.setSmartMotionMaxAccel(SubsystemConstants.ShooterWrist.maxAcc, 0);
    pidController.setSmartMotionMaxVelocity(SubsystemConstants.ShooterWrist.maxVel, 0);
    pidController.setSmartMotionAllowedClosedLoopError(SubsystemConstants.ShooterWrist.allowedError, 0);
    
    this.setpoint = SubsystemConstants.ShooterWrist.kMinRotation;
    this.pidController.setReference(setpoint, CANSparkMax.ControlType.kPosition);

    createSubsystemTable("Shooter Wrist");
    addSparkMax(List.of(primaryWrist, secondaryWrist));
    addRelativePIDController(pidController, encoder, setpoint);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setWristAngle(double angle) {
    this.pidController.setReference(angle, CANSparkMax.ControlType.kPosition);
  }

  public void setEnableIdleMode() {
    primaryWrist.setIdleMode(IdleMode.kBrake);
    secondaryWrist.setIdleMode(IdleMode.kBrake);

  }

  public void setDisabledIdleMode() {
    primaryWrist.setIdleMode(IdleMode.kCoast);
    secondaryWrist.setIdleMode(IdleMode.kCoast);
  }
}
