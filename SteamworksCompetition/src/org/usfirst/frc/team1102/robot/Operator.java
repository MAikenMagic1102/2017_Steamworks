package org.usfirst.frc.team1102.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Operator 
{

	Joystick joyOperator1;
	
	Shooter shooter;
	Feeder feeder;
	Winch winch;
	 boolean shooteron=false;
	boolean m_running = false;
	boolean m_running_auton = false;
	boolean m_threadStarted = false;
	Robot m_r;
	double shoot_speed=0;
	double feeder_speed =0;
	double injester_speed =0;
	
	double normal(double x)
	{
		if (x>1)x=1;
		if(x<-1)x=-1;
		if((x<0.1)&&(x>-0.1))x=0;
		return x;
	}
	 public Operator(Robot r)
	 {
		 joyOperator1 = new Joystick(1);
		 
		 m_r=r;
		 
	 }
	
	Thread operate = new Thread(new Runnable()
	 {
		@Override
		public void run() 
		{
			
			 shooter = new Shooter();
			 feeder = new Feeder();
			 winch = new Winch();
			
			while (true)
			{
				while(m_running)
				{
						
				    	Timer.delay(0.01);
				    	

				    	
				    	//Shooter speed control (Toggle control NOT hold control)
				    	if (joyOperator1.getRawButton(5)){
				    		shooteron=true;// change
				    	}else if (joyOperator1.getRawButton(6)) shooteron=false;
				    	
				    	if (shooteron){
				    		shoot_speed=670;				    		
				    	}else shoot_speed=0;
				    	
				    	
				    	//Winch proportional control on Operator Y axis
				    	if ((joyOperator1.getRawAxis(1) < 0.2))
				    		winch.set(joyOperator1.getRawAxis(1));
				    	
				    	
				    	
				    	
				    	//Controls for feeder. When operator button is pressed and the shooter is within the speed threshhold the feeder will turn on.
				    	if ((joyOperator1.getRawButton(4)&&shooter.ready())||(joyOperator1.getRawButton(4)&&joyOperator1.getRawButton(7)))
				    	{
				    		feeder_speed=0.65;
				    	}else feeder_speed=0.0;;

				    	
				    	
				    	
				    	
				    	//Un-Jam Fuel Sequence
				    	if (joyOperator1.getRawButton(8)){
				    		feeder.set(-1);
				    		Timer.delay(0.2);
				    		feeder.set(1);
				    		Timer.delay(0.3);
				    		feeder.set(0);
				    		
				    	}
				    	
				    	
				    	shooter.set(shoot_speed);
				    	feeder.set(feeder_speed);
				    	

				
				    	
				}
				while (m_running_auton)
				{
					
					
					shooter.set(shoot_speed);
			    	feeder.set(feeder_speed);
					
					
				}
				
				
				
				
			    while (!(m_running || m_running_auton)){
			    	Timer.delay(0.01);
			    	shoot_speed=0;
			    	feeder_speed =0;
			    	injester_speed =0;
			    	
			    };
			}
		}
		
		
	 });
	 void start() 
	 {
		 
		 m_running = true;
		 if (!m_threadStarted) operate.start();
		 m_threadStarted = true;
		 
		 
		 
	 }
	 
	 
	 
	 void start_auton() 
	 {
		 
		 m_running_auton = true;
		 if (!m_threadStarted) operate.start();
		 m_threadStarted = true;
		 
		 
	 }
	 void stop_auton()
	 {
		 m_running_auton=false;
		 shooteron=false;
		 feeder.set(0);
	 }
	 
	public void pause()
	{
		
	}
	void stop()
	{
		m_running=false;
		shooteron=false;
	}


	






}
