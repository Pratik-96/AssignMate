package com.example.assignmate.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.assignmate.databinding.ActivityAboutAssignMateBinding;
import com.example.assignmate.databinding.ActivityNoConnectionBinding;
import com.google.firebase.BuildConfig;

public class aboutAssignMate extends AppCompatActivity {


    String version;
    ActivityAboutAssignMateBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAboutAssignMateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try{
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),0);
             version = info.versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        binding.version.setText("Version "+version);
    }
}