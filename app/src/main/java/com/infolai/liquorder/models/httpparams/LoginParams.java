package com.infolai.liquorder.models.httpparams;

import com.google.gson.annotations.SerializedName;

public class LoginParams {
    @SerializedName("email")
    public String email;
    @SerializedName("password")
    public String password;

    public LoginParams(String e, String p) {
        email = e;
        password = p;
    }
}
