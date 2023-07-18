package com.example.assignmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class fetch_files extends AppCompatActivity {

    private ActivityFetchFilesBinding binding;




    adapter ad;

    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityFetchFilesBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        if (!checkConnection(getApplicationContext()))
        {
            startActivity(new Intent(getApplicationContext(),no_connection.class));
        }

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
                    binding.data.setVisibility(View.VISIBLE);
//                    binding.fetchLy.setVisibility(View.GONE);
                    binding.nodata.setVisibility(View.VISIBLE);


                }else
                    binding.data.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarID.setVisibility(View.GONE);
                binding.data.setVisibility(View.GONE);
//                binding.fetchLy.setVisibility(View.GONE);
                binding.nodata.setVisibility(View.VISIBLE);

            }
        });

        binding.backtocategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.backtocategory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(linearLayoutManager);


        FirebaseRecyclerOptions<file_model> options = new FirebaseRecyclerOptions.Builder<file_model>().setQuery(databaseReference, file_model.class).build();



        ad = new adapter(options,getApplicationContext());
        binding.recyclerView.setAdapter(ad);
        binding.recyclerView.setVisibility(View.VISIBLE);

        binding.searchBar.clearFocus();
        switch (type)
        {
            case "Assignments":binding.searchBar.setHint("Search Assignment here");
                                        break;
            case "Practicals":binding.searchBar.setHint("Search Assignment here");
                                break;
            case "Question Banks":binding.searchBar.setHint("Search Question Bank here");
                break;
            case "Notes":binding.searchBar.setHint("Search unit here");
                break;


        }
        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter_list(charSequence.toString(),subject,type);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        try {
            finalize();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }

    private void filter_list(String Text,String subject,String type) {

        if (!Text.isEmpty()) {
            FirebaseRecyclerOptions<file_model> options = new FirebaseRecyclerOptions.Builder<file_model>().setQuery(FirebaseDatabase.getInstance().getReference().child(subject).child(type).orderByChild("description").startAt("[a-zA-Z]{*}[ ][a-zA-Z]").endAt(Text.toLowerCase() + "/uf8ff"), file_model.class).build();
            ad = new adapter(options, fetch_files.this);
            ad.startListening();
            binding.recyclerView.setAdapter(ad);
            ad.notifyDataSetChanged();
        }
        else
        {
            FirebaseRecyclerOptions<file_model> options = new FirebaseRecyclerOptions.Builder<file_model>().setQuery(FirebaseDatabase.getInstance().getReference().child(subject).child(type), file_model.class).build();
            ad = new adapter(options,fetch_files.this);
            ad.startListening();
            binding.recyclerView.setAdapter(ad);

            binding.recyclerView.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        ad.startListening();

//        if (!checkConnection(getApplicationContext()))
//        {
//            startActivity(new Intent(getApplicationContext(),no_connection.class));
//        }
        binding.recyclerView.getRecycledViewPool().clear();
        ad.notifyDataSetChanged();

    }



    @Override
    protected void onStop() {
        super.onStop();

//        if (!checkConnection(getApplicationContext()))
//        {
//            startActivity(new Intent(getApplicationContext(),no_connection.class));
//        }
        ad.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ad.startListening();

        if (!checkConnection(getApplicationContext()))
        {
            startActivity(new Intent(getApplicationContext(),no_connection.class));
        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        ad.startListening();

//        if (!checkConnection(getApplicationContext()))
//        {
//            startActivity(new Intent(getApplicationContext(),no_connection.class));
}
}