package com.example.assignmate.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.window.SplashScreen;

import com.example.assignmate.databinding.ActivityNoConnectionBinding;
import com.example.assignmate.splash;

import java.net.InetAddress;

public class no_connection extends AppCompatActivity {

    ActivityNoConnectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNoConnectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inProgress(true);
                if (checkConnection(getApplicationContext()))
                {
                    startActivity(new Intent(getApplicationContext(), splash.class));
                    inProgress(false);
                    finish();
                }
                else
                {
                    inProgress(false);
                }
            }
        });
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
    public void inProgress(boolean progress)
    {
        if (progress)
        {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.retry.setVisibility(View.GONE);
        }
        else
        {
            binding.progressBar.setVisibility(View.GONE);
            binding.retry.setVisibility(View.VISIBLE);
        }
    }
}