package com.example.esap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread thread = new Thread(){

            public void run() {
                try {
                    sleep(1000);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                finally {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                    Log.i("currentUser", currentUser.toString());
                    Intent intent;
                    if(currentUser != null)
                    {
                        intent = new Intent(SplashScreen.this, MainActivity.class);
                        // send user to mainactivity
                    }
                    else{
                        intent = new Intent(SplashScreen.this, OTPActivity.class);
                    }
                    startActivity(intent);

                }
            }
        };
        thread.start();
    }
}