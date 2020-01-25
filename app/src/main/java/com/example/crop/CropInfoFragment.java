package com.example.crop;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.example.crop.SupportLibrary.HeavyLifter;
import com.karan.churi.PermissionManager.PermissionManager;

public class CropInfoFragment extends Fragment {
    private ImageView imageView;
    private TextView cropTitle, cropDetails;
    private PermissionManager permission;
    private String cropType = null;
    private byte[] imageData = null;
    private int rotation = 0;

    public CropInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crop_info, container, false);

        permission = new PermissionManager() {};
        permission.checkAndRequestPermissions(getActivity());
        imageView = view.findViewById(R.id.crop_photo);
        cropDetails = view.findViewById(R.id.crop_details);
        cropTitle = view.findViewById(R.id.crop_title);
        try {
            cropType = getArguments().getString("CROPTYPE");
            imageData = getArguments().getByteArray("IMAGE");
            rotation = getArguments().getInt("ROTATION");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (cropType != null) {
            cardUI();
        }
        if (imageData != null) {
            HeavyLifter.CompletionListener completionListener = new HeavyLifter.CompletionListener() {
                @Override
                public void onCompletion(String outputResult) {
                    cropType = outputResult;
                    cardUI();
                }
            };
            new HeavyLifter(getActivity(), completionListener, imageData, rotation);
        }
        return view;
    }

    void UIsetter(Drawable cropImage, String CropTitle, int resID) {
        imageView.setImageDrawable(cropImage);
        cropTitle.setText(CropTitle);
        cropDetails.setText(resID);
    }
    private void cardUI() {
        switch (cropType) {
            case "Wheat":
                UIsetter(AppCompatResources.getDrawable(getActivity(), R.drawable.wheat), cropType, R.string.wheat);
                break;
            case "Rice (Paddy)":
                UIsetter(AppCompatResources.getDrawable(getActivity(), R.drawable.rice), cropType, R.string.rice);
                break;
            case "Maize (Corn)":
                UIsetter(AppCompatResources.getDrawable(getActivity(), R.drawable.maize), cropType, R.string.maize);
                break;
            case "Mustard":
                UIsetter(AppCompatResources.getDrawable(getActivity(), R.drawable.mustard), cropType, R.string.mustard);
                break;
            case "Cotton":
                UIsetter(AppCompatResources.getDrawable(getActivity(), R.drawable.cotton), cropType, R.string.cotton);
                break;
            case "Potato":
                UIsetter(AppCompatResources.getDrawable(getActivity(), R.drawable.potato), cropType, R.string.potato);
                break;
            case "Sugarcane":
                UIsetter(AppCompatResources.getDrawable(getActivity(), R.drawable.sugarcane), cropType, R.string.sugarcane);
                break;
            case "Tea":
                UIsetter(AppCompatResources.getDrawable(getActivity(), R.drawable.tea), cropType, R.string.tea);
                break;
            case "Tomato":
                UIsetter(AppCompatResources.getDrawable(getActivity(), R.drawable.tomato), cropType, R.string.tomato);
                break;
            case "Low Confidence":
                UIsetter(AppCompatResources.getDrawable(getActivity(), R.drawable.crops), cropType, R.string.low);
            case "Unknown":
                UIsetter(AppCompatResources.getDrawable(getActivity(), R.drawable.crops), cropType, R.string.unknown);
                break;
            default:
                Toast.makeText(getActivity(), "Add this category in UIsetter", Toast.LENGTH_SHORT).show();
                cropTitle.setText(cropType);
                Toast.makeText(getActivity(), "Add this category in UIsetter", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permission.checkResult(requestCode, permissions, grantResults);
    }

}
