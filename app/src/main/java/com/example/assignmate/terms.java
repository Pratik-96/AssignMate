package com.example.assignmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.assignmate.databinding.ActivityTermsBinding;

public class terms extends AppCompatActivity {

    String version;
    ActivityTermsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTermsBinding.inflate(getLayoutInflater());
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