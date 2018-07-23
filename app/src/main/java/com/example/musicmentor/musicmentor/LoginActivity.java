package com.example.musicmentor.musicmentor;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;
    public static String currEmail;
    private DatabaseReference mDatabase;
    private FirebaseFirestore db;

    PersistData mPersistData;


    private Button record;
    private Button playAudio;
    private Button stopAudio;
    private MediaRecorder mediaRecorder;
    private String outputFile;

    final int SAMPLE_RATE = 44100; // The sampling rate
    boolean mShouldContinue; // Indicates if recording / playback should stop

    private static final String LOG_TAG = "AudioRecordTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        findViewById(R.id.textViewSignup).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
        Button button = findViewById(R.id.buttonLogin);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "BebasNeueLight.ttf");
        button.setTypeface(typeface);

        /** LET THE AUDIO BEGIN */
        playAudio = (Button) findViewById(R.id.btnPlayAudio);
        record = (Button) findViewById(R.id.btnRecordAudio);
        stopAudio = (Button) findViewById(R.id.btnStopAudio);
        playAudio.setEnabled(false);
        stopAudio.setEnabled(false);

        //Creating an internal directory

        //Getting a file within the dir.
        File mydir = this.getDir("mydir", Context.MODE_PRIVATE);
        File fileWithinMyDir = new File(mydir, "myfile");
        try {
            FileOutputStream out = new FileOutputStream(fileWithinMyDir);
        } catch (FileNotFoundException nfne) {
        }
        //Use the stream as usual to write    into the file

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
    }

    public void setUpFolder() {

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

//    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                                     @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 10) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                recordAudio(mediaRecorder);
//            }else{
//                //User denied Permission.
//            }
//        }
//    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        currEmail = email;
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum lenght of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    final FirebaseUser user = mAuth.getCurrentUser();
                    Log.v("userid: ", user.getUid().toString());
                    DocumentReference ref = db.collection("users").document(user.getUid().toString());
                    ref.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        String userType = (String) documentSnapshot.get("userType");
                                        String userGroupId = (String) documentSnapshot.get("userGroupId");
                                        ((MyApplication) getApplication()).setGroupId(userGroupId);
                                        if (userType.equals("Student")) {
                                            Log.v("Instudent", "Now");
                                            finish();
                                            Intent intent = new Intent(LoginActivity.this, HomePageStudentActivity.class);
                                            intent.putExtra("userGroupId", userGroupId);
                                            startActivity(intent);
                                        } else if (userType.equals("Teacher")) {
                                            Log.v("Inteacher", "Now");
                                            finish();
                                            Intent intent = new Intent(LoginActivity.this, HomePageTeacherActivity.class);
                                            intent.putExtra("userGroupId", userGroupId);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        if (mAuth.getCurrentUser() != null) {
            final FirebaseUser user = mAuth.getCurrentUser();
            DocumentReference ref = db.collection("users").document(user.getUid().toString());
            ref.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String userType = (String) documentSnapshot.get("userType");
                                String userGroupId = (String) documentSnapshot.get("userGroupId");
                                ((MyApplication) getApplication()).setGroupId(userGroupId);
                                if (userType.equals("Student")) {
                                    Log.v("Instudent", "Now");
                                    finish();
                                    Intent intent = new Intent(LoginActivity.this, HomePageStudentActivity.class);
                                    startActivity(intent);
                                } else if (userType.equals("Teacher")) {
                                    Log.v("Inteacher", "Now");
                                    finish();
                                    Intent intent = new Intent(LoginActivity.this, HomePageTeacherActivity.class);
                                    intent.putExtra("userGroupId", userGroupId);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewSignup:
                finish();
                startActivity(new Intent(this, SignupActivity.class));
                break;

            case R.id.buttonLogin:
                userLogin();
                break;
        }
    }
}