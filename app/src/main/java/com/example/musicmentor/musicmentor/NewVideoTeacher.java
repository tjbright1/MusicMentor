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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class NewVideoTeacher extends Activity {

    static final int REQUEST_VIDEO_CAPTURE = 1;

    private StorageReference videoRef;

    private DatabaseReference mDatabase;


    TextView task_1;
    TextView video_1;
    ImageButton button;
    VideoView videoView;
    VideoView resultVideo;
    EditText recordingTitle;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("tag2", "activitycreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_video_teacher);

        db = FirebaseFirestore.getInstance();



        button = (ImageButton) findViewById(R.id.buttonTeacher);
        resultVideo = (VideoView) findViewById(R.id.videoViewTeacher);

        recordingTitle = (EditText) findViewById(R.id.recordingTitleTeacher);
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

            final String childPosition = getIntent().getStringExtra("childPosition");
            final String groupPosition = getIntent().getStringExtra("groupPosition");
            final String parent = getIntent().getStringExtra("parent").substring(getIntent().getStringExtra("parent").indexOf(' ') + 1);

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            videoRef = storageRef.child("/" + parent + "/" + recordingTitle.getText().toString() + "/" + "newvideo.3pg");
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

                    // Add a new document with a generated ID
                    Map<String, Object> recording = new HashMap<>();
                    recording.put("title", recordingTitle.getText().toString());
                    recording.put("task", parent);

                    String userGroupId = ((MyApplication) getApplication()).getGroupId();
                    db.collection("userGroups").document(userGroupId).collection("recordings").document()
                            .set(recording)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    finish();
                                    Intent intent = new Intent(NewVideoTeacher.this, MainTeacherActivity.class);
                                    startActivity(intent);
                                }
                            });



                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("lessons").child("currentLesson").child("tasks").child(parent).child(recordingTitle.getText().toString()).setValue("Teacher");
                    mDatabase.child("notify").child(recordingTitle.getText().toString()).setValue("Student");
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Log.v("tag","SUCCESS");
                }
            });
        }
    }

}
