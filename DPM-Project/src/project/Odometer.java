package project;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * <h1>Odometer</h1>
 * @author Owais
 *
 */
public class Odometer extends Thread
{
	private static final long ODOMETER_PERIOD=25;
	private double x,y,theta;
	private double wheelBase = 15.2;
	private double wheelRadius = 2.1;
	double lastTachoL, lastTachoR, nowTachoL, nowTachoR;
	private EV3LargeRegulatedMotor leftmotor;
	private EV3LargeRegulatedMotor rightmotor;
	private Object lock;
	
	/**
	 * 
	 * @param leftmotor
	 * @param rightmotor
	 */
	public Odometer(EV3LargeRegulatedMotor leftmotor, EV3LargeRegulatedMotor rightmotor)
	{
		this.x = 0.0;
		this.y = 0.0;
		this.theta = 0.0;
		lock = new Object();
		this.leftmotor = leftmotor;
		this.rightmotor = rightmotor;
	}
	
	/**
	 * Run method required for thread.
	 */
	public void run()
	{
		long updateStart, updateEnd;

		while (true)
		{
			updateStart = System.currentTimeMillis();
			
			//Initiating local variables to store intermediate data
			//to calculate x, y and theta.
			double distL, distR, deltaD, deltaT, dX, dY;
			
			synchronized (lock) 
			{
				//Getting the latest tacho counts from the two motors
				nowTachoL = leftmotor.getTachoCount();
				nowTachoR = rightmotor.getTachoCount();
				
				//Calculating wheel displacements
				distL = Math.PI*wheelRadius*(nowTachoL-lastTachoL)/180;
				distR = Math.PI*wheelRadius*(nowTachoR-lastTachoR)/180;
				
				//Updating the last tacho counts for next iterations
				lastTachoL = nowTachoL;
				lastTachoR = nowTachoR;
				
				//Computing vehicle displacement
				deltaD = 0.5*(distL+distR);
				
				//Compute change in heading
				deltaT = (distL-distR)/wheelBase;
				
				//Update heading
				theta = theta + deltaT;
				
				//Computing X and Y displacements
				dX = deltaD * Math.sin(theta);
				dY = deltaD * Math.cos(theta);
				
				//Updating x and y positions
				x = x + dX;
				y = y + dY;
			}

			// this ensures that the odometer only runs once every period
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < ODOMETER_PERIOD) 
			{
				try 
				{
					Thread.sleep(ODOMETER_PERIOD - (updateEnd - updateStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometer will be interrupted by
					// another thread
				}
			}
		}

	}
	
	/**
	 * This method returns the current position of the robot.
	 * @param position Empty double array that will contain the x,y and theta values at the indices 0,1 and 2 respectively after the method ends.
	 * @param update A boolean array that indicates whether the x,y and theta values need to be updated. e.g [true,false,true] will only
	 *  update the x and theta values while y remains the same as before.
	 */
	public void getPosition(double[] position, boolean[] update) 
	{
		// ensure that the values don't change while the odometer is running
		synchronized (lock) 
		{
			if (update[0])
				position[0] = x;
			if (update[1])
				position[1] = y;
			if (update[2])
			{
				position[2]=getThetaDegrees();
			}
		}
	}
	
	/**
	 * This method returns the X position of the robot.
	 * @return X coordinate of the robot.
	 */
	public double getX()
	{
		double result;

		synchronized (lock)
		{
			result = x;
		}

		return result;
	}

	/**
	 * This method returns the Y coordinate of the robot.
	 * @return Y coordinate of the robot.
	 */
	public double getY() 
	{
		double result;

		synchronized (lock) 
		{
			result = y;
		}

		return result;
	}

	/**
	 * This method returns the heading of the robot in radians.
	 * @return Heading of robot in radians.
	 */
	public double getTheta() 
	{
		double result;

		synchronized (lock) 
		{
			result = theta;
		}

		return result;
	}
	
	/**
	 * This method returns the heading of the robot in degrees.
	 * @return Heading of the robot in degrees. (0-360)
	 */
	public double getThetaDegrees()
	{
		double result;

		synchronized (lock) 
		{
			
			if(theta >=0)
			{
				result = (theta*(180/Math.PI))%360;
			}
			else
			{
				result= (360-(((Math.abs(theta))*(180/Math.PI))%360));
			}
			
		}

		return result;
	}

	/**
	 * This method updates the odometer to the values input as parameters.
	 * @param position A double array that contains the new values of x,y and theta at the indices 0,1 and 2 respectively
	 * @param update A boolean array that indicates which values need to be updated, e.g [true,true,false] means that only the x and y values are updated.
	 */
	public void setPosition(double[] position, boolean[] update)
	{
		// ensure that the values don't change while the odometer is running
		synchronized (lock) 
		{
			if (update[0])
				x = position[0];
			if (update[1])
				y = position[1];
			if (update[2])
				theta = position[2];
		}
	}

	/**
	 * This method updates the x value of the odometer to the value input as the parameter.
	 * @param x New value of the x value of the odometer.
	 */
	public void setX(double x) 
	{	
		synchronized (lock) 
		{
			this.x = x;
		}
	}

	/**
	 * This method updates the y value of the odometer to the value input as the parameter.
	 * @param y New value of the y value of the odometer.
	 */
	public void setY(double y) 
	{
		synchronized (lock) 
		{
			this.y = y;
		}
	}

	/**
	 * This method updates the theta value of the odometer to the value input as the parameter.
	 * @param theta New value of theta of the odometer.
	 */
	public void setTheta(double theta) 
	{
		synchronized (lock) 
		{
			this.theta = theta;
		}
	}
}
