package com.example.esap;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class BotResponse {
    String recipient_id;
    String text;

    public BotResponse(String recipient_id, String text) {
        this.recipient_id = recipient_id;
        this.text = text;

    }

    public String getRecipient_id() {
        return recipient_id;
    }

    public String getText() {
        return text;
    }
}

