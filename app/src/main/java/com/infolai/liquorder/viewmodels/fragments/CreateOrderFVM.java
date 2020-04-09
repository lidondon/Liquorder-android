package com.infolai.liquorder.viewmodels.fragments;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.infolai.liquorder.models.Cellarer;
import com.infolai.liquorder.models.CellarerMenu;
import com.infolai.liquorder.repositories.CartRepository;
import com.infolai.liquorder.utilities.RetrofitUtil;
import com.infolai.liquorder.viewmodels.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrderFVM extends BaseViewModel {
    private static final String GET_CELLARERS = "GET_CELLARERS";
    private static final String GET_CELLARER_ACTIVE_MENU = "GET_CELLARER_ACTIVE_MENU";

    private RetrofitUtil retrofitUtil = RetrofitUtil.getInstance();;
    private MutableLiveData<List<Cellarer>> cellarers = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Integer> selectedMenuId = new MutableLiveData<>();
    private MutableLiveData<View> selectedItem = new MutableLiveData<>();
    private Cellarer selectedCellarer;

    public CreateOrderFVM(@NonNull Application application) {
        super(application);
        getCellarersFromApi();
    }

    public Cellarer getSelectedCellarer() {
        return selectedCellarer;
    }

    public LiveData<List<Cellarer>> getCellarers() {
        return cellarers;
    }

    public LiveData<Integer> getSelectedMenuId() {
        return selectedMenuId;
    }

    public LiveData<View> getSelectedItem() {
        return selectedItem;
    }

    public void clearCart() {
        CartRepository.getInstance().clearCart();
    }

    public void selectMenu(View view, int cellarerId) {
        loading.setValue(true);
        selectedItem.setValue(view);
        selectedCellarer = getCellarerById(cellarerId);
        retrofitUtil.getCellarerActiveMenu(cellarerId, new Callback<CellarerMenu>() {
            @Override
            public void onResponse(Call<CellarerMenu> call, Response<CellarerMenu> response) {
                if (response.isSuccessful()) {
                    selectMenuDone(response);
                } else {
                    apiServerFail(response.raw().message(), GET_CELLARER_ACTIVE_MENU, () -> selectMenu(view, cellarerId));
                }
            }

            @Override
            public void onFailure(Call<CellarerMenu> call, Throwable t) {
                connectionFail(CreateOrderFVM.class.toString(), getErrorMessage(t));
            }
        });
    }

    private void getCellarersFromApi() {
        loading.setValue(true);
        retrofitUtil.getCellarers(new Callback<List<Cellarer>>() {
            @Override
            public void onResponse(Call<List<Cellarer>> call, Response<List<Cellarer>> response) {
                if (response.isSuccessful()) {
                    getCellarersDone(response);
                } else {
                    apiServerFail(response.raw().message(), GET_CELLARERS, () -> getCellarersFromApi());
                }
            }

            @Override
            public void onFailure(Call<List<Cellarer>> call, Throwable t) {
                connectionFail(CreateOrderFVM.class.toString(), getErrorMessage(t));
            }
        });
    }

    private void getCellarersDone(Response<List<Cellarer>> response) {
        List<Cellarer> cellarerList = cellarers.getValue();

        cellarerList.clear();
        cellarerList.addAll(response.body());
        cellarers.setValue(cellarerList);
        recallSet.remove(GET_CELLARERS);
        loading.setValue(false);
    }

    private void selectMenuDone(Response<CellarerMenu> response) {
        loading.setValue(false);
        selectedMenuId.setValue(response.body().id);
        recallSet.remove(GET_CELLARER_ACTIVE_MENU);
    }

    private Cellarer getCellarerById(int id) {
        List<Cellarer> cellarerList = cellarers.getValue();
        Cellarer result = null;

        for (Cellarer c : cellarerList) {
            if (c.id == id) {
                result = c;
                break;
            }
        }

        return result;
    }
}
