package com.example.newwayforblind;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        int img_intro = R.drawable.img_intro;
        Glide.with(this).load(img_intro).into(imageView);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                task();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 3000);
    }

    public void task() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
