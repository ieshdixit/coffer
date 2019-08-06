package elope.crazyones.com.coffer.serverapi.response;

import com.google.gson.annotations.SerializedName;

public class NewSMSReceivedResponse {
    @SerializedName("_id")
    String _id;
    @SerializedName("sender")
    String sender;
    @SerializedName("body")
    String body;

    public void setSender(String email) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public String getBody() {
        return body;
    }
}
