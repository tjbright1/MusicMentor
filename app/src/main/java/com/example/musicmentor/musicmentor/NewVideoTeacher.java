package com.example.musicmentor.musicmentor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.database.DatabaseReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static android.icu.text.DisplayContext.LENGTH_SHORT;
import static java.security.AccessController.getContext;

public class NewVideoTeacher extends Activity {

    static final int REQUEST_VIDEO_CAPTURE = 1;

    private StorageReference videoRef;

    private DatabaseReference mDatabase;


    TextView task_1;
    TextView video_1;
    VideoView videoView;
    VideoView resultVideo;
    EditText feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("tag2", "activitycreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_video_teacher);

        resultVideo = (VideoView) findViewById(R.id.videoViewTeacher);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        feedback = (EditText) findViewById(R.id.videoFeedbackTeacher);

        Button button = (Button) findViewById(R.id.buttonFeedback);
        button.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                mDatabase.child("users").child("asdf").setValue(feedback.getText().toString());
            }
        });

            //Upload file to firebase
            //Uri file = Uri.fromFile(new File(videoUri.toString()));

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://musicmentorprototype.appspot.com");

        String childPosition = getIntent().getStringExtra("childPosition");
        String groupPosition = getIntent().getStringExtra("groupPosition");


        StorageReference fileReference = storageReference.child(groupPosition + "/" + childPosition + "/" + "newvideo.3pg");

        try {
            final File localFile = File.createTempFile("testing1", "3pg");
            fileReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
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
