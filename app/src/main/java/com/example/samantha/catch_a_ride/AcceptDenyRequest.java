package com.example.samantha.catch_a_ride;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.List;

public class AcceptDenyRequest extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String id = user.getUid();
    Driver driver;
    Rider rider;
    String riderID;

    TextView riderName, riderStart, riderDest, riderExtra;
    DatabaseReference currentDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_deny_request);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.acceptButton).setOnClickListener(this);
        findViewById(R.id.rejectButton).setOnClickListener(this);

        riderName = findViewById(R.id.potRiderName);
        riderStart = findViewById(R.id.potRiderStart);
        riderDest = findViewById(R.id.potRiderDest);
        riderExtra = findViewById(R.id.potRiderExtra);

        currentDriver = FirebaseDatabase.getInstance().getReference().child("drivers").child(id);
        Intent intent = getIntent();
        riderID = intent.getStringExtra("riderID");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        currentDriver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                driver = dataSnapshot.getValue(Driver.class);

                if (driver != null) {
                    Iterator<Rider> iterator = driver.getPotentialRiders().iterator();
                    while (iterator.hasNext()) {
                        Rider temp = iterator.next();
                        if (temp.getRiderID().equals(riderID)) {
                            rider = temp;
                            riderName.setText(rider.getName());
                            riderStart.setText(rider.getStart());
                            riderDest.setText(rider.getDest());
                            riderExtra.setText(rider.getExtra());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void accept()
    {
        driver.setCurrentRider(rider);
        driver.clearPotentialRiders();
        currentDriver.setValue(driver);
        Intent next = new Intent(AcceptDenyRequest.this, DriverAccepted.class);
        finish();
        startActivity(next);
    }

    public void reject()
    {
        driver.deletePotentialRider(rider);
        currentDriver.setValue(driver);
        Intent intent = new Intent();
        intent.putExtra("driving", true);
        finish();
        startActivity(new Intent(AcceptDenyRequest.this, DriversAvailable.class));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.acceptButton:
                accept();
                break;
            case R.id.rejectButton:
                reject();
                break;
        }
    }
}
