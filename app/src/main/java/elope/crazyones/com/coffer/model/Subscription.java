package elope.crazyones.com.coffer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Subscription implements Serializable {
    @SerializedName("_id")
    String _id;
    @SerializedName("SubscriptionIconLink")
    String SubscriptionIconLink;
    @SerializedName("SubscriptionName")
    String SubscriptionName;
    @SerializedName("SubscriptionCancelLink")
    String SubscriptionCancelLink;
    @SerializedName("SubscriptionCharges")
    String SubscriptionCharges;
    @SerializedName("SubscriptionDueDate")
    String SubscriptionDueDate;

    public Subscription(){
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSubscriptionIconLink() {
        return SubscriptionIconLink;
    }

    public void setSubscriptionIconLink(String subscriptionIconLink) {
        SubscriptionIconLink = subscriptionIconLink;
    }

    public String getSubscriptionName() {
        return SubscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        SubscriptionName = subscriptionName;
    }

    public String getSubscriptionCancelLink() {
        return SubscriptionCancelLink;
    }

    public void setSubscriptionCancelLink(String subscriptionCancelLink) {
        SubscriptionCancelLink = subscriptionCancelLink;
    }

    public String getSubscriptionCharges() {
        return SubscriptionCharges;
    }

    public void setSubscriptionCharges(String subscriptionCharges) {
        SubscriptionCharges = subscriptionCharges;
    }

    public String getSubscriptionDueDate() {
        return SubscriptionDueDate;
    }

    public void setSubscriptionDueDate(String subscriptionDueDate) {
        SubscriptionDueDate = subscriptionDueDate;
    }
}
