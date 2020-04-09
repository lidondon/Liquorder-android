package com.infolai.liquorder.viewmodels.activities;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.infolai.liquorder.models.BaseItem;
import com.infolai.liquorder.models.CellarerMenu;
import com.infolai.liquorder.models.Order;
import com.infolai.liquorder.models.httpparams.CreateOrderRequest;
import com.infolai.liquorder.models.httpparams.SaveOrderItemRequest;
import com.infolai.liquorder.repositories.CartRepository;
import com.infolai.liquorder.viewmodels.BaseViewModel;
import com.infolai.liquorder.viewmodels.fragments.CreateOrderFVM;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAVM extends BaseViewModel {
    private static final String CREATE_ORDER = "CREATE_ORDER";
    private static final String UPDATE_ORDER = "UPDATE_ORDER";
    private static final String GET_MENU = "GET_MENU";

    private CartRepository cartRepository = CartRepository.getInstance();
    private MutableLiveData<Boolean> done = new MutableLiveData<>();
    private MutableLiveData<List<BaseItem>> items = new MutableLiveData<>();
    private MutableLiveData<Integer> menuId = new MutableLiveData<>(-1);
    private int cellarerId;

    public CartAVM(@NonNull Application application) {
        super(application);
        items.setValue(cartRepository.getTotalItemList());
    }

    public void setCellarerId(int id) {
        cellarerId = id;
    }

    public LiveData<Boolean> isDone() {
        return done;
    }

    public LiveData<List<BaseItem>> getItems() {
        return items;
    }

    public LiveData<Integer> getMenuId() {
        return menuId;
    }

    public int getTotalAmount() {
        return cartRepository.getTotalAmount();
    }

    public Order getOrder() {
        return cartRepository.getOrder();
    }

    public void refreshItems() {
        List<BaseItem> newItemList = cartRepository.getTotalItemList();
        List<BaseItem> itemList = items.getValue();

        itemList.clear();
        itemList.addAll(newItemList);
        items.setValue(itemList);
    }

    public void createOrder(boolean isSubmit) {
        CreateOrderRequest request = cartRepository.getCreateOrderRequest(cellarerId);

        loading.setValue(true);
        retrofitUtil.createOrder(isSubmit, request, new Callback<Void>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    processDone(CREATE_ORDER, response);
                } else {
                    apiServerFail(response.raw().message(), CREATE_ORDER, () -> createOrder(isSubmit));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                loginFail(getErrorMessage(t));
            }
        });
    }

    public void updateOrder(boolean isSubmit) {
        SaveOrderItemRequest request = cartRepository.getSaveOrderItemRequest();

        loading.setValue(true);
        retrofitUtil.updateOrder(getOrder().id, isSubmit, request, new Callback<Void>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    processDone(UPDATE_ORDER, response);
                } else {
                    apiServerFail(response.raw().message(), UPDATE_ORDER, () -> updateOrder(isSubmit));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                loginFail(getErrorMessage(t));
            }
        });
    }

    private void processDone(String apiName, Response response) {
        if (response.isSuccessful()) {
            done.setValue(true);
            cartRepository.clearCart();
            recallSet.remove(apiName);
        } else {
            apiError.setValue(response.raw().message());
        }
        loading.setValue(false);
    }

    private void loginFail(String errorMsg) {
        Log.e(CartAVM.class.toString(), errorMsg);
        apiError.setValue(errorMsg);
        loading.setValue(false);
    }

    public void getMenuFromApi(int cellarerId) {
        loading.setValue(true);
        retrofitUtil.getCellarerActiveMenu(cellarerId, new Callback<CellarerMenu>() {
            @Override
            public void onResponse(Call<CellarerMenu> call, Response<CellarerMenu> response) {
                if (response.isSuccessful()) {
                    getMenuDone(response);
                } else {
                    apiServerFail(response.raw().message(), GET_MENU, () -> getMenuFromApi(cellarerId));
                }
            }

            @Override
            public void onFailure(Call<CellarerMenu> call, Throwable t) {
                connectionFail(CreateOrderFVM.class.toString(), getErrorMessage(t));
            }
        });
    }

    private void getMenuDone(Response<CellarerMenu> response) {
        loading.setValue(false);
        menuId.setValue(response.body().id);
        recallSet.remove(GET_MENU);
    }
}
