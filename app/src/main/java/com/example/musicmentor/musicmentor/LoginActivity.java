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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;

    private boolean videoRecordingSelected;

    // DROPDOWN COMPENENTS
//    private Spinner dropdown;
    private Button selectRecordForm;

    private DatabaseReference mDatabase;

    private Button record;
    private Button playAudio;
    private Button stopAudio;
    private MediaRecorder mediaRecorder;
    private String outputFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
        DROPDOWN STUFF
         */
//        dropdown = findViewById(R.id.spinner);
//        String[] items = new String[]{"Video", "Audio"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        dropdown.setAdapter(adapter);
//        videoRecordingSelected = false;

        selectRecordForm = (Button) findViewById(R.id.btnSwitchMethod);
        videoRecordingSelected = false;

        /*
        EVERYTHING ELSE
         */
        mAuth = FirebaseAuth.getInstance();

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

        // hide until selected
//        playAudio.setVisibility(View.GONE);
//        record.setVisibility(View.GONE);
//        stopAudio.setVisibility(View.GONE);

//        onVideoSelect();



        // set up dropdown
//        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                switch (i) {
//                    case 1: // video
//                        Toast.makeText(getApplicationContext(), "You have chosen video", Toast.LENGTH_SHORT).show();
//                        playAudio.setVisibility( playAudio.isShown() ?
//                                View.GONE: View.GONE);
//                        stopAudio.setVisibility(View.GONE);
//                        record.setVisibility(View.GONE);
//                        editTextEmail.setVisibility(View.GONE);
//
//                    case 0: // audio
//                        Toast.makeText(getApplicationContext(), "You have chosen audio", Toast.LENGTH_SHORT).show();
//                        playAudio.setVisibility(View.VISIBLE);
//                        stopAudio.setVisibility(View.VISIBLE);
//                        record.setVisibility(View.VISIBLE);
//                        editTextEmail.setVisibility(View.VISIBLE);
//
//                }

//                if (adapterView.getItemAtPosition(i).toString() == "video") {
//                    Toast.makeText(getApplicationContext(), "You have chosen video", Toast.LENGTH_SHORT).show();
//                    videoRecordingSelected = true;
//                    onVideoSelect();
//
//                } else if (adapterView.getItemAtPosition(i).toString() == "audio") {
//                    Toast.makeText(getApplicationContext(), "You have chosen audio", Toast.LENGTH_SHORT).show();
//                    videoRecordingSelected = false;
//
//                    onAudioSelect();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

    }

    public void onAudioSelect() {
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
                    mDatabase.child("users").child(user.getUid());
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            String value = dataSnapshot.child("users").child(user.getUid()).getValue(String.class);
                            Log.v("VALUEOF", value);
                            if (value.equals("Student")) {
                                Log.v("Instudent", "Now");
                                finish();
                                Intent intent = new Intent(LoginActivity.this, HomePageStudentActivity.class);
                                startActivity(intent);
                            } else if (value.equals("Teacher")) {
                                Log.v("Inteacher", "Now");
                                finish();
                                Intent intent = new Intent(LoginActivity.this, HomePageTeacherActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
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
            mDatabase.child("users").child(user.getUid());
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.child("users").child(user.getUid()).getValue(String.class);
                    Log.v("VALUEOF", value);
                    if (value.equals("Student")) {
                        finish();
                        Intent intent = new Intent(LoginActivity.this, HomePageStudentActivity.class);
                        startActivity(intent);
                    } else if (value.equals("Teacher")) {
                        finish();
                        Intent intent = new Intent(LoginActivity.this, HomePageTeacherActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
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