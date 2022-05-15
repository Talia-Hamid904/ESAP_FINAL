package com.example.esap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.AppCompatButton;


public class MainActivity extends AppCompatActivity {

    private AppCompatButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (AppCompatButton) findViewById(R.id.button) ;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, SpeechActivity.class);
                //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });
    }

}