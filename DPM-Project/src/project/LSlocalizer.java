package project;

/**
 * <h1>LSlocalizer</h1>
 * @author Owais
 *
 */
public class LSlocalizer 
{
	private Odometer odometer;
	private LSpoller lspoller1;
	private LSpoller lspoller2;
	private Navigation navigation;
	
	/**
	 * 
	 * @param odometer Odometer instance for this class
	 * @param lspoller LSpoller instance for this class
	 * @param navigation Navigation instance for this class
	 */
	public LSlocalizer(Odometer odometer, LSpoller lspoller1,LSpoller lspoller2, Navigation navigation)
	{
		this.odometer = odometer;
		this.lspoller1 = lspoller1;
		this.lspoller2 = lspoller2;
		this.navigation = navigation;
	}
	
	/**
	 * This method carries out the light sensor localization routine.
	 */
	public void localize()
	{
		double lightS1=0;
		double lightS2=0;
		boolean left=false, right=false;
		
		navigation.turn(100,100);
		
		//Scan the first horizontal gridline in front of the robot
		while(left==false && right ==false)
		{
			lightS1 = lspoller1.getLS();
			lightS2 = lspoller2.getLS();
			
			//If both light sensors detect the line at the same time
			//Stop the robot
			if((lightS1 < 43) && (lightS2 <43))
			{
				navigation.stopMotors();
				left=true;
				right=true;
			}
			//If the left light sensor detects the line before the right
			//Stop the left motor only to let the other catch up
			else if(lightS1 < 43)
			{
				navigation.turn(0,100);
				left=true;
			}
			//If the right light sensor detects the line before the left
			//Stop the right motor only to let the other catch up
			else if(lightS2 <43)
			{
				navigation.turn(100,0);
				right=true;
			}
			
		}
		
		//The distance between the light sensors and the centre of rotation
		//is to be determined. (12 is an arbitrary value)
		navigation.moveBackward(12);
		
		navigation.turnTo(90);
		left=false; right=false;
		
		while(left==false && right ==false)
		{
			lightS1 = lspoller1.getLS();
			lightS2 = lspoller2.getLS();
			
			//If both light sensors detect the line at the same time
			//Stop the robot
			if((lightS1 < 43) && (lightS2 <43))
			{
				navigation.stopMotors();
				left=true;
				right=true;
			}
			//If the left light sensor detects the line before the right
			//Stop the left motor only to let the other catch up
			else if(lightS1 < 43)
			{
				navigation.turn(0,100);
				left=true;
			}
			//If the right light sensor detects the line before the left
			//Stop the right motor only to let the other catch up
			else if(lightS2 <43)
			{
				navigation.turn(100,0);
				right=true;
			}
			
		}
		
		//The distance between the light sensors and the centre of rotation
		//is to be determined. (12 is an arbitrary value)
		navigation.moveBackward(12);
		
		//THE ROBOT IS NOW AT THE ORIGIN. NOW SET THE ODOMETER ACCORDING TO
		//THE INPUT PARAMETER, SPECIFICALLY THE STARTING CORNER.
		//This arbitrarily set at the bottom left corner for now.
		odometer.setPosition(new double[]{0.0,0.0,90.0}, new boolean[]{true,true,true});
	}
	
	/**
	 * This method scans the area for dark black lines using a differential filter.
	 * @return True if line present, otherwise false.
	 */
	public boolean scanLine()
	{
		boolean isLine=true;
		
		//Implement Filter here.
		
		return isLine;
	}
}
