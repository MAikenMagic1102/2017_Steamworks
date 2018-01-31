package org.usfirst.frc.team1102.robot;

import java.io.IOException;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.AnalogOutput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AdvancedDrive implements Vars{
	
	//Variable initialization
	Robot m_r;
	boolean running = false;
	boolean threadStarted = false;
	boolean m_running_auton = false;
	double shooter =0;
	double injester=0;
	double feeder =0;
	int inj = 0;
	final int driveMode=0;
	final int recordMode=1;
	final int replayMode=2;
	final int autonomusMode=3;
	final int noneMode = -1;
	DigitalInput side= new DigitalInput(16);//pos1,pos2,pos3;
	DigitalInput pos1= new DigitalInput(17);
	DigitalInput pos2= new DigitalInput(18);
	DigitalInput pos3= new DigitalInput(19);
	Injester injest= new Injester();
	String mod= ""+b2int(side.get())+""+b2int(pos1.get())+""+b2int(pos2.get())+""+b2int(pos3.get())	;
	int currentMode=noneMode;
	int b2int(boolean b)
	{
		if (b)return 1;
		return 0;
	}
	
	/////////////////////////////
	double Kp_s=0.00004;
	double Ki_s=0.0;
	double Kd_s=0.00004;
	//PID Constants
	double Kp_d=0.01;
	double Ki_d=0.0;
	double Kd_d=0.0;
	/////////////////////////////
	
	PIDMotorDrive FR = new PIDMotorDrive(rightFrontDrive,rightFrontDriveEncA,rightFrontDriveEncB,PIDSourceType.kRate,false,Kp_s,Ki_s,Kd_s,0);
    PIDMotorDrive FL = new PIDMotorDrive(leftFrontDrive,leftFrontDriveEncA,leftFrontDriveEncB,PIDSourceType.kRate,false,Kp_s,Ki_s,Kd_s,0);
    PIDMotorDrive BR = new PIDMotorDrive(rightRearDrive,rightRearDriveEncA,rightRearDriveEncB,PIDSourceType.kRate,false,Kp_s,Ki_s,Kd_s,0);
    PIDMotorDrive BL = new PIDMotorDrive(leftRearDrive,leftRearDriveEncA,leftRearDriveEncB,PIDSourceType.kRate,false,Kp_s,Ki_s,Kd_s,0);
    //motors declaration
    double FR2=0;
    double FL2=0;
    double BR2=0;
    double BL2=0;
    //Motor power variables
    double max_vel=1750;
    
    //Reverse mode boolean
    boolean reverse = false;
    //////////////////////////////
    
    Joystick joyDriver = new Joystick(0);
    //Joystic declaration
    //////////////////////////////
    
	WayPointList wpl= new WayPointList();
	WayPointList wpl1 = new WayPointList();
	String path = "/home/lvuser/auton."+mod;
	boolean recorded = false;
	boolean paused_recording = true;
	//File recording set up
	 void pause(){
		 paused_recording=true;
	 }
	//Start and stop recording methods
	 void unpause()
	 {
		 paused_recording=false;
	 }
    ////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//CONSTRUCTOR
	AdvancedDrive(Robot r)
	{
		m_r=r;
	}
	 
	 
	 
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////
	 void start() 
	 {
		 running = true;
		 if (!threadStarted) {
			 drive.start();
			 recorder.start();
		 }
		 threadStarted = true;
	 }
	//Start and stop main thread
	 void stop()
	 {
		 running = false;
	 }
	 /////////////////////////
	 
	 void start_auton() 
	 {
		 
		 m_running_auton = true;
		 if (!threadStarted) {
			 drive.start();
			 recorder.start();
		 }
		 threadStarted = true;
		 try{
				wpl1.readFile(path);
				wpl1.print();
				wpl=wpl1;
			}catch(IOException e)
			{
				
			}
		 replay_mode_counter=0;
		 recorded=true;
		 
		 
	 }
	 void stop_auton()
	 {
		 m_running_auton=false;
		
	 }
	 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 //DRIVE FUNCTIONS
	 
	
	 //Replay mode counter
	 int replay_mode_counter=0;
	 
	 //Remap Axis Method (Don't touch)
	 double remapAxis(double value)
	    {
		 	final int axis_map[] = {
		 	         2,    4,    6,    8,   10,   12,   14,   16,   18,   20,
		 	        22,   25,   27,   29,   32,   34,   37,   39,   42,   44,
		 	        47,   50,   52,   55,   58,   61,   64,   67,   70,   73,
		 	        77,   80,   83,   87,   90,   94,   97,  101,  105,  109,
		 	       113,  117,  121,  125,  129,  134,  138,  143,  147,  152,
		 	       157,  162,  167,  172,  177,  183,  188,  194,  200,  205,
		 	       212,  218,  224,  231,  237,  244,  251,  258,  265,  273,
		 	       280,  288,  296,  304,  313,  321,  330,  339,  348,  358,
		 	       368,  378,  388,  398,  409,  420,  432,  443,  455,  468,
		 	       480,  493,  507,  520,  534,  549,  564,  579,  595,  611,
		 	       628,  645,  662,  680,  699,  718,  738,  759,  779,  801,
		 	       823,  846,  870,  894,  920,  946,  972,  1000,  1000};
		 	
		 	
	        int t=1; //* Mapping function is symmetric. only store pos half  *//
	        int Scaled= (int) (value * 127.0); //Get whole number 0-127
	        if (Scaled < 0) {   //* Check here if the input value is negative*//
	           Scaled *= -1;    //* take the absolute value                  *//
	           t=-1;            //* remember to invert the result            *//
	           Scaled += 1;     //* correction to allow 100% power           *//
	        }else{
	           Scaled += 1;     //* correction to allow 100% power since     *//
	        }
	        if (Scaled < 11) {return 0.0; } //* deadband removal             *//
	        Scaled -= 11;         //* deadband values not included in table  *//
	        t *= axis_map[Scaled]; //* lookup value and correct for negative *//
	        return ((double)t)/1000.0; //* Convert to 0-1 value              *//
	    } 
	 
	 
	 
	 //Set wheels PID function
	 void setPID(double fl,double fr, double bl, double br)
	 {
		 FR.PIDset(fr);
		 FL.PIDset(fl);
		 BR.PIDset(br);
		 BL.PIDset(bl);		 
	 }
	 //TODO separate wrapper for joystick
	 //Set PID Constants for wheels
	 void setPIDConstants(double p,double i, double d)
	 {
		FL.pid.setPID(p, i, d);
		FR.pid.setPID(p, i, d);
		BL.pid.setPID(p, i, d);
		BR.pid.setPID(p, i, d);	 
	 }
	 //Reset encoders
	 void encoderReset(){
		 FL.encoder.reset();
		 FR.encoder.reset();
		 BR.encoder.reset();
		 BL.encoder.reset();
		 
	 }
	 //Switch pid modes
	 void setPIDMode(PIDSourceType st)
	 {
	 	FL.encoder.setPIDSourceType(st);
		FR.encoder.setPIDSourceType(st);
		BL.encoder.setPIDSourceType(st);
		BR.encoder.setPIDSourceType(st); 
	 }
	 //Enable Wheels PID
	 void PIDenable()
	 {
	 	FL.enable();
		FR.enable();
		BR.enable();
		BL.enable();
	 }
	 //Disable Wheels PID
	 void PIDdisable()
	 {
	 	FL.disable();
		FR.disable();
		BR.disable();
		BL.disable();
	 }
	 //Distance per pulse setter
	 void setDistancePerPulse(double d)
	 {
		FL.encoder.setDistancePerPulse(d);
		FR.encoder.setDistancePerPulse(d);
		BR.encoder.setDistancePerPulse(d);
		BL.encoder.setDistancePerPulse(d);
	 }
	 /** 
	  * Switching modes of the robot
	  * there are 3 modes: driving, recording and replay.
	  * Shown by **mode** variable being 0,1 or 2 respectively
	  */
	 void switch_modes(int mode)
	 {
		 if (mode==currentMode)return;
		 switch (mode)
		 {
		 	case 0: drive_mode_start();
		 	break;
			 
	 		case 1: record_mode_start();
	 		break;
			 
	 		case 2: replay_mode_start();
	 		break;
		 }
		 currentMode=mode;
		 		 
	 }
	 
	 //Switch to drive mode
	 void drive_mode_start()
	 {
		pause();
	 	encoderReset();
	 	setPIDMode(PIDSourceType.kRate);
		setPIDConstants(Kp_s, Ki_s, Kd_s);
		PIDenable();
		 System.out.println("==================Drive Mode");
	 }
	 
	 //Switch to record mode
	 void record_mode_start()
	 {
		drive_mode_start();
	 	unpause();
 		wpl.clear();
 		recorded=false;
 		System.out.println("-------------------Record Mode");
	 }
	 
	 //Switch to replay mode
	 void replay_mode_start()
	 {
		 pause();
		 if (!recorded){
				try{
					wpl.writeFile(path);
				}catch(IOException e)
				{
					
				}
				recorded=true;
			} 
		 try{
				wpl1.readFile(path);
				wpl1.print();
			}catch(IOException e)
			{
				
			}
 		 encoderReset();
	 	 setPIDMode(PIDSourceType.kDisplacement);
		 setPIDConstants(Kp_d, Ki_d, Kd_d);
		 setDistancePerPulse(1);
		 PIDenable();
		 replay_mode_counter=0;
		 System.out.println("++++++++++++++++++Replay Mode");
		 
	 }
	
	 
	 //Method called every iteration for replay
	 void replay_update()
	 {
		 //System.out.println(""+wpl.size()+"  " +replay_mode_counter);
		 if(replay_mode_counter<wpl1.size()){
			 WayPoint wp = wpl1.get(replay_mode_counter);
			 setPID(wp.x1,wp.x2, wp.x3, wp.x4);
			 
			 shooter =wp.shooter;
			 feeder =wp.feeder;
			 injester=wp.injester;
			 //System.out.println("Replay counter: "+ replay_mode_counter);
			 wp.print();
			 System.out.println(""+wpl1.size()+"  " +replay_mode_counter);
			 replay_mode_counter++;
		 }else{
			 if (running)switch_modes(driveMode);
			
			 return;
		 }
		 
	 }
	 
	 //Drive update function
	 void drive_update()
	 {
			if (joyDriver.getRawButton(7))reverse=true;
			if (joyDriver.getRawButton(8))reverse=false;
			
		 	double x = joyDriver.getRawAxis(0);
			double y = joyDriver.getRawAxis(1);
			double x2 = joyDriver.getRawAxis(4);
			
		 	if (joyDriver.getRawButton(5))
		 	{
		 		injester=0.8;
		 	}
		 	else if(joyDriver.getRawButton(6))
		 	{
		 		injester=-0.8;
		 	}
		 	else injester=0.0;
		 	injest.set(injester);
			if (reverse)calc_drive(-remapAxis(x),-remapAxis(y),remapAxis(x2));
			else calc_drive(remapAxis(x),remapAxis(y),remapAxis(x2));
			//wpl.print();
			//System.out.println("+++++"+BL.pid.getAvgError());
			//System.out.println(""+BL.encoder.getPIDSourceType());
			//System.out.println("////////////////////////");
	 }
	 
	 
	// Calculating drive math and setting top speed;
	 
	 public void calc_drive(double x, double x2, double y)
		 {
			  FR2  =(y +x2 + x)*max_vel; 
			  BR2  =(y +x2 - x)*max_vel; 
			  FL2  =(y -x2 + x)*max_vel; 
			  BL2  =(y -x2 - x)*max_vel;
			  setPID(FL2,FR2,BL2,BR2);
			  //System.out.println("-------------------"+FR2+","+FL2+","+BR2+","+BL2);
		 } 
	 

	 
	 //THE MAIN THREAD
	 /** ****************************************************************************************************************************************************************** */
		
	 Thread drive = new Thread(new Runnable()
	 {
		@Override
		public void run() 
		{
			
			
			while (true)
			{
				System.out.println("Main Loop "+running+", "+m_running_auton);
				path = "/home/lvuser/auton."+""+b2int(side.get())+""+b2int(pos1.get())+""+b2int(pos2.get())+""+b2int(pos3.get());
				if (running)
				{
					currentMode=noneMode;
					encoderReset();
					switch_modes(driveMode);
					while(running)
					{
						path = "/home/lvuser/auton."+""+b2int(side.get())+""+b2int(pos1.get())+""+b2int(pos2.get())+""+b2int(pos3.get());
						System.out.println("Running Loop "+running+", "+m_running_auton);
						if(joyDriver.getRawButton(4))switch_modes(driveMode);
						else if(joyDriver.getRawButton(2))switch_modes(recordMode);
						else if(joyDriver.getRawButton(1))switch_modes(replayMode);
						if (currentMode!=replayMode)
						{
							drive_update();
							Timer.delay(0.01);
							System.out.println("Driving");
							String mod= ""+b2int(side.get())+"  "+b2int(pos1.get())+"  "+b2int(pos2.get())+"  "+b2int(pos3.get())	;
							System.out.println(mod);				
						}
						else
						{
							path = "/home/lvuser/auton."+""+b2int(side.get())+""+b2int(pos1.get())+""+b2int(pos2.get())+""+b2int(pos3.get());
							replay_update();
							Timer.delay(0.1);
							System.out.println("Replaying");
						}
						
			       	}
					PIDdisable();
				}
				System.out.println("Main Loop 1 "+running+", "+m_running_auton);
				if (m_running_auton)
				{
					while (m_running_auton)
					{
						path = "/home/lvuser/auton."+""+b2int(side.get())+""+b2int(pos1.get())+""+b2int(pos2.get())+""+b2int(pos3.get());
						pause();
						
						replay_update();
						Timer.delay(0.1);
						System.out.println("Autonomus");
						
						
						
						
					}
					
					
				}
			    while ((!running)&&(!m_running_auton)){
			    	Timer.delay(0.1);
			    	path = "/home/lvuser/auton."+""+b2int(side.get())+""+b2int(pos1.get())+""+b2int(pos2.get())+""+b2int(pos3.get());
			    }
			}
		}
		
		
	 });
	 /** ****************************************************************************************************************************************************************** */
	
	
	
	
	 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	 //Recording loop
	Thread recorder = new Thread(new Runnable()
	 {
		@Override
		public void run() 
		{
			
			
			int counter = 0;
			while (true)
			{
				while((!paused_recording)&&(counter<32000)&&running)
				{
					
					Timer.delay(0.1);
					double x1=FL.encoder.getDistance();
					double x2=FR.encoder.getDistance();
					double x3=BL.encoder.getDistance();
					double x4=BR.encoder.getDistance();
					System.out.println("Record counter: "+counter);
					wpl.add(x1,x2,x3,x4,m_r.shooter,m_r.feeder,m_r.injester);	
					//Reads the Shooter/Feeder/Injestor Global Variables as they are set in the Robot Class Which are set in the Operator class.
					wpl.list.get(counter).print();
					counter++;
					
		       	}
				
			    while (paused_recording||!running){
			    	Timer.delay(0.1);
			    	counter = 0;
			    }
			    Timer.delay(0.1);
			}
		}
		
		
	 });
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	


    
    
    
}
