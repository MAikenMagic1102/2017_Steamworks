package org.usfirst.frc.team1102.robot;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Victor;

public class Winch implements Vars {
	
	//PIDMotor spokes;
	//Counter c;
	//double Kp,Ki,Kd;
	Victor hook;
	public Winch()
	{
		//c= new Counter(feederEncoder);
		//spokes= new PIDMotor(feederMotor,c,PIDSourceType.kRate,false,Kp,Ki,Kd,0);
		//spokes.reset();
		//spokes.enable();
		hook = new Victor(winchMotor);
	}
	
	
	void set(double speed)
	{
		hook.set(speed);
		//spokes.PIDset(speed);
	}
	
	
	
	
}
