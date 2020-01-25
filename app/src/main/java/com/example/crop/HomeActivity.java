package com.example.crop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.crop.SupportLibrary.ExportToEmail;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private FloatingActionButton camera;
    private HomeFragment homeFragment;
    private CommunityFragment communityFragment;
    private ProfileFragment profileFragment;
    private FirebaseAuth homeAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        camera = findViewById(R.id.floatingActionButton);


        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cropify");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);

        // FRAGMENTS
        homeFragment = new HomeFragment();
        communityFragment = new CommunityFragment();
        profileFragment = new ProfileFragment();
        homeAuth = FirebaseAuth.getInstance();
        ChangeFragment(homeFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        ChangeFragment(homeFragment);
                        return true;
                    case R.id.community:
                        ChangeFragment(communityFragment);
                        return true;
                    case R.id.profile:
                        ChangeFragment(profileFragment);
                        return true;
                    default:
                        return false;
                }

            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                if (ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)){
                        ActivityCompat.requestPermissions(HomeActivity.this,
                                new String[]{Manifest.permission.CAMERA}, 1);
                    }else{
                        ActivityCompat.requestPermissions(HomeActivity.this,
                                new String[]{Manifest.permission.CAMERA}, 1);
                    }
                }else

                 */
                startActivity(new Intent(HomeActivity.this, CameraActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        ChangeFragment(homeFragment);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_logout){
            homeAuth.signOut();
            startActivity(new Intent(HomeActivity.this,MainActivity.class));
            finish();
        }
        if (item.getItemId() == R.id.menu_export) {
            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Export Alert !!")
                    .setMessage("Would you like to export all the images via Email ?")
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new ExportToEmail(HomeActivity.this).execute();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            alertDialog.show();
        } else {
              startActivity(new Intent(HomeActivity.this,WebViewActivity.class));
        }
            return super.onOptionsItemSelected(item);
    }


    private void ChangeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frames,fragment);
        fragmentTransaction.commit();
    }

}
