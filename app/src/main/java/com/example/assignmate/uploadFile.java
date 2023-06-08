package com.example.assignmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.assignmate.databinding.ActivityMainBinding;
import com.example.assignmate.databinding.ActivityUploadFileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class uploadFile extends AppCompatActivity {

ProgressDialog dialog;
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

          private void upload(Uri uri) {
          }
      });





    }

    public String getFileName(Uri uri)
    {
        String fileName;
        if (uri.getScheme().equals("file")) {
            fileName = uri.getLastPathSegment();
        } else {
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(uri, new String[]{
                        MediaStore.Images.ImageColumns.DISPLAY_NAME
                }, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)+0);
                    return fileName;
                }
            } finally {

                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return "No file Selected";
    }
//
//   private void upload(Uri uri) {          // Uploads the file
////
////        dialog = new ProgressDialog(this);
////        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
////        dialog.setTitle("Uploading File...");
////        dialog.setProgress(0);
////        dialog.show();
//        StorageReference storageReference = storage.getReference();         //Get Reference returns root    path
//        String selectedSub = binding.spinner.getSelectedItem().toString();
//        storageReference.child(selectedSub).child(getFileName(uri)).getFile(uri)
//                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//
//
//                    String link = taskSnapshot.getStorage().getDownloadUrl().toString();
//                        DatabaseReference reference = database.getReference();      //Return path to root
//                        reference.child(getFileName(uri)).setValue(link).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(uploadFile.this, "Document Uploaded Successfully", Toast.LENGTH_SHORT).show();
//                                }
//                                else
//                                {
//                                    Toast.makeText(uploadFile.this, "Failed to Upload File!!", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(uploadFile.this, "Failed to Upload File!!", Toast.LENGTH_SHORT).show();
//
//                    }
//                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(@NonNull FileDownloadTask.TaskSnapshot snapshot) {
//                        int currentProgress = (int) (100*snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
//                        dialog.setProgress(currentProgress);
//                    }
//                });
//    }

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
            String fileName = getFileName(uri);
            binding.selectedFile.setText(fileName);

        }
        else
        {
            Toast.makeText(this, "Please select a file to upload", Toast.LENGTH_SHORT).show();
        }
    }
}