package com.example.newwayforblind;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnFindRoad(View view) {
        Intent intent = new Intent(this, NFindRoadActivity.class);
        startActivity(intent);
    }

    public void onSetStep(View view) {
        Intent intent = new Intent(getApplicationContext(), SetStepActivity.class);
        startActivity(intent);
    }
}
