package com.example.samantha.catch_a_ride;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class FirstProfileEdit extends AppCompatActivity implements View.OnClickListener {

    EditText editTextName, num1, num2, num3;
    Button buttonEdit;
    Spinner spinnerChoices;
    DatabaseReference databaseUsers;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_profile_edit);

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        num1 = (EditText) findViewById(R.id.firstPhoneNumberFirst);
        num2 = (EditText) findViewById(R.id.firstPhoneNumberSecond);
        num3 = (EditText) findViewById(R.id.firstPhoneNumberThird);
        editTextName = (EditText) findViewById(R.id.firstEditProfileName);
        buttonEdit = (Button) findViewById(R.id.firstSaveEditsButton);
        spinnerChoices = (Spinner) findViewById(R.id.firstEditPaymentSpinner);

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUserDetails();
            }
        });
    }

    private void addUserDetails() {
        String name = editTextName.getText().toString().trim();
        String payment = spinnerChoices.getSelectedItem().toString().trim();
        String temp1 = num1.getText().toString().trim();
        String temp2 = num2.getText().toString().trim();
        String temp3 = num3.getText().toString().trim();
        if (temp1.length() != 3 || temp2.length() != 3 || temp3.length() != 4) {
            Toast.makeText(this, "Enter an existing phone number.", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "You must enter a name.", Toast.LENGTH_LONG).show();
        } else if (payment.equals("-Select Payment Type-")) {
            Toast.makeText(this, "You must select a payment type.", Toast.LENGTH_LONG).show();
        } else {
            String id = user.getUid();
            String phoneNumber = temp1 + "-" + temp2 + "-" + temp3;
            User user = new User(id, name, payment, phoneNumber);
            databaseUsers.child(id).setValue(user);

            Toast.makeText(this, "Data added.", Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(this, RidingOrDriving.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.saveEditsButton:
                break;
        }
    }
}
