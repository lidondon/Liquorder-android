package com.infolai.liquorder.api;

import com.infolai.liquorder.models.Category;
import com.infolai.liquorder.models.FavoriteItem;
import com.infolai.liquorder.models.LiquorItem;
import com.infolai.liquorder.models.httpparams.FavoriteRequest;
import com.infolai.liquorder.models.httpparams.SaveOrderItemRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ILiquorService {
    String CATEGORY_ID = "categoryId";

    @GET(Api.Url.GET_LIQUOR_CATEGORIES)
    Call<List<Category>> getLiquorCategories();

    @GET(Api.Url.GET_LIQUOR_CATEGORY_ITEMS)
    Call<List<LiquorItem>> getCategoryItems(@Query(CATEGORY_ID) int categoryId);

    @GET(Api.Url.GET_FAVORITES)
    Call<List<FavoriteItem>> getFavorites();

    @POST(Api.Url.ADD_FAVORITE)
    Call<FavoriteItem> addFavorite(@Body FavoriteRequest request);

    @POST(Api.Url.REMOVE_FAVORITE)
    Call<Void> removeFavorite(@Body FavoriteRequest request);
}
