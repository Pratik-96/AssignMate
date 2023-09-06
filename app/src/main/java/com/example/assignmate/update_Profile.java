package com.example.assignmate;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link update_Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class update_Profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public update_Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment update_Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static update_Profile newInstance(String param1, String param2) {
        update_Profile fragment = new update_Profile();
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

    Button updateProfile;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_update__profile, container, false);
        ImageView backToProfile = view.findViewById(R.id.backtoProfile);
        EditText name = view.findViewById(R.id.name);
        EditText email = view.findViewById(R.id.email);
        EditText pass = view.findViewById(R.id.password);
        updateProfile = view.findViewById(R.id.update);
         progressBar = view.findViewById(R.id.progressBar);
        TextView backToAuth = view.findViewById(R.id.backtoauth);

        name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        backToAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new reauthenticate());
            }
        });

        backToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new Profile());
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname  = name.getText().toString();
                String uemail = email.getText().toString();
                if (!uname.equals("") && !uemail.equals(""))
                {

                    update(uname,uemail);
                }
            }
        });


        return view;
    }
    private void replaceFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
    private void update(String name,String email)  {
        progressBar.setVisibility(View.VISIBLE);
        updateProfile.setVisibility(View.GONE);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        try {
            FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(getContext(), "Name Updated Successfully..!!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        updateProfile.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        updateProfile.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), "Unknown Error occurred , Please authenticate again to continue.!!", Toast.LENGTH_SHORT).show();
        }

        try {
            FirebaseAuth.getInstance().getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(getContext(), "Email Updated Successfully..!!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        updateProfile.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        updateProfile.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), "Unknown Error occurred , Please authenticate again to continue.!!", Toast.LENGTH_SHORT).show();
        }


    }

}