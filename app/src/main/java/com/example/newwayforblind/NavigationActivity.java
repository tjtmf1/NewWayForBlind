package com.example.newwayforblind;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Locale;

public class NavigationActivity extends AppCompatActivity {

    Animation alpha;
    LinearLayout above, below;
    FrameLayout doubleTap;

    static final int STEP_CHANGED = 100;
    static final int STEP_COMPLETE = 300;
    static final int SOON = 6;
    static final int STRAIGHT = 5;
    private String[] route;
    private int routeLength;
    private Handler handler;
    private StepCheck stepCheck;
    private boolean ttsReady = false;
    private TextToSpeech tts;
    private int routeIndex;
    private int goalStep;
    private double stride;
    private boolean endPoint = false;
    private boolean isNav = false;
    private boolean isAlert = false;
    private boolean isStraight = false;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                ttsReady = true;
                tts.setLanguage(Locale.KOREA);
            }
        });

        doubleTap = (FrameLayout) findViewById(R.id.doubleTap);
        doubleTap.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void doubleTap() {
                doubleTap.setVisibility(View.GONE);
                initAnimation();
                tts.speak("안내를 시작합니다.", TextToSpeech.QUEUE_ADD, null, null);

                if(!isNav){
                    isNav = true;
                    startNav();
                }
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == STEP_CHANGED) {
                    Log.v("step", stepCheck.getStep() + "");
                    if (goalStep - stepCheck.getStep() <= SOON && !endPoint && !isAlert) {
                        tts.speak("잠시 후 " + route[routeIndex] + "방향입니다.", TextToSpeech.QUEUE_ADD, null, null);
                        isAlert = true;
                    }
                    if (stepCheck.getStep() >= STRAIGHT && !isStraight) {
                        Toast.makeText(getApplicationContext(), "직진으로 변경", Toast.LENGTH_SHORT).show();
                        isStraight = true;
                    }
                } else if (msg.what == STEP_COMPLETE) {
                    if (!endPoint)
                        arrivePoint();
                    else
                        endNav();
                }
            }
        };

        init();
    }

    public void initAnimation() {
        alpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        above = (LinearLayout)findViewById(R.id.above);
        below = (LinearLayout)findViewById(R.id.below);
        above.startAnimation(alpha);
        below.startAnimation(alpha);
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
        if(route[routeIndex].equals("직진")){
            isStraight = true;
        }
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
        tts.speak("목적지에 도달하였습니다.", TextToSpeech.QUEUE_ADD, null, null);
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
                isAlert = false;
                isStraight = false;
                break;
            }
        }
        if(routeIndex == routeLength)
            endPoint = true;
    }
}
