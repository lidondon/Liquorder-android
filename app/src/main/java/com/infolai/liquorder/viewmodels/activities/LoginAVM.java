package com.infolai.liquorder.viewmodels.activities;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.infolai.liquorder.R;
import com.infolai.liquorder.models.AuthData;
import com.infolai.liquorder.models.httpparams.LoginParams;
import com.infolai.liquorder.repositories.LoginDataRepository;
import com.infolai.liquorder.viewmodels.BaseViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAVM extends BaseViewModel {
    private LoginDataRepository repository = LoginDataRepository.getInstance();
    private MutableLiveData<AuthData> authData = new MutableLiveData<>(repository.authData);

    public LoginAVM(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public LiveData<AuthData> getAuthData() {
        return authData;
    }

    public void login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            apiError.setValue(context.getString(R.string.complete_email_password));
        } else {
            callLoginApi(email, password);
        }
    }

    private void callLoginApi(String email, String password) {
        LoginParams loginParams = new LoginParams(email, password);

        loading.setValue(true);
        retrofitUtil.login(loginParams, new Callback<AuthData>() {
            @Override
            public void onResponse(Call<AuthData> call, Response<AuthData> response) {
                loginSuccess(response, loginParams);
            }

            @Override
            public void onFailure(Call<AuthData> call, Throwable t) {
                loginFail(getErrorMessage(t));
            }
        });
    }

    private void loginSuccess(Response<AuthData> response, LoginParams loginParams) {
        if (response.isSuccessful()) {
            AuthData data = response.body();

            authData.setValue(data);
            repository.authData = data;
            repository.saveLoginData(context, loginParams);
            retrofitUtil.updateRetrofit();
        } else {
            apiError.setValue(response.raw().message());
        }
        loading.setValue(false);
    }

    private void loginFail(String errorMsg) {
        Log.e(LoginAVM.class.toString(), errorMsg);
        apiError.setValue(errorMsg);
        loading.setValue(false);
    }

}
