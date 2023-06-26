package com.example.assignmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.assignmate.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    FirebaseAuth mAuth;
    public static final String SUBJECT_NAME ="";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 99 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
        else
        {
            Toast.makeText(this, "permission", Toast.LENGTH_SHORT).show();
            // Open app settings to allow the user to manually grant notification permission
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkNotificationPermission();



        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast

                        Log.d("Token", token);
                    }
                });



        if (Objects.equals(FirebaseAuth.getInstance().getUid(), "Atda2EZUKxXMNlaTQ4IyUHMVyJ02"))
        {
            binding.floatingActionButton.setVisibility(View.VISIBLE);
        }
        else {
            binding.floatingActionButton.setVisibility(View.GONE);
        }
        mAuth=FirebaseAuth.getInstance();
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu menu = new PopupMenu(getApplicationContext(),binding.menu);
                menu.getMenu().add("About AssignMate");
                menu.getMenu().add("Logout");
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle()=="Logout")
                        {
                            mAuth.signOut();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            finish();
                        }
                        if (menuItem.getTitle()=="About AssignMate")
                        {
                            startActivity(new Intent(getApplicationContext(), aboutAssignMate.class));

                        }


                        return false;
                    }
                });








            }
        });
        Calendar c = Calendar.getInstance();
        int hrs = c.get(Calendar.HOUR_OF_DAY);

        if (hrs>=1 && hrs<=12)
        {
            binding.greet.setText("Good morning");
        } else if (hrs>12 && hrs<=18) {
            binding.greet.setText("Good afternoon");
        }
        else
        {
            binding.greet.setText("Good evening");
        }

        binding.floatingActionButton.setOnClickListener(view -> uploadFile());

        binding.javaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), documentType.class);
                intent.putExtra(SUBJECT_NAME,"Programming in Java");
                startActivity(intent);
                }
        });

        binding.dsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), documentType.class);
                intent.putExtra(SUBJECT_NAME,"Data Mining & Data Science");
                startActivity(intent);
            }
        });

        binding.osCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), documentType.class);
                intent.putExtra(SUBJECT_NAME,"Principles of Operating Systems");
                startActivity(intent);
            }
        });

        binding.aiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), documentType.class);
                intent.putExtra(SUBJECT_NAME,"Artificial Intelligence");
                startActivity(intent);
            }
        });

        binding.cloudCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), documentType.class);
                intent.putExtra(SUBJECT_NAME,"Cloud Computing");
                startActivity(intent);
            }
        });

    }
    public void uploadFile()
    {
        this.startActivity(new Intent(getApplicationContext(), uploadFile.class));

    }


}
