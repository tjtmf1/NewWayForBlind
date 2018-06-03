package com.example.newwayforblind;

import android.annotation.SuppressLint;
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
    }
    public void init(){
        //경로 받아오기
        //보폭 받아오기
    }

    public void startNav(){
        stepCheck = new StepCheck(getApplicationContext(), handler);
        routeIndex = 0;
        stepCheck.startSensor();
    }

    public void endNav(){
        stepCheck.endSensor();
    }

    public void arrivePoint(){
        //분기점 도착시
        //방향안내 후 다음 경로의 길이 파악
    }
}
