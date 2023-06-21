package com.example.assignmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.assignmate.databinding.ActivityFetchFilesBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
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

    ArrayList<file_model> file;

    adapter ad;
    ProgressBar progressBar;


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
//
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        FirebaseRecyclerOptions<file_model> options = new FirebaseRecyclerOptions.Builder<file_model>().setQuery(databaseReference, file_model.class).build();
         ad = new adapter(options,fetch_files.this);
        binding.recyclerView.setAdapter(ad);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ad.startListening();
        binding.recyclerView.getRecycledViewPool().clear();
        ad.notifyDataSetChanged();

    }



    @Override
    protected void onStop() {
        super.onStop();
        ad.stopListening();
    }
}