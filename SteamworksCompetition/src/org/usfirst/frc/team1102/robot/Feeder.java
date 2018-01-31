package org.usfirst.frc.team1102.robot;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Victor;

public class Feeder implements Vars {
	
	//PIDMotor spokes;
	//Counter c;
	//double Kp,Ki,Kd;
	Victor spokes;
	public Feeder()
	{
		//c= new Counter(feederEncoder);
		//spokes= new PIDMotor(feederMotor,c,PIDSourceType.kRate,false,Kp,Ki,Kd,0);
		//spokes.reset();
		//spokes.enable();
		spokes = new Victor(feederMotor);
	}
	
	
	void set(double speed)
	{
		spokes.set(speed);
		//spokes.PIDset(speed);
	}
	
	
	
	
}
