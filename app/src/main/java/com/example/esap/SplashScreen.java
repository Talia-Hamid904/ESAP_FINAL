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
                  //  currentUser.getDisplayName();
                    Intent intent;
                    if(currentUser != null)
                    {
                        /*Log.i("userName", currentUser.getDisplayName());
                        Log.i("userPhone", currentUser.getPhoneNumber());
                        Log.i("userEmail", currentUser.getEmail());*/
                       // String userPhoneNo = currentUser.getPhoneNumber();
                        intent = new Intent(SplashScreen.this, MainActivity.class);
                       // intent.putExtra("userPhone", userPhoneNo);
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