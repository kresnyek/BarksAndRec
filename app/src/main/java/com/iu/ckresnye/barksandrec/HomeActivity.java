package com.iu.ckresnye.barksandrec;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeActivity extends AppCompatActivity {
    //TODO change layout from just these 3 buttons
    Button bPet;
    Button bPark;
    Button bSettings;
    Button bLogout;
    ImageView iPark;
    TextView userNameGreeting;


    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseUser user;
    private FirebaseDatabase dbRef;
    private StorageReference mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        bPet = (Button) findViewById(R.id.buttonPet);
        bPark = (Button) findViewById(R.id.buttonPark);
        bSettings = (Button) findViewById(R.id.buttonSetting);
        bLogout = (Button) findViewById(R.id.logoutButton);
        iPark = (ImageView) findViewById(R.id.ImageHomeDisplay);

        userNameGreeting = (TextView) findViewById(R.id.UserGreetingText);
        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                getUser();
            }
        };
        dbRef = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference().child("Park").child("1");

        Glide.with(this).using(new FirebaseImageLoader()).load(mStorage).into(iPark);

        bPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUser();
            }
        });

        bPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), CommunityActivity.class);
                startActivity(i);
            }
        });

        bSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), MapsActivity.class);
                startActivity(i);
            }
        });

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private void checkUser()
    {
        dbRef.getReference().child("USERS").child(auth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        Intent i = new Intent(HomeActivity.this, (u.getHasPet() ? PetProfileActivity.class: AddPetActivity.class ));
                        startActivity(i);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    public void signOut()
    {
        auth.signOut();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    public void getUser()
    {
        user = auth.getCurrentUser();
        if(user == null)
        {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else
        {
            userNameGreeting.setText("Hello, " + user.getEmail());
        }

    }

}
