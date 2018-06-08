package com.example.newwayforblind;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_2 extends Fragment {

    LinearLayout linearLayout;

    public Fragment_2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_2, container, false);

        linearLayout = (LinearLayout)view.findViewById(R.id.linearLayout);
        linearLayout.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeTop() {
                Toast.makeText(getContext(), "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(getContext(), "bottom", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
