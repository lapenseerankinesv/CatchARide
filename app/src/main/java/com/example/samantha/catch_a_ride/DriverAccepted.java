package com.example.samantha.catch_a_ride;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class DriverAccepted extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    TextView riderName, riderStart, riderDest, riderExtra, riderPhone;
    DatabaseReference databaseDriver;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String id = user.getUid();

    Driver driver;
    Rider rider;
    ValueEventListener currentRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_accepted);
        mAuth = FirebaseAuth.getInstance();

        riderName = (TextView) findViewById(R.id.driveAcceptName);
        riderStart = (TextView) findViewById(R.id.driveAcceptStart);
        riderDest = (TextView) findViewById(R.id.driveAcceptDest);
        riderExtra = (TextView) findViewById(R.id.driveAcceptExtra);
        riderPhone = (TextView) findViewById(R.id.driveAcceptPhone);

        findViewById(R.id.driveAcceptFinished).setOnClickListener(this);

        databaseDriver = FirebaseDatabase.getInstance().getReference().child("drivers").child(id);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        currentRide = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                driver = dataSnapshot.getValue(Driver.class);

                if (driver != null) {
                    rider = driver.getCurrentRider();
                    if (rider != null) {
                        riderName.setText(rider.getName());
                        riderDest.setText(rider.getDest());
                        riderStart.setText(rider.getStart());
                        riderExtra.setText(rider.getExtra());
                        riderPhone.setText(rider.getPhoneNum());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseDriver.addValueEventListener(currentRide);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.driveAcceptFinished:
                driver.deleteCurrentRider();
                databaseDriver.setValue(driver);
                databaseDriver.removeEventListener(currentRide);
                finish();
                Intent next = new Intent(this, DriversAvailable.class);
                next.putExtra("driving", true);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Ride in progress.", Toast.LENGTH_LONG).show();
    }
}
