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


public class WaitingForDriver extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    TextView driverName, driverPayment;
    DatabaseReference databaseDriver;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String id = user.getUid();

    String driverID;
    Driver driver;
    Rider rider;
    int riderPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_driver);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.findNewDriver).setOnClickListener(this);
        driverName = (TextView) findViewById(R.id.waitDriverName);
        driverPayment = (TextView) findViewById(R.id.waitDriverPay);

        Intent lastIntent = getIntent();
        driverID = lastIntent.getStringExtra("driver");
        databaseDriver = FirebaseDatabase.getInstance().getReference().child("drivers").child(driverID);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        databaseDriver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                driver = dataSnapshot.getValue(Driver.class);

                if (driver != null) {
                    driverName.setText(driver.getDriverName());
                    driverPayment.setText(driver.getDriverPaymentType());

                    Iterator<Rider> iterator = driver.getPotentialRiders().iterator();
                    while (iterator.hasNext()) {
                        Rider temp = iterator.next();
                        if (temp.getRiderID().equals(id)) {
                            rider = temp;
                        }
                    }

                    if (rider == null) {
                        Rider temp = driver.getCurrentRider();
                        if (temp.getRiderID().equals(id)) {
                            //TODO go to Ride Accepted-Rider activity
                        } else {
                            Toast.makeText(WaitingForDriver.this, driverName + "rejected your ride request.", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(WaitingForDriver.this, RidersGetDrivers.class));
                        }
                    }
                }
                else {
                    Toast.makeText(WaitingForDriver.this, "Your ride request was rejected.", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(WaitingForDriver.this, RidersGetDrivers.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.findNewDriver:
                driver.deletePotentialRider(rider);
                databaseDriver.setValue(driver);
                startActivity(new Intent(this, RidersGetDrivers.class));
                break;
        }
    }
}
