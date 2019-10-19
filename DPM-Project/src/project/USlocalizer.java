package project;

import lejos.hardware.Sound;

/**
 * <h1>USlocalizer</h1>
 * @author Owais
 *
 */
public class USlocalizer
{
	private Odometer odometer;
	private USpoller uspoller;
	private Navigation navigation;
	private enum LocalizationType {FALLING_EDGE, RISING_EDGE};
	private LocalizationType loctype;
	private int ROTATION_SPEED = 80;
	
	public USlocalizer(Odometer odometer, USpoller uspoller, Navigation navigation)
	{
		this.odometer=odometer;
		this.uspoller=uspoller;
		this.navigation=navigation;
	}
	
	/**
	 * This method carries out the ultrasonic localization routine.
	 */
	private void localize()
	{
		double angleA=0, angleB=0;
		int USdistance;
		
		//TBD
		int thresholdDistance=38;
		
		int count=0;
		boolean facingToWall=true;
		
		// Set the robot to rotate
		navigation.turn(ROTATION_SPEED, -ROTATION_SPEED);
		
		//Rotate until the robot sees no wall
		while(facingToWall)
		{
			USdistance = getFilteredData();
			if(USdistance > thresholdDistance)
			{
				count++;
			}
			if((USdistance > thresholdDistance) && (count >= 3))
			{
				facingToWall = false;
				count=0;
			}
		}
		
		sleep(3000);
		
		//Keep rotating until the robot sees a wall, then latch the angle
		while(!facingToWall)
		{
			USdistance = getFilteredData();	
			if(USdistance < thresholdDistance)
			{
				count++;
			}
			
			if((USdistance < thresholdDistance) && (count >= 3))
			{
				facingToWall = true;
				angleA = odometer.getThetaDegrees();
				Sound.beep();
				navigation.stopMotors();
				count=0;
			}
		}
		
		//Switch direction and wait until it sees no wall
	    navigation.turn(-ROTATION_SPEED,ROTATION_SPEED);
		while(facingToWall)
		{
			USdistance = getFilteredData();	
			
			if(USdistance > thresholdDistance)
			{
				count++;
			}
			
			if((USdistance > thresholdDistance) && (count >= 3))
			{
				facingToWall = false;
				count=0;
			}
		}	
		
		//Keep rotating until the robot sees a wall, then latch the angle
		sleep(3000);
		
		while(!facingToWall)
		{
			USdistance = getFilteredData();		
			if(USdistance < thresholdDistance)
			{
				count++;
			}
			
			if((USdistance < thresholdDistance) && (count >= 3))
			{
				facingToWall = true;
				angleB = odometer.getThetaDegrees();
				Sound.beep();
				navigation.stopMotors();
			}
		}
		
		//angleA is clockwise from angleB, so assume the average of the
		// angles to the right of angleB is 45 degrees past 'north'
		double angle = 45 - (angleA - angleB)/2;
		
		// update the odometer position (example to follow:)
		odometer.setTheta(angle);
		navigation.turnTo(0);
	}
	
	/**
	 * Gets distance from USpoller and filters data for
	 * localization
	 * @return If distance is greater than 50 than 50 is returned otherwise the original value is returned.
	 * 
	 */
	private int getFilteredData()
	{
		int distance = uspoller.getUS();
		
		//Implement Filter here
		if(distance>50)
		{
			distance =50;
		}
		
		return distance;
	}
	
	/**
	 * Thread sleeps for a specified time
	 * 
	 * @param time The time the thread will sleep (in ms)
	 */
	private void sleep(int time) 
	{
		try 
		{
			Thread.sleep(time);
		} 
		catch (InterruptedException e) 
		{
		}
	}
}
