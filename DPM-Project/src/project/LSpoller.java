package project;

import lejos.robotics.SampleProvider;

/**
 * <h1>LSpoller</h1>
 * @author Owais
 *
 */
public class LSpoller extends Thread
{
	private SampleProvider ls;
	private float[] lsData;
	
	/**
	 * @param sp Sample Provider for light sensor
	 * @param lsData Array that stores values reported by the light sensor
	 */
	public LSpoller(SampleProvider sp, float[] lsData)
	{
		this.ls=sp;
		this.lsData=lsData;
	}
	
	/**
	 * This method polls the light sensor and scales the float to
	 * an integer value between 0 and 1023, a higher value being a darker color.
	 * @return Latest light sensor reading is returned.
	 */
	public int getLS()
	{
		int colorValue;
		ls.fetchSample(lsData, 0);
		colorValue=(int)(lsData[0]*100);
		return colorValue;
	}
}
