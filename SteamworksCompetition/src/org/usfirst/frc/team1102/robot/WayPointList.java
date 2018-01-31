
package org.usfirst.frc.team1102.robot;/*
*
*  Waypoints list 
*  Represents list of waypoints to record and replay robot track 
*
--- Constructor
public void WayPointList()

--- Manually add waypoint to the list
public void add(long x1, long x2,long x3,long x4,	long shooter, long feeder, long injester)

--- get waypoint by index
public  WayPoint get(int i)

-- get list size
public int size()

-- print list to console
public void print()

--- Store WayPointList to file
--- Roborio must give to store to directory "/home/lvuser/"
public void writeFile(String filename) throws IOException 

-- read WayPointList file from disk
public void writeFile(String filename) throws IOException 
	
*/

/*
*
*  Waypoints list 
*  Represents list of waypoints to record and replay robot track 
*
--- Constructor
public void WayPointList()

--- Manually add waypoint to the list
public void add(long x1, long x2,long x3,long x4,	long shooter, long feeder, long injester)

--- get waypoint by index
public  WayPoint get(int i)

-- get list size
public int size()

-- print list to console
public void print()

--- Store WayPointList to file
--- Roborio must give to store to directory "/home/lvuser/"
public void writeFile(String filename) throws IOException 

-- read WayPointList file from disk
public void writeFile(String filename) throws IOException 
	
*/

/*
*
*  Waypoints list 
*  Represents list of waypoints to record and replay robot track 
*
--- Constructor
public void WayPointList()

--- Manually add waypoint to the list
public void add(double x1, double x2,double x3,double x4,	double shooter, double feeder,double injester)

--- get waypoint by index
public  WayPoint get(int i)

-- get list size
public int size()

-- print list to console
public void print()

--- Store WayPointList to file
--- Roborio must give to store to directory "/home/lvuser/"
public void writeFile(String filename) throws IOException 

-- read WayPointList file from disk
public void writeFile(String filename) throws IOException 
	
*/

import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FilterOutputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class WayPointList {
	
	ArrayList<WayPoint> list = new ArrayList<WayPoint>();
	
	// empty constructor
	public void WayPointList(){
    }

	// clear list
	public void clear() {
		list.clear();
	}
	
	// add waypoint
	public void add(double x1, double x2,double x3,double x4,	double shooter, double feeder, double injester){
			WayPoint wp =  new WayPoint(x1,x2,x3,x4,shooter,feeder,injester);
			list.add(wp);
	}
	
	public void addWP(WayPoint wp){
		list.add(wp);
		wp.print();
    }
	
	// get waypoint by index
	public  WayPoint get(int i){
			return list.get(i);
	}
	
	// get list size
	public int size(){
		return list.size();
	}
	
	// Print list to system console
    public void print(){
		long len = list.size();
		int i;
		WayPoint wp;
		
		for (i=0;i<len;i++){
		  wp = list.get(i);
		  wp.print();
		}		
	}
	
	

/* 
*	  Write read full list to/from stream	
*/ 
		
    public void writeDataStream(DataOutputStream dos) throws IOException {
		long len = list.size();
		int i;
		WayPoint wp;
		try{
				// Put length in the file as first record
				dos.writeLong((long)len);
				for (i=0;i<len;i++){
				  // put every way point from list one by one to stream
				  wp = list.get(i);
				  wp.writeDataStream(dos);
				}
			
			} catch(Exception e){
			 // if any error occurs
			 e.printStackTrace();
			} finally{
			}
	}
	
	public void readDataStream(DataInputStream dis) throws IOException {
		long len;
		int i;
		WayPoint wp =  new WayPoint();
		// read data into buffer
        
		try{
				// read list length
				len = (int) dis.readLong();
				for (i=0;i<len;i++){
					//read  way points from the stream one by one
					// wp.ReadDataStream(dis);
					
					list.add(new WayPoint().ReadDataStream(dis));
				}
			
			} catch(Exception e){
			 // if any error occurs
			 e.printStackTrace();
			} finally{
			}
	}
/*
*			File operations
*/
	
	public void writeFile(String filename) throws IOException {
		
				
		FileOutputStream filestream = null;
		BufferedOutputStream bufferedstream  = null;
		DataOutputStream dos = null;
		
		
      try{
		// prepare streams
		File file = new File(filename); 
		filestream = new FileOutputStream(file);
		bufferedstream  = new BufferedOutputStream(filestream);
		dos = new DataOutputStream(bufferedstream);
        // write Waypoint list list to file
		writeDataStream(dos);
		dos.flush();
		 
      }catch(Exception e){
         // if any error occurs
         e.printStackTrace();
      }finally{
         // releases all system resources from the streams
		if(filestream!=null)
            filestream.close();
		if(bufferedstream!=null)
            bufferedstream.close();
		if(dos!=null)
            dos.close();
      }
	}
	
	
	public void readFile(String filename) throws IOException {
		list.clear();
		FileInputStream filestream = null;
		BufferedInputStream bufferedstream  = null;
		DataInputStream dis = null;
		
		
      try{
		  // Prepare streams
		File file = new File(filename); 
		filestream = new FileInputStream(file);
		bufferedstream  = new BufferedInputStream(filestream);
		dis = new DataInputStream(bufferedstream);
		// read list from file
		readDataStream(dis);
      }catch(Exception e){
         // if any error occurs
         e.printStackTrace();
      }finally{
         // releases all system resources from the streams
		if(filestream!=null)
            filestream.close();
		if(bufferedstream!=null)
            bufferedstream.close();
		if(dis!=null)
            dis.close();
      }
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	