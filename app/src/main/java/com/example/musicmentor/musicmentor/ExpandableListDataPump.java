package com.example.musicmentor.musicmentor;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static android.content.ContentValues.TAG;

public class ExpandableListDataPump {

    private static DatabaseReference mDatabase;

    public static HashMap<String, List<String>> getData() {
        final HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        mDatabase = FirebaseDatabase.getInstance().getReference("lessons/currentLesson/tasks");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        List<String> task = new ArrayList<String>();
                        Log.i("MyTag", child.getKey().toString());
                        expandableListDetail.put("Task1: " + child.getKey().toString(), task);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

        /*Query tasks = mDatabase.child("lessons").child("currentlesson").orderByKey();

        List<String> cricket = new ArrayList<String>();
        cricket.add("Video1: Slowly");
        cricket.add("Video2: Fast");

        List<String> football = new ArrayList<String>();
        football.add("Video1");
        football.add("Video2");

        List<String> basketball = new ArrayList<String>();
        basketball.add("Video1");
        basketball.add("Video2");
        basketball.add("Video3");

        expandableListDetail.put("Task1: Play C major Scale", cricket);
        expandableListDetail.put("Task2: A major scale", football);
        expandableListDetail.put("Task3: Play the Russian National Anthem", basketball);*/
        Log.v("returning", "now");
        return expandableListDetail;
    }
}