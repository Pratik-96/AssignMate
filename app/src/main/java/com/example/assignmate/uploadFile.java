package com.example.assignmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.assignmate.Models.file_model;

import android.Manifest;
import android.app.ProgressDialog;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import com.example.assignmate.databinding.ActivityUploadFileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Date;

public class uploadFile extends AppCompatActivity {

    ProgressDialog dialog;
    ActivityUploadFileBinding binding;
    FirebaseStorage storage;
    FirebaseDatabase database;

    Uri uri;

    String str, sub, type;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            select_pdf();
        }
        else {
            Toast.makeText(this, "Please Grant Permissions to use the AssignMate!!", Toast.LENGTH_SHORT).show();
        }


    }
    public void replaceFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadFileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        storage = FirebaseStorage.getInstance();    // Returns an object of Firebase storage
        database = FirebaseDatabase.getInstance();
        binding.description.clearFocus();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.subjects, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        binding.spinner1.setAdapter(adapter);

        binding.navBar.setItemSelected(R.id.upload_nav,true);
        binding.navBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                int selectedItem = binding.navBar.getSelectedItemId();
//
//                Log.d("TAG", "onItemSelected: "+selectedItem+"      "+i);
                if (selectedItem==R.id.home_nav)
                {
                    finish();
                    replaceFragment(new HomeFragment());
                }
                else if (selectedItem==R.id.profile_nav) {
                    finish();
                    replaceFragment(new Profile());
                }

            }
        });

        binding.description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.descriptionLy.setError("");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ArrayAdapter<CharSequence> docTypeAd = ArrayAdapter.createFromResource(getApplicationContext(), R.array.documentType, android.R.layout.simple_spinner_item);
        docTypeAd.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        binding.doctype1.setAdapter(docTypeAd);


        binding.selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(uploadFile.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    select_pdf();
                } else {
                    ActivityCompat.requestPermissions(uploadFile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }

            }
        });


        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sub = binding.spinner1.getSelectedItem().toString();
                type = binding.doctype1.getSelectedItem().toString();

                if (uri != null) {
                    if (binding.description.getText().toString().isEmpty()) {
                        binding.descriptionLy.setError("Description Cannot be Empty");


                    } else if (sub.equals("None")) {
                        Toast.makeText(uploadFile.this, "Please select a subject..", Toast.LENGTH_SHORT).show();
                    } else if (type.equals("None")) {
                        Toast.makeText(uploadFile.this, "Please select document type..", Toast.LENGTH_SHORT).show();
                    } else if (!binding.description.getText().toString().isEmpty() && (!sub.equals("None") && !type.equals("None"))) {
                        upload(uri);

                    }


                } else {
                    Toast.makeText(uploadFile.this, "Please select a file to upload..", Toast.LENGTH_SHORT).show();
                }
            }


        });


    }

    public String getFileName(Uri uri) {
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

    //
    private void upload(Uri uri) {          // Uploads the file

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle("Uploading File...");
        dialog.setProgress(0);
        dialog.show();


        str = getFileName(uri);
        String path = str.replaceAll("\\.", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\[", "").replaceAll("]", "").replaceAll("#", "").replaceAll("\\$", "");

        StorageReference storageReference = storage.getReference();         //Get Reference returns root    path
        String selectedSub = binding.spinner1.getSelectedItem().toString();
        String selectedType = binding.doctype1.getSelectedItem().toString();
        storageReference.child(selectedSub).child(selectedType).child(getFileName(uri)).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot snapshot) {
                Task<Uri> result = snapshot.getStorage().getDownloadUrl();   //gets a task uri

                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();     // Extracting url from result uri
                        DatabaseReference reference = database.getReference().child(selectedSub).child(selectedType).push();
                        String description = binding.description.getText().toString().toLowerCase();
                        SimpleDateFormat ts = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = new Date();
                        String timestamp = ts.format(date);
                        file_model model = new file_model(str, description, url,timestamp);
                        reference.setValue(model)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(uploadFile.this, "File Uploaded Sucessfully!!", Toast.LENGTH_SHORT).show();
                                            dialog.setTitle("File Uploaded Sucessfully!!");
                                            notificationsSender notificationsSender = new notificationsSender("/topics/all","New Document.",str+" has been added to "+selectedSub+"/"+selectedType,getApplicationContext(),uploadFile.this);
                                            notificationsSender.sendNotification();
//                                            notification();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(uploadFile.this, "Upload Failed!!", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                });
                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(uploadFile.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                int currentProgress = (int) (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                dialog.setProgress(currentProgress);
            }
        });

    }

    private void select_pdf() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 8);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();     //return data of selected file
            String fileName = getFileName(uri);
            binding.selectedFile.setText(fileName);

        } else {
            Toast.makeText(this, "Please select a file to upload", Toast.LENGTH_SHORT).show();
        }



    }

}




