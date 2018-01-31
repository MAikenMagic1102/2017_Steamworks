package org.usfirst.frc.team1102.robot;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;

/*
 * 
 * 	how to use
 * 
 *  testPidMotor = new PIDMotor ( <Talon PWM channel> , 
 *  								<encoder aChannel>, <encoder bChannel>,
 *  								[PIDSourceType.kRate | PIDSourceType.kDisplacement],
 *  								<contituous encoder? True/false>,
 *  								P , I, D, F )  
 *  To set PID 
 *    	PIDset(double setpoint)
 *  
 *  
 *  Reset regulator	
 *	 		PIDreset()
 *  Enable regulator
 *	 		enable()
 *  Disable regulator
 *			disable()
 *	Set what value on the encoder will be considered as "1" 
 *  		setOutputOne(double)  
 */
	 

public class PIDMotorDrive extends Victor 
{
	boolean m_contituous;
	double m_setpoint = 0.0;
	double m_setpoint_scale = 1.0;
	
	public Encoder encoder; 
	public PIDMotorDrive victor;
	public PIDController pid;
	

	 
	
	 
	 
	 	 
	
	
	public PIDMotorDrive(final int victor_channel, 
			int cha, int chb, PIDSourceType pidSource,
			boolean contituous, 
			double Kp, double Ki, double Kd, double Kf)
	{
		super(victor_channel);
		victor = this;
		
		encoder = new Encoder(cha,chb);
		encoder.setPIDSourceType(pidSource);
		
		m_contituous = contituous;
		pid = new PIDController( Kp, Ki, Kd, Kf, encoder, victor,0.02);
		pid.setContinuous( m_contituous);
		
	}
	
		
	public void PIDset(double setpoint)
	{
		// check, if pid setpoint need to be changed?
	
		  pid.setSetpoint(setpoint * m_setpoint_scale);
		    
	}

	
	
	public void reset()	{ pid.reset(); }
	public void enable() { pid.enable(); }
	public void disable(){ pid.disable(); }

	public void setOutputOne(double max)
	{
		m_setpoint_scale = 1/max;
	}

}
