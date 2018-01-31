                                                                                                               package org.usfirst.frc.team1102.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Driver implements Vars
{
	Joystick joyDriver;
	
	boolean running = true;
	boolean threadStarted = false;

	

	

	double normal(double x)
	{
		if (x>1)x=1;
		if(x<-1)x=-1;
		if((x<0.1)&&(x>-0.1))x=0;
		return x;
	}
	
	    Victor FR = new Victor(rightFrontDrive);
	    Victor FL = new Victor(leftFrontDrive);
	    Victor BR = new Victor(rightRearDrive);
	    Victor BL = new Victor(leftRearDrive);
	    double FR2=0;
	    double FL2=0;
	    double BR2=0;
	    double BL2=0;
	
	
	
	
	public void calc_normal_drive(double x, double x2, double y)
	 {
		 
		  FR2  =(y +x2 + x); 
		  BR2  =(y +x2 - x); 
		  FL2  =(y -x2 + x); 
		  BL2  =(y -x2 - x); 
		  
		    FR.set(FR2);
	    	FL.set(FL2);
	    	BR.set(BR2);
	    	BL.set(BL2);
			 
		 
	 }
	

	Thread tank = new Thread(new Runnable()
	 {
		@Override
		public void run() 
		{
			
			joyDriver = new Joystick(0);
			while (true)
			{
				while(running)
				{
					
					double x = joyDriver.getRawAxis(0);
					if (Math.abs(x)<0.1)x=0.0;
					
					double y = joyDriver.getRawAxis(1);
					if (Math.abs(y)<0.1)y=0.0;
					
					double x2 = joyDriver.getRawAxis(4);
					if (Math.abs(x2)<0.1)x2=0.0;
					
					calc_normal_drive(x,y,x2);
						
				    	
		       	}
				
			    while (!running){
			    	Timer.delay(0.01);
			    };
			}
		}
		
		
	 });
	 void start() 
	 {
		
		 running = true;
		 if (!threadStarted) tank.start();
		 threadStarted = true;
	 }
	
	 void stop()
	 {
		 running = false;
		 
		
	 }

	
	 
}
