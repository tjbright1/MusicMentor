package com.example.musicmentor.musicmentor;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
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

public class ViewRecordingTeacher extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;

    private StorageReference videoRef;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;


    TextView task_1;
    TextView video_1;
    VideoView videoView;
    VideoView resultVideo;
    EditText feedback;

    @Override
    public void onBackPressed()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String title = getIntent().getStringExtra("title");
        mDatabase.child("notify").child(title).setValue("seen");
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("tag2", "activitycreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recording_teacher);

        resultVideo = (VideoView) findViewById(R.id.videoViewTeacher);

        feedback = (EditText) findViewById(R.id.videoFeedbackTeacher);


        /*
        Button button = (Button) findViewById(R.id.buttonFeedback);
        button.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                String childPosition = getIntent().getStringExtra("childPosition");
                String groupPosition = getIntent().getStringExtra("groupPosition");
                mDatabase.child(groupPosition).child(childPosition).setValue(feedback.getText().toString());
                mDatabase1.child("notify").child(title).setValue("Student");
                finish();
                Intent intent = new Intent(ViewRecordingTeacher.this, MainTeacherActivity.class);
                startActivity(intent);
            }
        });
        */


        /*videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    finish();
                }
                return true;
            }
        });*/

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
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        */


        StorageReference fileReference = storageReference.child(parent + "/" + title + "/" + "newvideo.3pg");
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
