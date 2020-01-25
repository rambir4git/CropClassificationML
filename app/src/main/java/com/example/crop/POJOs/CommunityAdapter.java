package com.example.crop.POJOs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crop.R;

import java.util.ArrayList;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.MyViewHolder> {

    private ArrayList<CommunityDataModel> dataSet;

    public CommunityAdapter(ArrayList<CommunityDataModel> dataSet){
        this.dataSet=dataSet;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.community_binder, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ImageView img= holder.imageView;
        img.setImageResource(dataSet.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.community_plant_image);

        }
    }
}
