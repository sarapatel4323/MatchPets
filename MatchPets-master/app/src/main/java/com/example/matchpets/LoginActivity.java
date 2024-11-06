package com.example.matchpets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout edEmail, edPassword;
    private Button btnLogin , btnNewAccount;

    private FirebaseAuth myAuth;
    private  FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Login");
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener(){

            //if everytime author will change than it call this functon
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //if user is logged in
                if(user != null)
                {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        edEmail = (TextInputLayout) findViewById(R.id.edEmail);
        edPassword = (TextInputLayout) findViewById(R.id.edPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnNewAccount = (Button) findViewById(R.id.newAccount);

        btnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = edEmail.getEditText().getText().toString().trim();
                final String password = edPassword.getEditText().getText().toString().trim();

                myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if login is not successful
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    //for start the listener when activity starts
    @Override
    protected void onStart() {
        super.onStart();
        myAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    //for remove the listener when activity stops
    @Override
    protected void onStop() {
        super.onStop();
        myAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}