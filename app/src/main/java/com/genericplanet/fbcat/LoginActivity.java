package com.genericplanet.fbcat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private TextView signup;
    private Button login;
    private EditText email,password;
    private FirebaseAuth mAuth;
    private ProgressBar loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        mAuth = FirebaseAuth.getInstance();
        signup=findViewById(R.id.login_activity_signup);
        login=findViewById(R.id.loginActivityLogin);
        email=findViewById(R.id.loginActivityEmail);
        loginProgress=findViewById(R.id.progressBarLogin);
        password=findViewById(R.id.loginActivityPassword);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterationActivity.class);
                startActivity(intent);

            }
        });
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(TextUtils.isEmpty(email.getText().toString())|| TextUtils.isEmpty(password.getText().toString())){
                        if(TextUtils.isEmpty(email.getText().toString()))
                        email.setError("Enter Email Please");
                        if(TextUtils.isEmpty(password.getText().toString()))
                            password.setError("Enter Passowrd Please");

                    }
                    else
                    {
                        login.setVisibility(View.INVISIBLE);
                        loginProgress.setVisibility(View.VISIBLE);
                        mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Toast.makeText(LoginActivity.this,
                                                    "signInWithEmail:success",
                                                    Toast.LENGTH_SHORT).show();
                                            UserLogin();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            loginProgress.setVisibility(View.INVISIBLE);
                                            login.setVisibility(View.VISIBLE);
                                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
//                                        updateUI(null);
                                        }

                                        // ...
                                    }
                                });
                    }


                }
            });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
        if(currentUser!=null) {
           UserLogin();
        }
        }

    private void UserLogin() {
        Intent intent=new Intent(LoginActivity.this,RoomsActivity.class);
        startActivity(intent);
        finish();
    }


}

