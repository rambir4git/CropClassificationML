package com.example.crop.POJOs;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crop.R;

import java.util.List;

public class HomeCardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SavedImages> savedImagesList;
    private ClickListener mListener;
    private ButtonClickListener bListener;

    public HomeCardsAdapter(List<SavedImages> savedImagesList, ClickListener clickListener, ButtonClickListener buttonClickListener) {
        this.mListener = clickListener;
        this.savedImagesList = savedImagesList;
        this.bListener = buttonClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else
            return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new DummyCardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dummy_card, parent, false));
        else
            return new SavedImagesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_image_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        Log.d("Adapter", "onBindViewHolder: " + position);
        if (viewHolder.getItemViewType() == 0) {
            final DummyCardViewHolder holder = (DummyCardViewHolder) viewHolder;
            holder.galleryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bListener.onButtonClicked(ButtonClickListener.GALLERYBUTTON);
                }
            });
            holder.cameraBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bListener.onButtonClicked(ButtonClickListener.CAMERABUTTON);
                }
            });
        } else {
            final SavedImagesViewHolder holder = (SavedImagesViewHolder) viewHolder;
            holder.currentItem = savedImagesList.get(position - 1);
            holder.cropImage.setImageBitmap(savedImagesList.get(position - 1).getImage());
            holder.location.setText(savedImagesList.get(position - 1).getLocation());
            holder.cropType.setText(savedImagesList.get(position - 1).getCropType());
            holder.date.setText(savedImagesList.get(position - 1).getDate());

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(holder.currentItem);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return savedImagesList.size() + 1;
    }

    public interface ClickListener {
        void onItemClicked(SavedImages savedImage);
    }

    public interface ButtonClickListener {
        int CAMERABUTTON = 101;
        int GALLERYBUTTON = 201;

        void onButtonClicked(int id);
    }

    public class SavedImagesViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public SavedImages currentItem;
        private ImageView cropImage;
        private TextView cropType, location, date;

        public SavedImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            this.cropImage = itemView.findViewById(R.id.crop_image);
            this.cropType = itemView.findViewById(R.id.crop_string);
            this.date = itemView.findViewById(R.id.crop_date);
            this.location = itemView.findViewById(R.id.location_string);
        }
    }

    public class DummyCardViewHolder extends RecyclerView.ViewHolder {
        public Button cameraBtn, galleryBtn;

        public DummyCardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cameraBtn = itemView.findViewById(R.id.pictureBtn);
            this.galleryBtn = itemView.findViewById(R.id.home_galleryBtn);
        }
    }
}
