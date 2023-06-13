package com.example.assignmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText email,Pass;

    Button login;

    ProgressBar progressBar;

    FirebaseAuth mAuth;

    TextView signup;

    final int[] checked = new int[1];

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent home=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(home);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView forgotpass = findViewById(R.id.forgotpass);
        TextView txt2 =findViewById(R.id.signup);
        email=findViewById(R.id.email);
        Pass=findViewById(R.id.password);
        mAuth=FirebaseAuth.getInstance();
        login=findViewById(R.id.button);
        progressBar=findViewById(R.id.progressBar);
        CheckBox chkbx = findViewById(R.id.checkBox);
        String url = " <a href=''>Forgot Password ?</a>";
        forgotpass.setText(Html.fromHtml(url));
        String txt3 = "Don't have an Account ? <a href=''>Sign Up</a>";
        txt2.setText(Html.fromHtml(txt3));
        String txt = "<b>I agree to the <a href=''>Terms & Conditions</a></b>";
        chkbx.setText(Html.fromHtml(txt));
        chkbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)  //If check Box is Checked or not
                {
                    checked[0] = 1;
                } else {
                    checked[0] = 0;
                }

            }
        });
        Pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pass = charSequence.toString();
                if(pass.length()>=8)
                {

                    Pass.setError(null);

                }
                else
                {
                    Pass.setError("Password Length Must be atleast 8 Characters long");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String mail = charSequence.toString();
                if (!mail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    email.setError(null);
                } else {
                    email.setError("Invalid Email!!"); }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
    public void inProgress(boolean progress)
    {
        if (progress)
        {
            progressBar.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }
    }

    public void login(View view) {
//        Check box Checking && Name checking

        boolean flag = true;
        String key = Pass.getText().toString();

        inProgress(true);
        String mail = email.getText().toString();
        // Email Validation

        boolean mailchk = false;

        if (!mail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            mailchk = true;
        } else {
            mailchk = false;
//            progressBar.setVisibility(View.GONE);
        }


        if (checked[0] == 1 && flag && mailchk) {


            mAuth.signInWithEmailAndPassword(mail, key)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            inProgress(false);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(Login.this, "Login Successful!!", Toast.LENGTH_SHORT).show();
                                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(home);
                                finish();
                                inProgress(false);

                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(Login.this, task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Please Enter all Fields..", Toast.LENGTH_SHORT).show();
            inProgress(false);
        }


    }

    public void signup (View v){
        Intent act2 = new Intent(this, SignUp.class);
        startActivity(act2);
        finish();
    }
}
