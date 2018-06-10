package com.example.newwayforblind;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SensorListener implements SensorEventListener {
    static final int STEP_CHANGED = 100;
    static final int STEP_START = 200;
    Handler handler;
    boolean isStart;
    float[] mGravity = null;
    float[] mGeomagnetic = null;
    int[] orientation;
    SensorListener(Handler _handler){
        handler = _handler;
        isStart = true;
        orientation = new int[3];
    }
    SensorListener(){
        isStart = true;
        orientation = new int[3];
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            Message msg = new Message();
            if(!isStart){
                msg.arg1 = STEP_CHANGED;
                msg.arg2 = (int) event.values[0];
            }
            else {
                msg.arg1 = STEP_START;
                msg.arg2 = (int)event.values[0];
                isStart = false;
            }
            handler.sendMessage(msg);
            Log.d("STEP_COUNTER", event.values[0]+"");
        }
        else if(event.sensor.getType() == Sensor.TYPE_GRAVITY){
            mGravity = event.values;
        }
        else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            mGeomagnetic = event.values;
        }
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float[] values = new float[3];
                SensorManager.getOrientation(R, values);
                orientation[0] = (int)Math.toDegrees((double)values[0]);
                orientation[1] = (int)Math.toDegrees((double)values[1]);
                orientation[2] = (int)Math.toDegrees((double)values[2]);
                Log.v("ori[0] : ", (orientation[0] + 180) + "");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
