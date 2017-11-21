package com.iu.ckresnye.barksandrec;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddPetActivity extends AppCompatActivity {

    EditText petName;
    EditText petBreed;
    ImageView petImage;
    Button imageButton;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK)
        {
            Uri selectedImage = imageReturnedIntent.getData();
            petImage.setImageURI(selectedImage);
            petImage.setVisibility(View.VISIBLE);
        }
        else
        {
            Toast.makeText(this, "Image Error", Toast.LENGTH_SHORT).show();
        }

    }

}
