package com.example.shop;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private final int GOOGLE_KEY_AUTHENTICATION = 7377512;
    private SignInButton signInButton;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions googleSignInOptions;
    private ProfileFragmentActivated profileFragmentActivated;
    private static FirebaseAuth auth;
    private DatabaseReference firebaseDBRef;
    private FirebaseUser firebaseUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        firebaseDBRef = FirebaseDatabase.getInstance().getReference();
        profileFragmentActivated = new ProfileFragmentActivated();
        googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getContext(), googleSignInOptions);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        signInButton = view.findViewById(R.id.signInButton);

        setGooglePlusButtonText(signInButton, "Войти с помощью Google");
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent googleIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(googleIntent, GOOGLE_KEY_AUTHENTICATION);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_KEY_AUTHENTICATION) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),
                                                                                null);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {


                                    ValueEventListener vel = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            boolean flagAuthenticate = false;
                                            for(DataSnapshot curDataSnapshot : snapshot.getChildren()) {

                                                if(curDataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid() + "")) {

                                                    flagAuthenticate = true;
                                                    break;
                                                }
                                            }

                                            if(!flagAuthenticate) {

                                                firebaseUser = auth.getCurrentUser();
                                                User user = new User();
                                                user.setUserId(firebaseUser.getUid());
                                                user.setName(firebaseUser.getDisplayName());
                                                firebaseDBRef
                                                    .child("Users")
                                                    .child(firebaseUser.getUid())
                                                    .setValue(user);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    };
                                    firebaseDBRef.addValueEventListener(vel);
                                    signInProfile();
                                }
                            }
                        });
            } catch (ApiException e) {

                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null)
            signInProfile();
    }

    private void signInProfile() {

        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framelayout, profileFragmentActivated)
                .commit();
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {


        //signInButton.getLayoutParams().width = MainActivity.getWidthWindow();
        for (int indexChildParams = 0; indexChildParams < signInButton.getChildCount();
                                                                    indexChildParams++) {

            View view = signInButton.getChildAt(indexChildParams);
            if (view instanceof TextView) {

                TextView buttonTextView = (TextView) view;
                buttonTextView.setTextSize(14);
                buttonTextView.setTextColor(getResources().getColor(R.color.black));
                buttonTextView.setText(buttonText);
                return;
            }
        }
    }
}