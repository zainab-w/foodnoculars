package com.example.foodnoculars;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    TextView cancel;
    Button register;
    EditText username, email, password, confirmPassword;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        cancel = findViewById(R.id.btnCancel);
        register = findViewById(R.id.btnRegister);
        username = findViewById(R.id.etUsername);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        confirmPassword = findViewById(R.id.etConfirmPassword);



        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Register.this, Login.class));
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Firebase register
                String Email = email.getText().toString();
                String Username = username.getText().toString();
                String Password = password.getText().toString();
                String ConfirmPassword = confirmPassword.getText().toString().trim();

                //checkAllFields();
                if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(Username) || TextUtils.isEmpty(ConfirmPassword))
                {
                    // if the text fields are empty then show the below message.
                    Toast.makeText(Register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>()
                      {
                          @Override
                          public void onSuccess(AuthResult authResult) {
                              register();
                              rtbRegister();
                          }
                      }
                    )
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, "Registration failed!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

//    private void checkAllFields() {
//
//    }


    private void rtbRegister() {
        String getEmail = email.getText().toString();
        String getUsername = username.getText().toString();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Email", getEmail);
        hashMap.put("Username", getUsername);

        databaseReference.child("Users")
                .child(firebaseAuth.getUid())
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Register.this, Login.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Registered Failed " + e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

    }

    private void register() {

        String getEmail = email.getText().toString();
        String getPassword = password.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(getEmail, getPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>()
                                      {
                                          @Override
                                          public void onSuccess(AuthResult authResult) {
                                              //Toast.makeText(Register.this, "Registration Complete!", Toast.LENGTH_LONG).show();

                                          }
                                      }
                )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(Register.this, "Registration failed!" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }

}