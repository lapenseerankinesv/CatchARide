package com.example.samantha.catch_a_ride;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RiderAccepted extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView driverName, driverPayment, driverNumber;
    DatabaseReference databaseDriver;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String id = user.getUid();

    String driverID;
    Driver driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_accepted);

        mAuth = FirebaseAuth.getInstance();
        driverName = (TextView) findViewById(R.id.rideAcceptName);
        driverPayment = (TextView) findViewById(R.id.rideAcceptPay);
        driverNumber = (TextView) findViewById(R.id.rideAcceptPhone);

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
                    driverNumber.setText(driver.getDriverPhoneNumber());

                    Rider temp = driver.getCurrentRider();
                    if (temp == null)
                    {
                        Toast.makeText(RiderAccepted.this, "Ride has been finished.", Toast.LENGTH_LONG).show();
                        databaseDriver.removeEventListener(this);
                        finish();
                        startActivity(new Intent(RiderAccepted.this, RidersGetDrivers.class));
                    }
                }
                else {
                    Toast.makeText(RiderAccepted.this, "Ride has been finished.", Toast.LENGTH_LONG).show();
                    databaseDriver.removeEventListener(this);
                    finish();
                    startActivity(new Intent(RiderAccepted.this, RidingOrDriving.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Ride in progress.", Toast.LENGTH_LONG).show();
    }
}
