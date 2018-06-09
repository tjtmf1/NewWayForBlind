package com.example.newwayforblind;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class Orientation {
    private SensorManager sensorManager;
    private SensorListener sensorEventListener;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private Context context;
    float[] mGravity = null;
    float[] mGeomagnetic = null;
    float[] values = null;

    Orientation(Context context){
        this.context = context;
        values = new float[3];
        sensorEventListener = new SensorListener();
        sensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(sensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }
    public void startSensor(){
        sensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(sensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }
    public void endSensor(){
        if(sensorManager != null)
            sensorManager.unregisterListener(sensorEventListener);
    }

    public int getOrientation(){return sensorEventListener.orientation[0];} //+ 180;}
}
