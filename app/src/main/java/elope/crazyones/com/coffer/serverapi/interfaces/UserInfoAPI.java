package elope.crazyones.com.coffer.serverapi.interfaces;

import elope.crazyones.com.coffer.model.User;
import elope.crazyones.com.coffer.serverapi.response.BaseResponse;
import elope.crazyones.com.coffer.serverapi.response.CreateNewUserResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserInfoAPI {
    @POST("/v1/user")
    Call<BaseResponse<CreateNewUserResponse>> createNewUser(@Body User user);
}
