package com.example.newwayforblind;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class OrientationTestActivity extends AppCompatActivity {

    private StepCheck stepCheck;
    private Handler mHandler;
    private Orientation mOrientation;

    private TextView step;
    private TextView orientation;
    private TextView direction;
    int liveCoount=0;
    int warningCount=0;

    final int THRESHOLD=70;
    final int CORRECTION=360;

    int last=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation_test);

        step = (TextView) findViewById(R.id.step);
        orientation = (TextView) findViewById(R.id.orientation);
        direction = (TextView) findViewById(R.id.direction);

        mOrientation = new Orientation(getApplicationContext());
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d("Handler", "");
                step.setText(stepCheck.getStep() + "");
                orientation.setText(mOrientation.getOrientation() + "");
                checkHappiness(mOrientation.getOrientation());
            }
        };
        stepCheck = new StepCheck(getApplicationContext(), mHandler);
    }

    public void checkHappiness(int ori){

        int b=last-THRESHOLD;
        int a=last+THRESHOLD;
        int warningCnt=0;
        /*
         * 보정작업
         * min이 음수가 되면 360을 더하고,
         * max가 양수가 되면 360을 뺀다.*/

        if(a>360){ // 4사분면
            if((b<=ori&&ori<=360) || (0<=ori&&ori<=(a-CORRECTION))){ // 방향을 전환하지 않음
                direction.setText("직진");
            }else{ // 방향을 전환 함
                if(last - 180 <= ori && ori < last){
                    // 왼쪽
                    direction.setText("왼쪽");
                }else if((0 <= ori && ori < last - 180) || (last <= ori && ori < 360)){
                    // 오른쪽
                    direction.setText("오른쪽");
                }
            }
        }else if(b<0){ // 1사분면
            if((0<=ori&&ori<=a) || ((b+CORRECTION)<=ori&&ori<=360)){ // 방향을 전환하지 않음
                direction.setText("직진");
            }
            else{ // 방향을 전환 함
                if(last <= ori && ori < last - 180){
                    // 오른쪽
                    direction.setText("오른쪽");
                }else if((last - 180 <= ori && ori < 360) || (0 <= ori && ori < last)){
                    // 왼쪽
                    direction.setText("왼쪽");
                }
            }
        }else if(90 <= last && last < 180){ //2사분면
            if(b<=ori && ori<=a){ // 방향을 전환하지 않음
                direction.setText("직진");
            }else{ // 방향을 전환 함
                if(last <= ori && ori < last + 180){
                    // 오른쪽
                    direction.setText("오른쪽");
                }else if((0 <= ori && ori < last) || (last + 180 <= ori && ori < 360)){
                    // 왼쪽
                    direction.setText("왼쪽");
                }
            }
        }else if(180 <= last && last < 270){ // 3사분면
            if(b<=ori && ori<=a){ // 방향을 전환하지 않음
                direction.setText("직진");
            }else{ // 방향을 전환 함
                if((last <= ori && ori < 360) || (0 <= ori && ori < last - 180)){
                    // 오른쪽
                    direction.setText("오른쪽");
                }else if(last - 180 <= ori && ori < last){
                    // 왼쪽
                    direction.setText("왼쪽");
                }
            }
        }else if(270<=last && last <360){//4사분면
            if(b<=ori && ori<=a){
                //방향 전환 하지 않음
                direction.setText("직진");
            }else{
                //방향을 전환 함
                if (last-180<=ori && ori<last){
                    direction.setText("오른쪽");
                    //오른쪽
                }else if((last<=ori && ori<360) || (0<=ori && ori<last-180)){
                    //왼쪽
                    direction.setText("왼쪽");
                }
            }
        }else{
            Toast.makeText(this, "속해있지 않은 범위값, 범위 지정 오류", Toast.LENGTH_SHORT).show();
        }



    }
    public void onStart(View view) {
        stepCheck.startSensor();
        mOrientation.startSensor();
    }

    public void onFinish(View view) {
        stepCheck.endSensor();
        stepCheck.resetStep();
        mOrientation.endSensor();
    }
}