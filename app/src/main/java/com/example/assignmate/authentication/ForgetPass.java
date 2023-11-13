package com.example.assignmate.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.assignmate.databinding.ActivityForgetPassBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPass extends AppCompatActivity {
    ActivityForgetPassBinding binding;
    FirebaseAuth mAuth;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityForgetPassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();
        String mail = binding.email.getText().toString();

        dialog = new Dialog(getApplicationContext());

        binding.backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        binding.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inProgress(true);
                if (mail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {

                    binding.emailLayout.setError("Invalid email!!");

                    inProgress(false);
                }
                else
                {
                    reset_password(binding.email.getText().toString());
                }



            }


        });

        binding.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!mail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    binding.emailLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });



    }

    public void reset_password(String email) {

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(ForgetPass.this, "Email sent successfully!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                    inProgress(false);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgetPass.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                inProgress(false);

            }
        });

    }
    public void inProgress(boolean progress)
    {
        if (progress)
        {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.reset.setVisibility(View.GONE);
        }
        else
        {
            binding.progressBar.setVisibility(View.GONE);
            binding.reset.setVisibility(View.VISIBLE);
        }
    }
}