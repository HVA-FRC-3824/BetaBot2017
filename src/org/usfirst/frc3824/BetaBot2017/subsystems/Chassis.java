// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc3824.BetaBot2017.subsystems;

import org.usfirst.frc3824.BetaBot2017.Robot;
import org.usfirst.frc3824.BetaBot2017.RobotMap;
//import org.usfirst.frc3824.BetaBot2017.commands.*;
import org.usfirst.frc3824.BetaBot2017.commands.TeleopDrive;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/*
 *
 */
public class Chassis extends Subsystem
{
	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final SpeedController motorRightA = RobotMap.chassisMotorRightA;
    private final SpeedController motorRightB = RobotMap.chassisMotorRightB;
    private final SpeedController motorLeftA = RobotMap.chassisMotorLeftA;
    private final SpeedController motorLeftB = RobotMap.chassisMotorLeftB;
    private final RobotDrive robotDrive = RobotMap.chassisRobotDrive;
    private final Encoder encoderRight = RobotMap.chassisEncoderRight;
    private final Encoder encoderLeft = RobotMap.chassisEncoderLeft;
    private final AnalogInput ultrasound = RobotMap.chassisUltrasound;
    private final AnalogGyro gyro = RobotMap.chassisGyro;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public static double ULTRASONIC_X1 = 0.086669;
	public static double ULTRASONIC_Y1 = 10.0;
	
	public static double ULTRASONIC_X2 = 0.467592;
	public static double ULTRASONIC_Y2 = 50.0;
	
	public void initDefaultCommand()
	{
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        setDefaultCommand(new TeleopDrive());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	/*
	 * Method to control the drive through the specified joystick
	 */
	public void driveWithJoystick(Joystick stick)
	{
		// Drive with arcade with the Y axis for forward/backward and steer with twist
		// Note: Set the sensitivity to true to decrease joystick at small input
		double twist = stick.getTwist();

		// Cube twist to decrease sensitivity
		twist = twist * twist * twist;

		// Create a dead zone for forward/backward
		double moveValue = stick.getY();
		if (moveValue < 0)
			moveValue = -1.0 * (moveValue * moveValue);
		else
			moveValue = moveValue * moveValue;

		// Drive with arcade control
		Robot.chassis.robotDrive.arcadeDrive(moveValue, twist, false);
	}

	/*
	 * Method to stop the chassis drive motors
	 */
	public void stop()
	{
		// Stop all motors
		Robot.chassis.robotDrive.arcadeDrive(0, 0);
	}
	
	/* 
	 * Compute the distance based on the ultrasonic sensor
	 */
	public double getDistanceFromUltrasonicSensor()
	{
		return ((ULTRASONIC_Y2 - ULTRASONIC_Y1) / (ULTRASONIC_X2 - ULTRASONIC_X1)) * 
				(ultrasound.getVoltage() - ULTRASONIC_X1) + ULTRASONIC_Y1;
	}
}
