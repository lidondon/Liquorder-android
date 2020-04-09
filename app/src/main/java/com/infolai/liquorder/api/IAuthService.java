package com.infolai.liquorder.api;

import com.infolai.liquorder.models.AuthData;
import com.infolai.liquorder.models.httpparams.LoginParams;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAuthService {

    @POST(Api.Url.LOGIN_URL)
    Call<AuthData> login(@Body LoginParams params);

    @POST(Api.Url.REFRESH_TOKEN_URL)
    Call<AuthData> refreshToken(@Body AuthData authData);
}
