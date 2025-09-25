package project;


public class OdometerCorrection extends Thread
{
	private LSpoller lspollerleft;
	private LSpoller lspollerright;
	private Odometer odometer;
	
	public OdometerCorrection(Odometer odometer, LSpoller left, LSpoller right)
	{
		this.lspollerleft = left;
		this.lspollerright = right;
		this.odometer = odometer;
	}
	
	/**
	 * Run method required for thread.
	 */
	public void run()
	{
		
	}
	
	/**
	 * This method corrects he error in the odometer everytime the robot crosses a line.
	 */
	private void correctError()
	{
		
	}
	
	/**
	 * This method scans the area for dark black lines using a differential filter.
	 * @return True if line present, otherwise false.
	 */
	private boolean scanLine()
	{
		boolean isLine=true;
		
		//Implement Filter here.
		
		return isLine;
	}
	
}

