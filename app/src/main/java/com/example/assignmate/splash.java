package com.example.assignmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;


import com.example.assignmate.databinding.ActivitySplashBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import pl.droidsonroids.gif.GifDrawable;

public class splash extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    private ActivitySplashBinding binding;
    public void chk_Maintenance()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();



        mAuth=FirebaseAuth.getInstance();
        reference.child("Maintenance").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {


            @Override

            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {

                    boolean inMaintenance = (boolean) task.getResult().getValue();

                    if (Objects.equals(mAuth.getUid(), "RYmuvMvQn3WpjHpgIjDMgrazSpq1"))
                    {
                        inMaintenance=false;
                    }
                    boolean finalInMaintenance = inMaintenance;
                    new Handler().postDelayed((Runnable) new Runnable() {
                        @Override
                        public void run() {



                                if (!finalInMaintenance) {
                                    if (user != null) {
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    }
                                    else
                                    {
                                        startActivity(new Intent(getApplicationContext(), choose_sem.class));  //signup
                                        finish();
                                    }
                                }
                                else
                                {
                                    startActivity(new Intent(getApplicationContext(), Maintenance.class));
                                    finish();
                                }

                            }



                    }, 1600);


                }

            }
        });





    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = FirebaseAuth.getInstance().getCurrentUser();


        if (checkConnection(getApplicationContext())) {
            chk_Maintenance();
        }
        else
        {
            startActivity(new Intent(getApplicationContext(),no_connection.class));
            finish();
        }





//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                binding.view.setVisibility(View.VISIBLE);
//                binding.appname.setVisibility(View.VISIBLE);
//            }
//        }, 400);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                binding.appname.setText("A");
//            }
//        }, 500);
//
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                binding.appname.append("S");
//            }
//        }, 600);
//
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                binding.appname.append("S");
//            }
//        }, 700);
//
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                binding.appname.append("I");
//            }
//        }, 800);
//
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                binding.appname.append("G");
//            }
//        }, 900);
//
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                binding.appname.append("N");
//            }
//        }, 1000);
//
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                binding.appname.append("M");
//            }
//        }, 1100);
//
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                binding.appname.append("A");
//            }
//        }, 1200);
//
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                binding.appname.append("T");
//            }
//        }, 1300);
//
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                binding.appname.append("E");
//            }
//        }, 1400);
}
    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkCapabilities networkCapabilities = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());

            if (networkCapabilities != null) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true; // Connected to WiFi
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true; // Connected to mobile data
                }
            }
        }
        return false; // Not connected to any network
    }

}