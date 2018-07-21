package com.example.musicmentor.musicmentor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
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

public class UserPage extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private FirebaseFirestore db;
    private FirebaseUser user;

    TextView nameEdit, emailEdit, UTEdit, instrumentEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        nameEdit = (TextView) findViewById(R.id.nameEdit);
        emailEdit = (TextView) findViewById(R.id.emailEdit);
        UTEdit = (TextView) findViewById(R.id.UTEdit);
        instrumentEdit = (TextView) findViewById(R.id.InstrumentEdit);

        db = FirebaseFirestore.getInstance();

        updateFields();
        Button logout = (Button) findViewById(R.id.UPLog);
        logout.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                mAuth.signOut();
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });

    }

    private void updateFields()
    {
        DocumentReference ref = db.collection("users").document(user.getUid().toString());
        ref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String userType = (String) documentSnapshot.get("userType");
                            String IT = (String) documentSnapshot.get("instrument");
                            String name = (String) documentSnapshot.get("name");
                            UTEdit.setText(userType);
                            instrumentEdit.setText(IT);
                            nameEdit.setText(name);
                            emailEdit.setText(LoginActivity.currEmail);

                        }
                    }
                });
    }

}
