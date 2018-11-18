package com.example.samantha.catch_a_ride;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/*
 * Author: Val Lapensée-Rankine
 *
 * Profile
 * Shows the user their profile; pulls the user data for the specific user
 * from the database. From here the user can continue to the RidingOrDriving
 * activity or go to EditProfile.
 */
public class Profile extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    DatabaseReference databaseUsers;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String id;
    User currentUser;
    TextView userName, userPaymentMethod, userPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.pToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Profile");

        userName = (TextView) findViewById(R.id.profileDisplayName);
        userPaymentMethod = (TextView) findViewById(R.id.profileDisplayPayment);
        userPhoneNumber = (TextView) findViewById(R.id.profileDisplayPhoneNumber);

        mAuth = FirebaseAuth.getInstance();
        id = user.getUid();

        findViewById(R.id.continueButton).setOnClickListener(this);
        findViewById(R.id.editProfileButton).setOnClickListener(this);

        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(id);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User temp = dataSnapshot.getValue(User.class);
                currentUser = temp;
                userName.setText(temp.getUserName());
                userPaymentMethod.setText(temp.getUserPaymentType());
                userPhoneNumber.setText(temp.getUserPhoneNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.continueButton:
                finish();
                startActivity(new Intent(this, RidingOrDriving.class));
                break;
            case R.id.editProfileButton:
                finish();
                startActivity(new Intent(this, EditProfile.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.switchType:
                startActivity(new Intent(this, RidingOrDriving.class));
                break;
            case R.id.profile:
                startActivity(new Intent(this, Profile.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
