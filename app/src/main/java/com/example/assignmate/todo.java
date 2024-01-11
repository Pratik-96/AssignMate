package com.example.assignmate;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.assignmate.Models.Todo_model;
import com.example.assignmate.Models.file_model;
import com.example.assignmate.adapters.adapter;
import com.example.assignmate.adapters.todoAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link todo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class todo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public todo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment todo.
     */
    // TODO: Rename and change types and number of parameters
    public static todo newInstance(String param1, String param2) {
        todo fragment = new todo();
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

    String email=null;
    todoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        FloatingActionButton FAB = view.findViewById(R.id.edit_list);
        RecyclerView recyclerView = view.findViewById(R.id.todo_recycler);
        RelativeLayout animationView = view.findViewById(R.id.no_data);


        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);





        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

        } else if (account != null) {

            email = account.getEmail().toString();
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("TODO_LIST_DATA");

        FirebaseRecyclerOptions<Todo_model> options = new FirebaseRecyclerOptions.Builder<Todo_model>().setQuery(databaseReference.orderByChild("email").equalTo(email), Todo_model.class).build();

        databaseReference.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                   if (snapshot.getChildrenCount() == 0) {
                       recyclerView.setVisibility(View.GONE);

//                    binding.fetchLy.setVisibility(View.GONE);
                       animationView.setVisibility(View.VISIBLE);

                   } else{
                       recyclerView.setVisibility(View.VISIBLE);
                       animationView.setVisibility(View.GONE);
                   }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               animationView.setVisibility(View.VISIBLE);
               recyclerView.setVisibility(View.GONE);
            }
        });






        adapter = new todoAdapter(options,getContext());
        recyclerView.setAdapter(adapter);

        adapter.startListening();
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.dialoglayout,null);


                AlertDialog dialog = new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Add a Task")
                        .setView(view1)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText getTxt = view1.findViewById(R.id.dialogEditText);
                                 //TODO: Save the data in firebase

                                DatabaseReference todoRef = FirebaseDatabase.getInstance().getReference();
                                Todo_model model = new Todo_model(getTxt.getText().toString(),email,false);
                                todoRef.child("TODO_LIST_DATA").push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(getContext(), "Task Added Successfully..!!", Toast.LENGTH_SHORT).show();
                                            dialogInterface.dismiss();
                                        }
                                        else
                                        {
                                            Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            dialogInterface.dismiss();
                                        }
                                    }
                                });

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });

        return view;
    }
}