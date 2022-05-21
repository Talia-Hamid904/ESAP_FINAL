package com.example.esap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Intent intent = getIntent();
        String msg = intent.getStringExtra("key1");
        String address = intent.getStringExtra("key2");
        String phNo = intent.getStringExtra("key3");
        String userMsg = intent.getStringExtra("key4");

        TextView msgTextView = findViewById(R.id.UserMsg);
        TextView phoneTextView = findViewById(R.id.phone);
        TextView locationTextView = findViewById(R.id.location);

        msgTextView.setText(msg);
        phoneTextView.setText(address);
        locationTextView.setText(phNo);
    }
}