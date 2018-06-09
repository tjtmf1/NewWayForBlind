package com.example.newwayforblind;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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

    int warningCount=0;

    final int THRESHOLD=65;//임계치
    final int TURN_THRESHOLD=35;//뒤돌 때 임계치
    final int CORRECTION=360;//보정
    final int ALLOW=4;//임계치를 넘는것을 허용하는 횟수
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

        int a=last+THRESHOLD;
        int b=last-THRESHOLD;
        int c, d;
        int warningCnt=0;

        if(last < 180){
            c = last + 180 + TURN_THRESHOLD;
            d = last + 180 - TURN_THRESHOLD;
        }else{
            c = last - 180 + TURN_THRESHOLD;
            d = last - 180 - TURN_THRESHOLD;
        }

        if(270<=last && last < 360){ // 4사분면
            // 방향을 전환하지 않음
            if((a <= 360 && b <=ori && ori < a) || // a <= 360
                    (a > 360 && ((b<=ori&&ori<=360) || (0<=ori&&ori<=(a-CORRECTION))))){ // a > 360
//                direction.setText("직진");
            }else{ // 방향을 전환 함
                if(last - 180 + TURN_THRESHOLD <= ori && ori < last){
                    // 왼쪽
                    direction.setText("왼쪽");
                }else if((0 <= ori && ori < last - 180 - TURN_THRESHOLD) || (last <= ori && ori < 360)){
                    // 오른쪽
                    direction.setText("오른쪽");
                }else{
                    // 뒤로 돌았음
                    direction.setText("뒤로 돌았음");
                }
            }
        }else if(0 <= last && last < 90){ // 1사분면
            // 방향을 전환하지 않음
            if((b > 0 && b <= ori && ori < a) || // b > 0
                    (b <= 0 && (((b + CORRECTION) <= ori && ori <= 360) || (0 <= ori && ori <= a)))) { // b <= 0
//                direction.setText("직진");
            }
            else{ // 방향을 전환 함
                if(last <= ori && ori < last + 180 - TURN_THRESHOLD){
                    // 오른쪽
                    direction.setText("오른쪽");
                }else if((last + 180 + TURN_THRESHOLD <= ori && ori < 360) || (0 <= ori && ori < last)){
                    // 왼쪽
                    direction.setText("왼쪽");
                }else{
                    // 뒤로 돌았음
                    direction.setText("뒤로 돌았음");
                }
            }
        }else if(90 <= last && last < 180){ //2사분면
            if(b<=ori && ori<=a){ // 방향을 전환하지 않음
//                direction.setText("직진");
            }else{ // 방향을 전환 함
                if(last <= ori && ori < last + 180 - TURN_THRESHOLD){
                    // 오른쪽
                    direction.setText("오른쪽");
                }else if((c>360 && c - 360 <= ori && ori < last) ||
                        (c<360 && ((c <= ori && ori < 360) || (0 <= ori && ori < last)))){
                    // 왼쪽
                    direction.setText("왼쪽");
                }else{
                    // 뒤로 돌았음
                    direction.setText("뒤로 돌았음");
                }
            }
        }else if(180 <= last && last < 270){ // 3사분면
            if(b<=ori && ori<=a){ // 방향을 전환하지 않음
//                direction.setText("직진");
            }else{ // 방향을 전환 함
                if((d<0 && last < ori && ori <= d + 360) ||
                        (d>0 && ((0 <= ori && ori < d) || (last <= ori && ori <360)))){
                    // 오른쪽
                    direction.setText("오른쪽");
                }else if(last - 180 + TURN_THRESHOLD <= ori && ori < last){
                    // 왼쪽
                    direction.setText("왼쪽");
                }else{
                    // 뒤로 돌았음
                    direction.setText("뒤로 돌았음");
                }
            }
        }else{
            Toast.makeText(this, "속해있지 않은 범위값, 범위 지정 오류", Toast.LENGTH_SHORT).show();
        }

        last=ori;

    }

    /*
    현재 방향 정보 반환 함수

    1. pre와 now의 부호가 다를경우 && pre와 ori 둘 중 하나의 값이 90~180 과 -90~-180사이에 있는 경우
       제어 필요
    2. pre와 now의 부호가 같은경우 그대로 적용 가능

     */

//    public String checkHappiness(int now){
//
//        String dir="";//현재 방향정보
//        String str=step.getText().toString();
//        int stepCheck=Integer.parseInt(str);
//
//            pre=now;
//
//                int check=Math.abs(pre-now);
//
//                //부호 체크
//                if(pre*now>0&&pre!=0&&now!=0){ //부호가 같은 경우
//                    //임계치 확인
//                    if(check<=THRESHOLD){
//                        //방향을 변동하지 않음
//                        if(warningCount>0){
//                            warningCount--;
//                        }
//                    }else{
//                        //방향 변동 flag ON
//                        warningCount++;
//                        if(warningCount>ALLOW){//허용 오차를 넘었을 경우 =>방향 변동 인식
//                            //오른쪽, 왼쪽 판단
//                            warningCount=0;//초기화
//                            if(pre<now){
//                                //오른쪽
//                                direction.setText("오른쪽");
//                                dir="오른쪽";
//                            }else{
//                                //왼쪽
//                                direction.setText("왼쪽");
//                                dir="왼쪽";
//                            }
//                        }
//                    }
//                }else if(pre*now<0&&pre!=0&&now!=0){
//                    //부호가 다른 경우
//                    if( (90<=now&&now<=180) && (-180<=pre&& pre<=90)){
//                        //음수값을 양수로 보정
//                        pre=pre+CORRECTION;
//                        check=Math.abs(pre-now);
//                        //임계치 확인
//                        if(check<=THRESHOLD){
//                            //방향을 변동하지 않음
//                            if(warningCount>0){
//                                warningCount--;
//                            }
//                        }else {//방향 변동 flag ON
//                            warningCount++;
//                            if (warningCount > ALLOW) {//허용 오차를 넘었을 경우 =>방향 변동 인식
//                                //무조건 왼쪽
//                                direction.setText("왼쪽");
//                                dir="왼쪽";
//                            }
//                        }
//                    }else if((90<=pre&&pre<=180) && (-180<=now&& now<=90) ){
//                        //음수값을 양수로 보정
//                        now=now+CORRECTION;
//                        check=Math.abs(pre-now);
//                        //임계치 확인
//                        if(check<=THRESHOLD){
//                            //방향을 변동하지 않음
//                            if(warningCount>0){
//                                warningCount--;
//                            }
//                        }else {//방향 변동 flag ON
//                            warningCount++;
//                            if (warningCount > ALLOW) {//허용 오차를 넘었을 경우 =>방향 변동 인식
//                                //무조건 오른쪽
//                                direction.setText("오른쪽");
//                                dir="오른쪽";
//                            }
//                        }
//                    }
//
//                }
//
//        return dir;//현재 방향정보를 return
//    }

    public void onStart(View view) {
        stepCheck.startSensor();
        mOrientation.startSensor();
    }

    public void onFinish(View view) {
        stepCheck.endSensor();
        stepCheck.resetStep();
        mOrientation.endSensor();
    }

    public String checkDir(){
        String dir="";
        return dir;
    }
}