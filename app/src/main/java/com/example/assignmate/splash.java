package com.example.assignmate;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;


import com.example.assignmate.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.InetAddress;

public class splash extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = FirebaseAuth.getInstance().getCurrentUser();


        binding.moto.setVisibility(View.GONE);
        new Handler().postDelayed((Runnable) new Runnable() {
            @Override
            public void run() {
                if (checkConnection(getApplicationContext()))
                {
                    if (user!=null)
                    {
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(getApplicationContext(), SignUp.class));
                        finish();
                    }
                }
                else
                {
                        startActivity(new Intent(getApplicationContext(), no_connection.class));
                        finish();
                }


            }
        },2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.appname.setText("A");
            }
        },300);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.appname.append("S");
            }
        },400);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.appname.append("S");
            }
        },500);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.appname.append("I");
            }
        },600);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.appname.append("G");
            }
        },700);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.appname.append("N");
            }
        },800);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.appname.append("M");
            }
        },900);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.appname.append("A");
            }
        },1000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.appname.append("T");
            }
        },1100);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.appname.append("E");
            }
        },1200);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                binding.moto.setText("Assignment Solutions at\n Your Fingertips!!");
            }
        },1300);
    }
    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;
    }
}