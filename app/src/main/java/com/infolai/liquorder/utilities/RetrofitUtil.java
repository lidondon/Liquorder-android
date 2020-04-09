package com.infolai.liquorder.utilities;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.infolai.liquorder.R;
import com.infolai.liquorder.api.Api;
import com.infolai.liquorder.api.ICellarerService;
import com.infolai.liquorder.api.IAuthService;
import com.infolai.liquorder.api.ILiquorService;
import com.infolai.liquorder.api.IOrderService;
import com.infolai.liquorder.models.AuthData;
import com.infolai.liquorder.models.Category;
import com.infolai.liquorder.models.Cellarer;
import com.infolai.liquorder.models.CellarerMenu;
import com.infolai.liquorder.models.FavoriteItem;
import com.infolai.liquorder.models.LiquorItem;
import com.infolai.liquorder.models.MenuItem;
import com.infolai.liquorder.models.Order;
import com.infolai.liquorder.models.OrderItem;
import com.infolai.liquorder.models.httpparams.CreateOrderRequest;
import com.infolai.liquorder.models.httpparams.FavoriteRequest;
import com.infolai.liquorder.models.httpparams.LoginParams;
import com.infolai.liquorder.models.httpparams.SaveOrderItemRequest;
import com.infolai.liquorder.repositories.LoginDataRepository;
import com.infolai.liquorder.views.activities.LoginActivity;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {
    private static RetrofitUtil retrofitUtil;

    private LoginDataRepository loginRepository = LoginDataRepository.getInstance();
    private IAuthService authService;
    private Retrofit retrofit; // updated often, so every time call api should create a new service, means we cant store services

    private RetrofitUtil() {
        Retrofit authRetrofit = new Retrofit.Builder().baseUrl(Api.Url.AUTH_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        authService = authRetrofit.create(IAuthService.class);
    }

    public static RetrofitUtil getInstance() {
        if (retrofitUtil == null) {
            retrofitUtil = new RetrofitUtil();
        }

        return retrofitUtil;
    }

    public void updateRetrofit() {
        retrofit = new Retrofit.Builder().baseUrl(Api.Url.BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .client(getHttpClient()).build();
    }

    public void login(LoginParams params, Callback<AuthData> callback) {
        Call<AuthData> call = authService.login(params);

        call.enqueue(callback);
    }

    public void refreshTokenAndCallApiAgain(Context context, IRecallApiTask task) {
        AuthData orgAuthData = loginRepository.authData;
        Call<AuthData> call = authService.refreshToken(orgAuthData);

        call.enqueue(new Callback<AuthData>() {
            @Override
            public void onResponse(Call<AuthData> call, Response<AuthData> response) {
                loginRepository.authData = response.body();
                task.call();
            }

            @Override
            public void onFailure(Call<AuthData> call, Throwable t) {
                context.startActivity(new Intent(context, LoginActivity.class));
                Toast.makeText(context, R.string.please_login_again, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCellarers(Callback<List<Cellarer>> callback) {
        ICellarerService service = retrofit.create(ICellarerService.class);
        Call<List<Cellarer>> call = service.getCellarers();

        call.enqueue(callback);
    }

    public void getCellarerActiveMenu(int id, Callback<CellarerMenu> callback) {
        ICellarerService service = retrofit.create(ICellarerService.class);
        Call<CellarerMenu> call = service.getCellarerActiveMenu(id);

        call.enqueue(callback);
    }

    public void getMenuCategories(int menuId, Callback<List<Category>> callback) {
        IOrderService service = retrofit.create(IOrderService.class);
        Call<List<Category>> call = service.getMenuCategories(menuId);

        call.enqueue(callback);
    }

    public void getCategoryItems(int menuId, int categoryId, Callback<List<MenuItem>> callback) {
        IOrderService service = retrofit.create(IOrderService.class);
        Call<List<MenuItem>> call = service.getCategoryItems(menuId, categoryId);

        call.enqueue(callback);
    }

    public void createOrder(boolean isSubmit, CreateOrderRequest request, Callback<Void> callback) {
        IOrderService service = retrofit.create(IOrderService.class);
        Call<Void> call = service.createOrder(isSubmit, request);

        call.enqueue(callback);
    }

    public void updateOrder(int orderId, boolean isSubmit, SaveOrderItemRequest request, Callback<Void> callback) {
        IOrderService service = retrofit.create(IOrderService.class);
        Call call = service.updateOrder(orderId, isSubmit, request);

        call.enqueue(callback);
    }

    public void getOrders(String startDate, String endDate, Callback<List<Order>> callback) {
        IOrderService service = retrofit.create(IOrderService.class);
        Call call = service.getOrders(startDate, endDate);

        call.enqueue(callback);
    }

    public void getOrderItems(int orderId, Callback<List<OrderItem>> callback) {
        IOrderService service = retrofit.create(IOrderService.class);
        Call call = service.getOrderItems(orderId);

        call.enqueue(callback);
    }

    public void getLiquorCategories(Callback<List<Category>> callback) {
        ILiquorService service = retrofit.create(ILiquorService.class);
        Call<List<Category>> call = service.getLiquorCategories();

        call.enqueue(callback);
    }

    public void getLiquorCategoryItems(int categoryId, Callback<List<LiquorItem>> callback) {
        ILiquorService service = retrofit.create(ILiquorService.class);
        Call<List<LiquorItem>> call = service.getCategoryItems(categoryId);

        call.enqueue(callback);
    }

    public void getFavorites(Callback<List<FavoriteItem>> callback) {
        ILiquorService service = retrofit.create(ILiquorService.class);
        Call<List<FavoriteItem>> call = service.getFavorites();

        call.enqueue(callback);
    }

    public void addFavorite(FavoriteRequest request, Callback<FavoriteItem> callback) {
        ILiquorService service = retrofit.create(ILiquorService.class);
        Call<FavoriteItem> call = service.addFavorite(request);

        call.enqueue(callback);
    }

    public void removeFavorite(FavoriteRequest request, Callback<Void> callback) {
        ILiquorService service = retrofit.create(ILiquorService.class);
        Call<Void> call = service.removeFavorite(request);

        call.enqueue(callback);
    }



    private OkHttpClient getHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(chain -> {
            Request orgRequest = chain.request();
            Request request = orgRequest.newBuilder()
                    .addHeader(Api.Authorization.AUTH_HEADER, Api.Authorization.BEARER_PREFIX + loginRepository.authData.token)
                    .build();

            return chain.proceed(request);
        });

        return builder.build();
    }

    public interface IRecallApiTask {
        void call();
    }
}
