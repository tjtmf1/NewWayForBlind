package com.example.newwayforblind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;

public class StepCheck {
    static final int STEP_CHANGED = 100;
    static final int STEP_START = 200;
    static final int STEP_COMPLETE = 300;
    private Handler mainHandler;
    private Handler listenerHandler;
    private int last_step;
    private int step;
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private Sensor sensor;
    private Context context;
    private boolean isSetCount;
    private int setCount;
    @SuppressLint("HandlerLeak")
    public void init(){
        listenerHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.arg1 == STEP_CHANGED){
                    step = msg.arg2 - last_step;
                    mainHandler.sendEmptyMessage(STEP_CHANGED);
                    if(isSetCount){
                        if(step == setCount){
                            if(mainHandler != null){
                                mainHandler.sendEmptyMessage(STEP_COMPLETE);
                                isSetCount = false;
                                setCount = 0;
                            }
                        }
                    }
                }
                else if(msg.arg1 == STEP_START){
                    last_step = msg.arg2;
                }
            }
        };
        step = 0;
        isSetCount = false;
        setCount = 0;
    }
    StepCheck(Context _context, Handler _handler){
        context = _context;
        mainHandler = _handler;
        init();
    }

    StepCheck(Context _context){
        context = _context;
        init();
    }

    public void setHandler(Handler _handler){
        mainHandler = _handler;
    }
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        endSensor();
    }

    public void setStepCount(int nCount){
        isSetCount = true;
        setCount = nCount;
    }

    public void startSensor(){
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorEventListener = new SensorListener(listenerHandler);
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI);
    }
    public void endSensor(){
        if(sensorManager != null)
            sensorManager.unregisterListener(sensorEventListener);
    }
    public int getStep(){return step;}
}
