package com.example.assignmate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignmate.Models.*;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignmate.adapters.sem_adapter;
import com.example.assignmate.authentication.choose_sem;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.w3c.dom.Text;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    String user;

    LinearLayout java, dm, os, ai, cc, activity, placement;

    FirebaseAuth mAuth;

    ShimmerFrameLayout shimmerFrameLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view);
        shimmerFrameLayout.startShimmer();
//        ImageView menu = view.findViewById(R.id.menu);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        TextView greet = view.findViewById(R.id.greet);

        TextView assigned_sem = view.findViewById(R.id.assigned_sem);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        TextView uname = view.findViewById(R.id.uname);

        Button updatesem = view.findViewById(R.id.update_sem);

        updatesem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), choose_sem.class));
            }
        });


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

        if (account != null) {
            String name;
            if (account.getDisplayName().split("\\w+").length > 1) {
                int firstSpace = account.getDisplayName().indexOf(" ");
                name = account.getDisplayName().substring(0, firstSpace);
            } else {
                name = account.getDisplayName();
            }
            uname.setText(name + " !");
        } else {
            String name;
            if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() != null) {
                if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName().split("\\w+").length > 1) {
                    int firstSpace = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().indexOf(" ");
                    name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().substring(0, firstSpace);

                } else {
                    name = mAuth.getCurrentUser().getDisplayName();

                    if (name == null)
                    {
                        name = "User";
                    }
                }
            } else {
                name = "User";
            }

            uname.setText(name + " !");
        }


        Calendar c = Calendar.getInstance();
        int hrs = c.get(Calendar.HOUR_OF_DAY);
        Log.d("hrs", "onCreateView: " + hrs);

        if (hrs >= 1 && hrs < 12) {
            greet.setText("Good morning");
        } else if (hrs >= 12 && hrs < 16) {
            greet.setText("Good afternoon");
        } else {
            greet.setText("Good evening");
        }



        mAuth = FirebaseAuth.getInstance();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            user = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        } else if (account != null) {
            user = account.getId().toString();
        }


        LinearLayout no_sem = view.findViewById(R.id.no_sem);
        no_sem.setVisibility(View.VISIBLE);



        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                recyclerView.setVisibility(View.VISIBLE);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {


                    if (snapshot1.child("uid").getValue().equals(user)) {

                        no_sem.setVisibility(View.GONE);
                        Log.d("TAG", "onDataChange: " + snapshot1.child("sem").getValue().toString());
                        fetchData(snapshot1.child("sem").getValue().toString(), view);
                        assigned_sem.setText(snapshot1.child("sem").getValue().toString());


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    public void fetchData(String semName, View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        Log.d("TAG", "fetchData: " + semName);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference semesterDataRef = databaseReference.child("Semester_data").child(semName);
        FirebaseRecyclerOptions<sem_model> sem_options = new FirebaseRecyclerOptions.Builder<sem_model>().setQuery(semesterDataRef, sem_model.class).build();
        sem_adapter adapter = new sem_adapter(sem_options, getContext());
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

}
