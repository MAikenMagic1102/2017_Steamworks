package org.usfirst.frc.team1102.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;

public class Injester implements Vars {

	//N0 TOUCHY TOUCHY
	
	//TOUCH TOUCHY
	//>>>>
	
	VictorSP belts;
	public Injester()
	{
		belts =new VictorSP(injesterMotor);
	}
	
	void set(double power)
	{
		belts.set(power);
	}
	
	
	
	
	
	

}
