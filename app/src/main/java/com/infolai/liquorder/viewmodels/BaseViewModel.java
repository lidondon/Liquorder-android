package com.infolai.liquorder.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.infolai.liquorder.R;
import com.infolai.liquorder.api.Api;
import com.infolai.liquorder.utilities.RetrofitUtil;
import com.infolai.liquorder.viewmodels.activities.MenuAVM;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Set;

public class BaseViewModel extends AndroidViewModel {
    protected Context context;
    protected Set<String> recallSet = new HashSet<>(); //if call api successfully, have to remove api key
    protected RetrofitUtil retrofitUtil = RetrofitUtil.getInstance();
    protected MutableLiveData<Boolean> loading = new MutableLiveData<>();
    protected MutableLiveData<String> apiError = new MutableLiveData<>();

    public BaseViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public LiveData<String> getApiError() {
        return apiError;
    }


    protected String getErrorMessage(Throwable t) {
        int errorId = R.string.server_error;

        if (t instanceof SocketTimeoutException) {
            errorId = R.string.network_connection_timeout;
        } else if (t instanceof IOException) {
            errorId = R.string.network_connection_fail;
        }

        return context.getString(errorId);
    }

    protected boolean apiServerFail(String errorMsg, String apiName, RetrofitUtil.IRecallApiTask task) {
        boolean result = true;

        if (errorMsg.equals(Api.Status.UNAUTHORIZED) && !recallSet.contains(apiName)) {
            recallSet.add(apiName);
            RetrofitUtil.getInstance().refreshTokenAndCallApiAgain(context, task);
        } else {
            loading.setValue(false);
            apiError.setValue(errorMsg);
            result = false;
        }

        return result;
    }

    protected void connectionFail(String className, String errorMsg) {
        Log.e(className, errorMsg);
        apiError.setValue(errorMsg);
        loading.setValue(false);
    }
}
