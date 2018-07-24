package com.example.musicmentor.musicmentor;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ViewRecordingStudent extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;

    private StorageReference videoRef;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;


    TextView task_1;
    TextView video_1;
    VideoView videoView;
    VideoView resultVideo;
    TextView feedback;

    @Override
    public void onBackPressed()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String title = getIntent().getStringExtra("title");
        mDatabase.child("notify").child(title).setValue("seen");
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs

//        /* Handle Audio */
//        try {
//            MediaPlayer player = new MediaPlayer();
//            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            player.setDataSource("https://firebasestorage.googleapis.com/v0/b/fir-b9532.appspot.com/o/songs%2Fsong1.mp3?alt=media&token=a4424b28-93c3-4a0c-a6b9-9136dcf63335");
//            player.prepare();
//            player.start();
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("tag2", "activitycreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recording_student);

        resultVideo = (VideoView) findViewById(R.id.videoViewStudent);
        feedback = (TextView) findViewById(R.id.videoFeedbackStudent);

        //Upload file to firebase
        //Uri file = Uri.fromFile(new File(videoUri.toString()));

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://musicmentorprototype.appspot.com");

        final String childPosition = getIntent().getStringExtra("childPosition");
        final String groupPosition = getIntent().getStringExtra("groupPosition");
        final String parent = getIntent().getStringExtra("parent").substring(getIntent().getStringExtra("parent").indexOf(' ') + 1);
        final String title = getIntent().getStringExtra("title");

        /*
        mDatabase.child(groupPosition).child(childPosition);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
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
        */


        StorageReference fileReference = storageReference.child(parent + "/" + title  + "/" + "newvideo.3pg");
        Log.i("tryingit", "now");
        try {
            final File localFile = File.createTempFile("testing1", "3pg");
            fileReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.i("successfulyeah", "whatev");
                    resultVideo.setVideoURI(Uri.fromFile(localFile));
                    resultVideo.start();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
            //Toast.makeText(getContext(),"Button working",LENGTH_SHORT).show();
        }
        catch (IOException ioe){
        }
    }

}
