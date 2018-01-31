package org.usfirst.frc.team1102.robot;

import edu.wpi.first.wpilibj.AnalogOutput;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Victor;

public class Shooter implements Vars{

	
	PIDMotor wheels;


	double Kp=0.00025;
	double Ki=0.0;
	double Kd=0.0004;
	boolean on_speed=false;
	final int max_err=80;
	double speed=0.0;
	//Victor wheels;
	//TODO debug shooter
	//
	int rate = 0;
	
	public Shooter()
	{

		wheels= new PIDMotor(shooterMotor,shooterEncoder,PIDSourceType.kRate,false,Kp,Ki,Kd,0);
		wheels.pid.setOutputRange(0, 1);
		wheels.reset();
		wheels.enable();
		//wheels = new Victor(shooterMotor);
	}
	
	
	void set(double s)
	{
		//wheels.set(speed);
		speed=s;
		
		rate=(int) wheels.encoder.pidGet();

		wheels.PIDset(speed);
		//pid_debug_in.setVoltage(5.0*(double)rate/2000.0);
		//pid_debug_out.setVoltage(5*(wheels.pid.get()));

		
	}
	boolean ready()
	{
		if((Math.abs(wheels.pid.getAvgError())<max_err)&&(speed!=0))return true;
		else return false;
	}
	
	
	
}
