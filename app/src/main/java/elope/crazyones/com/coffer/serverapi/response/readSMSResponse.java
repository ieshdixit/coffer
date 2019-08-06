package elope.crazyones.com.coffer.serverapi.response;

import com.google.gson.annotations.SerializedName;

public class readSMSResponse {
    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;
    public String getMessage() {
        return message;
    }
    public boolean getSuccess() {
        return success;
    }
}
