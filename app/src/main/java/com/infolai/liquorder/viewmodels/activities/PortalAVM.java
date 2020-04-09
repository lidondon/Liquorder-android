package com.infolai.liquorder.viewmodels.activities;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.infolai.liquorder.models.AuthData;
import com.infolai.liquorder.models.httpparams.LoginParams;
import com.infolai.liquorder.repositories.LoginDataRepository;
import com.infolai.liquorder.utilities.RetrofitUtil;
import com.infolai.liquorder.viewmodels.BaseViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PortalAVM extends BaseViewModel {
    private LoginDataRepository loginDataRepository;
    private MutableLiveData<Boolean> hasLoginData;
    private MutableLiveData<Boolean> loginSuccess;
    private MutableLiveData<String> connectionError;

    public PortalAVM(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        retrofitUtil = RetrofitUtil.getInstance();
        loginDataRepository = LoginDataRepository.getInstance();
        init();
        silentlyLogin();
    }

    private void init() {
        hasLoginData = new MutableLiveData<>();
        loginSuccess = new MutableLiveData<>();
        connectionError = new MutableLiveData<>();
    }

    public LiveData<Boolean> getHasLoginData() {
        return hasLoginData;
    }

    public LiveData<Boolean> isLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<String> getConnectionError() {
        return connectionError;
    }

    private void silentlyLogin() {
        LoginParams loginParams = loginDataRepository.getLoginData(context);

        if (loginParams == null) {
            hasLoginData.setValue(false);
        } else {
            callLoginApi(loginParams);
        }
    }

    private void callLoginApi(LoginParams loginParams) {
        retrofitUtil.login(loginParams, new Callback<AuthData>() {
            @Override
            public void onResponse(Call<AuthData> call, Response<AuthData> response) {
                loginResponse(response, loginParams);
            }

            @Override
            public void onFailure(Call<AuthData> call, Throwable t) {
                String errorMsg = getErrorMessage(t);

                connectionError.setValue(errorMsg);
            }
        });
    }

    private void loginResponse(Response<AuthData> response, LoginParams loginParams) {
        if (response.isSuccessful()) {
            loginDataRepository.authData = response.body();
            loginSuccess.setValue(true);
            retrofitUtil.updateRetrofit();
        } else {
            loginDataRepository.removeLoginData(context);
            loginSuccess.setValue(false);
        }
    }
}
