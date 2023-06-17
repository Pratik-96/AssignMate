package com.example.assignmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import com.example.assignmate.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        new Handler().postDelayed((Runnable) new Runnable() {
            @Override
            public void run() {
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
                binding.moto.setText("Share, Collaborate, Excel!");
            }
        },1300);
    }
}