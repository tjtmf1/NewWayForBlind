package com.example.newwayforblind;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SensorListener implements SensorEventListener {
    static final int STEP_CHANGED = 100;
    static final int STEP_START = 200;
    Handler handler;
    boolean isStart;
    SensorListener(Handler _handler){
        handler = _handler;
        isStart = true;
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
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}