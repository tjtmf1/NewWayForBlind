package com.example.newwayforblind;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    ViewPagerAdapter adapter;
    ViewPager viewPager;

    Fragment_1 fragment_1;
    Fragment_2 fragment_2;
    MapFragment fragment_3;

    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        setBottomNavigation();
        setViewPager();

        bottomNavigation.setSelectedItemId(R.id.action_fragment_2);
    }

    public void initialize() {
        bottomNavigation = (BottomNavigationView)findViewById(R.id.bottomNavigation);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        fragment_1 = new Fragment_1();
        fragment_2 = new Fragment_2();
        fragment_3 = new MapFragment();
    }

    public void setBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_fragment_1:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.action_fragment_2:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.action_fragment_3:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });
    }

    public void setViewPager() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else {
                    bottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        adapter.addFragment(fragment_1);
        adapter.addFragment(fragment_2);
        adapter.addFragment(fragment_3);
        viewPager.setAdapter(adapter);
    }

    public void btnFindRoad(View view) {
        Intent intent = new Intent(this, NFindRoadActivity.class);
        startActivity(intent);
    }

    public void onSetStep(View view) {
        Intent intent = new Intent(getApplicationContext(), SetStepActivity.class);
        startActivity(intent);
    }

    public void onOrientationTest(View view) {
        Intent intent = new Intent(getApplicationContext(), OrientationTestActivity.class);
        startActivity(intent);
    }
}
