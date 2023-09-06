package com.example.assignmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignmate.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;

import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    EditText name,Pass1,Pass2,email;
    TextView reg;
    CheckBox chkbox;

    ProgressBar progressBar;

    ActivitySignUpBinding binding;

    Button button;

    String User_Name;
    FirebaseAuth mAuth;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;


    TextInputLayout passly,mailly,cnfpassly;

    final int[] checked = new int[1];

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                Toast.makeText(this, "Creating your account.!!", Toast.LENGTH_SHORT).show();


                // Create the One Tap sign-in client

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } catch (ApiException e) {
                Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_SHORT).show();            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth=FirebaseAuth.getInstance();
        name = (EditText) findViewById(R.id.name);
        chkbox = findViewById(R.id.checkBox);
        reg=findViewById(R.id.noacc);
        mailly=findViewById(R.id.emailLayout);
        cnfpassly=findViewById(R.id.cnfpassLayout);
        Pass1=(EditText) findViewById(R.id.password);
        Pass2=(EditText) findViewById(R.id.cnfpass);
        passly=findViewById(R.id.pasLayout);
        email = (EditText)findViewById(R.id.email);
        progressBar=findViewById(R.id.progressBar);
        String txt = "<b><a href='www.google.com'>Terms & Conditions</a></b>";
        binding.terms.setText(Html.fromHtml(txt));
        String txt2 = "<b>Already have an account ? <a href=''>Log In</a></b>";
        reg.setText(Html.fromHtml(txt2));
        button=findViewById(R.id.button);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken("660056468922-cqpahr9lrkufo2ndhsnun12mf4j3ee3v.apps.googleusercontent.com").build();
        gsc = GoogleSignIn.getClient(getApplicationContext(),gso);
        binding.signWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signWithGoogle = gsc.getSignInIntent();
                startActivityForResult(signWithGoogle,101);
            }
        });

        binding.terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), terms.class));
            }
        });


        Pass2.setLongClickable(false);

        binding.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!binding.name.getText().toString().isEmpty())
                    {
                        binding.nameLayout.setError("");
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        Pass1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pass = charSequence.toString();
                if(pass.length()>=8)
                {
                    Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
                    Matcher matcher = pattern.matcher(pass);
                    boolean iscontainspc=matcher.find();
                    if (iscontainspc)
                    {
                        binding.pasLayout.setError(null);

                    }
                    else {
                        binding.pasLayout.setError("Weak Password!!");
                    }
                }
                else
                {
                    binding.pasLayout.setError("Password Length Must be atleast 8 Characters long");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Pass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String key = Pass1.getText().toString();
                String key2 = Pass2.getText().toString();
                if (!key.equals(key2)) {         // Password checking
                    binding.cnfpassLayout.setError("Passwords are not matching");
                } else {
                    binding.cnfpassLayout.setError(null);
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
//                    email.setError("");
                    binding.emailLayout.setError("");
                } else {
//                    email.setError("Invalid Email!!");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    public void register(View v){
        Intent act2 = new Intent(this,Login.class);            // Login Listener
        startActivity(act2);
        finish();
    }
    public void signup(View view) {
//        Check box Checking && Name checking
        boolean flag = true;
        String key = Pass1.getText().toString();
        String key2 = Pass2.getText().toString();
        inProgress(true);
        if (!key.equals(key2)) {         // Password checking
            flag = false;
            binding.cnfpassLayout.setError("Passwords are not matching!!");

//            progressBar.setVisibility(View.GONE);
        } else {
            flag = true;
        }
        String mail=email.getText().toString();
        // Email Validation

        boolean mailchk = false;

        if (!mail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            mailchk = true;

        } else {
            mailchk = false;
            binding.emailLayout.setError("Invalid Email!!");
//            progressBar.setVisibility(View.GONE);
        }
        String str = name.getText().toString();
        if (str.isEmpty())
        {
            binding.nameLayout.setError("Name cannot be empty!!");
        }
        if (binding.password.getText().toString().isEmpty())
        {
            binding.pasLayout.setError("Password cannot be empty!!");
        }
        if (binding.cnfpass.getText().toString().isEmpty())
        {
            binding.cnfpassLayout.setError("Field cannot be empty!!");
        }

//


        if (checked[0] == 1 && flag && mailchk && !str.isEmpty()) {

             User_Name = binding.name.getText().toString();
            String User_Email = email.getText().toString();
            String User_Pass = Pass2.getText().toString();


            String password = Pass2.getText().toString();

            mAuth.createUserWithEmailAndPassword(mail, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(SignUp.this, "Signing Up..", Toast.LENGTH_SHORT).show();
                                Log.d("uname", "onComplete: "+binding.name.getText().toString());

                                Intent home = new Intent(getApplicationContext(), Login.class);
                                home.putExtra("uname",str);
                                notification();
                                startActivity(home);
                                finish();
                                inProgress(false);

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignUp.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                inProgress(false);

                            }

                        }
                    });


        }
//                else
//                {
//                    Toast.makeText(this, "SignUp Failed..", Toast.LENGTH_SHORT).show();
//                }
//            }
//            else
//            {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(this, "User Already Exists , Please Login to continue..", Toast.LENGTH_SHORT).show();
//            }


//                    Log.d("btn", "onClick: Button is working properly..");


        else if (checked[0] == 0) {
            Toast.makeText(SignUp.this, "Please Accept our Terms And Conditions.", Toast.LENGTH_SHORT).show();
//                    Log.e("error", "onClick: Button is not working properly.." );

            inProgress(false);



        }
        else {
            button.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Please enter all the fields correctly!!", Toast.LENGTH_SHORT).show();
            button.setVisibility(View.VISIBLE);
        }
    }
    public void inProgress(boolean progress)
    {
        if (progress)
        {
            progressBar.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      if (requestCode == 99 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            notification();
        } else {
            Toast.makeText(this, "Please Grant Permissions to use the App..", Toast.LENGTH_SHORT).show();
        }

    }

    public void notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("data_uploaded", "notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "data_uploaded")
                .setContentText("")
                .setSmallIcon(R.drawable.google_play_books)
                .setAutoCancel(true)
                .setContentText("Welcome "+User_Name+" to AssignMate!!");
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());

        if (ActivityCompat.checkSelfPermission(SignUp.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SignUp.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 99);

            return;
        }
        managerCompat.notify(999, builder.build());




    }


    }
