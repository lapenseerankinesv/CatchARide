package com.example.samantha.catch_a_ride;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/*
 * Author: Val Lapensée-Rankine
 *
 * MainActivity
 * Login activity. From here an existing user can input their username and
 * password and the app will log them in, or if they don't have an account
 * they can go to the SignUpActivity.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //private static final String TAG = "MainActivity";
    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.gotToSignUp).setOnClickListener(this);
        findViewById(R.id.loginButton).setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.editTextLoginEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextLoginPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()){
            editTextEmail.setError("Email is required.");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextPassword.setError("Enter valid email address.");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required.");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Minimum length of password is 6 characters.");
        }

        progressBar.setVisibility(View.VISIBLE);


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.this, RidingOrDriving.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.gotToSignUp:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.loginButton:
                userLogin();
                break;
        }
    }

}
