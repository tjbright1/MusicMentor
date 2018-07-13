package com.example.musicmentor.musicmentor;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.VideoView;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import java.io.IOException;

import android.Manifest;
import android.media.MediaRecorder;
import java.io.*;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.jar.*;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.app.Activity.RESULT_OK;

public class NewVideoStudent extends Activity {

    static final int REQUEST_VIDEO_CAPTURE = 1;

    private StorageReference videoRef;

    private DatabaseReference mDatabase;


    TextView task_1;
    TextView video_1;
    Button button;
    VideoView videoView;
    VideoView resultVideo;
    TextView feedback;
    Button audioButton;
    Button playButton;

    private static final String LOG_TAG = "AudioRecordTest";

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    final int REQUEST_PERMISSION_CODE = 200;
    private boolean permissionToRecord = false;
    private String[] permissions = {RECORD_AUDIO};


    private static String mFileName = null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("tag2", "activitycreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_video_student);

        audioButton = (Button) findViewById((R.id.audioButton));
        playButton = (Button) findViewById(R.id.playButton);

        button = (Button) findViewById(R.id.button);
        resultVideo = (VideoView) findViewById(R.id.videoView);

        feedback = (TextView) findViewById(R.id.videoFeedbackStudent);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        final String childPosition = getIntent().getStringExtra("childPosition");
        final String groupPosition = getIntent().getStringExtra("groupPosition");

        mDatabase.child(groupPosition).child(childPosition);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.child(groupPosition).child(childPosition).getValue(String.class);
                feedback.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

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

            String childPosition = getIntent().getStringExtra("childPosition");
            String groupPosition = getIntent().getStringExtra("groupPosition");

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            videoRef = storageRef.child("/" + groupPosition + "/" + childPosition + "/" + "newvideo.3pg");
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

}
