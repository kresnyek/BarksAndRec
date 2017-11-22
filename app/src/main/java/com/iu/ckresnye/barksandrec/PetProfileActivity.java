package com.iu.ckresnye.barksandrec;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PetProfileActivity extends AppCompatActivity {

    Button bAddFitBark, bEdit;
    TextView tName, tBreed, tBday, tSteps;
    EditText eName, eBreed, eBDay;
    ImageView iPet;
    TabHost tabs;

    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);
        bAddFitBark = (Button) findViewById(R.id.buttonAddFitBark);
        bEdit = (Button) findViewById(R.id.buttonEditSubmit);
        tName = (TextView) findViewById(R.id.textViewPetName);
        tBreed = (TextView) findViewById(R.id.textViewBreed);
        tBday = (TextView) findViewById(R.id.textViewBirthday);
        tSteps = (TextView) findViewById(R.id.textViewSteps);
        eName = (EditText) findViewById(R.id.editTextName);
        eBreed = (EditText) findViewById(R.id.editTextBreed);
        eBDay = (EditText) findViewById(R.id.editTextBirthday);
        iPet = (ImageView) findViewById(R.id.imageViewPet);
        tabs = (TabHost) findViewById(R.id.tab_host);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference().child(user.getUid());
        dbRef = FirebaseDatabase.getInstance().getReference();

        Glide.with(this).using(new FirebaseImageLoader()).load(mStorageReference).into(iPet);


        dbRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getPetInfo(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tabs.setup();
        TabHost.TabSpec spec1 = tabs.newTabSpec("About Tab");
        spec1.setContent(R.id.About);
        spec1.setIndicator("About");
        tabs.addTab(spec1);

        TabHost.TabSpec spec2 = tabs.newTabSpec("Goals Tab");
        spec2.setContent(R.id.Goals);
        spec2.setIndicator("Goals");
        tabs.addTab(spec2);

        TabHost.TabSpec spec3 = tabs.newTabSpec("Edit Tab");
        spec3.setContent(R.id.Edit);
        spec3.setIndicator("Edit");
        tabs.addTab(spec3);

    }

    void loadImage(FileDownloadTask.TaskSnapshot taskSnapshot)
    {
        //taskSnapshot.
    }

    void getPetInfo(DataSnapshot dataSnapshot)
    {
        ArrayList data = new ArrayList<String>();
        Pet pet = new Pet();

        for(DataSnapshot petDataSnapshot: dataSnapshot.getChildren())
        {
            data.add(petDataSnapshot.getValue().toString());
            //Toast.makeText(this, "text: " + p, Toast.LENGTH_LONG).show();

            //pets.add(new com.iu.ckresnye.barksandrec.Pet(pet.getName(), pet.getBreed(), pet.getBday()));
        }

        pet.setName((String)data.get(2));
        pet.setBreed((String) data.get(1));
        //pet.setBday((Date) data.get(0));

        Log.i("CASSIE", pet.toString());

        //For One pet for now
        tName.setText(pet.getName());
        tBreed.setText(pet.getBreed());
        tBday.setText(pet.getBday().toString());

    }



}
