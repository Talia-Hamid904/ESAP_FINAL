package com.example.esap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
            ImageView imageView = (ImageView) findViewById(R.id.imageView12);
            imageView.setImageResource(R.drawable.fire400);
            ImageView imageView2 = (ImageView) findViewById(R.id.imageView19);
            imageView2.setImageResource(R.drawable.burn400);
            ImageView imageView3 = (ImageView) findViewById(R.id.imageView21);
            imageView3.setImageResource(R.drawable.burn400);
        }
        else{
            Log.i("splitService", splitService[0] );
            ImageView imageView = (ImageView) findViewById(R.id.imageView12);
            imageView.setImageResource(R.drawable.injury400);
            ImageView imageView2 = (ImageView) findViewById(R.id.imageView19);
            imageView2.setImageResource(R.drawable.heatstroke400);
            ImageView imageView3 = (ImageView) findViewById(R.id.imageView21);
            imageView3.setImageResource(R.drawable.snakebite);
        }

    }
}