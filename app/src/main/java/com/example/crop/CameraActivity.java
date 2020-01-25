package com.example.crop;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Mode;

import de.hdodenhof.circleimageview.CircleImageView;

public class CameraActivity extends AppCompatActivity {
    private CameraView cameraView;
    private ProgressBar progressBar;
    private CircleImageView imageView;
    public FloatingActionButton startBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        startBtn = findViewById(R.id.startBtn);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
        cameraView = findViewById(R.id.cameraView);
        cameraView.setMode(Mode.PICTURE);
        cameraView.setUseDeviceOrientation(true);
        cameraView.setLifecycleOwner(this);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.takePicture();
            }
        });
        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                Bundle args = new Bundle();
                args.putByteArray("IMAGE", result.getData());
                args.putInt("ROTATION", result.getRotation());
                CropInfoFragment cropInfoFragment = new CropInfoFragment();
                cropInfoFragment.setArguments(args);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.camera_frame, cropInfoFragment).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.camera_frame, new GalleryFragment()).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

}

