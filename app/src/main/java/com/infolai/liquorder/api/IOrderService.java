package com.infolai.liquorder.api;

import com.infolai.liquorder.models.Category;
import com.infolai.liquorder.models.Cellarer;
import com.infolai.liquorder.models.MenuItem;
import com.infolai.liquorder.models.Order;
import com.infolai.liquorder.models.OrderItem;
import com.infolai.liquorder.models.httpparams.CreateOrderRequest;
import com.infolai.liquorder.models.httpparams.SaveOrderItemRequest;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IOrderService {
    String MENU_ID = "menuId";
    String CATEGORY_ID = "categoryId";
    String IS_SUBMIT = "isSubmit";
    String ID = "id";
    String START_DATE = "startDate";
    String END_DATE = "endDate";
    String ORDER_ID = "orderId";

    @GET(Api.Url.GET_MENU_CATEGORIES)
    Call<List<Category>> getMenuCategories(@Path(MENU_ID) int menuId);

    @GET(Api.Url.GET_MENU_CATEGORY_ITEMS)
    Call<List<MenuItem>> getCategoryItems(@Path(MENU_ID) int menuId, @Query(CATEGORY_ID) int categoryId);

    @POST(Api.Url.CREATE_ORDER)
    Call<Void> createOrder(@Query(IS_SUBMIT) boolean isSubmit, @Body CreateOrderRequest request);

    @POST(Api.Url.UPDATE_ORDER)
    Call<Void> updateOrder(@Path(ID) int id, @Query(IS_SUBMIT) boolean isSubmit, @Body SaveOrderItemRequest request);

    @GET(Api.Url.GET_ORDERS)
    Call<List<Order>> getOrders(@Query(START_DATE) String startDate, @Query(END_DATE) String endDate);

    @GET(Api.Url.GET_ORDER_ITEMS)
    Call<List<OrderItem>> getOrderItems(@Path(ORDER_ID) int orderId);
}
