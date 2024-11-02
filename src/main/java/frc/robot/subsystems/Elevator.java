// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import java.util.List;
import java.util.function.BiConsumer;

import frc.robot.Constants.SubsystemConstants;
import frc.team9410.lib.subsystem.ControlledSubsystem;

public class Elevator extends SubsystemBase {
  private final CANSparkMax primaryElevator = new CANSparkMax(SubsystemConstants.Elevator.kPrimaryElevatorCanId, MotorType.kBrushless);
  private final CANSparkMax secondaryElevator = new CANSparkMax(SubsystemConstants.Elevator.kSecondaryElevatorCanId, MotorType.kBrushless);
  private final SparkPIDController pidController = primaryElevator.getPIDController();
  private RelativeEncoder encoder = primaryElevator.getEncoder();
  private double setpoint = 0;

  private final BiConsumer<String, Object> updateData;
  private NetworkTable dashboardTable;
  private NetworkTableInstance inst;
  private NetworkTable subsystemTable;


  /** Creates a new Elevator. */
  public Elevator(BiConsumer<String, Object> updateData) {
    this.primaryElevator.restoreFactoryDefaults();
    this.secondaryElevator.restoreFactoryDefaults();
    this.secondaryElevator.follow(primaryElevator, true);
    this.primaryElevator.setIdleMode(IdleMode.kBrake);
    this.secondaryElevator.setIdleMode(IdleMode.kBrake);
    this.encoder.setPosition(0);
    this.pidController.setFeedbackDevice(encoder);

    this.pidController.setP(SubsystemConstants.Elevator.kP);
    this.pidController.setI(SubsystemConstants.Elevator.kI);
    this.pidController.setD(SubsystemConstants.Elevator.kD);
    this.pidController.setIZone(SubsystemConstants.Elevator.kIz);
    this.pidController.setFF(SubsystemConstants.Elevator.kFF);
    this.pidController.setOutputRange(SubsystemConstants.Elevator.kMinOutput, SubsystemConstants.Elevator.kMaxOutput);
    
    pidController.setSmartMotionMaxAccel(SubsystemConstants.Elevator.maxAcc, 0);
    pidController.setSmartMotionMaxVelocity(SubsystemConstants.Elevator.maxVel, 0);
    pidController.setSmartMotionAllowedClosedLoopError(SubsystemConstants.Elevator.allowedError, 0);
    
    this.updateData = updateData;

    // createSubsystemTable("Elevator");
    // addSparkMax(List.of(primaryElevator, secondaryElevator));
    // addRelativePIDController(pidController, encoder, setpoint);
    inst = NetworkTableInstance.getDefault();
    dashboardTable = inst.getTable("Dashboard");
    subsystemTable = inst.getTable("Elevator");
  }

  @Override
  public void periodic() {
    updateData.accept("elevatorPosition", getPosition());

    String deviceName = "CAN ID 41";
    subsystemTable.getEntry(deviceName + ": Velocity").setDouble(primaryElevator.getEncoder().getVelocity());
    subsystemTable.getEntry(deviceName + ": Output Current").setDouble(primaryElevator.getOutputCurrent());
    subsystemTable.getEntry(deviceName + ": Output Voltage").setDouble(primaryElevator.getAppliedOutput());
    subsystemTable.getEntry(deviceName + ": Motor Temperature").setDouble(primaryElevator.getMotorTemperature());

    String deviceName2 = "CAN ID 42";
    subsystemTable.getEntry(deviceName2 + ": Velocity").setDouble(secondaryElevator.getEncoder().getVelocity());
    subsystemTable.getEntry(deviceName2 + ": Output Current").setDouble(secondaryElevator.getOutputCurrent());
    subsystemTable.getEntry(deviceName2 + ": Output Voltage").setDouble(secondaryElevator.getAppliedOutput());
    subsystemTable.getEntry(deviceName2 + ": Motor Temperature").setDouble(secondaryElevator.getMotorTemperature());
  }

  public void setElevatorPosition(double position) {
    this.pidController.setReference(position, CANSparkMax.ControlType.kPosition);
  }
 
  public void elevatorOff() {
    this.pidController.setReference(SubsystemConstants.Elevator.kMinRotation, CANSparkMax.ControlType.kPosition);
  }

  public void setEnableIdleMode() {
    primaryElevator.setIdleMode(IdleMode.kBrake);
    secondaryElevator.setIdleMode(IdleMode.kBrake);
  }

  public void setDisabledIdleMode() {
    primaryElevator.setIdleMode(IdleMode.kBrake);
    secondaryElevator.setIdleMode(IdleMode.kBrake);
  }

  public double getPosition() {
    return this.encoder.getPosition();
  }
}
