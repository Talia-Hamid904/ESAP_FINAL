package com.example.esap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MessageInfo2 extends AppCompatActivity {
    RequestQueue requestQueue;
    private String phoneNo;
    private String phoneNoSrc;
    private String message;
    String service;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private FirebaseFirestore db;

    private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private String  serverKey ="key=" + "AAAAIe2e9r4:APA91bHqO7gi-jG2EMxVcvPkiTdLcPES9Nx1BEt8qp6EOQu7G3uJHhy8mswEJLFXvhBygP_JOgWThdvsIoovM2lY-XJjKz24Acgk_hg1oNlQ-L4FJD-wYPRd0GzatAZtPbwdCg7Qbo0l";
    private String contentType = "application/json";
    String address;
    String userPhoneNo;
    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_info2);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        requestQueue = Volley.newRequestQueue(this);
        service = intent.getStringExtra("key");

       // String[] splitService = service.split(" ");
        TextView msg = findViewById(R.id.report_confirm);
        ImageView background =  findViewById(R.id.top_img);


        if (service.equals("ambulance")) {
            msg.setText("آپ کی درخواست درج کر لی گئی ہے۔ آپ کی مدد کے لئے ایمبولینس جلد بھیج دی جائے گی۔");
        }
        if (service.equals("firebrigade")) {
            background.setBackgroundResource(R.drawable.fire_3d);
            msg.setText("آپ کی درخواست درج کر لی گئی ہے۔ آپ کی مدد کے لئے فائیربریگیڈ جلد بھیج دی جائے گی۔");
        }
        double latitude = intent.getDoubleExtra("key2", 0.0);
        double longitude = intent.getDoubleExtra("key3", 0.0);
        TextView location = findViewById(R.id.report_location);
        address = getAddress(latitude,longitude);
        location.setText(address);
        text = intent.getStringExtra("key4");
        TextView userMsg = findViewById(R.id.report);
        userMsg.setText(text);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userPhoneNo = currentUser.getPhoneNumber();
        addDataToFirestore(text,service, address, userPhoneNo );

        Thread thread1 = new Thread() {

            public void run() {
                try {
                    sendSMSMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
            }
        };

        Thread thread3 = new Thread() {

            public void run() {
                try {
                    sendServerMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
            }
        };

        Thread thread = new Thread() {

            public void run() {
                try {
                    sleep(8000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    Intent intent = new Intent(MessageInfo2.this, HelpImages.class);
                    intent.putExtra("key", service);
                    startActivity(intent);
                }
            }
        };
        thread1.start();
        thread3.start();
        thread.start();

    }

    private void sendServerMessage() {
        String topic = "/topics/ambulance" ;//topic has to match what the receiver subscribed to

        JSONObject notification = new JSONObject();
        JSONObject notificationBody = new JSONObject();

        try {
            notificationBody.put("Title", "Message");
            Log.i("NotificationBody", notificationBody.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            notificationBody.put("Message", text);
            Log.i("NotificationBody", notificationBody.toString());//Enter your notification message
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            notificationBody.put("Address", address);
            Log.i("NotificationBody", notificationBody.toString());//Enter your notification message
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            notificationBody.put("User's Phone Number", userPhoneNo);
            Log.i("NotificationBody", notificationBody.toString());//Enter your notification message
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            notificationBody.put("User's Message", service);
            Log.i("NotificationBody", notificationBody.toString());//Enter your notification message
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            notification.put("to", topic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            notification.put("data", notificationBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("TAG", "try");
        sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {
        Log.e("TAG", "sendNotification");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("TAG", "onResponse: $response");
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("TAG","Error :" + error.toString());
            }
        })
        {
            @Override
            public HashMap<String, String> getHeaders () {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorization",serverKey);
                params.put("Content-Type",contentType);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
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

    protected void sendSMSMessage() {
        phoneNo = "03036765805";
        phoneNoSrc = "03036765805";
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
                Toast.makeText(MessageInfo2.this, "Your Data has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(MessageInfo2.this, "Fail to add data \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}