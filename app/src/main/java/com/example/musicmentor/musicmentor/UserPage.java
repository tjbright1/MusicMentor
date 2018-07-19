package com.example.musicmentor.musicmentor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserPage extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private DatabaseReference mDatabase;
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

        mDatabase = FirebaseDatabase.getInstance().getReference();

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
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String UT = dataSnapshot.child("users").child(user.getUid()).child("userType").getValue(String.class);
                String IT = dataSnapshot.child("users").child(user.getUid()).child("instrumentType").getValue(String.class);
                String name = dataSnapshot.child("users").child(user.getUid()).child("name").getValue(String.class);

                UTEdit.setText(UT);
                instrumentEdit.setText(IT);
                nameEdit.setText(name);
                emailEdit.setText(LoginActivity.currEmail);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }



}
