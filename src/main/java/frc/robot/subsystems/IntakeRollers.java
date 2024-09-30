// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import frc.robot.Constants.RobotConstants;
import frc.robot.Constants.SubsystemConstants;

import frc.team9410.lib.subsystem.BaseSubsystem;

public class IntakeRollers extends BaseSubsystem {
  private final TalonFX intake = new TalonFX(SubsystemConstants.IntakeRollers.kIntakeCanId, RobotConstants.kCtreCanBusName);
  private final VelocityVoltage voltageVelocity = new VelocityVoltage(0, 0, false, 0, 0, false, false, false);
  private final VelocityTorqueCurrentFOC torqueVelocity = new VelocityTorqueCurrentFOC(86, 86, 0, 0, false, false, false);
  private final NeutralOut brake = new NeutralOut();

  public IntakeRollers() {
    setConfigs(intake);
  }

  @Override
  public void periodic() {
    super.periodic();
  }

  public void setTorque(double velocity, double feedforward) {
    intake.setControl(torqueVelocity.withVelocity(velocity).withFeedForward(feedforward));
  }

  public void setVoltage(double velocity) {
    intake.setControl(voltageVelocity.withVelocity(velocity).withFeedForward(8));
  }

  public void setOff() {
    intake.setControl(brake);
  }

  private static void setConfigs(TalonFX motor) {
    TalonFXConfiguration configs = new TalonFXConfiguration();
    /* Voltage-based velocity requires a feed forward to account for the back-emf of the motor */
    configs.Slot0.kP = 0.3; // An error of 1 rotation per second results in 2V output
    configs.Slot0.kI = 0.0; // An error of 1 rotation per second increases output by 0.5V every second
    configs.Slot0.kD = 0.0000; // A change of 1 rotation per second squared results in 0.01 volts output
    configs.Slot0.kV = 0.12; // Falcon 500 is a 500kV motor, 500rpm per V = 8.333 rps per V, 1/8.33 = 0.12 volts / Rotation per second
    // Peak output of 8 volts
    configs.Voltage.PeakForwardVoltage = 12;
    configs.Voltage.PeakReverseVoltage = -12;

    motor.getConfigurator().apply(configs);
  }

  public void setEnableIdleMode() {
    intake.setNeutralMode(NeutralModeValue.Coast);
  }

  public void setDisabledIdleMode() {
    intake.setNeutralMode(NeutralModeValue.Coast);
  }
}
