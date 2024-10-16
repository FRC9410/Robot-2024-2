// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.BiConsumer;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import frc.robot.Constants.RobotConstants;
import frc.robot.Constants.SubsystemConstants;
import frc.team9410.lib.subsystem.BaseSubsystem;

public class ShooterFeeder extends BaseSubsystem {
  private final TalonFX feeder = new TalonFX(SubsystemConstants.ShooterFeeder.kFeederCanId, RobotConstants.kCtreCanBusName);
  private final VelocityVoltage feederVoltageVelocity = new VelocityVoltage(0, 0, false, 0, 0, false, false, false);
  private final NeutralOut brake = new NeutralOut();

  private final BiConsumer<String, Object> updateData;

  public ShooterFeeder(BiConsumer<String, Object> updateData) {
    setConfigs(feeder);
    this.updateData = updateData;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    updateData.accept("feederVelocity", feeder.getVelocity().getValueAsDouble());
  }

  public void setOff() {
    feeder.setControl(brake);
  }

  public void setVelocity(double velocity) {
    feeder.setControl(feederVoltageVelocity.withVelocity(velocity));

  }

  private static void setConfigs(TalonFX motor) {
    TalonFXConfiguration configs = new TalonFXConfiguration();
    /* Voltage-based velocity requires a feed forward to account for the back-emf of the motor */
    configs.Slot0.kP = 0.13; // An error of 1 rotation per second results in 2V output
    configs.Slot0.kI = 0.0; // An error of 1 rotation per second increases output by 0.5V every second
    configs.Slot0.kD = 0.0000; // A change of 1 rotation per second squared results in 0.01 volts output
    configs.Slot0.kV = 0.12; // Falcon 500 is a 500kV motor, 500rpm per V = 8.333 rps per V, 1/8.33 = 0.12 volts / Rotation per second
    // Peak output of 8 volts
    configs.Voltage.PeakForwardVoltage = 12;
    configs.Voltage.PeakReverseVoltage = -12;

    motor.getConfigurator().apply(configs);
  }

  public void setEnableIdleMode() {
    feeder.setNeutralMode(NeutralModeValue.Coast);
  }

  public void setDisabledIdleMode() {
    feeder.setNeutralMode(NeutralModeValue.Coast);
  }

  public double getVelocity() {
    return feeder.getVelocity().getValueAsDouble();
  }
}
