package project;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * <h1>Naviagtion</h1>
 * @author Owais
 *
 */
public class Navigation 
{
	private Odometer odometer;
	private EV3LargeRegulatedMotor leftmotor;
	private EV3LargeRegulatedMotor rightmotor;
	private double wheelBase = 15.2;
	private double wheelRadius = 2.1;
	private int ROTATION_SPEED=150;
	private int FORWARD_SPEED=200;
	
	/**
	 * 
	 * @param odometer Odometer instance for this class
	 * @param leftmotor Leftmotor instance to control movement
	 * @param rightmotor Rightmotor instance to control movement
	 */
	public Navigation(Odometer odometer, EV3LargeRegulatedMotor leftmotor, EV3LargeRegulatedMotor rightmotor)
	{
		this.odometer = odometer;
		this.leftmotor = leftmotor;
		this.rightmotor = rightmotor;
	}
	
	/**
	 * This method make the robot move from its current location as reported by the odometer to the position
	 * input as the parameters by first moving vertically(in the y direction) then horizontally(in the x direction).
	 * @param x X coordinate of the destination
	 * @param y Y coordinate of the destination
	 */
	public void travelTo(double x, double y)
	{
		
	}
	
	/**
	 * This method make the robot turn to the heading input as the parameter.
	 * @param theta Heading (degrees) from 0-360 (north is 0) to which the robot is required to turn.
	 */
	public void turnTo(double theta)
	{
		//Set speed of motors
				leftmotor.setSpeed(ROTATION_SPEED);
				rightmotor.setSpeed(ROTATION_SPEED);
				
				//Get present heading
				double oldHeading = odometer.getThetaDegrees();
				
				//Simple filter to manage 0 and 360 to
				//simplify calculations.
				if(oldHeading<=360 && oldHeading >=357.5)
				{
					oldHeading=0.0;
				}
				
				//Nested if-else statements that make the robot turn the minimal angle.
				if((theta-oldHeading)>=0)
				{
					//If the new heading is greater than the present heading, then the robot turns
					//clockwise if the difference is less than 180 and counter-clockwise if the 
					//difference is greater than 180.
					if((theta-oldHeading)<180.0)
					{
						leftmotor.rotate(convertAngle(theta-oldHeading), true);
						rightmotor.rotate(-convertAngle(theta-oldHeading), false);
					}
					else if((theta-oldHeading)>180.0)
					{
						leftmotor.rotate(-convertAngle(360-theta+oldHeading), true);
						rightmotor.rotate(convertAngle(360-theta+oldHeading), false);
					}
				}
				else
				{
					//If the new heading is smaller than the present heading, then the robot turns
					//counter-clockwise if the difference is less than 180 and clockwise if the 
					//difference is greater than 180.
					if((oldHeading-theta)<180.0)
					{
						leftmotor.rotate(-convertAngle(oldHeading-theta), true);
						rightmotor.rotate(convertAngle(oldHeading-theta), false);
					}
					else if((oldHeading-theta)>180.0)
					{
						leftmotor.rotate(convertAngle(360-oldHeading+theta), true);
						rightmotor.rotate(-convertAngle(360-oldHeading+theta), false);
					}
				}
	}
	
	/**
	 * Method that sets the motor speeds jointly
	 * @param lSpd Speed of left motor (float)
	 * @param rSpd Speed of right motor (float)
	 */
	public void setSpeeds(float lSpd, float rSpd) 
	{
		this.leftmotor.setSpeed(lSpd);
		this.rightmotor.setSpeed(rSpd);
		if (lSpd < 0)
		{
			this.leftmotor.backward();
		}
		else
		{
			this.leftmotor.forward();
		}
		if (rSpd < 0)
		{
			this.rightmotor.backward();
		}
		else
		{
			this.rightmotor.forward();
		}
	}

	/**
	 * Method that sets the motor speeds jointly
	 * @param lSpd Speed of left motor (int)
	 * @param rSpd Speed of right motor(int)
	 */
	public void setSpeeds(int lSpd, int rSpd) 
	{
		this.leftmotor.setSpeed(lSpd);
		this.rightmotor.setSpeed(rSpd);
	}
	
	/**
	 * This method makes the robot move forward.
	 */
	public void moveForward()
	{
		this.setSpeeds(FORWARD_SPEED,FORWARD_SPEED);
		leftmotor.forward();
		rightmotor.forward();
	}
	
	/**
	 * This method makes the robot move forward a specific distance.
	 * @param distance Distance(cm) the robot is required to move.
	 */
	public void moveForward(double distance)
	{
		this.setSpeeds(FORWARD_SPEED,FORWARD_SPEED);
		leftmotor.rotate(convertDistance(distance), true);
		rightmotor.rotate(convertDistance(distance), false);
	}
	
	/**
	 * This method makes the robot move backward.
	 */
	public void moveBackward()
	{
		this.setSpeeds(FORWARD_SPEED,FORWARD_SPEED);
		leftmotor.backward();
		rightmotor.backward();
	}
	
	/**
	 * This method makes the robot move backward a specific distance.
	 * @param distance Distance(cm) the robot is required to move.
	 */
	public void moveBackward(double distance)
	{
		this.setSpeeds(FORWARD_SPEED,FORWARD_SPEED);
		leftmotor.rotate(-convertDistance(distance), true);
		rightmotor.rotate(-convertDistance(distance), false);
	}
	
	/**
	 * This method makes the robot rotate 
	 * @param Lspeed Speed of left wheel
	 * @param Rspeed Speed of right wheel
	 */
	public void turn(int Lspeed, int Rspeed)
	{
		leftmotor.setAcceleration(2000);
		rightmotor.setAcceleration(2000);
		leftmotor.setSpeed(Math.abs(Lspeed));
		rightmotor.setSpeed(Math.abs(Rspeed));
		if(Lspeed<0)
		{
			leftmotor.backward();
		}
		else
		{
			leftmotor.forward();
		}
		
		if(Rspeed<0)
		{
			rightmotor.backward();
		}
		else
		{
			rightmotor.forward();
		}
	}
	
	/**
	 * This method stops both motors immediately
	 */
	public void stopMotors()
	{
		leftmotor.setSpeed(0);
		leftmotor.setSpeed(0);
		try
		{
			Thread.sleep(100);
		}
		catch(InterruptedException e)
		{
			
		}
	}
	
	/**
	 * This method makes the robot move a specific angle.
	 * @param angle Angle(degrees) the robot is required to move. If positive then turns right, if negative then turns left.
	 */
	public void turn(double angle)
	{
		this.setSpeeds(ROTATION_SPEED, ROTATION_SPEED);
		leftmotor.rotate(convertAngle(angle),true);
		rightmotor.rotate(-convertAngle(angle),false);
	}
	
	/**
	 * This method takes as input a distance in cm and converts it into the number of wheel rotations required to cover that distance.
	 * @param distance Distance is cm.
	 * @return Wheel rotations required to travel the distance specified.
	 */
	public int convertDistance(double distance)
	{
		return (int) ((180.0 * distance) / (Math.PI * wheelRadius));
	}

	/**
	 * This method takes as input an angle in degrees and converts it into the number of wheel rotations requires to turn that angle.
	 * @param angle Angle in degrees the robot is required to turn.
	 * @return Number of wheel rotations required to turn the required angle.
	 */
	public int convertAngle(double angle) 
	{
		return convertDistance(Math.PI * wheelBase * angle / 360.0);
	}
	
	/**
	 * This method takes an angle in radians and converts it to degrees.
	 * @param radian Angle in radians.
	 * @return Angle in degrees.
	 */
	public double convertToDegrees(double radian)
	{
		return (radian*180)/Math.PI;
	}
}
