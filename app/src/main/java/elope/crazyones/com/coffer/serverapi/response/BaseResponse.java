package elope.crazyones.com.coffer.serverapi.response;

import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {
    @SerializedName("status")
    String status;
    @SerializedName("data")
    T data;

    public void setData(T data) {
        this.data = data;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public T getData() {
        return data;
    }
}
