// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.CANSparkBase.IdleMode;

import frc.robot.Constants.RobotConstants;
import frc.robot.Constants.SubsystemConstants;
import frc.team9410.lib.subsystem.BaseSubsystem;

public class ShooterWheels extends BaseSubsystem {
  final private TalonFX primaryWheel;
  final private TalonFX secondaryWheel;
  final private VelocityVoltage secondaryWheelVoltageVelocity;
  final private VelocityVoltage primaryWheelVoltageVelocity;
  final private NeutralOut brake;

  public ShooterWheels() {
    primaryWheel = new TalonFX(SubsystemConstants.ShooterWheels.kPrimaryWheelCanId, RobotConstants.kCtreCanBusName);
    secondaryWheel = new TalonFX(SubsystemConstants.ShooterWheels.kSecondaryWheelCanId, RobotConstants.kCtreCanBusName);
    secondaryWheelVoltageVelocity = new VelocityVoltage(0, 0, true, 0, 0, false, false, false);
    primaryWheelVoltageVelocity = new VelocityVoltage(0, 0, true, 0, 0, false, false, false);
    brake = new NeutralOut();

    setConfigs(primaryWheel);
    setConfigs(secondaryWheel);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setVelocity(double velocity) {
    this.primaryWheel.setControl(primaryWheelVoltageVelocity.withVelocity(-velocity).withFeedForward(-SubsystemConstants.ShooterWheels.kFF));
    this.secondaryWheel.setControl(secondaryWheelVoltageVelocity.withVelocity(velocity-5).withFeedForward(SubsystemConstants.ShooterWheels.kFF));
  }

  public void setOff() {
    this.primaryWheel.setControl(brake);
    this.secondaryWheel.setControl(brake);
  }

  private static void setConfigs(TalonFX motor) {
    TalonFXConfiguration configs = new TalonFXConfiguration();
    /* Voltage-based velocity requires a feed forward to account for the back-emf of the motor */
    configs.Slot0.kP = 0.11; // An error of 1 rotation per second results in 2V output
    configs.Slot0.kI = 0.0; // An error of 1 rotation per second increases output by 0.5V every second
    configs.Slot0.kD = 0.0; // A change of 1 rotation per second squared results in 0.01 volts output
    configs.Slot0.kV = 0.12; // Falcon 500 is a 500kV motor, 500rpm per V = 8.333 rps per V, 1/8.33 = 0.12 volts / Rotation per second
    // Peak output of 8 volts
    configs.Voltage.PeakForwardVoltage = 16;
    configs.Voltage.PeakReverseVoltage = -16;

    motor.getConfigurator().apply(configs);
  }
  public void setEnableIdleMode() {
    primaryWheel.setNeutralMode(NeutralModeValue.Coast);
    secondaryWheel.setNeutralMode(NeutralModeValue.Coast);

  }

  public void setDisabledIdleMode() {
    primaryWheel.setNeutralMode(NeutralModeValue.Coast);
    secondaryWheel.setNeutralMode(NeutralModeValue.Coast);
  }

  public double getPrimaryWheelVelocity() {
    return primaryWheel.getVelocity().getValueAsDouble();
  }

  public double getSecondaryWheelVelocity() {
    return secondaryWheel.getVelocity().getValueAsDouble();
  }
}
