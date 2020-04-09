package com.infolai.liquorder.viewmodels.fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.infolai.liquorder.models.Category;
import com.infolai.liquorder.models.FavoriteItem;
import com.infolai.liquorder.models.LiquorItem;
import com.infolai.liquorder.models.httpparams.FavoriteRequest;
import com.infolai.liquorder.utilities.Util;
import com.infolai.liquorder.viewmodels.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesFVM extends BaseViewModel {
    private static final String GET_FAVORITES = "GET_FAVORITES";
    private static final String ADD_FAVORITE = "ADD_FAVORITE";
    private static final String REMOVE_FAVORITE = "REMOVE_FAVORITE";
    private static final String GET_LIQUOR_CATEGORIES = "GET_LIQUOR_CATEGORIES";
    private static final String GET_LIQUOR_CATEGORY_ITEMS = "GET_LIQUOR_CATEGORY_ITEMS";

    private MutableLiveData<List<Category>> categories = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<LiquorItem>> items = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<LiquorItem> successItem = new MutableLiveData<>();
    private MutableLiveData<LiquorItem> failItem = new MutableLiveData<>();
    private MutableLiveData<Boolean> getItemCompleted = new MutableLiveData<>();
    private List<FavoriteItem> fItemList = new ArrayList<>();
    private int[] categoryIndices = new int[] { 0 };
    private int getCategoryItemsCount;
    private boolean getFavoritesDone;

    public FavoritesFVM(@NonNull Application application) {
        super(application);
        getFavorites();
        getCategoriesFromApi();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<List<LiquorItem>> getItems() {
        return items;
    }

    public LiveData<LiquorItem> getSuccessItem() {
        return successItem;
    }

    public LiveData<LiquorItem> getFailItem() {
        return failItem;
    }

    public LiveData<Boolean> isGetItemsCompleted() {
        return getItemCompleted;
    }

    public int[] getCategoryIndices() {
        return categoryIndices;
    }

    public int getCategoryPositionByItemPosition(int position) {
        return Util.getCategoryPositionByItemPosition(position, categoryIndices);
    }

    public void addFavorite(LiquorItem item) {
        loading.setValue(true);
        successItem.setValue(null);
        retrofitUtil.addFavorite(new FavoriteRequest(item.id, 0), new Callback<FavoriteItem>() {
            @Override
            public void onResponse(Call<FavoriteItem> call, Response<FavoriteItem> response) {
                if (response.isSuccessful()) {
                    loading.setValue(false);
                    item.favoriteId = response.body().id;
                    successItem.setValue(item);
                } else {
                    if (!apiServerFail(response.raw().message(), ADD_FAVORITE, () -> addFavorite(item))) failItem.setValue(item);
                }
            }

            @Override
            public void onFailure(Call<FavoriteItem> call, Throwable t) {
                connectionFail(FavoritesFVM.class.toString(), getErrorMessage(t));
                failItem.setValue(item);
            }
        });
    }

    public void removeFavorite(LiquorItem item) {
        loading.setValue(true);
        successItem.setValue(null);
        retrofitUtil.removeFavorite(new FavoriteRequest(0, item.favoriteId), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    loading.setValue(false);
                    item.favoriteId = -1;
                    successItem.setValue(item);
                } else {
                    if (!apiServerFail(response.raw().message(), REMOVE_FAVORITE, () -> removeFavorite(item))) failItem.setValue(item);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                connectionFail(FavoritesFVM.class.toString(), getErrorMessage(t));
                failItem.setValue(item);
            }
        });
    }

    private void getFavorites() {
        loading.setValue(true);
        retrofitUtil.getFavorites(new Callback<List<FavoriteItem>>() {
            @Override
            public void onResponse(Call<List<FavoriteItem>> call, Response<List<FavoriteItem>> response) {
                if (response.isSuccessful()) {
                    getFavoritesDone(response);
                } else {
                    apiServerFail(response.raw().message(), GET_FAVORITES, () -> getFavorites());
                }
            }

            @Override
            public void onFailure(Call<List<FavoriteItem>> call, Throwable t) {
                connectionFail(FavoritesFVM.class.toString(), getErrorMessage(t));
            }
        });
    }

    private void getFavoritesDone(Response<List<FavoriteItem>> response) {
        int categoryCount = categories.getValue().size();

        fItemList.clear();
        fItemList.addAll(response.body());
        loading.setValue(false);
        recallSet.remove(GET_FAVORITES);
        getFavoritesDone = true;
        if (categoryCount > 0) {
            startGetItems(categoryCount);
        }
    }

    private void getCategoriesFromApi() {
        loading.setValue(true);
        retrofitUtil.getLiquorCategories(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    getCategoriesDone(response);
                } else {
                    apiServerFail(response.raw().message(), GET_LIQUOR_CATEGORIES, () -> getCategoriesFromApi());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                connectionFail(FavoritesFVM.class.toString(), getErrorMessage(t));
            }
        });
    }

    private void getCategoriesDone(Response<List<Category>> response) {
        List<Category> categoryList = categories.getValue();

        categoryList.clear();
        categoryList.addAll(response.body());
        categories.setValue(categoryList);
        loading.setValue(false);
        recallSet.remove(GET_LIQUOR_CATEGORIES);
        if (categoryList.size() > 0 && getFavoritesDone) {
            startGetItems(categoryList.size());
        }
    }

    private void startGetItems(int categoryCount) {
        getItemCompleted.setValue(false);
        categoryIndices = new int[categoryCount];
        getCategoryItems();
    }

    private void getCategoryItems() {
        List<Category> categoryList = categories.getValue();
        Category category = categoryList.get(getCategoryItemsCount);

        loading.setValue(true);
        retrofitUtil.getLiquorCategoryItems(category.id, new Callback<List<LiquorItem>>() {
            @Override
            public void onResponse(Call<List<LiquorItem>> call, Response<List<LiquorItem>> response) {
                if (response.isSuccessful()) {
                    getCategoryItemsDone(category, response);
                } else {
                    apiServerFail(response.raw().message(), GET_LIQUOR_CATEGORY_ITEMS, () -> getCategoryItems());
                }
            }

            @Override
            public void onFailure(Call<List<LiquorItem>> call, Throwable t) {
                connectionFail(FavoritesFVM.class.toString(), getErrorMessage(t));
            }
        });
    }

    private void getCategoryItemsDone(Category category, Response<List<LiquorItem>> response) {
        List<LiquorItem> orgItemList = items.getValue();
        List<LiquorItem> newItemList = response.body();
        LiquorItem categoryItem = new LiquorItem();

        categoryIndices[getCategoryItemsCount] = orgItemList.size();
        categoryItem.category = category;
        orgItemList.add(categoryItem);
        bindFavorite(newItemList);
        orgItemList.addAll(newItemList);
        items.setValue(orgItemList);
        loading.setValue(false);
        recallSet.remove(GET_LIQUOR_CATEGORY_ITEMS);

        checkIsGetItemsCompleted();
        if (getCategoryItemsCount != 0) getCategoryItems();
    }

    private void bindFavorite(List<LiquorItem> itemList) {
        for (LiquorItem item : itemList) {
            FavoriteItem fItem = fItemList.stream().filter(f -> f.liquorId == item.id).findFirst().orElse(null);

            if (fItem != null) {
                item.favoriteId = fItem.id;
                fItemList.remove(fItem);
            }
        }
    }

    private void checkIsGetItemsCompleted() {
        getCategoryItemsCount++;
        if (getCategoryItemsCount == categories.getValue().size()) {
            loading.setValue(false);
            getCategoryItemsCount = 0;
            getItemCompleted.setValue(true);
        }
    }
}
