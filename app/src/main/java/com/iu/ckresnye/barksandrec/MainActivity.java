package com.iu.ckresnye.barksandrec;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button bLogin;
    Button bRegister;
    EditText eUsername;
    EditText ePassword;

    ProgressBar bar;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance();
        bar = new ProgressBar(this);

        bLogin = (Button) findViewById(R.id.buttonLogin);
        bRegister = (Button) findViewById(R.id.buttonRegister);
        eUsername = (EditText) findViewById(R.id.inputUsername);
        ePassword = (EditText) findViewById(R.id.InputPassword);
//        ParkEvents e = new ParkEvents("\"Great Pyr Fun\"", "Nov 30",
//                "Come and bring your Pyr for a snow filled fun day at Ferguson!",new ArrayList<String>(),"","", true, 0, "4");
//        dbRef.getReference().child("EVENTS").child("1").child("4").setValue(e);
//        e = new ParkEvents("Santa Paws", "Dec 24",
//                "News from up north hint that a certain dog will be making his rounds this christmas eve! Come enjoy a free 'cookies and milk' and get your holiday picture with Santa Paws!",new ArrayList<String>(),"","", true, 0, "3");
//        dbRef.getReference().child("EVENTS").child("1").child("3").setValue(e);
//        e = new ParkEvents("IU Send Off", "Dec 16",
//                "Come enjoy some treats and say goodbye to our student owners before they head home for the holiday",new ArrayList<String>(),"","", true, 0, "2");
//        dbRef.getReference().child("EVENTS").child("1").child("2").setValue(e);
//        e = new ParkEvents("Super cool event", "Dec 21",
//                "Awesome stuff is going on, but its a surprise!",new ArrayList<String>(),"","", true, 0, "1");
//        dbRef.getReference().child("EVENTS").child("1").child("1").setValue(e);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    public void loginUser()
    {
        String email = eUsername.getText().toString().trim();
        String password = ePassword.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            //email is empty
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            //Password is empty
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }

        bar.setVisibility(View.VISIBLE);

        //both have values if here
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        bar.setVisibility(View.GONE);
                        if(task.isSuccessful())
                        {
                            dbRef.getReference().child("USERS").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(!dataSnapshot.exists())
                                    {
                                        User u = new User();
                                        u.setParkId("1");
                                        u.setHasPet(true);
                                        dbRef.getReference().child("USERS")
                                                .child(firebaseAuth.getCurrentUser().getUid())
                                                .setValue(u);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Unable to Login: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void registerUser()
    {
        String email = eUsername.getText().toString().trim();
        String password = ePassword.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            //email is empty
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            //Password is empty
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }

        bar.setVisibility(View.VISIBLE);

        //both have values if here
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        bar.setVisibility(View.GONE);
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                            //TODO create register sequence
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Unable to Register: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        Toast.makeText(this, "You are successfully Registered !!", Toast.LENGTH_SHORT).show();


    }
}
