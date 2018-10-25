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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    DatabaseReference databaseUser;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    User current;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.pToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Profile");

        mAuth = FirebaseAuth.getInstance();
        id = user.getUid();

        current = new User("temp", "temp", "temp", "temp");

        findViewById(R.id.continueButton).setOnClickListener(this);
        findViewById(R.id.editProfileButton).setOnClickListener(this);

        TextView textViewName = (TextView) findViewById(R.id.profileDisplayName);
        TextView textViewNumber = (TextView) findViewById(R.id.profileDisplayPhoneNumber);
        TextView textViewPayment = (TextView) findViewById(R.id.profileDisplayPayment);
        databaseUser = FirebaseDatabase.getInstance().getReference("users");

        /*databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User temp = userSnapshot.getValue(User.class);
                    if (temp.getUserID().equals(id)) {
                        current = temp;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Read failed: " + databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
        textViewName.setText(current.getUserName());
        textViewNumber.setText(current.getUserPhoneNumber());
        textViewPayment.setText(current.getUserPaymentType());*/

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
