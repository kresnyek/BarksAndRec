package com.iu.ckresnye.barksandrec;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventsActivity extends AppCompatActivity {
    ArrayList<ParkEvents> events;
    EventsAdapter adapter;
    RecyclerView recyclerView;
    User user;

    FirebaseAuth auth;
    FirebaseDatabase dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        dbref = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        events = new ArrayList<>();

        dbref.getReference().child("USERS").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                dbref.getReference().child("EVENTS").child(user.getParkId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren())
                        {
                            events.add(data.getValue(ParkEvents.class));
                        }
                        adapter = new EventsAdapter(events, getApplicationContext());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(EventsActivity.this));
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


    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }

}
