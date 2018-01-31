/*
*
*  WayPoint 
*  Represents One way point for recording and replay robot tracks
*
*/
package org.usfirst.frc.team1102.robot;
import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.FilterOutputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class WayPoint {
	double x1;
	double x2;
	double x3;
	double x4;
	double shooter;
	double feeder;
	double injester;
	
	// sample constructor
	public WayPoint(){
	}
	
	// constructor with data setter
	public WayPoint(double lx1, double lx2,double lx3,double lx4, double lshooter, double lfeeder, double linjester){
		x1=lx1;
		x2=lx2;
		x3=lx3;
		x4=lx4;
		shooter = lshooter;
		feeder = lfeeder;
		injester = linjester;
	}
	
	// print One point data to console
	public void print(){
		String str ="wayPointList.add("+ x1 + "," +x2+ "," +x3+ "," +x4+ "," +shooter+ "," +feeder + "," + injester+");";
		System.out.println(str);
	}
	
	// write wayPoint to the output stream
    public void writeDataStream(DataOutputStream dos) throws IOException {
		try{
			dos.writeDouble(x1);
			dos.writeDouble(x2);
			dos.writeDouble(x3);
			dos.writeDouble(x4);
			dos.writeDouble(shooter);
			dos.writeDouble(feeder);
			dos.writeDouble(injester);
			} catch(Exception e){
			 // if any error occurs
			 e.printStackTrace();
			} finally{
			}
	}
	
	// read and fill WayPoint from the stream
	 public WayPoint ReadDataStream(DataInputStream dis) throws IOException {
		try{
			x1 = dis.readDouble();
			x2 = dis.readDouble();
			x3 = dis.readDouble();
			x4 = dis.readDouble();
			shooter = dis.readDouble();
			feeder = dis.readDouble();
			injester = dis.readDouble();
			
			} catch(Exception e){
			 // if any error occurs
			 e.printStackTrace();
			} finally{
			}
		return this;
	}
	
}