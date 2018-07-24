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
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class AddTaskActivity extends AppCompatActivity {

    private EditText taskTitle;
    private TextView newTaskText;
    private Button createTask;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = FirebaseFirestore.getInstance();

        createTask = (Button) findViewById(R.id.createTask);
        taskTitle = (EditText) findViewById(R.id.newTaskTitle);
        newTaskText = (TextView) findViewById(R.id.newTaskText);

        createTask.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                //mDatabase.child("notify").child("tasks").child(taskTitleToSave).setValue("Notseen");
                Map<String, Object> task = new HashMap<>();
                task.put("title", taskTitle.getText().toString());
                String userGroupId = ((MyApplication) getApplication()).getGroupId();

                // Add a new document with a generated ID
                db.collection("userGroups").document(userGroupId).collection("tasks").document(taskTitle.getText().toString())
                        .set(task)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent i = new Intent(AddTaskActivity.this, MainTeacherActivity.class);
                                startActivity(i);
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
