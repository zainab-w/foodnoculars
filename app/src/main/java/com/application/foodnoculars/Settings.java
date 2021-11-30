package com.application.foodnoculars;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    Button btnSubmit;
    EditText name, message, subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnSubmit = findViewById(R.id.btnSubmit);
        name = findViewById(R.id.etName);
        message = findViewById(R.id.etMessage);
        subject = findViewById(R.id.etSubject);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL,new String[] {"zainabwalters12@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT,  "" + subject.getText());
                intent.putExtra(Intent.EXTRA_TEXT,
                        "Name: " + name.getText()
                + "\n\nMessage: " + message.getText());
            try {
                startActivity(Intent.createChooser(intent, "Select email"));
                }
            catch (android.content.ActivityNotFoundException ex)
            {
                Toast.makeText(Settings.this, "There are no email clients",Toast.LENGTH_LONG).show();
            }


            }
        });
    }
}