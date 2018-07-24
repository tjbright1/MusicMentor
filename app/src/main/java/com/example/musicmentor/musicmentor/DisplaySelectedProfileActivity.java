package com.example.musicmentor.musicmentor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

public class DisplaySelectedProfileActivity extends AppCompatActivity {

    private TextView nameText, ageText, instrumentText, priceText, levelText, credentialsText;
    private Button request;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_selected_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameText = findViewById(R.id.textView23);
        ageText = findViewById(R.id.textView24);
        instrumentText = findViewById(R.id.textView25);
        priceText = findViewById(R.id.textView26);
        levelText = findViewById(R.id.textView28);
        credentialsText = findViewById(R.id.textView19);
        request = findViewById(R.id.button4);
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String age = intent.getStringExtra("age");
        final String credentials = intent.getStringExtra("credentials");
        final String playingLevel = intent.getStringExtra("playingLevel");
        final String price = intent.getStringExtra("price");
        final String instrument = intent.getStringExtra("instrument");

        nameText.setText(name);
        ageText.setText(age);
        credentialsText.setText(credentials);
        levelText.setText(playingLevel);
        priceText.setText(price);
        instrumentText.setText(instrument);
        db = FirebaseFirestore.getInstance();

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(DisplaySelectedProfileActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(DisplaySelectedProfileActivity.this);
                }
                builder.setTitle("Request User")
                        .setMessage("Are you sure you want to request this user?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.v("Requestuser", "now");
                                CollectionReference usersRef = db.collection("users");
                                final Query query = usersRef.whereEqualTo("age", age);
                                if (query != null) {
                                    query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                            if (queryDocumentSnapshots != null) {
                                                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                    Log.v("COOLLL", "Now");
                                                    String price = snapshot.getString("price");
                                                    Log.v("PRICEEE", price);
                                                    String id = snapshot.getId();
                                                    Log.v("ID", id);
                                                    DocumentReference setRef = db.collection("users").document(id).collection("request").document();
                                                    Map<String, Object> user = new HashMap<>();
                                                    Object request = new Object();
                                                    final String username = ((MyApplication) getApplication()).getUsername();
                                                    user.put("name", username);
                                                    final String hash = getSaltString();
                                                    user.put("hash", hash);
                                                    setRef.set(user)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    final FirebaseUser user = mAuth.getCurrentUser();
                                                                    Map<String, Object> useri = new HashMap<>();
                                                                    useri.put("userGroupId", hash);
                                                                    db.collection("users").document(user.getUid()).collection("userGroupIds").document()
                                                                            .set(useri)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    finish();
                                                                                    Intent intent = new Intent(DisplaySelectedProfileActivity.this, HomePageStudentActivity.class);
                                                                                    startActivity(intent);
                                                                                }
                                                                            });
                                                                }
                                                            });
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

}
