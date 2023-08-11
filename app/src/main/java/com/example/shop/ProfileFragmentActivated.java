package com.example.shop;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ProfileFragmentActivated extends Fragment {

    private FirebaseAuth auth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_activated, container, false);
        Button logoutButton = view.findViewById(R.id.logOutButton);
        TextView profileNameTextView = view.findViewById(R.id.profileNameTextView);
        ImageView profileImageView = view.findViewById(R.id.profileImageView);

        profileNameTextView.setText("Привет, " + auth.getCurrentUser().getDisplayName() + "!");
        Picasso.get().load(auth.getCurrentUser().getPhotoUrl()).into(profileImageView);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framelayout, new ProfileFragment())
                        .commit();
            }
        });

        return view;
    }
}