
package org.usfirst.frc.team1102.robot;




import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot implements Vars {


//AutonDriver Mechanum = new AutonDriver();  
AdvancedDrive Mechanum = new AdvancedDrive(this);
Operator operator = new Operator(this);
Victor release = new Victor(releaseMotor);
boolean released = false;
double front=0.0;
double shooter =0;
double injester=0;
double feeder =0;
//VictorSP belts =new VictorSP(injesterMotor);
    
  
    
    public Robot() 
    {
		   
    }
    
    public void robotInit() 
    {
    	
    }

	public void autonomous() 
	{
		release.set(0.2);
		Timer.delay(0.2);
		release.set(0.2);
		released=false;
		
		
		operator.start_auton();
		Mechanum.start_auton();
		while(isAutonomous()&&isEnabled())
		{
			operator.feeder_speed=Mechanum.feeder;
			operator.shoot_speed=Mechanum.shooter;
			operator.injester_speed=Mechanum.injester;	
			Timer.delay(0.01);
		}
		operator.start_auton();
		Mechanum.stop_auton();
		
		
		
    }

   
    public void operatorControl() 
    {
    	
    	
  
    	Timer.delay(1);
  		Mechanum.start();
  		operator.start();
  		//Joystick joyOperator1 = new Joystick (1);
  		while (isOperatorControl() && isEnabled()) 
		{
  			//if (joyOperator1.getRawButton(2))belts.set(0.85);
  			//else belts.set(0.0);
			Timer.delay(0.01);
			front=Mechanum.inj;
			feeder=operator.feeder_speed;
			shooter=operator.shoot_speed;
			injester=operator.injester_speed;
		
			
	    	
		}
		Mechanum.stop();
		operator.stop();	
			
			
    }

    
    public void test()
    {
    	
    }

   }