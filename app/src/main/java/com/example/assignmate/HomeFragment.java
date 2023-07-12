package com.example.assignmate;

import static com.example.assignmate.MainActivity.SUBJECT_NAME;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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
    // TODO: Rename and change types and number of parameters
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
    LinearLayout java,dm,os,ai,cc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
//        ImageView menu = view.findViewById(R.id.menu);
        LinearLayout cards = view.findViewById(R.id.cards);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        TextView greet = view.findViewById(R.id.greet);
        java = view.findViewById(R.id.javaCard);
        dm = view.findViewById(R.id.dsCard);
        os = view.findViewById(R.id.osCard);
        ai = view.findViewById(R.id.aiCard);
        cc = view.findViewById(R.id.cloudCard);
        ImageView exit = view.findViewById(R.id.exit_app);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

       java.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), documentType.class);
                intent.putExtra(SUBJECT_NAME,"Programming in Java");
                startActivity(intent);
                }
        });
        dm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), documentType.class);
                intent.putExtra(SUBJECT_NAME,"Data Mining & Data Science");
                startActivity(intent);
            }
        });

        os.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), documentType.class);
                intent.putExtra(SUBJECT_NAME,"Principles of Operating Systems");
                startActivity(intent);
            }
        });

        ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), documentType.class);
                intent.putExtra(SUBJECT_NAME,"Artificial Intelligence");
                startActivity(intent);
            }
        });

        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), documentType.class);
                intent.putExtra(SUBJECT_NAME,"Cloud Computing");
                startActivity(intent);
            }
        });

        Calendar c = Calendar.getInstance();
        int hrs = c.get(Calendar.HOUR_OF_DAY);

        if (hrs>=1 && hrs<12)
        {
         greet.setText("Good morning");
        } else if (hrs>12 && hrs<18) {
           greet.setText("Good afternoon");
        }
        else
        {
           greet.setText("Good evening");
        }

        GoogleSignInAccount account  = GoogleSignIn.getLastSignedInAccount(getContext());
       if (account!=null) {
           if (account.getPhotoUrl() != null) {
//               Picasso.get().load(account.getPhotoUrl()).into(menu);

           }
       }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);
                cards.setVisibility(View.VISIBLE);

            }
        },1000);


        mAuth=FirebaseAuth.getInstance();



        if (!(ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED))
      {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
        }

        return view;
    }
}