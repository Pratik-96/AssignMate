package com.example.assignmate;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.assignmate.Models.file_model;
import com.example.assignmate.authentication.add_sem_det;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private static final String SEMESTER_1_STORAGE_REF = "gs://java-76623.appspot.com";
    private static final String SEMESTER_2_STORAGE_REF = "gs://data-mining-102aa.appspot.com";
    private static final String SEMESTER_3_STORAGE_REF = "gs://cloud-computing-180c7.appspot.com";
    private static final String SEMESTER_4_STORAGE_REF = "gs://assignmate-pro.appspot.com";

    private static final String SEMESTER_5_STORAGE_REF = "gs://assignmate-df72e.appspot.com";
    private static final String SEMESTER_6_STORAGE_REF = "gs://fir-login-c10ae.appspot.com";

    String str, sub, type;
    ProgressDialog dialog;

    ArrayList<String> subject_names ;

    FirebaseStorage storage;
    FirebaseDatabase database;
    String selectedSem;
    String selectedSub, selectedType;
    Uri uri;

    EditText description;
    Spinner semSpinner;

    Spinner spinner1, doctype1;
    TextView selectedFile;

    ArrayAdapter<String> subadapter;

    public UploadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadFragment newInstance(String param1, String param2) {
        UploadFragment fragment = new UploadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            select_pdf();
        } else {
            Toast.makeText(getContext(), "Please Grant Permissions to use the AssignMate!!", Toast.LENGTH_SHORT).show();
        }


    }

    private void select_pdf() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 8);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        selectedFile = view.findViewById(R.id.selected_file);
        TextInputLayout descriptionLy = view.findViewById(R.id.description_ly);
        description = view.findViewById(R.id.description1);
        description.clearFocus();

        Spinner selectSem = view.findViewById(R.id.sem);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.subjects, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        ArrayAdapter<CharSequence> semAdapter = ArrayAdapter.createFromResource(getContext(), R.array.semesters, android.R.layout.simple_spinner_item);
        semAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        selectSem.setAdapter(semAdapter);
        semSpinner = view.findViewById(R.id.sem);

        Button update_sem_det = view.findViewById(R.id.add_sem_det);


        subject_names = new ArrayList<>();

        subadapter =  new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,subject_names);
        //        subadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1 = view.findViewById(R.id.spinner);
        if (spinner1!=null) {
            spinner1.setAdapter(subadapter);
            spinner1.setSelection(0);
        }


        selectSem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedSem = selectSem.getSelectedItem().toString();

                subject_names.clear();


                FirebaseDatabase database1 = FirebaseDatabase.getInstance();


                database1.getReference().child("Semester_data").child(selectedSem).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Log.d("SemesterData", "onDataChange: " + dataSnapshot.child("sub_name").getValue());

                            subject_names.add((String) dataSnapshot.child("sub_name").getValue());
                            subadapter.notifyDataSetChanged();




                            Log.d("sub_data", "onCreateView: "+subject_names);
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


        Log.d("sub_spinner", "onCreateView: "+subject_names);










        update_sem_det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), add_sem_det.class));
            }
        });

        LinearLayout no_access = view.findViewById(R.id.no_access);
        LinearLayout admin_access = view.findViewById(R.id.upload_layout);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (mAuth.getUid() != null) {
            Log.d("id", "token:- " + mAuth.getUid());
            if (Objects.equals(mAuth.getUid(), "RYmuvMvQn3WpjHpgIjDMgrazSpq1")) {

                no_access.setVisibility(View.GONE);
                admin_access.setVisibility(View.VISIBLE);

            } else {
                no_access.setVisibility(View.VISIBLE);
                admin_access.setVisibility(View.GONE);
            }
        } else {
            no_access.setVisibility(View.VISIBLE);
            admin_access.setVisibility(View.GONE);
        }
        doctype1 = view.findViewById(R.id.doctype);//Get Reference returns root    path
        spinner1 = view.findViewById(R.id.spinner);
        Log.d("spinner", "spinner: " + spinner1);
        Log.d("spinner", "doctype: " + doctype1);
//        spinner1.setAdapter(adapter);

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                descriptionLy.setError("");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ArrayAdapter<CharSequence> docTypeAd = ArrayAdapter.createFromResource(getContext(), R.array.documentType, android.R.layout.simple_spinner_item);
        docTypeAd.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        Spinner doctype = view.findViewById(R.id.doctype);
        doctype.setAdapter(docTypeAd);

        ImageView selectFile = view.findViewById(R.id.select_file);
        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    select_pdf();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }

            }
        });


        Button upload = view.findViewById(R.id.upload1);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sub = spinner1.getSelectedItem().toString();
                type = doctype.getSelectedItem().toString();

                String selectedSem = semSpinner.getSelectedItem().toString();
                String selectedSub = spinner1.getSelectedItem().toString();
                if (uri != null) {
                    if (description.getText().toString().isEmpty()) {
                        descriptionLy.setError("Description Cannot be Empty");


                    }  else if (type.equals("None")) {
                        Toast.makeText(getContext(), "Please select document type..", Toast.LENGTH_SHORT).show();
                    } else if (!description.getText().toString().isEmpty() && !type.equals("None")) {
                        upload(uri, view,selectedSub,selectedSem);

                    }


                } else {
                    Toast.makeText(getContext(), "Please select a file to upload..", Toast.LENGTH_SHORT).show();
                }
            }


        });


        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();     //return data of selected file
            String fileName = getFileName(uri);
            selectedFile.setText(fileName);

        } else {
            Toast.makeText(getContext(), "Please select a file to upload", Toast.LENGTH_SHORT).show();
        }
    }

    private void upload(Uri uri, View view,String selectedSub,String Semester) {          // Uploads the file

        dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle("Uploading File...");
        dialog.setProgress(0);
        dialog.show();
        String Final_Ref = "";

        switch (Semester)
        {
            case "Semester 1":Final_Ref = SEMESTER_1_STORAGE_REF;
            break;

            case "Semester 2":Final_Ref = SEMESTER_2_STORAGE_REF;
            break;

            case "Semester 3":Final_Ref = SEMESTER_3_STORAGE_REF;
            break;

            case "Semester 4":Final_Ref = SEMESTER_4_STORAGE_REF;
            break;

            case "Semester 5":Final_Ref = SEMESTER_5_STORAGE_REF;
            break;

            case "Semester 6":Final_Ref = SEMESTER_6_STORAGE_REF;
            break;
        }

        FirebaseStorage storage = FirebaseStorage.getInstance(Final_Ref);    // Returns an object of Firebase storage

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        str = getFileName(uri);
        String path = str.replaceAll("\\.", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\[", "").replaceAll("]", "").replaceAll("#", "").replaceAll("\\$", "");

        StorageReference storageReference = storage.getReference();




        selectedType = doctype1.getSelectedItem().toString();


        if (selectedSub.equals("Activities")) {
            selectedType = " ";
        }



        selectedSem = semSpinner.getSelectedItem().toString();
        String token = "/topics/"+selectedSem.replace(' ','_');
        Log.d("Token upload", token);

        storageReference.child(selectedSem).child(selectedSub).child(selectedType).child(getFileName(uri)).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot snapshot) {
                Task<Uri> result = snapshot.getStorage().getDownloadUrl();   //gets a task uri

                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        String descrption;// Extracting url from result uri
                        if (!selectedSem.equals("Semester 5"))
                        {
                            DatabaseReference reference = database.getReference().child(selectedSem).child(selectedSub).child(selectedType).push();

                            descrption = description.getText().toString().toLowerCase();

                            SimpleDateFormat ts = new SimpleDateFormat("dd/MM/yyyy");
                            Date date = new Date();
                            String timestamp = ts.format(date);
                            file_model model = new file_model(str, descrption, url, timestamp);
                            reference.setValue(model)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "File Uploaded Sucessfully!!", Toast.LENGTH_SHORT).show();
                                                dialog.setTitle("File Uploaded Sucessfully!!");
                                                String token = "/topics/"+selectedSem.replace(' ','_');
                                                Log.d("Token upload", token);
                                                notificationsSender notificationsSender = new notificationsSender(token, "New Document.", str + " has been added to "+selectedSem+"/" + selectedSub + "/" + selectedType, getContext(), getActivity());
                                                notificationsSender.sendNotification();
                                                dialog.dismiss();
                                            } else {
                                                Toast.makeText(getContext(), "Upload Failed!!", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                        }
                        else {
                            DatabaseReference reference = database.getReference().child(selectedSub).child(selectedType).push();

                            descrption = description.getText().toString().toLowerCase();

                            SimpleDateFormat ts = new SimpleDateFormat("dd/MM/yyyy");
                            Date date = new Date();
                            String timestamp = ts.format(date);
                            file_model model = new file_model(str, descrption, url, timestamp);
                            reference.setValue(model)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "File Uploaded Sucessfully!!", Toast.LENGTH_SHORT).show();
                                                dialog.setTitle("File Uploaded Sucessfully!!");
                                                notificationsSender notificationsSender = new notificationsSender("/topics/"+selectedSem, "New Document.", str + " has been added to "+selectedSem+"/" + selectedSub + "/" + selectedType, getContext(), getActivity());
                                                notificationsSender.sendNotification();
                                                dialog.dismiss();
                                            } else {
                                                Toast.makeText(getContext(), "Upload Failed!!", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                        }
                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

    public String getFileName(Uri uri) {
        String fileName;
        if (uri.getScheme().equals("file")) {
            fileName = uri.getLastPathSegment();
        } else {
            Cursor cursor = null;
            try {
                cursor = getActivity().getContentResolver().query(uri, new String[]{
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


}