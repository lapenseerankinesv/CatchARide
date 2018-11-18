package com.example.samantha.catch_a_ride;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
 * Author: Val Lapensée-Rankine
 *
 * MessageDriver
 * After user had chosen the driver they want, this activity is where they can
 * input their starting point, destination, and any extra information and then
 * send it in a ride request to the driver. Or the user can choose to go back
 * to the list of available drivers.
 */
public class MessageDriver extends AppCompatActivity implements View.OnClickListener {

    EditText enterStart, enterDest, extraInfo;
    Button buttonSend;
    TextView name;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth mAuth;
    String driverName, driverID;
    DatabaseReference databaseUsers;
    DatabaseReference databaseDriver;

    Driver driver;
    Rider rider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_driver);

        Intent lastIntent = getIntent();
        driverName = lastIntent.getStringExtra("driverName");
        driverID = lastIntent.getStringExtra("driverID");

        name = (TextView) findViewById(R.id.driverName);
        enterStart = (EditText) findViewById(R.id.editStart);
        enterDest = (EditText) findViewById(R.id.editDest);
        extraInfo = (EditText) findViewById(R.id.editExtra);
        buttonSend = (Button) findViewById(R.id.sendRequest);

        mAuth = FirebaseAuth.getInstance();
        String id = user.getUid();
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(id);
        databaseDriver = FirebaseDatabase.getInstance().getReference().child("drivers").child(driverID);

        name.setText(driverName);
        findViewById(R.id.messageBack).setOnClickListener(this);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRiderDetails();
            }
        });
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
                String riderID = temp.getUserID();
                String riderName = temp.getUserName();
                String riderPhone = temp.getUserPhoneNumber();
                rider = new Rider(riderID, riderName, riderPhone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseDriver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Driver temp = dataSnapshot.getValue(Driver.class);
                driver = temp;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendRiderDetails() {
        String start = enterStart.getText().toString().trim();
        String dest = enterDest.getText().toString().trim();
        String extra = extraInfo.getText().toString().trim();

        if (TextUtils.isEmpty(start)) {
            Toast.makeText(this, "Enter a starting point.", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(dest)) {
            Toast.makeText(this, "Enter a destination.", Toast.LENGTH_LONG).show();
        }
        else {
            rider.setStart(start);
            rider.setDest(dest);
            rider.setExtra(extra);

            driver.addPotentialRider(rider);
            databaseDriver.setValue(driver);

            Intent intent = new Intent(MessageDriver.this, WaitingForDriver.class);
            intent.putExtra("driver", driver.getDriverID());

            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.messageBack:
                finish();
                startActivity(new Intent(this, RidersGetDrivers.class));
                break;
        }
    }
}
