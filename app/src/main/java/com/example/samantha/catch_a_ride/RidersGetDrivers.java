package com.example.samantha.catch_a_ride;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RidersGetDrivers extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference databaseDrivers;
    ListView listViewDrivers;
    List<Driver> driverList;
    TextView available;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riders_get_drivers);

        mAuth = FirebaseAuth.getInstance();
        databaseDrivers = FirebaseDatabase.getInstance().getReference("drivers");
        listViewDrivers = (ListView) findViewById(R.id.driversList);

        driverList = new ArrayList<>();

        listViewDrivers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RidersGetDrivers.this, MessageDriver.class);
                Driver temp = driverList.get(position);
                intent.putExtra("driverID", temp.getDriverID());
                intent.putExtra("driverName", temp.getDriverName());
                startActivity(intent);
            }
        });

        available = (TextView) findViewById(R.id.drivers);
        Toolbar toolbar = findViewById(R.id.rgdToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Drivers Available");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        databaseDrivers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                driverList.clear();
                for(DataSnapshot driverSnapshot: dataSnapshot.getChildren())
                {
                    Driver driver = driverSnapshot.getValue(Driver.class);
                    driverList.add(driver);
                }
                DriverList adapter = new DriverList(RidersGetDrivers.this, driverList);
                listViewDrivers.setAdapter(adapter);
                if(driverList.isEmpty())
                {
                    available.setText("No drivers currently available.");
                    available.setVisibility(View.VISIBLE);
                }
                else
                {
                    available.setVisibility(View.GONE);
                }
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
