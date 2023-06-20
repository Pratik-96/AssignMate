package com.example.assignmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.assignmate.databinding.ActivityFetchFilesBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class fetch_files extends AppCompatActivity {

    private ActivityFetchFilesBinding binding;

    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityFetchFilesBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
       Bundle bundle = getIntent().getExtras();

           String subject = bundle.getString("name");
           String type = bundle.getString("docType");
        binding.selectedCategory.setText(type);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(subject).child(type);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Called for individual items at database reference
                String filename = snapshot.child("File Name:").getValue(String.class);
                String description = snapshot.child("Description").getValue(String.class);
                String url = snapshot.child("Url").getValue(String.class);
//               ((adapter)(binding.recyclerView.getAdapter())).update(filename,description,url);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        adapter adapter = new adapter(binding.recyclerView,getApplicationContext(),new ArrayList<String>(),new ArrayList<String>());
//        binding.recyclerView.setAdapter(adapter);
    }
}