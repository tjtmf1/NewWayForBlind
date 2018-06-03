package com.example.newwayforblind;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class NavigationActivity extends AppCompatActivity {
    static final int STEP_CHANGED = 100;
    static final int STEP_COMPLETE = 300;
    private String[] route;
    private int routeLength;
    private Handler handler;
    private StepCheck stepCheck;
    private boolean ttsReady = false;
    private TextToSpeech tts;
    private ImageView imageView;
    private int routeIndex;
    private int goalStep;
    private double stride;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == STEP_CHANGED) {

                }else if(msg.what == STEP_COMPLETE){
                    arrivePoint();
                }
            }
        };
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                ttsReady = true;
            }
        });
        imageView = (ImageView)findViewById(R.id.navigationImage);
        init();
        startNav();
    }
    public void init(){
        Intent intent = getIntent();
        String result = intent.getStringExtra("route");
        route = result.split("/");
        routeLength = route.length;
        //보폭 받아오기
        stride = 1;
    }

    public void startNav(){
        stepCheck = new StepCheck(getApplicationContext(), handler);
        routeIndex = 0;
        stepCheck.startSensor();
        goalStep = (int)(Integer.parseInt(route[1]) / stride + 0.5);
        while(true) {
            if (ttsReady) {
                String text = route[routeIndex];
                text += "으로 " + goalStep + "걸음 가세요.";
                tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
                stepCheck.setStepCount(goalStep);
                routeIndex = 2;
                break;
            }
        }
    }

    public void endNav(){
        stepCheck.endSensor();
    }

    public void arrivePoint(){
        goalStep = Integer.parseInt(route[routeIndex + 1]);
        stepCheck.resetStep();
        while(true) {
            if (ttsReady) {
                String text = route[routeIndex];
                text += "으로 " + goalStep + "걸음 가세요.";
                tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
                stepCheck.setStepCount(goalStep);
                routeIndex += 2;
                break;
            }
        }
    }
}
