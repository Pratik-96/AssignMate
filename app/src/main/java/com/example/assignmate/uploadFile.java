package com.example.assignmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.assignmate.databinding.ActivityMainBinding;
import com.example.assignmate.databinding.ActivityUploadFileBinding;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class uploadFile extends AppCompatActivity {


 ActivityUploadFileBinding binding;
FirebaseStorage storage;
FirebaseDatabase database;

Uri uri;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            select_pdf();
        } else {
            Toast.makeText(this, "Please Grant Permissions to use the App..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUploadFileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        storage = FirebaseStorage.getInstance();    // Returns an object of Firebase storage
        database=FirebaseDatabase.getInstance();
        String[] subjects ={"Subject 1","Subject 2","Subject 3","Subject 4"};
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.subjects, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        binding.spinner.setAdapter(adapter);

      binding.selectFile.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if (ContextCompat.checkSelfPermission(uploadFile.this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
              {
                  select_pdf();
              }
              else
              {
                  ActivityCompat.requestPermissions(uploadFile.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
              }
          }
      });


      binding.upload.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if (uri!=null)
              {
                  upload(uri);
              }
              else
              {
                  Toast.makeText(uploadFile.this, "Please Select a File..", Toast.LENGTH_SHORT).show();
              }
          }
      });





    }

    private void upload(Uri uri) {
        StorageReference storageReference = storage.getReference();
    }

    private void select_pdf() {
        Intent intent= new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,8);
                
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8 && resultCode == RESULT_OK && data != null) {
            uri=data.getData();     //return data of selected file

        }
        else
        {
            Toast.makeText(this, "Please select a file to upload", Toast.LENGTH_SHORT).show();
        }
    }
}