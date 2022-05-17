package com.example.esap;

public class firebase_msg_resp {

    // variables for storing our data.
    public String user_msg, resp, location, userPhone;

    public firebase_msg_resp() {
        // empty constructor
        // required for Firebase.
    }

    // Constructor for all variables.
    public firebase_msg_resp(String user_msg, String resp, String location, String userPhone) {
        this.user_msg= user_msg;
        this.resp = resp;
        this.location = location;
        this.userPhone = userPhone;
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

    public String getUserPhone() {
        return userPhone;
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

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
