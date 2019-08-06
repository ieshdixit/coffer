package elope.crazyones.com.coffer.model;

import com.google.gson.annotations.SerializedName;

public class ReceivedSMS {

    @SerializedName("sender")
    private String sender;
    @SerializedName("body")
    private String body;

    public ReceivedSMS(String msg, String sender){
        this.body = msg;
        this.sender = sender;
    }

}
