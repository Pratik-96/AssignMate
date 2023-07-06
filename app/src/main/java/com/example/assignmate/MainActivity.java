package com.example.assignmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.PaintKt;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.assignmate.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    FirebaseAuth mAuth;
    public static final String SUBJECT_NAME ="";

    FirebaseDatabase database;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(currentUser == null || account==null){
            Intent log=new Intent(getApplicationContext(), Login.class);
            startActivity(log);
            finish();
        }
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                binding.progressBar.setVisibility(View.GONE);
                binding.cards.setVisibility(View.VISIBLE);

            }
        },1000);

        checkNotificationPermission();

        if (!(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
        }


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
                        FirebaseMessaging.getInstance().subscribeToTopic("all");
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
                menu.getMenu().add("Update AssignMate");
                menu.getMenu().add("Logout");

                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle()=="Logout")
                        {
                            mAuth.signOut();
                            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                            GoogleSignInClient gsc = GoogleSignIn.getClient(getApplicationContext(),gso);
                            gsc.signOut();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            finish();
                        }
                        if (menuItem.getTitle()=="About AssignMate")
                        {
                            startActivity(new Intent(getApplicationContext(), aboutAssignMate.class));

                        }
                        if (menuItem.getTitle().equals("Update AssignMate"))
                        {
                            DatabaseReference reference = database.getReference();

                            reference.child("Updates").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful())
                                    {
                                        Uri url = Uri.parse(task.getResult().getValue().toString());
                                       Intent intent = new Intent(Intent.ACTION_VIEW,url);
                                        Toast.makeText(getApplicationContext(), "Downloading New Update..", Toast.LENGTH_SHORT).show();
                                       startActivity(intent);
                                    }
                                }
                            });


                        }


                        return false;
                    }
                });








            }
        });
        Calendar c = Calendar.getInstance();
        int hrs = c.get(Calendar.HOUR_OF_DAY);

        if (hrs>=1 && hrs<12)
        {
            binding.greet.setText("Good morning");
        } else if (hrs>12 && hrs<18) {
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
