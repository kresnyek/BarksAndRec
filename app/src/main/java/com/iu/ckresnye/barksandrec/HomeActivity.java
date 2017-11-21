package com.iu.ckresnye.barksandrec;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    //TODO change layout from just these 3 buttons
    Button bFitBark;
    Button bPaypal;
    Button bMaps;
    Button bLogout;
    TextView userNameGreeting;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        bFitBark = (Button) findViewById(R.id.buttonFitbark);
        bPaypal = (Button) findViewById(R.id.buttonPaypal);
        bMaps = (Button) findViewById(R.id.buttonMaps);
        bLogout = (Button) findViewById(R.id.logoutButton);
        userNameGreeting = (TextView) findViewById(R.id.UserGreetingText);
        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                getUser();
            }
        };

        bFitBark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AddPetActivity.class);
                startActivity(i);
            }
        });

        bPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(view.getContext(), );
//                startActivity(i);
            }
        });

        bMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(view.getContext(), );
//                startActivity(i);
            }
        });

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
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
