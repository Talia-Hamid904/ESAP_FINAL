package com.example.esap;

public class UserData {
    String phone;
    String uid;

    public UserData(String phone, String uid) {
        this.phone = phone;
        this.uid = uid;
    }

    public String getphone() {

        return phone;
    }

    public String getuid() {
        return uid;
    }

    public void setphone(String phone){
        this.phone=phone;
    }

    public void setUid(String uid){
        this.uid=uid;
    }
}
