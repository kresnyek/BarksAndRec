package com.iu.ckresnye.barksandrec;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

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
        tBreed = (TextView) findViewById(R.id.editTextBreed);
        tBday = (TextView) findViewById(R.id.textViewBirthday);
        tSteps = (TextView) findViewById(R.id.textViewSteps);
        eName = (EditText) findViewById(R.id.editTextName);
        eBreed = (EditText) findViewById(R.id.editTextBreed);
        eBDay = (EditText) findViewById(R.id.editTextBirthday);
        iPet = (ImageView) findViewById(R.id.imageViewPet);
        tabs = (TabHost) findViewById(R.id.tab_host);

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

}
