package com.example.musicmentor.musicmentor;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
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


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;
    public static String currEmail;
    private DatabaseReference mDatabase;
    private FirebaseFirestore db;

    PersistData mPersistData;


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
    }

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