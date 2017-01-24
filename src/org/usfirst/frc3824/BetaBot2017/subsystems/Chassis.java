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

import org.usfirst.frc3824.BetaBot2017.Constants;
import org.usfirst.frc3824.BetaBot2017.Robot;
import org.usfirst.frc3824.BetaBot2017.RobotMap;
//import org.usfirst.frc3824.BetaBot2017.commands.*;
import org.usfirst.frc3824.BetaBot2017.commands.TeleopDrive;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/*
 *
 */
public class Chassis extends Subsystem
{
	public static double ULTRASONIC_X1 = 0.086669;
	public static double ULTRASONIC_Y1 = 10.0;
	
	public static double ULTRASONIC_X2 = 0.467592;
	public static double ULTRASONIC_Y2 = 50.0;

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

	// Parameter used for drive while running under PID Control. The values
	// not set by the controller constructor can be set by a command directly
	private double m_magnitude;

	// PID controller for driving based on Gyro
	private PIDController angleGyroPID;

	public Chassis() {
		// Create angleGyroPID
		angleGyroPID = new PIDController(
			Constants.DRIVETRAIN_DRIVE_STRAIGHT_P,
            Constants.DRIVETRAIN_DRIVE_STRAIGHT_I, 
            Constants.DRIVETRAIN_DRIVE_STRAIGHT_D, 
            new GyroPIDSource(), new AnglePIDOutput()
        );
		
		// The gyro angle uses input values from 0 to 360
		angleGyroPID.setInputRange(0, 360);
		
		angleGyroPID.setAbsoluteTolerance(Constants.TURN_THRESHOLD);
				
		// Consider 0 and 360 to be the same point
		angleGyroPID.setContinuous(true);
	}
	
	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	
	public void initDefaultCommand()
	{
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        setDefaultCommand(new TeleopDrive());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
	
	// ************************************
	// Methods to call from commands
	// ************************************	

	/*
	 * Method to stop the chassis drive motors and disable/reset pids
	 */
	public void reset()
	{
		angleGyroPID.disable();
		angleGyroPID.reset();
		gyro.reset();
		encoderLeft.reset();
		encoderRight.reset();
		Robot.chassis.robotDrive.arcadeDrive(0, 0);
	}
	
	/*
	 * Method to control the drive through the specified joystick
	 */
	public void driveWithJoystick(Joystick stick)
	{
		// Cube twist to decrease sensitivity
    	double twist = stick.getTwist();
		twist = twist * twist * twist;

		// Square forward/backward to decrease sensitivity
		double moveValue = stick.getY();
		if (moveValue < 0) {
			// remember to preserve direction, it is lost when squaring
			moveValue = -1.0 * (moveValue * moveValue);
		} else {
			moveValue = moveValue * moveValue;
		}

   		// Drive with arcade control
		Robot.chassis.robotDrive.arcadeDrive(moveValue, twist);
	}
		
	/**
	 * Method to configure the gyro based turn/drive straight PID controller
	 */
	public void driveStraightPID(double power) {		
		// update the drive power
		m_magnitude = power;

		startGyroPID(
			Constants.DRIVETRAIN_DRIVE_STRAIGHT_P,
			Constants.DRIVETRAIN_DRIVE_STRAIGHT_I,
			Constants.DRIVETRAIN_DRIVE_STRAIGHT_D,
			Constants.DRIVETRAIN_DRIVE_MINIMUM_OUTPUT,
			Constants.DRIVETRAIN_DRIVE_MAXIMUM_OUTPUT,
			Constants.DRIVETRAIN_DRIVE_TOLERANCE,
			// drive straight means keep current heading
			0
		);
	}
	
	/**
	 * Method to configure the gyro based turn/drive straight PID controller
	 */
	public void turnAnglePID(double desiredHeading, double power) {		
		// update the drive power
		m_magnitude = power;

		startGyroPID(
			Constants.DRIVETRAIN_DRIVE_STRAIGHT_P,
			Constants.DRIVETRAIN_DRIVE_STRAIGHT_I,
			Constants.DRIVETRAIN_DRIVE_STRAIGHT_D,
			Constants.DRIVETRAIN_DRIVE_MINIMUM_OUTPUT,
			Constants.DRIVETRAIN_DRIVE_MAXIMUM_OUTPUT,
			Constants.DRIVETRAIN_DRIVE_TOLERANCE,
			desiredHeading
		);
	}
	
	/**
	 * Method to update output power while under PID control
	 * ie. after startGyroPID() is called
	 * 
	 * @param magnitude
	 */
	public void updateMagnitude(double magnitude) {
		m_magnitude = magnitude;
	}
	
	// ************************************
	// Methods to get values from chassis sensors
	// ************************************	

	/**
	 * Method to return the present gyro angle
	 */
	public double getCurrentHeading()
	{
		// Return the relative gyro angle
		return getRelativeAngle(gyro.getAngle());
	}
	
	public double getEncoderDistance() {
		// Return the maximum encoder distance in case the other is not working
		return Math.max(encoderLeft.getDistance(), encoderRight.getDistance());
	}

	public boolean gyroPIDOnTarget() {
		return angleGyroPID.onTarget();
	}
	
	/* 
	 * Compute the distance based on the ultrasonic sensor
	 */
	public double getUltrasonicDistance()
	{
		return ((ULTRASONIC_Y2 - ULTRASONIC_Y1) / (ULTRASONIC_X2 - ULTRASONIC_X1)) * 
				(ultrasound.getVoltage() - ULTRASONIC_X1) + ULTRASONIC_Y1;
	}
	
	// ************************************
	// Private helpers
	// ************************************	

	/**
	 * set chassis to be under PID control
	 * 
	 * @param P
	 * @param I
	 * @param D
	 * @param minimumOutput
	 * @param maximumOutput
	 * @param tolerance
	 * @param desiredHeading (relative to current heading, 0 is keep current heading)
	 */
	private void startGyroPID(double P, double I, double D, double minimumOutput, double maximumOutput, double tolerance, double desiredHeading)
	{
		// reset other PIDS
		reset();

		angleGyroPID.setPID(P, I, D);
		
		// our angleGyroPID works from 0 to 360, make sure target is in that range
		double target = getRelativeAngle(getCurrentHeading() + desiredHeading);
		
		angleGyroPID.setSetpoint(target);

		// Limit the output power when turning
		angleGyroPID.setOutputRange(minimumOutput, maximumOutput);

		angleGyroPID.setAbsoluteTolerance(tolerance);
		
		angleGyroPID.enable();
	}
	
	/**
	 * Method to return a relative gyro angle (between 0 and 360)
	 */
	private double getRelativeAngle(double angle)
	{
		// Adjust the angle if negative
		while (angle < 0.0)
			angle += 360.0;

		// Adjust the angle if greater than 360
		while (angle >= 360.0)
			angle -= 360.0;

		// Return the angle between 0 and 360
		return angle;
	}
	
	/**
	 * Class declaration for the PIDOutput
	 */
	private class AnglePIDOutput implements PIDOutput
	{
		/**
		 * Virtual function to receive the PID output and set the drive direction 
		 */
		public void pidWrite(double PIDoutput)
		{
			// Drive the robot given the speed and direction
			// Note: The Arcade drive expects a joystick which is negative forward
			robotDrive.arcadeDrive(-m_magnitude, PIDoutput, false);
		}
	}
	
	/**
	 * Gyro PID source that computes relative angle from gyro
	 */
	private class GyroPIDSource implements PIDSource
	{
		@Override
		public double pidGet()
		{
			return getRelativeAngle(gyro.getAngle());
		}

		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {
			gyro.setPIDSourceType(pidSource);
		}

		@Override
		public PIDSourceType getPIDSourceType() {
			return gyro.getPIDSourceType();
		}
	}

}
