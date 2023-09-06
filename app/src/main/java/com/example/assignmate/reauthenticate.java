package com.example.assignmate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link reauthenticate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class reauthenticate extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public reauthenticate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment reauthenticate.
     */
    // TODO: Rename and change types and number of parameters
    public static reauthenticate newInstance(String param1, String param2) {
        reauthenticate fragment = new reauthenticate();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_reauthenticate, container, false);
        EditText email = view.findViewById(R.id.email);
        EditText pass = view.findViewById(R.id.password);
        Button reauth = view.findViewById(R.id.authenticate);
        ImageView back = view.findViewById(R.id.backtoProfile);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        Button backToLogin = view.findViewById(R.id.goToLogin);
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Login.class));
                getActivity().finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new Profile());
            }
        });
        reauth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                reauth.setVisibility(View.GONE);
                AuthCredential credential = EmailAuthProvider.getCredential(email.getText().toString(),pass.getText().toString());
                try {
                    FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                replaceFragment(new update_Profile());
                                progressBar.setVisibility(View.GONE);
                                reauth.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                reauth.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
                catch (Exception e)
                {
                    Toast.makeText(getContext(), "Invalid Credentials, Please Login Again.!!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    reauth.setVisibility(View.VISIBLE);
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getContext(), Login.class));
                            getActivity().finish();
                        }
                    });
                }

            }
        });


        return view;
    }
    public void replaceFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

}