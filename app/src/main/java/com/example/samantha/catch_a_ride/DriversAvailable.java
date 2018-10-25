package com.example.samantha.catch_a_ride;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class DriversAvailable extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drivers_available);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.daToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Driving");

        mAuth = FirebaseAuth.getInstance();

        //findViewById(R.id.).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            /*case R.id.ridingButton:

                break; */
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
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
