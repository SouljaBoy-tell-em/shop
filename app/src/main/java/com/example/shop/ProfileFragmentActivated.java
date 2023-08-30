package com.example.shop;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.example.shop.databinding.FragmentProfileActivatedBinding;

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
//        FirebaseAuth.getInstance().signOut();


//        Button logoutButton = view.findViewById(R.id.logoutButton);
//
//        logoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                FirebaseAuth.getInstance().signOut();
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.framelayout, new ProfileFragment())
//                        .commit();
//            }
//        });

        return view;
    }

//    private void createTopProfile(View view) {
//
//        TextView profileNameTextView = view.findViewById(R.id.profileNameTextView);
//        ImageView profileImageView = view.findViewById(R.id.profileImageView);
//        profileNameTextView.setText("Привет, " + auth.getCurrentUser().getDisplayName() + "!");
//        Picasso.get().load(auth.getCurrentUser().getPhotoUrl()).into(profileImageView);
//    }

}