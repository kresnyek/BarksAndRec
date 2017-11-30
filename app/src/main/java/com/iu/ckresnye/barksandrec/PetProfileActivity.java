package com.iu.ckresnye.barksandrec;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static com.iu.ckresnye.barksandrec.BuildConfig.CLIENT_ID;

public class PetProfileActivity extends AppCompatActivity {

    Button bAddFitBark, bEdit;
    TextView tName, tBreed, tBday, tGoal, tCurrent;
    EditText eName, eBreed, eBDay;
    ImageView iPet;
    TabHost tabs;
    WebView web;
    View fitBarkHider;

    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private StorageReference mStorageReference;
    private String accessCode;
    private String token;

    private static String fitBarkURL = "https://app.fitbark.com/oauth/authorize";
    private Pet pet;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);
        bAddFitBark = (Button) findViewById(R.id.buttonAddFitBark);
        bEdit = (Button) findViewById(R.id.buttonEditSubmit);
        tName = (TextView) findViewById(R.id.textViewPetName);
        tBreed = (TextView) findViewById(R.id.textViewBreed);
        tBday = (TextView) findViewById(R.id.textViewBirthday);
        tGoal = (TextView) findViewById(R.id.currentGoalTextView);
        tCurrent = (TextView) findViewById(R.id.averagesTextView);
        eName = (EditText) findViewById(R.id.editTextName);
        eBreed = (EditText) findViewById(R.id.editTextBreed);
        eBDay = (EditText) findViewById(R.id.editTextBirthday);
        iPet = (ImageView) findViewById(R.id.imageViewPet);
        tabs = (TabHost) findViewById(R.id.tab_host);
        web = (WebView) findViewById(R.id.web_view);
        fitBarkHider = (View) findViewById(R.id.fitbarkHider);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference().child("Pet").child(user.getUid());
        dbRef = FirebaseDatabase.getInstance().getReference();

        Glide.with(this).using(new FirebaseImageLoader()).load(mStorageReference).into(iPet);

        dbRef.child("USERS").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getUserInfo(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        dbRef.child("PETS").child(user.getUid()).addValueEventListener(new ValueEventListener() {
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

        bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pet.setName(eName.getText().toString());
                pet.setBreed(eBreed.getText().toString());
                //pet.setBday(new Date(eBDay.getText().toString()));

                dbRef.child("PETS").child(auth.getCurrentUser().getUid()).setValue(pet);
            }
        });
        bAddFitBark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpFitBark();
            }
        });

    }

    @Override
    public void onResume()
    {
        super.onResume();
        // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent
        Uri uri = getIntent().getData();
        if (uri == null){
            //

        }
        if (uri != null && uri.toString().contains(BuildConfig.REDIRECT)) {
            // use the parameter your API exposes for the code (mostly it's "code")
            String code = uri.getQueryParameter("code");
            if (code != null) {
                // get access token
                // we'll do that in a minute
            } else if (uri.getQueryParameter("error") != null) {
                Log.d("CASSIE", "ERROR ERROR ERROR");
            }
        }
    }

    void getPetInfo(DataSnapshot dataSnapshot)
    {

            pet = dataSnapshot.getValue(Pet.class);

            if(pet != null) {

                tName.setText(pet.getName());
                tBreed.setText(pet.getBreed());
                tBday.setText(pet.getBday().toString());

                fitBarkHider.setVisibility(pet.getActivatedFitbark() ? View.GONE : View.VISIBLE);

                if(pet.getActivatedFitbark())
                {
                    updateUserInfo();
                }
            }
            else
            {
                //somehow got here even without a pet, redirect
                Intent i = new Intent(this, AddPetActivity.class);
                startActivity(i);
                finish();
            }

    }

    void getUserInfo(DataSnapshot dataSnapshot)
    {
        mUser = dataSnapshot.getValue(User.class);
        if(mUser == null)
        {
            //do stuff

        }


    }

    void setUpFitBark()
    {
        web.setVisibility(View.VISIBLE);
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                checkIfDone(view, url);

            }
        });
        web.loadUrl(fitBarkURL + "?response_type=code&" + "client_id=" + CLIENT_ID + "&redirect_uri=" + BuildConfig.REDIRECT);

    }

    void checkIfDone(WebView view, String url)
    {
        if (!url.contains("client_id")) {
           accessCode = url.substring(url.lastIndexOf('/')+1).trim();
            view.destroy();
            view.setVisibility(View.GONE);
            secondOauthStep();

        }
    }

    void secondOauthStep()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                OutputStream out = null;
                try {
                    // Add your data
                    URL url = new URL("https://app.fitbark.com/oauth/token");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    out = new BufferedOutputStream(conn.getOutputStream());
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                    String data = "client_id=" + BuildConfig.CLIENT_ID
                            + "&client_secret=" + BuildConfig.CLIENT_SECRET
                            + "&grant_type=authorization_code&redirect_uri=" + BuildConfig.REDIRECT
                            + "&code=" + accessCode;
                    writer.write(data);

                    writer.flush();

                    writer.close();

                    out.close();

                    conn.connect();
                    int responseCode = conn.getResponseCode();

                    String response = "";

                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            response += line;
                        }

                        mUser.setToken(response.substring(17, response.indexOf("\",\"token_type")));
                        pet.setActivatedFitbark(true);
                        dbRef.child("USERS").child(auth.getCurrentUser().getUid()).setValue(mUser);
                        dbRef.child("PETS").child(auth.getCurrentUser().getUid()).setValue(pet);
                    }
                } catch (Exception e) {
                    Log.e("CASSIE", "eh", e);
                }


            }
        }).start();

    }

    void updateUserInfo()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://app.fitbark.com/api/v2/dog_relations");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Authorization", "Bearer " + mUser.getToken());

                    conn.connect();
                    int responseCode = conn.getResponseCode();

                    String response = "";

                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            response += line;
                        }

                        JSONObject json = new JSONObject(response);
                        String dogSlug = json.getJSONArray("dog_relations").getJSONObject(0).getJSONObject("dog").getString("slug");

                        url = new URL("https://app.fitbark.com/api/v2/dog/" + dogSlug);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Authorization", "Bearer " + mUser.getToken());

                        conn.connect();
                        responseCode = conn.getResponseCode();

                        response = "";

                        if (responseCode == HttpsURLConnection.HTTP_OK) {
                            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            while ((line = br.readLine()) != null) {
                                response += line;
                            }

                            json = new JSONObject(response);
                            tCurrent.setText("Average Activity: " + Integer.toString(json.getJSONObject("dog").getInt("activity_value")));
                            tGoal.setText("Daily Goal: " + Integer.toString(json.getJSONObject("dog").getInt("daily_goal")));
                        }

                    }
                }
                    catch(Exception e)
                    {
                        Log.e("CASSIE", e.getMessage());
                    }

            }
        }).start();

    }

}
