package elope.crazyones.com.coffer.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User {
    @SerializedName("email")
    private String email;
    @SerializedName("displayName")
    private String displayName;
    @SerializedName("birthdate")
    private Date birthdate;
    @SerializedName("photoUrl")
    private String photoUrl;
    @SerializedName("gender")
    private Date gender;
    @SerializedName("mobileNumber")
    private String mobileNumber;

    public User(FirebaseUser firebaseUser){
        this.email = firebaseUser.getEmail();
        this.displayName = firebaseUser.getDisplayName();
        this.photoUrl = firebaseUser.getPhotoUrl().toString();
        this.mobileNumber = firebaseUser.getPhoneNumber();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setGender(Date gender) {
        this.gender = gender;
    }

    public Date getGender() {
        return gender;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
}
