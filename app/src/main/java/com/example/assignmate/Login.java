package com.example.assignmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignmate.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText email,Pass;

    TextInputLayout emaily,passly;

    Button login;

    ProgressBar progressBar;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    ActivityLoginBinding binding;
    FirebaseAuth mAuth;

    TextView signup;

    final int[] checked = new int[1];

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(currentUser != null || account!=null){
            Intent home=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(home);
            finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                Toast.makeText(Login.this, "Login Successful!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } catch (ApiException e) {
                Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView forgotpass = findViewById(R.id.forgotpass);
        TextView txt2 =findViewById(R.id.signup);
        email=findViewById(R.id.email);
        Pass=findViewById(R.id.password);
        mAuth=FirebaseAuth.getInstance();
        login=findViewById(R.id.button);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getApplicationContext(),gso);
        binding.signWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signWithGoogle = gsc.getSignInIntent();
                startActivityForResult(signWithGoogle,101);
            }
        });


        binding.forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetPass.class));
            }
        });


        progressBar=findViewById(R.id.progressBar);
        CheckBox chkbx = findViewById(R.id.checkBox);
        String url = " <a href=''>Forgot Password ?</a>";
        forgotpass.setText(Html.fromHtml(url));
        String txt3 = "Don't have an Account ? <a href=''>Sign Up</a>";
        txt2.setText(Html.fromHtml(txt3));
        String txt = "<b><a href=''>Terms & Conditions</a></b>";
        binding.terms.setText(Html.fromHtml(txt));

        binding.terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), terms.class));
            }
        });

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

                    binding.passwordlayout.setError("");

                }
                else
                {
                    binding.passwordlayout.setError("Password Length Must be atleast 8 Characters long");
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
                    binding.emailLayout.setError("");
                }
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
        if(key.length()>=8)
        {

            binding.passwordlayout.setError("");
            flag=true;

        }
        else
        {
            binding.passwordlayout.setError("Password Length Must be atleast 8 Characters long");
            flag=false;
        }

        // Email Validation

        boolean mailchk = false;

        if (!mail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            mailchk = true;

        } else {
            mailchk = false;
//            progressBar.setVisibility(View.GONE);
            binding.emailLayout.setError("Invalid Email!!");
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
            } else {
                Toast.makeText(this, "Please Accept our Terms And Conditions.", Toast.LENGTH_SHORT).show();
                inProgress(false);
            }
        }




    public void signup (View v){
        Intent act2 = new Intent(this, SignUp.class);
        startActivity(act2);
        finish();
    }
}
