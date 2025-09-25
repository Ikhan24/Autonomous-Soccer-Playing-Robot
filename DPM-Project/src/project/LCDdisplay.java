package project;

import lejos.hardware.lcd.TextLCD;


public class LCDdisplay extends Thread 
{
	private static final long DISPLAY_PERIOD=25;
	private Odometer odometer;
	private TextLCD textlcd;
	
	/**
	 * 
	 * @param odometer
	 * @param textlcd
	 */
	public LCDdisplay(Odometer odometer, TextLCD textlcd)
	{
		this.odometer = odometer;
		this.textlcd = textlcd;
	}
	
	/**
	 * Run method required for thread
	 */
	public void run()
	{
		long displayStart, displayEnd;
		double[] position = new double[3];

		// clear the display once
		textlcd.clear();

		while (true) 
		{
			displayStart = System.currentTimeMillis();

			// clear the lines for displaying odometry information
			textlcd.drawString("X:              ", 0, 0);
			textlcd.drawString("Y:              ", 0, 1);
			textlcd.drawString("T:              ", 0, 2);
			
			// get the odometry information
			odometer.getPosition(position, new boolean[] { true, true, true });

			// display odometry information
			for (int i = 0; i < 3; i++) 
			{
				textlcd.drawString(formattedDoubleToString(position[i], 2), 3, i);
			}

			// throttle the OdometryDisplay
			displayEnd = System.currentTimeMillis();
			if (displayEnd - displayStart < DISPLAY_PERIOD) 
			{
				try
				{
					Thread.sleep(DISPLAY_PERIOD - (displayEnd - displayStart));
				}
				catch (InterruptedException e)
				{
					// there is nothing to be done here because it is not
					// expected that OdometryDisplay will be interrupted
					// by another thread
				}
			}
		}

	}
	
	/**
	 * This method converts a double to a string to be printed on the LCD of the robot.
	 * @param x The number that needs to be changed to a string.
	 * @param places The number of decimal places to be shown on the LCD.
	 * @return The formatted string is returned.
	 */
	public String formattedDoubleToString(double x, int places)
	{
		String result = "";
		String stack = "";
		long t;
		
		// put in a minus sign as needed
		if (x < 0.0)
		{
			result += "-";
		}
		
		// put in a leading 0
		if (-1.0 < x && x < 1.0)
		{
			result += "0";
		}
		else
		{
			t = (long)x;
			if (t < 0)
			{
				t = -t;
			}
			
			while (t > 0) 
			{
				stack = Long.toString(t % 10) + stack;
				t /= 10;
			}
			result += stack;
		}
		
		// put the decimal, if needed
		if (places > 0) 
		{
			result += ".";
		
			// put the appropriate number of decimals
			for (int i = 0; i < places; i++) 
			{
				x = Math.abs(x);
				x = x - Math.floor(x);
				x *= 10.0;
				result += Long.toString((long)x);
			}
		}
		
		return result;

	}
}

