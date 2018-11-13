package com.example.samantha.catch_a_ride;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.List;

public class DriversAvailable extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String id;
    Driver driver;

    boolean isAvailable;
    Button availableButton;

    ListView listViewRiders;
    TextView riderRequests, currentRequests, currentText;
    DatabaseReference databaseUser, databaseDrivers, currentDriver;
    User currentUser;

    List<Rider> potentialRiders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drivers_available);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.daToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Driving");

        id = user.getUid();
        availableButton = (Button) findViewById(R.id.isAvailableButton);
        availableButton.setOnClickListener(this);

        riderRequests = (TextView) findViewById(R.id.rideRequests);
        listViewRiders = (ListView) findViewById(R.id.riderListView);
        currentRequests = (TextView) findViewById(R.id.currentRequests);
        currentText = (TextView) findViewById(R.id.currentText);

        databaseUser = FirebaseDatabase.getInstance().getReference().child("users").child(id);
        databaseDrivers = FirebaseDatabase.getInstance().getReference().child("drivers");

        availableButton.setText("Start Driving");
        potentialRiders = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        isAvailable = false;

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.isAvailableButton:
                changeAvailable();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if(!isAvailable) {
            super.onDestroy();
        }
        else
        {
            databaseDrivers.child(id).removeValue();
            super.onDestroy();
        }
    }

    public void changeAvailable() {
        isAvailable = !isAvailable;
        if (isAvailable) {
            availableButton.setText("Stop Driving");
            currentText.setText("Now Offering Rides");
            riderRequests.setVisibility(View.VISIBLE);
            listViewRiders.setVisibility(View.VISIBLE);

            String driverID = currentUser.getUserID();
            String driverName = currentUser.getUserName();
            String driverPayment = currentUser.getUserPaymentType();
            String driverPhoneNum = currentUser.getUserPhoneNumber();
            driver = new Driver(driverID, driverName, driverPayment, driverPhoneNum);

            databaseDrivers.child(id).setValue(driver);
            currentDriver = databaseDrivers.child(id);

            currentDriver.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Driver temp = dataSnapshot.getValue(Driver.class);
                    if (temp != null) {
                        potentialRiders = temp.getPotentialRiders();
                        RiderList adapter = new RiderList(DriversAvailable.this, potentialRiders);
                        listViewRiders.setAdapter(adapter);
                        if (potentialRiders.isEmpty()) {
                            currentRequests.setVisibility(View.VISIBLE);
                        } else {
                            currentRequests.setVisibility(View.GONE);
                        }
                    } else {
                        potentialRiders.clear();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            availableButton.setText("Start Driving");
            currentText.setText("Are you offering rides now?");
            riderRequests.setVisibility(View.GONE);
            listViewRiders.setVisibility(View.GONE);

            driver.clearPotentialRiders();
            databaseDrivers.child(id).setValue(driver);
            databaseDrivers.child(id).removeValue();
        }
    }

    @Override
    public void onBackPressed() {
        if (!isAvailable) {
            super.onBackPressed();
        }
        else {
            Toast.makeText(getApplicationContext(), "Cannot leave when you are taking riders.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User temp = dataSnapshot.getValue(User.class);
                currentUser = temp;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                if (!isAvailable) {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(this, MainActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Cannot leave when you are taking riders.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.switchType:
                if (!isAvailable) {
                    startActivity(new Intent(this, RidingOrDriving.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Cannot leave when you are taking riders.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.profile:
                if (!isAvailable) {
                    startActivity(new Intent(this, Profile.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Cannot leave when you are taking riders.", Toast.LENGTH_LONG).show();
                }
        }

        return super.onOptionsItemSelected(item);
    }

}
