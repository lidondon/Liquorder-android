package com.infolai.liquorder.viewmodels.fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.infolai.liquorder.R;
import com.infolai.liquorder.models.Cellarer;
import com.infolai.liquorder.models.Order;
import com.infolai.liquorder.models.OrderItem;
import com.infolai.liquorder.repositories.CartRepository;
import com.infolai.liquorder.utilities.Util;
import com.infolai.liquorder.viewmodels.BaseViewModel;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFVM extends BaseViewModel {
    private static final String GET_CELLARERS = "GET_CELLARERS";
    private static final String GET_ORDERS = "GET_ORDERS";
    private static final String GET_ORDER_ITEMS = "GET_ORDER_ITEMS";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private MutableLiveData<List<Cellarer>> cellarers = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<Order>> orders = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<Order>> filteredOrders = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Boolean> getOrderItemsDone = new MutableLiveData<>();
    private CartRepository cartRepository = CartRepository.getInstance();
    private Order order;

    public OrdersFVM(@NonNull Application application) {
        super(application);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        getCellarersFromApi();
    }

    public LiveData<List<Cellarer>> getCellarers() {
        return cellarers;
    }

    public LiveData<List<Order>> getOrders() {
        return filteredOrders;
    }

    public LiveData<Boolean> isGetOrderItemsDone() {
        return getOrderItemsDone;
    }

    public String[] getDefaultDateRange() {
        Calendar calendar = Calendar.getInstance();
        String[] result = new String[2];

        result[1] = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE, -60);
        result[0] = dateFormat.format(calendar.getTime());
        getOrdersFromApi(result[0], result[1]);

        return result;
    }

    public void setOrderIn2Cart(Order o) {
        order = o;
        getOrderItems();
    }

    public void filterCellarer(int cellarerId) {
        List<Order> allOrderList = orders.getValue();
        List<Order> orgFilteredOrderList = filteredOrders.getValue();

        orgFilteredOrderList.clear();
        if (cellarerId != R.string.unlimited) {
            List<Order> filteredOrderList = new ArrayList<>();

            for (Order order : allOrderList) {
                if (order.cellarerId == cellarerId) filteredOrderList.add(order);
            }
            orgFilteredOrderList.addAll(filteredOrderList);
        } else {
            orgFilteredOrderList.addAll(allOrderList);
        }
        filteredOrders.setValue(orgFilteredOrderList);
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

    public void getOrdersFromApi(final String startDate, final String endDate) {
        loading.setValue(true);
        String utcStartDate = Util.toUtcDate(Calendar.getInstance().getTime().toString());
        String utcEndDate = Util.toUtcDate(endDate);
        retrofitUtil.getOrders(startDate, endDate, new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    getOrdersDone(response);
                } else {
                    apiServerFail(response.raw().message(), GET_ORDERS, () -> getOrdersFromApi(utcStartDate, utcEndDate));
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                connectionFail(OrdersFVM.class.toString(), getErrorMessage(t));
            }
        });
    }

    private void getOrdersDone(Response<List<Order>> response) {
        List<Order> orderList = orders.getValue();
        List<Order> filteredOrderList = filteredOrders.getValue();

        orderList.clear();
        orderList.addAll(response.body());
        orders.setValue(orderList);
        filteredOrderList.clear();
        filteredOrderList.addAll(response.body());
        filteredOrders.setValue(filteredOrderList);
        recallSet.remove(GET_ORDERS);
        loading.setValue(false);
    }

    private void getOrderItems() {
        loading.setValue(true);
        retrofitUtil.getOrderItems(order.id, new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                if (response.isSuccessful()) {
                    getOrderItemsDone(response);
                } else {
                    apiServerFail(response.raw().message(), GET_ORDER_ITEMS, () -> getOrderItems());
                }
            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {
                connectionFail(OrdersFVM.class.toString(), getErrorMessage(t));
            }
        });
    }

    private void getOrderItemsDone(Response<List<OrderItem>> response) {
        cartRepository.setOrder(order, response.body());
        getOrderItemsDone.setValue(true);
        recallSet.remove(GET_ORDER_ITEMS);
        loading.setValue(false);
    }
}
