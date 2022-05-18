package com.example.esap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class HelpImages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_help_images);
        String service = intent.getStringExtra("key");
        String[] splitService= service.split(" ");
        Log.i("splitService", splitService[0] );
        if(splitService[0].equals("firebrigade")){
            View imageView = (View) findViewById(R.id.pic1);
            imageView.setBackgroundResource(R.drawable.fire400);
            View imageView2 = (View) findViewById(R.id.pic2);
            imageView2.setBackgroundResource(R.drawable.burn400);
            View imageView3 = (View) findViewById(R.id.pic3);
            imageView3.setBackgroundResource(R.drawable.burn400);
        }
        else{
            Log.i("splitService", splitService[0] );
            View imageView = (View) findViewById(R.id.pic1);
            imageView.setBackgroundResource(R.drawable.injury400);
            View imageView2 = (View) findViewById(R.id.pic2);
            imageView2.setBackgroundResource(R.drawable.heatstroke400);
            View imageView3 = (View) findViewById(R.id.pic3);
            imageView3.setBackgroundResource(R.drawable.snakebite);
        }

        super.onCreate(savedInstanceState);
    }
}