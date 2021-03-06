// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3824.BetaBot2017.commands;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc3824.BetaBot2017.Constants;
import org.usfirst.frc3824.BetaBot2017.Robot;

/**
 *
 */
public class ChassisDriveStraight extends Command {

	private Timer  m_Timer;
	private double m_DriveDuration;
	private double m_DrivePower;
	
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public ChassisDriveStraight() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.chassis);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        
		// instantiate a timer
        m_Timer = new Timer();

		// default test values
        m_DriveDuration  = 3.0;
        m_DrivePower     = 0.7;
    }

	/**
	 * Method to drive the chassis in a straight line
	 */
	public ChassisDriveStraight(double duration, double power, double direction)
	{
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.chassis);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

		// instantiate a timer
        m_Timer = new Timer();

		// copy the parameters to the class variables
        m_DriveDuration  = duration;
        m_DrivePower     = power;
	}

    
    // Called just before this Command runs the first time
    protected void initialize() {
		// Set the PID up for driving straight
    	Robot.chassis.driveStraightPID(m_DrivePower);

		// reset and start the timer
		m_Timer.reset();
		m_Timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
		return (m_Timer.get() > m_DriveDuration);
    }

    // Called once after isFinished returns true
    protected void end() {
		Robot.chassis.reset();
		m_Timer.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
