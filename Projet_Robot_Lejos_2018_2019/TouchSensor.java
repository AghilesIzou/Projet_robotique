package Capteur;

import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;

//class contenant des méthodes pour récupérer des valeurs du capteur de touché prise à cette adresse : https://stemrobotics.cs.pdx.edu/node/5187?root=4576

public class TouchSensor
{
    EV3TouchSensor sensor;
    SampleProvider sp;

    /**
     * Creates TouchSensor object.
     * @param port SensorPort of Touch Sensor device.
     */
    public TouchSensor()
    {
        sensor = new EV3TouchSensor(SensorPort.S3);
        sp = sensor.getTouchMode();
    }

    /**
     * Check state of Touch Sensor.
     * @return True if touched.
     */
    public boolean isTouched()
    {
       float [] sample = new float[sp.sampleSize()];

       sp.fetchSample(sample, 0);

       if (sample[0] == 0)
           return false;
       else
           return true;
    }

    /**
     * Release internal EV3TouchSensor.
     */
    public void close()
    {
        sensor.close();
    }
}