package com.example.assignmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.assignmate.databinding.ActivityDocumentTypeBinding;

public class documentType extends AppCompatActivity {

    private ActivityDocumentTypeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityDocumentTypeBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        binding.assignmentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),fetch_files.class));
            }
        });
                binding.practicalCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), fetch_files.class));
                    }
                });
                binding.paperCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), fetch_files.class));
                    }
                });

    }
}