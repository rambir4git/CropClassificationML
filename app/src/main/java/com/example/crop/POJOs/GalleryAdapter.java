package com.example.crop.POJOs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crop.R;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryImagesViewHolder> {

    private List<SavedImages> savedImagesList;
    private ClickListener mListener;

    public GalleryAdapter(List<SavedImages> savedImagesList, ClickListener clickListener) {
        this.mListener = clickListener;
        this.savedImagesList = savedImagesList;
    }

    @NonNull
    @Override
    public GalleryImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GalleryImagesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryImagesViewHolder holder, int position) {
        holder.currentItem = savedImagesList.get(position);
        holder.imageView.setImageBitmap(savedImagesList.get(position).getImage());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(holder.currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return savedImagesList.size();
    }

    public interface ClickListener {
        void onItemClicked(SavedImages savedImage);
    }

    public class GalleryImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        SavedImages currentItem;

        public GalleryImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.galleryImage);
        }
    }
}
