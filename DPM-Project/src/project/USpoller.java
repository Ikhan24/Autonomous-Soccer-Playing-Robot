package project;

import lejos.robotics.SampleProvider;

/**
 * <h1>USpoller</h1>
 * @author Owais
 *
 */
public class USpoller extends Thread
{
	private SampleProvider us;
	private float[] usData;
	
	/**
	 * @param sp Sample Provider for ultrasonic sensor.
	 * @param usData Array that stores values reported by the ultrasonic sensor.
	 */
	public USpoller(SampleProvider sp, float[] usData)
	{
		this.us=sp;
		this.usData=usData;
	}
	
	/**
	 * This method polls the ultrasonic sensor and scales the float to
	 * an integer value between 0 and 255, a higher value being a larger distance.
	 * @return Latest ultrasonic sensor reading is returned.
	 */
	public int getUS()
	{
		int distance=0;
		us.fetchSample(usData, 0);
		distance= (int)(usData[0]*100.0);
		return distance;
	}
}
