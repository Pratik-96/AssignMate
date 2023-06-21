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
import com.google.firebase.database.ValueEventListener;

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
        binding.selectedCategory2.setText(type);


        binding.progressBarID.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(subject).child(type);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.progressBarID.setVisibility(View.GONE);
                if (snapshot.getChildrenCount()==0)
                {
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.nodata.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarID.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.GONE);
                binding.nodata.setVisibility(View.VISIBLE);

            }
        });
//

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        FirebaseRecyclerOptions<file_model> options = new FirebaseRecyclerOptions.Builder<file_model>().setQuery(databaseReference, file_model.class).build();



        ad = new adapter(options,fetch_files.this);
        binding.recyclerView.setAdapter(ad);
        binding.recyclerView.setVisibility(View.VISIBLE);
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