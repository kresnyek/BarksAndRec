package com.iu.ckresnye.barksandrec;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class AddPetActivity extends AppCompatActivity {

    EditText petName, petBreed;
    ImageView petImage;
    Button imageButton, submitButton;

    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private StorageReference mStorageReference;
    Uri pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        if(user == null)
        {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        dbRef = FirebaseDatabase.getInstance().getReference();

        petName = (EditText) findViewById(R.id.editTextPetName);
        petBreed = (EditText) findViewById(R.id.editTextPetBreed);
        petImage = (ImageView) findViewById(R.id.petImage);
        imageButton = (Button) findViewById(R.id.buttonAddPetImage);
        submitButton = (Button) findViewById(R.id.buttonSubmit);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerPet();
            }
        });
    }

    void chooseImage()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);//one can be replaced with any action code

    }

    void registerPet()
    {
        String name = petName.getText().toString().trim();
        String breed = petBreed.getText().toString().trim();
        com.iu.ckresnye.barksandrec.Pet newPet = new com.iu.ckresnye.barksandrec.Pet(name, breed, new Date());
        dbRef.child("PETS").child(user.getUid()).setValue(newPet);
        StorageReference petStorage = mStorageReference.child("Pet").child(user.getUid());

        petStorage.putFile(pic).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddPetActivity.this, "Success on Image!", Toast.LENGTH_SHORT);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPetActivity.this, "Failed on Image", Toast.LENGTH_SHORT);
            }
        });

        dbRef.child("USERS").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                u.setHasPet(true);
                dbRef.child("USERS").child(auth.getCurrentUser().getUid()).setValue(u);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toast.makeText(this, "New pet added!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK)
        {
            pic = imageReturnedIntent.getData();
            petImage.setImageURI(pic);
            petImage.setVisibility(View.VISIBLE);
        }
        else
        {
            Toast.makeText(this, "Image Error", Toast.LENGTH_SHORT).show();
        }

    }

}
