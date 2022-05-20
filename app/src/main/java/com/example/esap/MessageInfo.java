package com.example.esap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MessageInfo extends AppCompatActivity {

    private String phoneNo;
    private String phoneNoSrc;
    private String message;
    String service;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private FirebaseFirestore db;
    String[] splitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_info);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        service = intent.getStringExtra("key");
        splitService = service.split(" ");
        TextView msg = findViewById(R.id.report_confirm);

        if (splitService[0].equals("ambulance")) {
               msg.setText("آپ کی درخواست درج کر لی گئی ہے۔ آپ کی مدد کے لئے ایمبولینس جلد بھیج دی جائے گی۔");
        }
        if (splitService[0].equals("firebrigade")) {
               msg.setText("آپ کی درخواست درج کر لی گئی ہے۔ آپ کی مدد کے لئے فائیربریگیڈ جلد بھیج دی جائے گی۔");
        }
        double latitude = intent.getDoubleExtra("key2", 0.0);
        double longitude = intent.getDoubleExtra("key3", 0.0);
        TextView location = findViewById(R.id.report_location);
        String address = getAddress(latitude,longitude);
        location.setText(address);
        String text = intent.getStringExtra("key4");
        TextView userMsg = findViewById(R.id.report);
         userMsg.setText(text);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userPhoneNo = currentUser.getPhoneNumber();
        addDataToFirestore(text,splitService[0], address, userPhoneNo );

        Thread thread1 = new Thread() {

            public void run() {
                try {
                    sendSMSMessage(address, userPhoneNo, text, splitService[0], splitService[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
            }
        };

        Thread thread = new Thread() {

            public void run() {
                try {
                    sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    Intent intent = new Intent(MessageInfo.this, HelpImages.class);
                    intent.putExtra("key", service);
                    startActivity(intent);
                }
            }
        };
        thread1.start();
        thread.start();


    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);

            Log.v("IGA", "Address: " + add);
            return add;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    protected void sendSMSMessage(String address, String userPhoneNo, String text, String intent, String entity) {
        phoneNo = "03155903128";
        phoneNoSrc = "03155903128";
        message = "ambulance";

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                    new Intent(SENT), 0);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                    new Intent(DELIVERED), 0);

            //---when the SMS has been sent---
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "SMS sent",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(getBaseContext(), "Generic failure",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(getBaseContext(), "No service",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(getBaseContext(), "Null PDU",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(getBaseContext(), "Radio off",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(SENT));

            //---when the SMS has been delivered---
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "SMS delivered",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(getBaseContext(), "SMS not delivered",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, phoneNoSrc, message, sentPI, deliveredPI);

        }
        String topic = "ambulance";

// See documentation on defining a message payload.
        /*Message message = Message.builder()
                .putData("address", address)
                .putData("userPhoneNo", userPhoneNo)
                .putData("text",text)
                .putData("intent",intent)
                .putData("entity",entity)
                .setTopic(topic)
                .build();*/

// Send a message to the devices subscribed to the provided topic.
        /*String response = FirebaseMessaging.getInstance().send(message);
// Response is a message ID string.
        System.out.println("Successfully sent message: " + response);*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    private void addDataToFirestore(String user_msg, String resp, String loc, String userPhone) {

        // creating a collection reference
        // for our Firebase Firestore database.
        CollectionReference dbCourses = db.collection("user_msg_resp");

        // adding our data to our courses object class.
        firebase_msg_resp data = new firebase_msg_resp(user_msg, resp, loc, userPhone);

        // below method is use to add data to Firebase Firestore.
        dbCourses.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(MessageInfo.this, "Your Data has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(MessageInfo.this, "Fail to add data \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

