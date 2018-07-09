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
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class AddTaskActivity extends AppCompatActivity {

    private EditText taskTitle;
    private TextView newTaskText;
    private Button createTask;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createTask = (Button) findViewById(R.id.createTask);
        taskTitle = (EditText) findViewById(R.id.newTaskTitle);
        newTaskText = (TextView) findViewById(R.id.newTaskText);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        createTask.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                String taskTitleToSave = taskTitle.getText().toString();
                mDatabase.child("lessons").child("currentLesson").child("tasks").child(taskTitleToSave).setValue("taskinfo");
                Intent i = new Intent(getApplicationContext(),MainTeacherActivity.class);
                startActivity(i);
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
