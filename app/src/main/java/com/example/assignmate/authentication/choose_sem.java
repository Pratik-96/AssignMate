package com.example.assignmate.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.assignmate.Models.User;
import com.example.assignmate.databinding.ActivityChooseSemBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class choose_sem extends AppCompatActivity {

    ActivityChooseSemBinding binding;

    RadioButton radioButton;
    boolean isUpdating = false;
    String name;
    String id = null;

    String email;
    GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseSemBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());






         account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        name = null;

        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            id = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        }
        else if (account!=null)
        {
            name = account.getDisplayName().toString();
            id= account.getId().toString();
        }
        DatabaseReference fetchref = FirebaseDatabase.getInstance().getReference().child("Users");
        fetchref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (id.equals(dataSnapshot.child("uid").getValue())) {
                        binding.update.setVisibility(View.VISIBLE);
                        binding.submit.setVisibility(View.GONE);
                        break;
                    }
                    else
                    {

                        binding.submit.setVisibility(View.VISIBLE);
                        binding.update.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });














        String state = getIntent().getStringExtra("fromActivity");
//        if (Objects.equals(state, "Profile"))
//        {
//            binding.submit.setVisibility(View.GONE);
//            binding.update.setVisibility(View.VISIBLE);
//
//        }




        final String[] selectedSem = {""};
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(id);


                selectedSem[0] = radioButton.getText().toString();

            }
        });

        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSemData(selectedSem);
            }
        });


         account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

         name = null;



        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser()!=null) {

                    name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                    id = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                    email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                }
                else if (account!=null)
                {
                    name = account.getDisplayName().toString();
                    id= account.getId().toString();
                    email = account.getEmail().toString();
                }

                Log.d("TAG", "onClick: "+name);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").push();
                User u = new User(id, name, selectedSem[0],email);
                reference.setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(choose_sem.this, "Welcome " + name + " to AssignMate..!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            finish();
                        }
                    }
                });
//
            }
        });


    }

    private void updateSemData(String[] selectedSem) {

                DatabaseReference fetchref = FirebaseDatabase.getInstance().getReference().child("Users");
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());





                 name = null;
                if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
                     name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                    id = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                    email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                }
                else if (account!=null)
                {
                    name = account.getDisplayName().toString();
                    id = account.getId().toString();
                    email = account.getEmail().toString();
                }

                HashMap  user = new HashMap();
                user.put("name",name);
                user.put("sem",selectedSem[0]);
                user.put("uid",id);
                user.put("email",email);

                fetchref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (id.equals(dataSnapshot.child("uid").getValue().toString()) && !isUpdating) {


                                       // User Already Exists
                                Log.d("update", "onDataChange: "+dataSnapshot.getKey());
                                fetchref.child(dataSnapshot.getKey()).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(choose_sem.this, "Semester Updated Successfully!!", Toast.LENGTH_SHORT).show();

                                        }
                                        else
                                        {
                                            Toast.makeText(choose_sem.this, task.getException().getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        isUpdating=true;
                                    }
                                });

                                break;
                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



                finish();

    }
}

