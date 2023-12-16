package com.example.assignmate.authentication;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.assignmate.Models.sem_model;
import com.example.assignmate.R;
import com.example.assignmate.databinding.ActivityAddSemDetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class add_sem_det extends AppCompatActivity {


    ActivityAddSemDetBinding binding;

    String str;


    Uri uri;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSemDetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.semesters, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        binding.semSpinner.setAdapter(adapter);

        binding.selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    select_pdf();
                } else {
                    ActivityCompat.requestPermissions(add_sem_det.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }

            }
        });





        binding.updateSemDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedSem = binding.semSpinner.getSelectedItem().toString();
                dialog = new ProgressDialog(add_sem_det.this);
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setTitle("Uploading File...");
                dialog.setProgress(0);
                dialog.show();
                if (uri!=null) {
                    str = getFileName(uri);
                    String path = str.replaceAll("\\.", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\[", "").replaceAll("]", "").replaceAll("#", "").replaceAll("\\$", "");
                    FirebaseStorage  storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReference();
                    storageReference.child("Semesters_assets").child(selectedSem).child(getFileName(uri)).putFile(uri).
                            addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                     @Override
                                                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                         Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                         result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                             @Override
                                                             public void onSuccess(Uri uri) {
                                                                 String url = uri.toString();
                                                                 String sub_name = binding.SemesterName.getText().toString();
                                                                 sem_model model = new sem_model(sub_name,url);
                                                                 DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Semester_data")
                                                                         .child(selectedSem).push();
                                                                 reference.setValue(model)
                                                                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                             @Override
                                                                             public void onComplete(@NonNull Task<Void> task) {
                                                                                 if (task.isSuccessful()) {
                                                                                     Toast.makeText(getApplicationContext(), "File Uploaded Sucessfully!!", Toast.LENGTH_SHORT).show();
                                                                                     dialog.setTitle("File Uploaded Sucessfully!!");
                                                                                     dialog.dismiss();
                                                                                 } else {
                                                                                     Toast.makeText(getApplicationContext(), "Upload Failed!!", Toast.LENGTH_SHORT).show();
                                                                                     dialog.dismiss();
                                                                                 }
                                                                             }
                                                                         });
                                                             }
                                                         }).addOnFailureListener(new OnFailureListener() {
                                                             @Override
                                                             public void onFailure(@NonNull Exception e) {
                                                                 Toast.makeText(add_sem_det.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                             }
                                                         });
                                                     }
                                                 }
                            );
                }
            }
        });



    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();     //return data of selected file
            String fileName = getFileName(uri);
            binding.selectedFile.setText(fileName);

        } else {
            Toast.makeText(getApplicationContext(), "Please select a file to upload", Toast.LENGTH_SHORT).show();
        }
    }

    private void select_pdf() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 8);

    }

    public String getFileName(Uri uri) {
        String fileName;
        if (uri.getScheme().equals("file")) {
            fileName = uri.getLastPathSegment();
        } else {
            Cursor cursor = null;
            try {
                cursor = getApplicationContext().getContentResolver().query(uri, new String[]{
                        MediaStore.Images.ImageColumns.DISPLAY_NAME
                }, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME) + 0);
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

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            select_pdf();
        } else {
            Toast.makeText(getApplicationContext(), "Please Grant Permissions to use the AssignMate!!", Toast.LENGTH_SHORT).show();
        }


    }
}