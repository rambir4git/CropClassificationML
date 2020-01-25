package com.example.crop;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText email,pass;
    private Button login;
    private TextView register,skip;
    private FirebaseAuth mainAuth;
    private ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        mainAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null) {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e = email.getText().toString();
                String p = pass.getText().toString();

                signIn(e,p);


            }
        });


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SliderActivity.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignInActivity.class));
                finish();
            }
        });


    }

    private void signIn(String e, String p) {

        // Validations
        if (e.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
            email.setError("Invalid Email");
            email.requestFocus();
            return;
        }
        if (p.isEmpty()) {
            pass.setError("Password is required");
            pass.requestFocus();
            return;
        }
        if (p.length() < 8) {
            pass.setError("Minimum length should be 8");
            pass.requestFocus();
            return;
        }
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        mainAuth.signInWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (!task.isSuccessful()) {
                    print(task.getException().getMessage());
                }
            }
        });
    }

    private void print(String message) {

        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    private void init(){
        email = findViewById(R.id.emailID);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        skip = findViewById(R.id.skip);
    }

}
