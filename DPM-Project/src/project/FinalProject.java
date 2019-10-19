package project;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

/**
 * <h1>FinalProject</h1>
 * This class contains the main method of the project
 * that instantiates all objects and initiates operation.
 * @author Owais
 *
 */
public class FinalProject 
{
	private static final Port lsPort1 = LocalEV3.get().getPort("S1");
	private static final Port lsPort2 = LocalEV3.get().getPort("S2");
	private static final Port usPort = LocalEV3.get().getPort("S3");
	private static final EV3LargeRegulatedMotor leftmotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	private static final EV3LargeRegulatedMotor rightmotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		//Light Sensor 1 (LEFT)
		SensorModes lsSensor1 = new EV3ColorSensor(lsPort1);	
		SampleProvider lsColor1 = lsSensor1.getMode("Red");	
		float[] lsData1 = new float[lsColor1.sampleSize()];
		LSpoller lspoll1 = new LSpoller(lsColor1, lsData1);
		
		//Light Sensor 2 (RIGHT)
		SensorModes lsSensor2 = new EV3ColorSensor(lsPort2);	
		SampleProvider lsColor2 = lsSensor2.getMode("Red");	
		float[] lsData2 = new float[lsColor2.sampleSize()];
		LSpoller lspoll2 = new LSpoller(lsColor2, lsData2);
		
		//Ultrasonic Sensor
		SensorModes usSensor = new EV3UltrasonicSensor(usPort);		
		SampleProvider usDistance = usSensor.getMode("Distance");	
		float[] usData = new float[usDistance.sampleSize()];
		USpoller uspoll = new USpoller(usDistance,usData);
		
		Odometer odometer = new Odometer(leftmotor, rightmotor);
		Navigation navigation = new Navigation(odometer, leftmotor, rightmotor);
		USlocalizer uslocalizer = new USlocalizer(odometer, uspoll, navigation);
		LSlocalizer lslocalizer = new LSlocalizer(odometer, lspoll1, lspoll2, navigation);
	}

}
