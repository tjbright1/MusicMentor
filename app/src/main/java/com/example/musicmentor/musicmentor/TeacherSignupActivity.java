package com.example.musicmentor.musicmentor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class TeacherSignupActivity extends AppCompatActivity {

    private EditText price;
    private EditText age;
    private EditText credentials;
    private TextView playingLevelText;
    private Spinner playingLevel;
    private Button submit;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();

        price = (EditText) findViewById(R.id.editText);
        age = (EditText) findViewById(R.id.editText2);
        credentials = (EditText) findViewById(R.id.editText4);
        playingLevelText = (TextView) findViewById(R.id.textView8);
        playingLevel = (Spinner) findViewById(R.id.spinner);
        submit = (Button) findViewById(R.id.button2);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.levels, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        playingLevel.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String userId = intent.getStringExtra("userId");
                String instrument = intent.getStringExtra("instrument");
                String name = intent.getStringExtra("name");
                Map<String, Object> user = new HashMap<>();
                user.put("price", price.getText().toString());
                user.put("age", age.getText().toString());
                user.put("credentials", credentials.getText().toString());
                user.put("playingLevel", playingLevel.getSelectedItem().toString());
                user.put("userType", "Teacher");
                user.put("userGroupId", "SHV9e03KmJUpEV9jKFAF");
                user.put("instrument", instrument);
                user.put("name", name);

                db.collection("users").document(userId).set(user)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();
                                startActivity(new Intent(TeacherSignupActivity.this, HomePageTeacherActivity.class));
                            }
                        });
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

}
