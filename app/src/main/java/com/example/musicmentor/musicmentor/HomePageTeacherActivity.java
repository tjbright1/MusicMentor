package com.example.musicmentor.musicmentor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class HomePageTeacherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_teacher);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        ImageButton button = (ImageButton) findViewById(R.id.teacherLesson);
        button.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainTeacherActivity.class);
                startActivity(i);
            }
        });

        ImageButton logout = (ImageButton) findViewById(R.id.logoutTeacher);
        logout.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                mAuth.signOut();
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });

        ImageButton userPage = (ImageButton) findViewById(R.id.userPageButton);
        userPage.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),UserPage.class);
                startActivity(i);
            }
        });

    }

}
