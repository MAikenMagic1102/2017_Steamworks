                                                                                                               package org.usfirst.frc.team1102.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.AnalogOutput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonDriver implements Vars
{
	Joystick joyDriver;
	
	boolean running = true;
	boolean threadStarted = false;
	double Kp=0.00004;
	double Ki=0.0;
	double Kd=0.00004;

	
	double feeder=0;
	boolean reverse = false;
	NetworkTable table;
	
	
	
	
	
	//**********************************************************************//
    //*               The lookup table for joysticks.                      *//
    //*   Table is of integers.  Joystick values (0.0-1.0) are multiplied  *//
    //* by 127 and cast to be integers for referencing the table. Result of*//
    //* lookup is then cast to a double and then divided by 1000 to result *//
    //* in a return value that is in the 0.0-1.0 range for motor settings. *//
    //**********************************************************************//
    int axis_map[] = {
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
    //**********************************************************************//
    //*             Applies the lookup table for a given value.            *//
    //*   Operand is a double (0.0 to 1.0 or -0.0 to -1.0), returned value *//
    //*   is a remapped double in the range (0.0 to 1.0 or -0.0 to -1.0)   *//
    //**********************************************************************//
    double remapAxis(double value)
    {
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
	
	
	
	
	
	
	
	double v_max=13;
	double r=3;
	double omega=1750;
	double runtime(){
		return time_i-(System.nanoTime()/1000000000);
	}
	public boolean record=false;
	boolean driver = true;
	boolean pause = false;
	double time_i=System.nanoTime()/1000000000;
	public AutonDriver(){
		table = NetworkTable.getTable("cv");
	}
	ArrayList<Double> fl = new ArrayList<Double>();
	ArrayList<Double> fr = new ArrayList<Double>();
	ArrayList<Double> bl = new ArrayList<Double>();
	ArrayList<Double> br = new ArrayList<Double>();
	double normal(double x)
	{
		if (x>1)x=1;
		if(x<-1)x=-1;
		if((x<0.1)&&(x>-0.1))x=0;
		return x;
	}
	
	    PIDMotorDrive FR = new PIDMotorDrive(rightFrontDrive,rightFrontDriveEncA,rightFrontDriveEncB,PIDSourceType.kDisplacement,false,Kp,Ki,Kd,0);
	    PIDMotorDrive FL = new PIDMotorDrive(leftFrontDrive,leftFrontDriveEncA,leftFrontDriveEncB,PIDSourceType.kDisplacement,false,Kp,Ki,Kd,0);
	    PIDMotorDrive BR = new PIDMotorDrive(rightRearDrive,rightRearDriveEncA,rightRearDriveEncB,PIDSourceType.kDisplacement,false,Kp,Ki,Kd,0);
	    PIDMotorDrive BL = new PIDMotorDrive(leftRearDrive,leftRearDriveEncA,leftRearDriveEncB,PIDSourceType.kDisplacement,false,Kp,Ki,Kd,0);
	    double FR2=0;
	    double FL2=0;
	    double BR2=0;
	    double BL2=0;
		AnalogOutput pid_debug_in = new AnalogOutput(0);
		AnalogOutput pid_debug_out = new AnalogOutput(1);
	
	
	
	public void calc_normal_drive(double x, double x2, double y)
	 {
		 
		  FR2  =(y +x2 + x)*omega; 
		  BR2  =(y +x2 - x)*omega; 
		  FL2  =(y -x2 + x)*omega; 
		  BL2  =(y -x2 - x)*omega; 
		  
		    FR.PIDset(FR2);
	    	FL.PIDset(FL2);
	    	BR.PIDset(BR2);
	    	BL.PIDset(BL2);
			 
		 
	 }
	

	Thread tank = new Thread(new Runnable()
	 {
		@Override
		public void run() 
		{
			table.putNumber("Camera Mode", 0);
			joyDriver = new Joystick(0);
	
			FL.pid.setPID(Kp, Ki, Kd);
			FR.pid.setPID(Kp, Ki, Kd);
			BL.pid.setPID(Kp, Ki, Kd);
			BR.pid.setPID(Kp, Ki, Kd);
			if (driver){
				
				FL.encoder.setPIDSourceType(PIDSourceType.kRate);
				FL.enable();
				
				FR.encoder.setPIDSourceType(PIDSourceType.kRate);
				FR.enable();
				
				BL.encoder.setPIDSourceType(PIDSourceType.kRate);
				BL.enable();
				
				BR.encoder.setPIDSourceType(PIDSourceType.kRate);
				BR.enable();
			}else{
				Kp=1;
				Ki=0.0;
				Kd=0.0;
				FL.pid.setPID(Kp, Ki, Kd);
				FR.pid.setPID(Kp, Ki, Kd);
				BL.pid.setPID(Kp, Ki, Kd);
				BR.pid.setPID(Kp, Ki, Kd);
				FL.encoder.setDistancePerPulse(1);
				FR.encoder.setDistancePerPulse(1);
				BR.encoder.setDistancePerPulse(1);
				BL.encoder.setDistancePerPulse(1);
				FL.enable();
				FR.enable();
				BR.enable();
				BL.enable();

			}
			int c =0; 
			while (true)
			{
				while(running)
				{
					
					
					
					
					
					if(joyDriver.getPOV()==0)
						table.putNumber("Camera Mode", 0);
					if(joyDriver.getPOV()==90)
						table.putNumber("Camera Mode", 1);

					if(joyDriver.getPOV()==180)
						table.putNumber("Camera Mode", 2);
					

					if(joyDriver.getPOV()==270)
						table.putNumber("Camera Mode", 3);
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					if(driver)
					//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
					{
						paused();
						
						
						double x = remapAxis(joyDriver.getRawAxis(0));
						if (Math.abs(x)<0.05)x=0.0;
						else x=x-0.05;
						double y = remapAxis(joyDriver.getRawAxis(1));
						if (Math.abs(y)<0.05)y=0.0;
						else y=y-0.05;
						
						double x2 = remapAxis(joyDriver.getRawAxis(4));
						if (Math.abs(x2)<0.05)x2=0.0;
						else x2=x2-0.05;
						
						
						if(!reverse&&(y<0))feeder=1;
						else if(reverse&&(y>0))feeder=1;
						else feeder=0;
						int maxspeed=1;
						if (joyDriver.getRawButton(1))reverse=!reverse;
						if (reverse){
							calc_normal_drive(-maxspeed*x,-maxspeed*y,maxspeed*x2);
							
						}
						else calc_normal_drive(maxspeed*x,maxspeed*y,maxspeed*x2);
						double [] coords = new double [4];
						coords[0]=FL.encoder.getDistance();
						coords[1]=FR.encoder.getDistance();
						coords[2]=BL.encoder.getDistance();
						coords[3]=BR.encoder.getDistance();
						
						table.putNumberArray("Coords", coords);
						table.putNumber("time_key", runtime());
						
						
						
						
						
						
						System.out.println(FL.encoder.getRate());
						System.out.println(FR.encoder.getRate());
						System.out.println(BL.encoder.getRate());
						System.out.println(BR.encoder.getRate());
						System.out.println("///////////////////////");
						//System.out.println(BR.pid.get());
						pid_debug_in.setVoltage((5.0*(double)BR.encoder.getRate())/3000);
						pid_debug_out.setVoltage(5*BR.pid.get());
						
						Timer.delay(0.01);
					
					
					
					
					}
					/////////////////////////////////////////////
					else
					{
					if(record){
						double x = joyDriver.getRawAxis(0);
						if (Math.abs(x)<0.05)x=0.0;
						else x=x-0.05;
						double y = joyDriver.getRawAxis(1);
						if (Math.abs(y)<0.05)y=0.0;
						else y=y-0.05;
						
						double x2 = joyDriver.getRawAxis(4);
						if (Math.abs(x2)<0.05)x2=0.0;
						else x2=x2-0.05;
						
						
						
						if(!reverse&&(y<0))feeder=1;
						else if(reverse&&(y>0))feeder=1;
						else feeder=0;
						int maxspeed=1;
						if (joyDriver.getRawButton(1))reverse=!reverse;
						if (reverse){
							calc_normal_drive(-maxspeed*x,-maxspeed*y,maxspeed*x2);
							
						}
						else calc_normal_drive(maxspeed*x,maxspeed*y,maxspeed*x2);
						double [] coords = new double [4];
						coords[0]=FL.encoder.getDistance();
						coords[1]=FR.encoder.getDistance();
						coords[2]=BL.encoder.getDistance();
						coords[3]=BR.encoder.getDistance();
						
						table.putNumberArray("Coords", coords);
						table.putNumber("time_key", runtime());
						
						
						
						
						
						
						/*System.out.println(FL.encoder.getDistance());
						System.out.println(FR.encoder.getDistance());
						System.out.println(BL.encoder.getDistance());
						System.out.println(BR.encoder.getDistance());*/
						//System.out.println(BR.pid.get());
						pid_debug_in.setVoltage((5.0*(double)BR.encoder.getRate())/3000);
						pid_debug_out.setVoltage(5*BR.pid.get());
						
						Timer.delay(0.01);
						
						
						
					}
					else{
						
						FL.PIDset(fl.get(c));
						FR.PIDset(fr.get(c));
						BL.PIDset(bl.get(c));
						BR.PIDset(br.get(c));
					
						System.out.println(FL.encoder.getDistance());
						System.out.println(FR.encoder.getDistance());
						System.out.println(BL.encoder.getDistance());
						System.out.println(BR.encoder.getDistance());

						System.out.println(c+"  /////////////////////////////////");
						System.out.println(br.size());
						Timer.delay(0.1);
						c++;
						if((c>=br.size())){
							c=0;
							record=true;
							paused();
							FL.disable();
							FR.disable();
							BR.disable();
							BL.disable();

							
						}
					}
						

					}
					
					
					
					
					
					
					
					
					
					
					
					
					
					if (joyDriver.getRawButton(5)&&(fl.size()>0)){
						
						record=false;
						driver=false;
						Kp=0.01;
						Ki=0.0;
						Kd=0.0;
						FL.pid.setPID(Kp, Ki, Kd);
						FR.pid.setPID(Kp, Ki, Kd);
						BL.pid.setPID(Kp, Ki, Kd);
						BR.pid.setPID(Kp, Ki, Kd);
						FL.encoder.setDistancePerPulse(1);
						FR.encoder.setDistancePerPulse(1);
						BR.encoder.setDistancePerPulse(1);
						BL.encoder.setDistancePerPulse(1);
						FL.encoder.reset();
						FR.encoder.reset();
						BR.encoder.reset();
						BL.encoder.reset();
						FL.encoder.setPIDSourceType(PIDSourceType.kDisplacement);
						
						
						FR.encoder.setPIDSourceType(PIDSourceType.kDisplacement);
						
						
						BL.encoder.setPIDSourceType(PIDSourceType.kDisplacement);
						
						
						BR.encoder.setPIDSourceType(PIDSourceType.kDisplacement);
						
						FL.enable();
						FR.enable();
						BR.enable();
						BL.enable();

						
						
						
					}
					if (joyDriver.getRawButton(4)){
						record=true;
						driver=true;
						
						Kp=0.00004;
						Ki=0.0;
						Kd=0.00004;
						FL.pid.setPID(Kp, Ki, Kd);
						FR.pid.setPID(Kp, Ki, Kd);
						BL.pid.setPID(Kp, Ki, Kd);
						BR.pid.setPID(Kp, Ki, Kd);
						FL.encoder.reset();
						FR.encoder.reset();
						BR.encoder.reset();
						BL.encoder.reset();
						
						FL.encoder.setPIDSourceType(PIDSourceType.kRate);
						
						
						FR.encoder.setPIDSourceType(PIDSourceType.kRate);
						
						
						BL.encoder.setPIDSourceType(PIDSourceType.kRate);
						
						
						BR.encoder.setPIDSourceType(PIDSourceType.kRate);
						
						FL.enable();
						FR.enable();
						BR.enable();
						BL.enable();

						
						
					}
					if (joyDriver.getRawButton(6)){
						record=true;
						driver=false;
						n_paused();
						fl.clear();
						fr.clear();
						bl.clear();
						br.clear();
						Kp=0.000035;
						Ki=0.0;
						Kd=0.0001;
						FL.pid.setPID(Kp, Ki, Kd);
						FR.pid.setPID(Kp, Ki, Kd);
						BL.pid.setPID(Kp, Ki, Kd);
						BR.pid.setPID(Kp, Ki, Kd);
						FL.encoder.reset();
						FR.encoder.reset();
						BR.encoder.reset();
						BL.encoder.reset();
						
						FL.encoder.setPIDSourceType(PIDSourceType.kRate);
						
						
						FR.encoder.setPIDSourceType(PIDSourceType.kRate);
						
						
						BL.encoder.setPIDSourceType(PIDSourceType.kRate);
						
						
						BR.encoder.setPIDSourceType(PIDSourceType.kRate);
						/*FL.disable();
						FR.disable();
						BR.disable();
						BL.disable();
						*/FL.enable();
						FR.enable();
						BR.enable();
						BL.enable();

						
					}
					System.out.println("-------------------"+FR2+","+FL2+","+BR2+","+BL2);
					System.out.println(""+FR.victor.get()+","+FL.victor.get()+","+BR.victor.get()+","+BL.victor.get());
					System.out.println("+++++"+BL.pid.getAvgError());
					System.out.println(""+BL.encoder.getPIDSourceType());
					System.out.println("////////////////////////");
		       	}

			    while (!running){
			    	Timer.delay(0.01);
					FL.encoder.reset();
					FR.encoder.reset();
					BR.encoder.reset();
					BL.encoder.reset();
			    };
			}
		}
		
		
	 });
	
	Thread recorder = new Thread(new Runnable()
	 {
		@Override
		public void run() 
		{
			
			
			while (true)
			{
				while(!pause)
				{
					
					Timer.delay(0.1);
					if(record)
						{
							fl.add(FL.encoder.getDistance());
							fr.add(FR.encoder.getDistance());
							bl.add(BL.encoder.getDistance());
							br.add(BR.encoder.getDistance());
							System.out.println("////////////////////////////////////////");
							System.out.println("FL.PIDset("+FL.encoder.getDistance()+");");
							System.out.println("FR.PIDset("+FR.encoder.getDistance()+");");
							System.out.println("BL.PIDset("+BL.encoder.getDistance()+");");
							System.out.println("BR.PIDset("+BR.encoder.getDistance()+");");
							System.out.println("Timer.delay(0.1)");
						}
				
					
		       	}
				
			    while (pause){
			    	Timer.delay(0.1);
					
			    };
			}
		}
		
		
	 });
	 void start() 
	 {
		
		 running = true;
		 if (!threadStarted) {
			 tank.start();
			 recorder.start();
		 }
		 threadStarted = true;
	 }
	
	 void stop()
	 {
		 running = false;
		 
		
	 }
	 void paused(){
		 pause=true;
	 }
	 void n_paused()
	 {
		 pause=false;
	 }
	 
	

	
	 
}
