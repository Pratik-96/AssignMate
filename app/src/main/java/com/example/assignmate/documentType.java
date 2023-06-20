package com.example.assignmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.assignmate.databinding.ActivityDocumentTypeBinding;

public class documentType extends AppCompatActivity {

    private ActivityDocumentTypeBinding binding;
    public static final String SUBJECT_NAME = "";
    public static final String DOCUMENT_TYPE = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityDocumentTypeBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        String selectedsub = getIntent().getStringExtra(SUBJECT_NAME);


        binding.assignmentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), fetch_files.class);
                Bundle bundle = new Bundle();
               bundle.putString("docType","Assignments");
               bundle.putString("name",selectedsub);
               intent.putExtras(bundle);
                startActivity(intent);
            }
        });
                binding.practicalCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), fetch_files.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("docType","Practicals");
                        bundle.putString("name",selectedsub);
                        intent.putExtras(bundle);
                        startActivity(intent);                   }
                });
                binding.paperCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), fetch_files.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("docType","Question Papers");
                        bundle.putString("name",selectedsub);
                        intent.putExtras(bundle);
                        startActivity(intent);               }
                });

    }
}