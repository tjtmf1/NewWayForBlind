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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class NavigationActivity extends AppCompatActivity {

    private Animation alpha, blink;
    private LinearLayout above, below;
    private FrameLayout doubleTap;
    private TextView blinkText;
    private ImageView blinkArrow;
    private TextView goal;

    static final int STEP_CHANGED = 100;
    static final int STEP_COMPLETE = 300;
    static final int SOON = 6;
    static final int STRAIGHT = 5;
    private String[] route;
    private int routeLength;
    private Handler handler;
    private StepCheck stepCheck;
    private Orientation orientation;
    private OrientationCheck orientationCheck;
    private boolean ttsReady = false;
    private TextToSpeech tts;
    private int routeIndex;
    private int goalStep;
    private double stride;
    private boolean endPoint = false;
    private boolean isNav = false;
    private boolean isAlert = false;
    private boolean isStraight = false;
    private boolean isCorrectDir = true;
    private String curDirection;

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

        goal = (TextView)findViewById(R.id.goal);

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
                    String dir = "";
                    if(stepCheck.getStep() >= 3)
                        dir = orientationCheck.checkDirection(orientation.getOrientation());
                    Log.v("direction", dir + "      " + orientation.getOrientation());
                    if(!curDirection.equals("직진")) {
                        if (dir.equals("왼쪽")) {                     //사용자의 진행 방향이 왼쪽
                            if(!curDirection.equals("왼쪽")){         //가야 할 방향이 왼쪽이 아니면
                                if(isCorrectDir){                     //틀린 방향이라고 아직 인식하지 못한 상태면
                                    if(ttsReady){
                                        tts.speak("잘못된 방향입니다. 뒤로 돌아주세요", TextToSpeech.QUEUE_ADD, null,null);
                                        isCorrectDir = false;
                                    }
                                }
                            }
                        } else if (dir.equals("오른쪽")) {
                            if(!curDirection.equals("오른쪽")){
                                if(isCorrectDir){
                                    if(ttsReady){
                                        tts.speak("잘못된 방향입니다. 뒤로 돌아주세요", TextToSpeech.QUEUE_ADD, null,null);
                                        isCorrectDir = false;
                                    }
                                }
                            }
                        } else if(dir.equals("뒤돌기")){               //사용자가 뒤를 돌고
                            if(!isCorrectDir){                         //현재 잘못된 진행 방향이면
                                goalStep += stepCheck.getStep() - 6;   //잘못 걸어온 걸음수 만큼 추가하고 (뒤돌아서 인식되는 시점은 이미 6걸음을 간 상태)
                                stepCheck.resetStep();                 //걸음 수를 초기화 한 후
                                stepCheck.setStepCount(goalStep);      //목표 걸음수를 다시 설정
                                tts.speak("직진으로 " + goalStep + "걸음 가세요.", TextToSpeech.QUEUE_ADD, null,null);
                                isCorrectDir = true;
                                handler.removeMessages(STEP_COMPLETE);
                            }
                        }
                    }
                    if (isCorrectDir && goalStep - stepCheck.getStep() <= SOON && !endPoint && !isAlert) {
                        tts.speak("잠시 후 " + route[routeIndex] + "방향입니다.", TextToSpeech.QUEUE_ADD, null, null);
                        isAlert = true;
                        setArrow(route[routeIndex]);
                    }
                    if (isCorrectDir && stepCheck.getStep() >= STRAIGHT && !isStraight) {
                        isStraight = true;
                        setArrow("직진");
                    }
                } else if (msg.what == STEP_COMPLETE) {
                    if(isCorrectDir) {
                        if (!endPoint)
                            arrivePoint();
                        else
                            endNav();
                    }
                }
            }
        };

        orientation = new Orientation(getApplicationContext());


        init();
    }

    public void setArrow(String direction) {
        if(direction.equals("오른쪽")) {
            blinkArrow.setImageResource(R.drawable.img_arrow2);
        }
        else if(direction.equals("왼쪽")) {
            blinkArrow.setImageResource(R.drawable.img_arrow3);
        }
        else if(direction.equals("직진")) {
            blinkArrow.setImageResource(R.drawable.img_arrow1);
        }
    }

    public void initAnimation() {
        alpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        blink = AnimationUtils.loadAnimation(this, R.anim.blink);

        above = (LinearLayout)findViewById(R.id.above);
        below = (LinearLayout)findViewById(R.id.below);
        blinkText = (TextView)findViewById(R.id.blinkText);
        blinkArrow = (ImageView)findViewById(R.id.blinkArrow);

        above.startAnimation(alpha);
        below.startAnimation(alpha);
        blinkText.startAnimation(blink);
        blinkArrow.startAnimation(blink);
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
        orientationCheck = new OrientationCheck();
        routeIndex = 0;
        stepCheck.startSensor();
        orientation.startSensor();
        curDirection = route[0];
        goalStep = (int)(Integer.parseInt(route[1]) / stride + 0.5);
        if(route[routeIndex].equals("직진")){
            isStraight = true;
        }
        while(true) {
            if (ttsReady) {
                String text = route[routeIndex];
                goal.setText(goalStep + " 걸음");
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
        orientation.endSensor();

        tts.speak("목적지에 도착 하였습니다. 잠시 후 메인화면으로 돌아갑니다.", TextToSpeech.QUEUE_ADD, null, null);
        CustomDialog dialog = new CustomDialog(this);
        dialog.show();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 5500);

    }

    public void arrivePoint(){
        curDirection = route[routeIndex];
        goalStep = Integer.parseInt(route[routeIndex + 1]);
        stepCheck.resetStep();
        while(true) {
            if (ttsReady) {
                goal.setText(goalStep + " 걸음");
                isAlert = false;
                String text = route[routeIndex] + "으로 " + goalStep + "걸음 가세요.";
                setArrow(route[routeIndex]);
                if(routeIndex +2 == routeLength){
                    endPoint = true;
                    text = route[routeIndex] + "으로" + goalStep + "걸음 후 목적지입니다.";
                }
                if(goalStep <= SOON){
                    isAlert = true;
                    if((routeIndex + 2) != routeLength){
                        text = route[routeIndex] + "으로" + goalStep + "걸음 후 " + route[routeIndex + 2] + "방향입니다.";
                    }
                }
                tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
                stepCheck.setStepCount(goalStep);
                routeIndex += 2;
                isStraight = false;
                break;
            }
        }
        if(routeIndex == routeLength)
            endPoint = true;
    }
}
