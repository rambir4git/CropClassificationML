package com.example.crop;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    private TextView signUp;
    private ImageView back;
    private Button email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,RegisterActivity.class));
            }
        });

    }

    private void init() {
        signUp = findViewById(R.id.signUp);
        back = findViewById(R.id.back_arrow);
        email = findViewById(R.id.button3);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignInActivity.this,MainActivity.class));
        finish();
    }
}
