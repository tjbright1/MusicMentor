package com.example.musicmentor.musicmentor;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
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

/*
 Code for spinner usage is a modified version of code found at
   https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
 */

public class NewVideoStudent extends Activity {

    static final int REQUEST_VIDEO_CAPTURE = 1;

    private StorageReference videoRef;

    private DatabaseReference mDatabase;

    private boolean videoRecordingSelected;

    // DROPDOWN COMPENENTS
    private Button selectRecordForm;;

    // VIDEO COMPONENTS
    Button button;
    VideoView resultVideo;

    TextView feedback;

    // AUDIO COMPONENTS
    private Button record;
    private Button playAudio;
    private Button stopAudio;
    private MediaRecorder mediaRecorder;
    private String outputFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("tag2", "activitycreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_video_student);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        /**
         * DROPDOWN STUFF
         */
        videoRecordingSelected = true;
        selectRecordForm = (Button) findViewById(R.id.btnSwitchMethod);

        /**
         * VIDEO STUFF
         */
        button = (Button) findViewById(R.id.button);
        resultVideo = (VideoView) findViewById(R.id.videoView);
        feedback = (TextView) findViewById(R.id.videoFeedbackStudent);

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

        /**
         * AUDIO STUFF
         */
        playAudio = (Button) findViewById(R.id.btnPlayAudio);
        record = (Button) findViewById(R.id.btnRecordAudio);
        stopAudio = (Button) findViewById(R.id.btnStopAudio);
        playAudio.setEnabled(false);
        stopAudio.setEnabled(false);

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3pg";

//        outputFile = Environment.getExternalStorageDirectory() +"/Android/data/" + this.getApplicationContext().getPackageName();

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(outputFile);


        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecordBtnClicked(mediaRecorder);
            }
        });

        stopAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaRecorder.stop();
                } catch (IllegalStateException ise) {
                    ise.printStackTrace();
                }

                mediaRecorder.release();
//                mediaRecorder = null;
                record.setEnabled(true);
                stopAudio.setEnabled(false);
                playAudio.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Stopping...", Toast.LENGTH_LONG).show();
            }
        });

        playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Playing...", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                }

//                mediaPlayer.release();
            }
        });

        /**
         * RECORDING METHOD SELECTION STUFF
         */

        selectRecordForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoRecordingSelected = !videoRecordingSelected;
                Toast.makeText(getApplicationContext(), "" + videoRecordingSelected, Toast.LENGTH_LONG).show();
                if (videoRecordingSelected) {
                    onVideoSelect();
                    selectRecordForm.setText("Use Audio");
                } else {
                    onAudioSelect();
                    selectRecordForm.setText("Use Video");
                }
            }
        });
    }

    public void onRecordBtnClicked(MediaRecorder mediaRecorder) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO},
                    0);
        } else {
            recordAudio(mediaRecorder);
        }
    }

    public void recordAudio(MediaRecorder mediaRecorder) {

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException ise) {
            System.out.println(ise);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        record.setEnabled(false);
        stopAudio.setEnabled(true);

        Toast.makeText(getApplicationContext(), "Recording...", Toast.LENGTH_LONG).show();
    }

    public void dispatchTakeVideoIntent(View videoView) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    public void onAudioSelect() {
        if (button.isShown()) {
            button.setVisibility(View.GONE);
        }
        if (resultVideo.isShown()) {
            resultVideo.setVisibility(View.GONE);
        }

        if (!playAudio.isShown()) {
            playAudio.setVisibility(View.VISIBLE);
        }
        if (!stopAudio.isShown()) {
            stopAudio.setVisibility(View.VISIBLE);
        }
        if (!record.isShown()) {
            record.setVisibility(View.VISIBLE);
        }
    }

    public void onVideoSelect() {
        if (!button.isShown()) {
            button.setVisibility(View.VISIBLE);
        }
        if (!resultVideo.isShown()) {
            resultVideo.setVisibility(View.VISIBLE);
        }

        if (playAudio.isShown()) {
            playAudio.setVisibility(View.GONE);
        }
        if (stopAudio.isShown()) {
            stopAudio.setVisibility(View.GONE);
        }
        if (record.isShown()) {
            record.setVisibility(View.GONE);
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
