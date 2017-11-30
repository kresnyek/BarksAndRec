package com.iu.ckresnye.barksandrec;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventDetailActivity extends AppCompatActivity {
    Button bRSVP;
    TextView tTitle, tDate, tDetails;
    String id;
    ParkEvents event;

    FirebaseAuth auth;
    FirebaseDatabase dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        bRSVP = (Button) findViewById(R.id.buttonJoinEvent);
        tTitle = (TextView) findViewById(R.id.textViewEventTitleD);
        tDate = (TextView) findViewById(R.id.textViewDated);
        tDetails = (TextView) findViewById(R.id.textViewEventDetailsd);

        Bundle b = getIntent().getExtras();
        if(b.getString("id") != null)
            id = b.getString("id");
        else
            finish();

        Log.i("CASSIE", "???: " + id);

        //get event info
        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance();

        dbRef.getReference().child("EVENTS").child("1").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                event = dataSnapshot.getValue(ParkEvents.class);

                tTitle.setText(event.getTitle());
                tDate.setText(event.getDate());
                tDetails.setText(event.getDetails());

                if(event.getAttendees().contains(auth.getCurrentUser().getUid()))
                {
                    bRSVP.setEnabled(false);
                    bRSVP.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }



        });

        bRSVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.getAttendees().add(auth.getCurrentUser().getUid());
                dbRef.getReference().child("EVENTS").child("1").child(id).setValue(event);//update
                startActivity(new Intent(view.getContext(), EventsActivity.class));
            }
        });




    }

}
