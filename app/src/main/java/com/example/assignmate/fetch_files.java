package com.example.assignmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.assignmate.Models.file_model;
import com.example.assignmate.adapters.adapter;
import com.example.assignmate.authentication.no_connection;
import com.example.assignmate.databinding.ActivityFetchFilesBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class fetch_files extends AppCompatActivity {

    private ActivityFetchFilesBinding binding;



    String user;



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
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        binding.searchBar.clearFocus();
        if (!checkConnection(getApplicationContext()))
        {
            startActivity(new Intent(getApplicationContext(), no_connection.class));
        }

        binding.searchBar.setFocusableInTouchMode(true);
        Bundle bundle = getIntent().getExtras();

        String subject = bundle.getString("name");
        String type = bundle.getString("docType");

        binding.selectedCategory.setText(type);
        binding.selectedCategory2.setText(type);

        if (!type.equals("Notes"))
        {
            binding.filter.setVisibility(View.GONE);
        }
        if (type.equals(" "))
        {
            binding.selectedCategory.setText("Activities");
            binding.selectedCategory2.setText("Activities");

        }
        if (type.equals("Placement"))
        {
            binding.selectedCategory.setText("Placement");
            binding.selectedCategory2.setText("Placement");

        }
        binding.progressBarID.setVisibility(View.VISIBLE);
        binding.shimmerView.setVisibility(View.VISIBLE);
        binding.shimmerView.startShimmer();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            user = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        } else if (account != null) {
            user = account.getId().toString();
        }




        DatabaseReference semesterRef = FirebaseDatabase.getInstance().getReference();
        semesterRef.child("Users").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {


                            if (snapshot1.child("uid").getValue().equals(user)) {

                                if (snapshot1.child("sem").getValue().equals("Semester 5"))
                                {
                                    fetchData(subject,type);

                                } else if (subject.equals("Placement Preparation") && type.equals("Placement")) {
                                    fetchData(subject,type);
                                    
                                } else {

                                    fetchData(snapshot1.child("sem").getValue().toString(), subject, type);
                                }


                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );




    }


    private void fetchData(String subject, String type)
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(subject).child(type);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.progressBarID.setVisibility(View.GONE);
                binding.shimmerView.stopShimmer();
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
                binding.shimmerView.stopShimmer();
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
        ad.startListening();

        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getApplicationContext(),binding.filter);
                menu.getMenu().add("Show Only Nirali Notes");
                menu.getMenu().add("Show All");
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("Show Only Nirali Notes"))
                        {
                            FirebaseRecyclerOptions<file_model> options = new FirebaseRecyclerOptions.Builder<file_model>().setQuery(FirebaseDatabase.getInstance().getReference().child(subject).child(type).orderByChild("file_Name").equalTo("Nirali Publication.pdf"), file_model.class).build();
                            ad = new adapter(options, fetch_files.this);
                            ad.startListening();
                            binding.recyclerView.setAdapter(ad);
                            ad.notifyDataSetChanged();
                            return true;
                        }
                        else if (menuItem.getTitle().equals("Show All"))
                        {
                            FirebaseRecyclerOptions<file_model> options = new FirebaseRecyclerOptions.Builder<file_model>().setQuery(databaseReference, file_model.class).build();
                            ad = new adapter(options, fetch_files.this);
                            ad.startListening();
                            binding.recyclerView.setAdapter(ad);
                            ad.notifyDataSetChanged();
                            return true;
                        }
                        return false;

                    }
                });
            }
        });


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


    private void fetchData(String selectedSem, String subject, String type)
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(selectedSem).child(subject).child(type);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.progressBarID.setVisibility(View.GONE);
                binding.shimmerView.stopShimmer();
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
                binding.shimmerView.stopShimmer();
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
        ad.startListening();

        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getApplicationContext(),binding.filter);
                menu.getMenu().add("Show Only Nirali Notes");
                menu.getMenu().add("Show All");
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("Show Only Nirali Notes"))
                        {
                           if(selectedSem.equals("Semester 5"))
                           {
                               FirebaseRecyclerOptions<file_model> options = new FirebaseRecyclerOptions.Builder<file_model>().setQuery(FirebaseDatabase.getInstance().getReference().child(subject).child(type).orderByChild("file_Name").equalTo("Nirali Publication.pdf"), file_model.class).build();
                               ad = new adapter(options, fetch_files.this);
                               ad.startListening();
                               binding.recyclerView.setAdapter(ad);
                               ad.notifyDataSetChanged();
                               return true;
                           }
                           else {
                               FirebaseRecyclerOptions<file_model> options = new FirebaseRecyclerOptions.Builder<file_model>().setQuery(FirebaseDatabase.getInstance().getReference().child(selectedSem).child(subject).child(type).orderByChild("file_Name").equalTo("Nirali Publication.pdf"), file_model.class).build();
                               ad = new adapter(options, fetch_files.this);
                               ad.startListening();
                               binding.recyclerView.setAdapter(ad);
                               ad.notifyDataSetChanged();
                               return true;
                           }
                        }
                        else if (menuItem.getTitle().equals("Show All"))
                        {
                            if(selectedSem.equals("Semester 5"))
                            {
                                FirebaseRecyclerOptions<file_model> options = new FirebaseRecyclerOptions.Builder<file_model>().setQuery(FirebaseDatabase.getInstance().getReference().child(subject).child(type), file_model.class).build();
                                ad = new adapter(options, fetch_files.this);
                                ad.startListening();
                                binding.recyclerView.setAdapter(ad);
                                ad.notifyDataSetChanged();
                                return true;
                            }
                            else {
                                FirebaseRecyclerOptions<file_model> options = new FirebaseRecyclerOptions.Builder<file_model>().setQuery(FirebaseDatabase.getInstance().getReference().child(selectedSem).child(subject).child(type), file_model.class).build();
                                ad = new adapter(options, fetch_files.this);
                                ad.startListening();
                                binding.recyclerView.setAdapter(ad);
                                ad.notifyDataSetChanged();
                                return true;
                            }
                        }
                        return false;

                    }
                });
            }
        });


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

                if (selectedSem.equals("Semester 5")) {
                    filter_list(charSequence.toString(), subject, type);
                }
                else {
                    filter_list(charSequence.toString(),selectedSem,subject,type);
                }
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
            FirebaseRecyclerOptions<file_model> options = new FirebaseRecyclerOptions.Builder<file_model>().setQuery(FirebaseDatabase.getInstance().getReference().child(subject).child(type).orderByChild("description").startAt(Text).endAt(Text.toLowerCase()+ "/uf8ff"), file_model.class).build();
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


    private void filter_list(String Text,String selectedSem,String subject,String type) {

        if (!Text.isEmpty()) {
            FirebaseRecyclerOptions<file_model> options = new FirebaseRecyclerOptions.Builder<file_model>().setQuery(FirebaseDatabase.getInstance().getReference().child(selectedSem).child(subject).child(type).orderByChild("description").startAt(Text).endAt(Text.toLowerCase()+ "/uf8ff"), file_model.class).build();
            ad = new adapter(options, fetch_files.this);
            ad.startListening();
            binding.recyclerView.setAdapter(ad);
            ad.notifyDataSetChanged();
        }
        else
        {
            FirebaseRecyclerOptions<file_model> options = new FirebaseRecyclerOptions.Builder<file_model>().setQuery(FirebaseDatabase.getInstance().getReference().child(selectedSem).child(subject).child(type), file_model.class).build();
            ad = new adapter(options,fetch_files.this);
            ad.startListening();
            binding.recyclerView.setAdapter(ad);

            binding.recyclerView.setVisibility(View.VISIBLE);
        }


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (ad!=null) {
            ad.startListening();
        }
        binding.searchBar.clearFocus();
//        if (!checkConnection(getApplicationContext()))
//        {
//            startActivity(new Intent(getApplicationContext(),no_connection.class));
//        }
        binding.recyclerView.getRecycledViewPool().clear();

        if (ad!=null) {
            ad.notifyDataSetChanged();
        }
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
        binding.searchBar.clearFocus();
        if (ad!=null) {
            ad.startListening();
        }
        if (!checkConnection(getApplicationContext()))
        {
            startActivity(new Intent(getApplicationContext(),no_connection.class));
        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        binding.searchBar.clearFocus();

        if (ad!=null) {
            ad.startListening();
        }
//        if (!checkConnection(getApplicationContext()))
//        {
//            startActivity(new Intent(getApplicationContext(),no_connection.class));
    }
}