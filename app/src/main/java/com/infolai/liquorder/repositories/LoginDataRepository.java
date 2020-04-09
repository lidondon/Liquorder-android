package com.infolai.liquorder.repositories;

import android.content.Context;

import com.google.gson.Gson;
import com.infolai.liquorder.models.AuthData;
import com.infolai.liquorder.models.httpparams.LoginParams;
import com.infolai.liquorder.utilities.SharedPreferenceUtil;

public class LoginDataRepository {
    private static final String LOGIN_DATA = "LOGIN_DATA";
    private static LoginDataRepository loginDataRepository;

    public AuthData authData;
    private SharedPreferenceUtil prefUtil;
    private Gson gson;

    private LoginDataRepository() {
        prefUtil = SharedPreferenceUtil.getInstance();
        gson = new Gson();
    }

    public static LoginDataRepository getInstance() {
        if (loginDataRepository == null) loginDataRepository = new LoginDataRepository();

        return loginDataRepository;
    }

    public LoginParams getLoginData(Context context) {
        String jLoginData = prefUtil.getDate(context, LOGIN_DATA);

        return jLoginData.isEmpty() ? null : gson.fromJson(jLoginData, LoginParams.class);
    }

    public void saveLoginData(Context context, LoginParams data) {
        prefUtil.saveData(context, LOGIN_DATA, gson.toJson(data));
    }

    public void removeLoginData(Context context) {
        prefUtil.removeData(context, LOGIN_DATA);
    }
}
