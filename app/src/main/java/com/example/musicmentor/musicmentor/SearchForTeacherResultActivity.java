package com.example.musicmentor.musicmentor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class SearchForTeacherResultActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseFirestore mFirestore;
    private RecyclerView mMainList;
    private FirebaseFirestore db;
    private List<String> namesList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_teacher_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = FirebaseFirestore.getInstance();
        listView = findViewById(R.id.listView);
        namesList = new ArrayList<>();

        Intent intent = getIntent();
        String highPrice = intent.getStringExtra("highPrice");
        String instrument = intent.getStringExtra("instrument");
        String level = intent.getStringExtra("level");

        CollectionReference usersRef = db.collection("users");
        Log.v("INSTVAL", instrument);
        Log.v("LEVELVAL", level);
        final Query query = usersRef.whereEqualTo("instrument", instrument).whereEqualTo("playingLevel", level).whereLessThan("price", highPrice);
        if (query != null) {
            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (queryDocumentSnapshots != null) {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            String name = snapshot.getString("name");
                            String age = snapshot.getString("age");
                            String playingLevel = snapshot.getString("playingLevel");
                            String price = snapshot.getString("price");
                            String instrument = snapshot.getString("instrument");
                            namesList.add(snapshot.getString("name") + "      Price: " + price + "      Instrument: " + instrument + "      Level: " + playingLevel);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item, namesList) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                // Get the Item from ListView
                                View view = super.getView(position, convertView, parent);

                                // Initialize a TextView for ListView each Item
                                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                                // Set the text color of TextView (ListView Item)
                                tv.setTextColor(Color.RED);

                                // Generate ListView Item using TextView
                                return view;
                            }
                        };
                        adapter.notifyDataSetChanged();
                        Log.v("SETIT:", "ADAP");
                        listView.setAdapter(adapter);
                    }
                }
            });
        }
    }



}
