package com.example.musicmentor.musicmentor;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

    private StorageReference audioRef;

    // DROPDOWN COMPENENTS
    private boolean videoRecordingSelected;
    private Button selectRecordForm;;

    // AUDIO COMPONENTS
    private Button record;
    private Button playAudio;
    private Button stopAudio;
    private MediaRecorder mediaRecorder;
    private String outputFile;


    TextView task_1;
    TextView video_1;
    VideoView videoView;
    VideoView resultVideo;
    EditText feedback;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("tag2", "activitycreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_video_teacher);

        /**
         * DROPDOWN STUFF
         */
        videoRecordingSelected = true;
        selectRecordForm = (Button) findViewById(R.id.btnSwitchRecordingMethod);

        /**
         * VIDEO STUFF
         */

        resultVideo = (VideoView) findViewById(R.id.videoViewTeacher);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        feedback = (EditText) findViewById(R.id.videoFeedbackTeacher);

        button = (Button) findViewById(R.id.buttonFeedback);
        button.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                String childPosition = getIntent().getStringExtra("childPosition");
                String groupPosition = getIntent().getStringExtra("groupPosition");
                mDatabase.child(groupPosition).child(childPosition).setValue(feedback.getText().toString());

            }
        });

            //Upload file to firebase
            //Uri file = Uri.fromFile(new File(videoUri.toString()));

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://musicmentorprototype.appspot.com");

        String childPosition = getIntent().getStringExtra("childPosition");
        String groupPosition = getIntent().getStringExtra("groupPosition");


        StorageReference fileReference = storageReference.child(groupPosition + "/" + childPosition + "/" + "newvideo.webm");

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

    public void storeAudio() {
        Uri audioUri = Uri.fromFile(new File(outputFile).getAbsoluteFile());

        String childPosition = getIntent().getStringExtra("childPosition");
        String groupPosition = getIntent().getStringExtra("groupPosition");

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        videoRef = storageRef.child("/" + groupPosition + "/" + childPosition + "/" + "newvideo.3pg");
        UploadTask uploadTask = videoRef.putFile(audioUri);

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
