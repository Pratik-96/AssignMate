package com.example.assignmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.PaintKt;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.assignmate.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    FirebaseAuth mAuth;
    public static final String SUBJECT_NAME ="";

    FirebaseDatabase database;

    public void replaceFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 99 && !(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            Toast.makeText(this, "Please grant notification permission to AssignMate!!", Toast.LENGTH_SHORT).show();
            // Open app settings to allow the user to manually grant notification permission
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }

        if (requestCode==100 && !(grantResults[0] == PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);

            Toast.makeText(getApplicationContext(), "Please grant the permission to use AssignMate!!", Toast.LENGTH_SHORT).show();
        }

    }



    private void checkNotificationPermission() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
        boolean isNotificationEnabled = notificationManager.areNotificationsEnabled();

        if (!isNotificationEnabled) {
            Toast.makeText(getApplicationContext(), "Please allow notification permission", Toast.LENGTH_LONG).show();

            // Open app settings to allow the user to manually grant notification permission
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);
        }
    }



    public boolean chk_Maintenance()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final boolean[] Maintenance = new boolean[1];
        reference.child("Maintenance").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override

            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {

                    Boolean inMaintenance = (Boolean)(task.getResult().getValue());
                    if (inMaintenance)
                    {
                        startActivity(new Intent(getApplicationContext(), Maintenance.class));
                        finish();
                        Maintenance[0] =true;
                    }
                    else
                    {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        Maintenance[0] =false;
                    }

                }

            }
        });
        return Maintenance[0];


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            String name = getIntent().getStringExtra("uname");
            if (name!=null)
            {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();
                FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Log.d("uname", "Name updated..: ");
                        }
                    }
                });
            }
        }
        replaceFragment(new HomeFragment());


        checkNotificationPermission();
        mAuth=FirebaseAuth.getInstance();
        binding.navBar.setItemSelected(R.id.home_nav,true);
        binding.navBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                int selectedItem = binding.navBar.getSelectedItemId();
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
//
//                Log.d("TAG", "onItemSelected: "+selectedItem+"      "+i);
                if (FirebaseAuth.getInstance().getCurrentUser() != null || account != null) {
                    if (selectedItem == R.id.home_nav) {
                        replaceFragment(new HomeFragment());
                    } else if (selectedItem == R.id.upload_nav) {
//                    startActivity(new Intent(getApplicationContext(), uploadFile.class));
                        replaceFragment(new UploadFragment());


                    } else if (selectedItem == R.id.profile_nav) {
                        replaceFragment(new Profile());
                    }

                } else {
                    Toast.makeText(MainActivity.this, "You've been logged out, Please Login Again to continue..", Toast.LENGTH_SHORT).show();
                }
            }
        });

//
//        database = FirebaseDatabase.getInstance();
//
//        mAuth = FirebaseAuth.getInstance();
//
//        GoogleSignInAccount account  = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
//        Log.d("TAG", account.getId());
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                binding.progressBar.setVisibility(View.GONE);
//                binding.cards.setVisibility(View.VISIBLE);
//
//            }
//        },1000);
//
//        checkNotificationPermission();
//
//        if (!(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED))
//        {
//            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
//        }
//
//

//
//
//


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        FirebaseMessaging.getInstance().subscribeToTopic("all");
                        // Log and toast

                        Log.d("Token", token);
                    }
                });








    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                 .setMessage("Are you sure you want to exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        moveTaskToBack(true);
                        MainActivity.this.finish();

                    }
                }).setNegativeButton("No",null).show();

    }
}

