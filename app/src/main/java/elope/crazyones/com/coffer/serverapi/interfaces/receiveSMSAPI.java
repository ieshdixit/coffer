package elope.crazyones.com.coffer.serverapi.interfaces;

import elope.crazyones.com.coffer.model.ReceivedSMS;
import elope.crazyones.com.coffer.serverapi.response.BaseResponse;
import elope.crazyones.com.coffer.serverapi.response.NewSMSReceivedResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface receiveSMSAPI {
    @POST("/v1/user")
    Call<BaseResponse<NewSMSReceivedResponse>> logNewSMS(@Body ReceivedSMS sms);
}
