package com.example.newwayforblind;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class OrientationTestActivity extends AppCompatActivity {

    private StepCheck stepCheck;
    private Handler mHandler;
    private Orientation mOrientation;

    private TextView step;
    private TextView orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation_test);

        step = (TextView) findViewById(R.id.step);
        orientation = (TextView) findViewById(R.id.orientation);

        mOrientation = new Orientation(getApplicationContext());
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d("Handler", "");
                step.setText(stepCheck.getStep() + "");
                orientation.setText(mOrientation.getOrientation() + "");
            }
        };
        stepCheck = new StepCheck(getApplicationContext(), mHandler);
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
