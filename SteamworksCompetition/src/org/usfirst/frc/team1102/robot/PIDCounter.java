package org.usfirst.frc.team1102.robot;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Timer;




public class PIDCounter extends Counter {
	
	
	public PIDCounter(int channel){
		super(channel);
		
		
		
	}
	@Override
	public double getRate()
	{
		return 1/getPeriod();
		
	}

	
	

}
