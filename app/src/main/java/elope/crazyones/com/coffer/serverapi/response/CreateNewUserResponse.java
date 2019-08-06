package elope.crazyones.com.coffer.serverapi.response;

import com.google.gson.annotations.SerializedName;

public class CreateNewUserResponse {
    // variable name should be same as in the json response from php
    @SerializedName("_id")
    String _id;
    @SerializedName("email")
    String email;
    @SerializedName("token")
    String token;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
