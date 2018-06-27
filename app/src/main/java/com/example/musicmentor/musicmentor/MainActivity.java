package com.example.musicmentor.musicmentor;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Logic borrowed from a tutorial at https://dzone.com/articles/android-tips
 * */
public class MainActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;

    private StorageReference videoRef;


    TextView task_1;
    TextView video_1;
    Button button;
    VideoView videoView;
    VideoView resultVideo;

    private ExpandableListView expandableListView;

    String[] parent = new String[]{"Lesson1", "Lesson2"};
    String[] t1 = new String[]{"Task1", "Task2"};
    String[] t2 = new String[]{"Task1"};
    String[] video1 = new String[]{"Video1"};
    String[] video2 = new String[]{"Video2"};
    String[] video3 = new String[]{"Video1"};

    LinkedHashMap<String, String[]> thirdLevelq1 = new LinkedHashMap<>();
    LinkedHashMap<String, String[]> thirdLevelq2 = new LinkedHashMap<>();
    LinkedHashMap<String, String[]> thirdLevelq3 = new LinkedHashMap<>();
    /**
     * Second level array list
     */
    List<String[]> secondLevel = new ArrayList<>();
    /**
     * Inner level data
     */
    List<LinkedHashMap<String, String[]>> data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("tag2", "activitycreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        task_1 = (TextView) findViewById(R.id.task_1);

        video_1 = (TextView) findViewById(R.id.video_1);

        task_1.setVisibility(View.GONE);    // hide until lesson is clicked
        video_1.setVisibility(View.GONE);   // hide until task is clicked

        button = (Button) findViewById(R.id.button);
        resultVideo = (VideoView) findViewById(R.id.videoView);

        setUpAdapter();
    }
    /**
     * onClick handler
     */
    public void toggle_lesson(View v) {
        task_1.setVisibility( task_1.isShown()
                ? View.GONE
                : View.VISIBLE );
        video_1.setVisibility( View.GONE );
//        resultVideo.setVisibility( View.GONE );
//        button.setVisibility( View.GONE );
    }

    public void toggle_task(View v) {
        video_1.setVisibility( video_1.isShown()
                ? View.GONE
                : View.VISIBLE );
//        resultVideo.setVisibility( View.GONE );
//        button.setVisibility( View.GONE );
    }

//    public void toggle_video(View v) {
//        resultVideo.setVisibility( resultVideo.isShown()
//            ? View.GONE
//            : View.VISIBLE);
//
//        button.setVisibility( resultVideo.isShown()
//            ? View.GONE
//            : View.VISIBLE);
//    }


    public void dispatchTakeVideoIntent(View videoView) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.v("tag1", "activityresult");
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            resultVideo.setVideoURI(videoUri);
            Log.v("videouri: ", videoUri.toString());

            //Upload file to firebase
            //Uri file = Uri.fromFile(new File(videoUri.toString()));


            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            videoRef = storageRef.child("/videos/"+"newvideo.3pg");
            UploadTask uploadTask = videoRef.putFile(videoUri);

// Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Log.v("tag","SUCCESS");
                }
            });
        }
    }



    private void setUpAdapter() {
        secondLevel.add(t1);
        secondLevel.add(t2);
        thirdLevelq1.put(t1[0], video1);
        thirdLevelq1.put(t1[1], video2);
        thirdLevelq2.put(t2[0], video3);

        data.add(thirdLevelq1);
        data.add(thirdLevelq2);
        data.add(thirdLevelq3);
        expandableListView = (ExpandableListView) findViewById(R.id.expandible_listview);
        //passing three level of information to constructor
        ThreeLevelListAdapter threeLevelListAdapterAdapter = new ThreeLevelListAdapter(this, parent, secondLevel, data);
        expandableListView.setAdapter(threeLevelListAdapterAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });


    }

}
