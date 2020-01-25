package com.example.crop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends AppCompatActivity {

    private TextView skip;
    private Button allow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        allow = findViewById(R.id.allow);
        skip = findViewById(R.id.loc_skip);

        allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(LocationActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(LocationActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)){
                        ActivityCompat.requestPermissions(LocationActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }else{
                        ActivityCompat.requestPermissions(LocationActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LocationActivity.this,HomeActivity.class));
                finish();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults){
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(LocationActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }

        }
        startActivity(new Intent(LocationActivity.this,HomeActivity.class));
        finish();

    }
}
