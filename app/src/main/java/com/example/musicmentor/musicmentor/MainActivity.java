package com.example.musicmentor.musicmentor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        final HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        mDatabase = FirebaseDatabase.getInstance().getReference("lessons/currentLesson/tasks");
        mDatabase1 = FirebaseDatabase.getInstance().getReference();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        List<String> task = new ArrayList<String>();
                        Log.i("MyTag", child.getKey().toString());
                        for(DataSnapshot video : child.getChildren()) {
                            Log.i("Videoname: ", video.getKey().toString());
                            task.add(video.getKey().toString());
                        }
                        task.add("Add New Recording +");
                        expandableListDetail.put("Task: " + child.getKey().toString(), task);
                    }
                }
                expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                expandableListAdapter = new CustomExpandableListAdapter(MainActivity.this, expandableListTitle, expandableListDetail, "Student");
                expandableListView.setAdapter(expandableListAdapter);
                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                    @Override
                    public void onGroupExpand(int groupPosition) {

                    }
                });

                expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                    @Override
                    public void onGroupCollapse(int groupPosition) {


                    }
                });

                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) {

                        Log.i("change:", parent.getExpandableListAdapter().getChild(groupPosition, childPosition).toString());
                        Log.i("ASDF", "JKL:");

                        finish();
                        Intent intent;
                        Log.v("parent: ", parent.getExpandableListAdapter().getGroup(groupPosition).toString());
                        if (parent.getExpandableListAdapter().getChild(groupPosition, childPosition).toString() == "Add New Recording +") {
                            intent = new Intent(MainActivity.this, NewVideoStudent.class);
                            intent.putExtra("groupPosition", Integer.toString(groupPosition));
                            intent.putExtra("childPosition", Integer.toString(childPosition));
                            intent.putExtra("title", parent.getExpandableListAdapter().getChild(groupPosition, childPosition).toString());
                            intent.putExtra("parent", parent.getExpandableListAdapter().getGroup(groupPosition).toString());
                        } else {
                            intent = new Intent(MainActivity.this, ViewRecordingStudent.class);
                            intent.putExtra("groupPosition", Integer.toString(groupPosition));
                            intent.putExtra("childPosition", Integer.toString(childPosition));
                            intent.putExtra("parent", parent.getExpandableListAdapter().getGroup(groupPosition).toString());
                            intent.putExtra("title", parent.getExpandableListAdapter().getChild(groupPosition, childPosition).toString());
                        }
                        Log.v("childPosition", Integer.toString(childPosition));
                        Log.v("groupPosition", Integer.toString(groupPosition));
                        startActivity(intent);
                        return false;
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }

        });
    }
}


