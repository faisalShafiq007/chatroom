package com.genericplanet.fbcat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterationActivity extends AppCompatActivity {
    private EditText editEmail, editPassword, editName,editUsername;
    private Button signupButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private ProgressBar registrationProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editEmail=findViewById(R.id.signupEmail);
        editPassword=findViewById(R.id.signupPassword);
        editName=findViewById(R.id.signupName);
        signupButton=findViewById(R.id.signupButton);
        editUsername=findViewById(R.id.editUsername);
        registrationProgress=findViewById(R.id.progressBarRegistration);
        mAuth=FirebaseAuth.getInstance();
        firestore= FirebaseFirestore.getInstance();


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name, email, password,username;
                email = editEmail.getText().toString();
                password = editPassword.getText().toString();
                name = editName.getText().toString();
                username=editUsername.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
                        ||TextUtils.isEmpty(username)||username.contains(" ")) {
                    if (TextUtils.isEmpty(name)) {
                        editName.setError("Enter name please");
                    }
                    if (TextUtils.isEmpty(email)) {
                        editEmail.setError("Enter email address please");
                    }
                    if (TextUtils.isEmpty(password)) {
                        editPassword.setError("Enter Password please");
                    }
                    if(TextUtils.isEmpty(username))
                        editUsername.setError("Enter User name Please");
                    if(username.contains(" "))
                        editUsername.setError("User name must not contain spaces");
                } else {
                    signupButton.setVisibility(View.INVISIBLE);
                    registrationProgress.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        username.toLowerCase();
                                        register(user,name,username);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        registrationProgress.setVisibility(View.INVISIBLE);
                                        signupButton.setVisibility(View.VISIBLE);
                                        Toast.makeText(RegisterationActivity.this, task.getException().getLocalizedMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    // ...
                                }
                            });

                }
            }
        });
    }


    private void register(FirebaseUser user, String name,String username) {
        Map<String,Object> userInfo=new HashMap<>();
        userInfo.put("name",name);
        userInfo.put("id",mAuth.getCurrentUser().getUid());
        userInfo.put("email",mAuth.getCurrentUser().getEmail());
        userInfo.put("username",username);
        firestore.collection("users").document().set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"user created",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),RoomsActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

}

