package com.example.musicmentor.musicmentor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        db = FirebaseFirestore.getInstance();

        final HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        final String userGroupId = ((MyApplication) getApplication()).getGroupId();
        Log.v("clifford", userGroupId);
        db.collection("userGroups").document(userGroupId).collection("tasks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (final DocumentSnapshot document: task.getResult()){
                            List<String> taski = new ArrayList<String>();
                            String taskTitle = (String) document.get("title");
                            Map<String, Object> docs = document.getData();
                            for (final String recording : docs.keySet()) {
                                if (!recording.equals("title")) {
                                    taski.add(recording);
                                }
                            }
                            expandableListDetail.put("Task: " + taskTitle, taski);
                            expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                            expandableListAdapter = new CustomExpandableListAdapter(MainActivity.this, expandableListTitle, expandableListDetail, "Teacher");
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

                                    finish();
                                    Intent intent;
                                    Log.v("parent: ", parent.getExpandableListAdapter().getGroup(groupPosition).toString());
                                    if (parent.getExpandableListAdapter().getChild(groupPosition, childPosition).toString() == "Add New Recording +") {
                                        intent = new Intent(MainActivity.this, NewVideoStudent.class);
                                        intent.putExtra("groupPosition", Integer.toString(groupPosition));
                                        intent.putExtra("childPosition", Integer.toString(childPosition));
                                        intent.putExtra("parent", parent.getExpandableListAdapter().getGroup(groupPosition).toString());
                                        intent.putExtra("title", parent.getExpandableListAdapter().getChild(groupPosition, childPosition).toString());
                                    } else {
                                        Log.i("creating", "intent");
                                        intent = new Intent(MainActivity.this, ViewRecordingStudent.class);
                                        intent.putExtra("groupPosition", Integer.toString(groupPosition));
                                        intent.putExtra("childPosition", Integer.toString(childPosition));
                                        intent.putExtra("title", parent.getExpandableListAdapter().getChild(groupPosition, childPosition).toString());
                                        intent.putExtra("parent", parent.getExpandableListAdapter().getGroup(groupPosition).toString());

                                    }
                                    Log.i("starting","activity");
                                    startActivity(intent);
                                    return false;
                                }
                            });
                        }
                    }
                });

    }

}
