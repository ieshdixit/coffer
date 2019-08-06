package elope.crazyones.com.coffer.serverapi.interfaces;

import elope.crazyones.com.coffer.serverapi.response.readSMSResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface readSMSAPI {
    @Multipart
    @POST("/v1/sms csv")
    Call<readSMSResponse> uploadFile(@Part MultipartBody.Part file);
}
