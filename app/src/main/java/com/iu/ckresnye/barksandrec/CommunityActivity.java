package com.iu.ckresnye.barksandrec;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CommunityActivity extends AppCompatActivity {
    Button bEvent, bGoals;
    ImageButton bHome;
    ImageView parkPic;
    TextView parkName, parkHours;

    private FirebaseDatabase dbRef;
    private StorageReference storage;
    private FirebaseAuth auth;

    private User user;
    private Park park;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        dbRef = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        bEvent = (Button) findViewById(R.id.buttonEvents);
        bGoals = (Button) findViewById(R.id.buttonGoals);
        bHome = (ImageButton) findViewById(R.id.buttonHome);
        parkPic = (ImageView) findViewById(R.id.imageViewPark);
        parkName = (TextView) findViewById(R.id.textViewParkName);
        parkHours = (TextView) findViewById(R.id.textViewHours);

        dbRef = FirebaseDatabase.getInstance();

        dbRef.getReference().child("USERS").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                dbRef.getReference().child("PARKS").child(user.getParkId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        park = dataSnapshot.getValue(Park.class);
                        parkName.setText(park.getName());
                        parkHours.setText("Open from: " + park.getHours());
                        storage = FirebaseStorage.getInstance().getReference().child("Park").child(user.getParkId());

                        Glide.with(CommunityActivity.this).using(new FirebaseImageLoader()).load(storage).into(parkPic);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CommunityActivity.this, HomeActivity.class));
            }
        });

        bGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CommunityActivity.this, GoalsActivity.class));
            }
        });

        bEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CommunityActivity.this, EventsActivity.class));
            }
        });
    }
}
