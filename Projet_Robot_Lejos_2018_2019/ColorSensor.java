package Capteur;

import java.util.ArrayList;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.ColorDetector;
import lejos.robotics.ColorIdentifier;
import lejos.utility.Delay;
import Utility.lcd;

//class contenant des méthodes pour récupérer des valeurs du capteur de couleur prise à cette adresse : https://github.com/stemrobotics/EV3-Exercises/blob/master/ColorSensor.java
//des méthodes ont été rajoutées

public class ColorSensor implements ColorDetector, ColorIdentifier
{
	EV3ColorSensor	sensor;
	float[]		sample;
	Color Rgb;
	int IdCouleur;

    
	public ColorSensor()
	{
		sensor = new EV3ColorSensor(SensorPort.S1);
		setAmbientMode();
		setFloodLight(false);
	}

	
	public EV3ColorSensor getSensor()
	{
		return sensor;
	}
	
	
	public void setAmbientMode()
	{
		sensor.setCurrentMode("Ambient");
		sample = new float[sensor.sampleSize()];
	}
	
	
	public void setRedMode()
	{
		sensor.setCurrentMode("Red");
		sample = new float[sensor.sampleSize()];
	}
	
	
	public void setColorIdMode()
	{
		sensor.setCurrentMode("ColorID");
		sample = new float[sensor.sampleSize()];
	}
	

	public void setRGBMode()
	{
		sensor.setCurrentMode("RGB");
		sample = new float[sensor.sampleSize()];
	}

	
	public int getColorID()
	{
		sensor.fetchSample(sample, 0);
		
		return (int) sample[0];
	}

	
	public Color getColor()
	{
		sensor.fetchSample(sample, 0);
		
		return new Color((int)(sample[0] * 255), (int)(sample[1] * 255), (int)(sample[2] * 255));
	}

	
	public float getAmbient()
	{
		sensor.fetchSample(sample, 0);
		
		return sample[0];
	}


	public float getRed()
	{
		sensor.fetchSample(sample, 0);
		
		return sample[0];
	}
	
	
	public void close()
	{
		sensor.close();
	}


	public boolean isFloodLightOn()
	{
		return sensor.isFloodlightOn();
	}

	
	public void setFloodLight(boolean on)
	{
		sensor.setFloodlight(on);
	}
	
	
	public void setFloodLight(int color)
	{
		sensor.setFloodlight(color);
	}
	public void getRGB() {
		sensor.fetchSample(sample, 0);
		Rgb =new Color((int)(sample[0] * 255), (int)(sample[1] * 255), (int)(sample[2] * 255));
        
        lcd.print(6, "r=%d g=%d b=%d", Rgb.getRed(), Rgb.getGreen(), Rgb.getBlue());
        
	}
	public int idColorRGB() {
		sensor.fetchSample(sample, 0);
		Rgb =new Color((int)(sample[0] * 255), (int)(sample[1] * 255), (int)(sample[2] * 255));
        if ((0<=Rgb.getRed()&&Rgb.getRed()<=1)&&(0<=Rgb.getGreen()&&Rgb.getGreen()<=1)&&(0<=Rgb.getBlue()&&Rgb.getBlue()<=1)) {
        	IdCouleur= 0;//pas de couleur*
        	}
        if ((66<Rgb.getRed()&&Rgb.getRed()<85)&&(80<Rgb.getGreen()&&Rgb.getGreen()<98)&&(40<Rgb.getBlue()&&Rgb.getBlue()<58)) {
        	IdCouleur= 1;//blanc
        }
        if ((1<Rgb.getRed()&&Rgb.getRed()<14)&&(1<Rgb.getGreen()&&Rgb.getGreen()<17)&&(1<Rgb.getBlue()&&Rgb.getBlue()<13)) {
        	IdCouleur= 2;//noir
        }
        if ((4<Rgb.getRed()&&Rgb.getRed()<20)&&(38<Rgb.getGreen()&&Rgb.getGreen()<54)&&(24<Rgb.getBlue()&&Rgb.getBlue()<40)) {
        	IdCouleur= 3;//bleu
        }
        if ((10<Rgb.getRed()&&Rgb.getRed()<26)&&(41<Rgb.getGreen()&&Rgb.getGreen()<57)&&(2<Rgb.getBlue()&&Rgb.getBlue()<18)) {
        	IdCouleur= 4;//vert
        }
        if ((37<Rgb.getRed()&&Rgb.getRed()<53)&&(4<Rgb.getGreen()&&Rgb.getGreen()<20)&&(0<Rgb.getBlue()&&Rgb.getBlue()<14)) {
        	IdCouleur= 5;//rouge
        }
        if ((13<Rgb.getRed()&&Rgb.getRed()<29)&&(13<Rgb.getGreen()&&Rgb.getGreen()<29)&&(4<Rgb.getBlue()&&Rgb.getBlue()<20)) {
        	IdCouleur= 6;//marron
        }
        if ((55<Rgb.getRed()&&Rgb.getRed()<75)&&(66<Rgb.getGreen()&&Rgb.getGreen()<85)&&(8<Rgb.getBlue()&&Rgb.getBlue()<22)) {
        	IdCouleur= 7;//jaune
        }
        if ((25<Rgb.getRed()&&Rgb.getRed()<36)&&(30<Rgb.getGreen()&&Rgb.getGreen()<44)&&(13<Rgb.getBlue()&&Rgb.getBlue()<24)) {
        	IdCouleur= 8;//gris
        }
		return IdCouleur;
	}
	
	public static String nomCouleur(int couleur) {
		switch (couleur) {
		case 0:
			return "pas de couleur";
		case 1:
			return "blanc";
		case 2:
			return "noir";
		case 3:
			return "bleu";
		case 4:
			return "vert";
		case 5:
			return "rouge";
		case 6:
			return "marron";
		case 7:
			return "jaune";
		case 8:
			return "gris";
		}
		return "";
	}
}
