package com.example.newwayforblind;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class CustomDialog extends Dialog {
    private static final int LAYOUT = R.layout.dialog_custom;

    private Context context;
    private TextView msg;
    private Animation blink;

    public CustomDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        blink = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        msg = (TextView)findViewById(R.id.msg);
        msg.startAnimation(blink);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                dismiss();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 5000);
    }
}
