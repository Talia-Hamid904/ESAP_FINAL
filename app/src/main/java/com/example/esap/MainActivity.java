package com.example.esap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

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
                myIntent.putExtra("type","");
                //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });

    }
    public void onClickAccident(View view){
        ImageButton e_btn = findViewById(R.id.imageButton3);
        e_btn.setBackgroundResource(R.drawable.button_shadow);

    }

    public void onClickSuffocation(View view){
        ImageButton e_btn = findViewById(R.id.imageButton5);
        e_btn.setBackgroundResource(R.drawable.button_shadow);
    }

    public void onClickFire(View view){
        ImageButton e_btn = findViewById(R.id.imageButton7);
        e_btn.setBackgroundResource(R.drawable.button_shadow);
    }

    public void onClickMaternity(View view){
        ImageButton e_btn = findViewById(R.id.imageButton6);
        e_btn.setBackgroundResource(R.drawable.button_shadow);
    }

    public void onClickFoodPoison(View view){
        ImageButton e_btn = findViewById(R.id.imageButton8);
        e_btn.setBackgroundResource(R.drawable.button_shadow);
    }

    public void onClickAttack(View view){
        ImageButton accbtn = findViewById(R.id.imageButton4);
        accbtn.setBackgroundResource(R.drawable.button_shadow);
    }
    public void onClickFireBrigade(View view){
        ImageButton fire_btn = findViewById(R.id.imageButton12);
        Intent myIntent = new Intent(MainActivity.this, SpeechActivity.class);
        myIntent.putExtra("type","firebrigade");
        //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }

    public void onClickAmbulance(View view){
        Intent myIntent = new Intent(MainActivity.this, SpeechActivity.class);
        myIntent.putExtra("type","ambulance");
        //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }



}