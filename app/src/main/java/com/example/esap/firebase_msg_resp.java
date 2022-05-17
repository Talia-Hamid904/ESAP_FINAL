package com.example.esap;

public class firebase_msg_resp {

    // variables for storing our data.
    public String user_msg, resp, location;

    public firebase_msg_resp() {
        // empty constructor
        // required for Firebase.
    }

    // Constructor for all variables.
    public firebase_msg_resp(String user_msg, String resp, String location) {
        this.user_msg= user_msg;
        this.resp = resp;
        this.location = location;
    }

    //getter methods for all variables.
    public String getUser_msg() {
        return user_msg;
    }

    public String getResp() {
        return resp;
    }

    public String getLocation() {
        return resp;
    }


    // setter method for all variables.
    public void setUser_msg(String user_msg) {
        this.user_msg= user_msg;
    }


    public void setResp(String resp) {
        this.resp = resp;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
