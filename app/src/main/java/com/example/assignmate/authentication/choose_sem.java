package com.example.assignmate.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.assignmate.Models.User;
import com.example.assignmate.databinding.ActivityChooseSemBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class choose_sem extends AppCompatActivity {

    ActivityChooseSemBinding binding;

    RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseSemBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra("uname");


        final String[] selectedSem = {""};
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(id);

                Log.d("radio", "onCreate: "+radioButton.getText());
                selectedSem[0] =radioButton.getText().toString();

            }
        });


        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").push();
                User u = new User(FirebaseAuth.getInstance().getUid(),name,selectedSem[0]);
                reference.setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(choose_sem.this, "Welcome "+name+" to AssignMate..!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            finish();
                        }
                    }
                });
            }
        });




    }
}