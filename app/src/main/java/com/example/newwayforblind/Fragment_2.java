package com.example.newwayforblind;


import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_2 extends Fragment {

    LinearLayout linearLayout;
    private TextToSpeech tts;

    public Fragment_2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_2, container, false);

        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.KOREA);
            }
        });

        ImageView imageView = (ImageView)view.findViewById(R.id.tutorial);
        int img_tutorial = R.drawable.img_tutorial2;
        Glide.with(this).load(img_tutorial).into(imageView);

        linearLayout = (LinearLayout)view.findViewById(R.id.linearLayout);
        linearLayout.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeTop() {
                tts.speak("왼쪽 스와이프로 길안내 페이지로 이동할 수 있습니다.", TextToSpeech.QUEUE_ADD, null, null);
                tts.speak("해당 페이지에서는 Up 스와이프로 출발지 / 도착지 입력을 할 수 있으며,", TextToSpeech.QUEUE_ADD, null, null);
                tts.speak("Down 스와이프로 네비게이션을 실행시킬 수 있습니다.", TextToSpeech.QUEUE_ADD, null, null);
            }
            public void onSwipeBottom() {
                tts.speak("왼쪽 스와이프로 길안내 페이지로 이동할 수 있습니다.", TextToSpeech.QUEUE_ADD, null, null);
                tts.speak("해당 페이지에서는 Up 스와이프로 보폭측정을 시작할 수 있으며", TextToSpeech.QUEUE_ADD, null, null);
                tts.speak("Down 스와이프로 보폭측정을 종료할 수 있습니다.", TextToSpeech.QUEUE_ADD, null, null);
            }
        });

        return view;
    }

}
