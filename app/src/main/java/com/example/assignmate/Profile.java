package com.example.assignmate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.Image;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.security.spec.ECField;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.function.ObjIntConsumer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView profileImage = view.findViewById(R.id.profilePic);
        TextView name = view.findViewById(R.id.name);
        TextView email = view.findViewById(R.id.mail);
        LinearLayout about = view.findViewById(R.id.about);
        LinearLayout update = view.findViewById(R.id.update);
        LinearLayout logout = view.findViewById(R.id.logout);
        LinearLayout share = view.findViewById(R.id.share);
        LinearLayout feedback = view.findViewById(R.id.feedback);
        LinearLayout community = view.findViewById(R.id.joinCommunity);
        LinearLayout changeProfileInfo = view.findViewById(R.id.updateProfile);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());


            changeProfileInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        replaceFragment(new reauthenticate());
                    }

                 else
                {
                    Toast.makeText(getContext(), "Cannot update a google profile..!!", Toast.LENGTH_SHORT).show();
                }
                }

            });

        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri communityUrl = Uri.parse("https://chat.whatsapp.com/BZEy8cUtuHU1mAvxAihI2D");
                Intent intent = new Intent(Intent.ACTION_VIEW,communityUrl);
                startActivity(intent);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"assignmate.co@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                intent.setType("message/rfc822");
                try {

                    intent.setPackage("com.google.android.gm");
                }
                catch (Exception e)
                {
                    intent.setPackage(null);
                }
                startActivity(Intent.createChooser(intent,"Give Feedback Using Mail:"));
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                reference.child("Updates").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            String url = (task.getResult().getValue().toString());
                            String message = "Check out AssignMate - Your Ultimate Companion for Academic Success! AssignMate revolutionizes the way students handle assignments and study materials. It offers comprehensive assignment solutions, organized study materials, and an intuitive interface. Download AssignMate today and unleash your academic potential! "+url;
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT,message);
                            intent.setType("text/plain");
                            Intent shareIntent = Intent.createChooser(intent, null);
                            startActivity(shareIntent);
                        }
                    }

                });
            }

        });




        profileImage.setImageResource(R.drawable.profile);
        if (account!=null)
        {
            if (account.getPhotoUrl()!=null) {
                Picasso.get().load(account.getPhotoUrl()).into(profileImage);
            }
            name.setText(account.getDisplayName());
            email.setText(account.getEmail());
        }
        else if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            name.setVisibility(View.VISIBLE);
        }


        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), aboutAssignMate.class));
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                            reference.child("Updates").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful())
                                    {
                                        Uri url = Uri.parse(task.getResult().getValue().toString());
                                       Intent intent = new Intent(Intent.ACTION_VIEW,url);
                                       Intent intent1 = Intent.createChooser(intent,"Update using drive (Recommended)");
                                       Toast.makeText(getContext(), "Downloading Latest Update..", Toast.LENGTH_SHORT).show();
                                       startActivity(intent1);
                                    }
                                }
                            });
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getContext())
                        .setMessage("Are you sure you want to Logout from your account ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                                GoogleSignInClient gsc = GoogleSignIn.getClient(getContext(),gso);
                                gsc.signOut();
                                Intent intent = new Intent(getContext(), Login.class);
                                startActivity(intent);
                               getActivity().finish();

                            }
                        }).setNegativeButton("No",null).show();

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
