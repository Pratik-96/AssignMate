package com.example.assignmate;

import static android.view.View.GONE;
import static com.example.assignmate.MainActivity.SUBJECT_NAME;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    LinearLayout java,dm,os,ai,cc,activity,placement;

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
        TextView uname = view.findViewById(R.id.uname);
        activity = view.findViewById(R.id.Activities);
        placement=view.findViewById(R.id.Placement);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

        if (account!=null) {
            String name;
            if (account.getDisplayName().split("\\w+").length > 1) {
                int firstSpace = account.getDisplayName().indexOf(" ");
                name = account.getDisplayName().substring(0, firstSpace);
            } else
            {
                name = account.getDisplayName();
            }
            uname.setText(name+" !");
        }
        else
        {
            String name;
            if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName()!=null) {
                if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName().split("\\w+").length > 1) {
                    int firstSpace = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().indexOf(" ");
                    name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().substring(0, firstSpace);

                } else {
                    name = mAuth.getCurrentUser().getDisplayName();
                }
            }
            else
            {
                name="User";
            }

                uname.setText(name+" !");
            }


        ImageView exit = view.findViewById(R.id.exit_app);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Do you really want to exit the app ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getActivity().moveTaskToBack(true);
                                getActivity().finish();
                            }
                        }).setNegativeButton("No",null).show();

            }
        });

        java.setVisibility(GONE);
        os.setVisibility(GONE);
        ai.setVisibility(GONE);
        cc.setVisibility(GONE);
        placement.setVisibility(GONE);
        dm.setVisibility(GONE);
        activity.setVisibility(GONE);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                java.setVisibility(View.VISIBLE);
                java.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.home_anim));
            }
        },1100);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dm.setVisibility(View.VISIBLE);
                dm.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.home_anim));
            }
        },1200);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                os.setVisibility(View.VISIBLE);
                os.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.home_anim));
            }
        },1300);

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ai.setVisibility(View.VISIBLE);
                ai.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.home_anim));
            }
        },1400);
        ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), documentType.class);
                intent.putExtra(SUBJECT_NAME,"Artificial Intelligence");
                startActivity(intent);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cc.setVisibility(View.VISIBLE);
                cc.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.home_anim));
            }
        },1500);

        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), documentType.class);
                intent.putExtra(SUBJECT_NAME,"Cloud Computing");
                startActivity(intent);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.setVisibility(View.VISIBLE);
                activity.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.home_anim));
            }
        },1600);
        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), fetch_files.class);
                Bundle bundle = new Bundle();
                bundle.putString("docType"," ");
                bundle.putString("name","Activities");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                placement.setVisibility(View.VISIBLE);
                placement.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.home_anim));
            }
        },1700);
        placement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), fetch_files.class);
                Bundle bundle = new Bundle();
                bundle.putString("docType","Placement");
                bundle.putString("name","Placement Preparation");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Calendar c = Calendar.getInstance();
        int hrs = c.get(Calendar.HOUR_OF_DAY);
        Log.d("hrs", "onCreateView: "+hrs);

        if (hrs>=1 && hrs<12)
        {
         greet.setText("Good morning");
        } else if (hrs>=12 && hrs<16) {
           greet.setText("Good afternoon");
        }
        else
        {
           greet.setText("Good evening");
        }



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(GONE);
                cards.setVisibility(View.VISIBLE);

            }
        },1000);


        mAuth=FirebaseAuth.getInstance();



        return view;
    }

}