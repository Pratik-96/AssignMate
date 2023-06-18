package com.example.assignmate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.assignmate.databinding.ActivityFetchFilesBinding;

public class fetch_files extends AppCompatActivity {

    private ActivityFetchFilesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityFetchFilesBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
//TODO Populate recycler view and create document design xml file and design the download file card.
    }
}