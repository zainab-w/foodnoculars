package com.application.foodnoculars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    TextView register, forgotPassword;
    Button login;
    EditText email,password;

    DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.btnRegister);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        //forgotPassword = findViewById(R.id.forgotPassword);


        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this, Register.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                login();

            }
        });
//        forgotPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                forgotPassword();
//            }
//        });
    }

    private void forgotPassword() {
        startActivity(new Intent(Login.this, ForgetPassword.class));

    }

    private void login() {


        String getEmail = email.getText().toString();
        String getPassword = password.getText().toString();


        if (TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getPassword))
        {
            // if the text fields are empty then show the below message.
            Toast.makeText(Login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
        else
        {
            firebaseAuth.signInWithEmailAndPassword(getEmail,getPassword)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(Login.this, "Logged In" , Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this,HomePage.class));
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }


    }
}