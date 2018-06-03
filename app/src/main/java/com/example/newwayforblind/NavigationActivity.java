package com.example.newwayforblind;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Locale;

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
    private boolean endPoint = false;
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
                    if(routeLength == routeIndex + 1)
                        endPoint = true;
                    if(stepCheck.getStep() - goalStep <= 10 && !endPoint){
                        //route[routeIndex + 1] 에 따라 방향표시
                        Toast.makeText(getApplicationContext(), route[routeIndex+1], Toast.LENGTH_SHORT).show();
                    }else if(stepCheck.getStep() == 5){
                        Toast.makeText(getApplicationContext(), "직진으로 변경", Toast.LENGTH_SHORT).show();
                    }
                }else if(msg.what == STEP_COMPLETE){
                    if(!endPoint)
                        arrivePoint();
                    else
                        endNav();
                }
            }
        };
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                ttsReady = true;
                tts.setLanguage(Locale.KOREA);
            }
        });
        imageView = (ImageView)findViewById(R.id.navigationImage);
        imageView.setImageResource(android.R.drawable.btn_star);
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
