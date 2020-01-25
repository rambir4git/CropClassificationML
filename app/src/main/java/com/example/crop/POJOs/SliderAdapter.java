package com.example.crop.POJOs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.crop.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    // static data
    public int[] slide_images = {
            R.drawable.first,
            R.drawable.second,
            R.drawable.third
    };

    public String[] slider_heading = {
            "Find out what's happening to your plant",
            "Get in touch and exchange with your experts",
            "Receive recommendations on how to grow your crop"
    };

    public String[] slider_desc = {
            "Take a picture of a plant and Plantix will  provide you the instant solutions.",
            "The Plantix Community is your doorway to farming experts around the world.",
            "The crop advisory give you a series of recommendations on how to grow your crops." +
                    "It will support you in your decisions during the crop cycles so that you get" +
                    "the best possible yields."
    };


    public SliderAdapter(Context context) {
        this.context = context;
    }



    @Override
    public int getCount() {
        return slider_heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

        TextView heading = view.findViewById(R.id.slide_heading);
        TextView desc = view.findViewById(R.id.slide_desc);
        CircleImageView imageView = view.findViewById(R.id.slide_img);

        heading.setText(slider_heading[position]);
        desc.setText(slider_desc[position]);
        imageView.setImageResource(slide_images[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
