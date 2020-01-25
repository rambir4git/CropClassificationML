package com.example.crop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.crop.POJOs.SliderAdapter;

public class SliderActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout dotLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] dots;
    private Button next,prev;
    private int currPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        viewPager = findViewById(R.id.slider_viewpager);
        dotLayout = findViewById(R.id.slider_dots);
        next = findViewById(R.id.next_slide);
        prev = findViewById(R.id.prev_slide);
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        viewPager.addOnPageChangeListener(listener);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currPage == 2){
                  startActivity(new Intent(SliderActivity.this,LocationActivity.class));
                  finish();
                } else
                viewPager.setCurrentItem(currPage+1);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(currPage-1);
            }
        });


    }

    public void addDotsIndicator(int position){
        dots = new TextView[3];
        dotLayout.removeAllViews();
        for(int i=0;i<dots.length;++i){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.transparent_green));

            dotLayout.addView(dots[i]);
        }

        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.dark_green));
        }
    }

    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);
            currPage = i;
            if(currPage == 0) {
                next.setEnabled(true);
                next.setText("Next");
                prev.setEnabled(false);
                prev.setVisibility(View.INVISIBLE);
                prev.setText("");
            }else if(i == dots.length-1){
                next.setEnabled(true);
                next.setText("Finish");
                prev.setEnabled(true);
                prev.setVisibility(View.VISIBLE);
                prev.setText("Back");
            }else{
                next.setEnabled(true);
                next.setText("Next");
                prev.setEnabled(true);
                prev.setVisibility(View.VISIBLE);
                prev.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
