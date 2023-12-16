package com.example.assignmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.assignmate.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    FirebaseAuth mAuth;
    public static final String SUBJECT_NAME ="";
    String user ;

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
                        startActivity(new Intent(getApplicationContext(), com.example.assignmate.authentication.Maintenance.class));
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
        
        
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            user = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        } else if (account != null) {
            user = account.getId().toString();
        }

        DatabaseReference semesterRef = FirebaseDatabase.getInstance().getReference();
        semesterRef.child("Users").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {


                            if (snapshot1.child("uid").getValue().equals(user)) {

                                if (snapshot1.child("sem").getValue().equals("Semester 1"))
                                {
                                    message("Semester_1");

                                } else if (snapshot1.child("sem").getValue().equals("Semester 2")) {
                                    message("Semester_2");
                                } else if (snapshot1.child("sem").getValue().equals("Semester 3")){
                                    message("Semester_3");
                                    
                                } else if (snapshot1.child("sem").getValue().equals("Semester 4")) {
                                    message("Semester_4");
                                    
                                } else if (snapshot1.child("sem").getValue().equals("Semester 5")) {
                                    message("Semester_5");
                                } else if (snapshot1.child("sem").getValue().equals("Semester 6")) {
                                    message("Semester_6");

                                }


                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );



        








    }
    
    public void message(String Semester)
    {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        FirebaseMessaging.getInstance().subscribeToTopic(Semester);
                        // Log and toast

                        Log.d("Token", token+"  "+Semester);
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

