package com.example.musicmentor.musicmentor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class HomePageStudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_student);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        ImageButton button = (ImageButton) findViewById(R.id.studentLesson);
        button.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

        ImageButton logout = (ImageButton) findViewById(R.id.logoutStudent);
        logout.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                mAuth.signOut();
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });

    }

}
