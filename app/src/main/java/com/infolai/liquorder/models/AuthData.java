package com.infolai.liquorder.models;

import com.google.gson.annotations.SerializedName;

public class AuthData {
    @SerializedName("token")
    public String token;
    @SerializedName("refreshToken")
    public String refreshToken;
}
