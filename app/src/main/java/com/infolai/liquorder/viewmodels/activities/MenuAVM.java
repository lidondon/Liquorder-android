package com.infolai.liquorder.viewmodels.activities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.infolai.liquorder.models.Category;
import com.infolai.liquorder.models.MenuItem;
import com.infolai.liquorder.models.Order;
import com.infolai.liquorder.repositories.CartRepository;
import com.infolai.liquorder.utilities.Util;
import com.infolai.liquorder.viewmodels.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuAVM extends BaseViewModel {
    private static final String GET_MENU_CATEGORIES = "GET_MENU_CATEGORIES";
    private static final String GET_CATEGORY_ITEMS = "GET_CATEGORY_ITEMS";

    private MutableLiveData<List<Category>> categories = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<MenuItem>> items = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Boolean> getItemCompleted = new MutableLiveData<>();
    private CartRepository cartRepository = CartRepository.getInstance();
    private int[] categoryIndices = new int[] { 0 };
    private int menuId;
    private int getCategoryItemsCount;

    public MenuAVM(@NonNull Application application) {
        super(application);
    }

    public void setMenuId(int id) {
        menuId = id;
        getCategoriesFromApi();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<List<MenuItem>> getItems() {
        return items;
    }

    public LiveData<Boolean> isGetItemsCompleted() {
        return getItemCompleted;
    }

    public int[] getCategoryIndices() {
        return categoryIndices;
    }

    public boolean isCartEmpty() {
        return cartRepository.isEmpty();
    }

    public Order getCartOrder() {
        return cartRepository.getOrder();
    }

    public int getCategoryPositionByItemPosition(int position) {
        return Util.getCategoryPositionByItemPosition(position, categoryIndices);
    }

    public void resetItemQuantities() {
        List<MenuItem> itemList = items.getValue();

        for (MenuItem item : itemList) {
            item.quantity = cartRepository.getQuantity(item.liquorId);
        }
    }

    private void getCategoriesFromApi() {
        loading.setValue(true);
        retrofitUtil.getMenuCategories(menuId, new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    getCategoriesDone(response);
                } else {
                    apiServerFail(response.raw().message(), GET_CATEGORY_ITEMS, () -> getCategoriesFromApi());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                connectionFail(MenuAVM.class.toString(), getErrorMessage(t));
            }
        });
    }

    private void getCategoriesDone(Response<List<Category>> response) {
        List<Category> categoryList = categories.getValue();

        categoryList.clear();
        categoryList.addAll(response.body());
        categories.setValue(categoryList);
        loading.setValue(false);
        recallSet.remove(GET_MENU_CATEGORIES);
        if (categoryList.size() > 0) {
            getItemCompleted.setValue(false);
            categoryIndices = new int[categoryList.size()];
            getCategoryItems();
        }
    }

    private void getCategoryItems() {
        List<Category> categoryList = categories.getValue();
        Category category = categoryList.get(getCategoryItemsCount);

        loading.setValue(true);
        retrofitUtil.getCategoryItems(menuId, category.id, new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                if (response.isSuccessful()) {
                    getCategoryItemsDone(category, response);
                } else {
                    apiServerFail(response.raw().message(), GET_CATEGORY_ITEMS, () -> getCategoryItems());
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                connectionFail(MenuAVM.class.toString(), getErrorMessage(t));
            }
        });
    }

    private void getCategoryItemsDone(Category category, Response<List<MenuItem>> response) {
        List<MenuItem> orgItemList = items.getValue();
        List<MenuItem> newItemList = response.body();
        MenuItem categoryItem = new MenuItem();

        newItemList.sort((i1, i2) -> {
            int result = 0;

            if (i1.isConsumerFav && !i2.isConsumerFav) {
                result = -1;
            } else if (!i1.isConsumerFav && i2.isConsumerFav) {
                result = 1;
            }

            return result;
        });
        categoryIndices[getCategoryItemsCount] = orgItemList.size();
        categoryItem.category = category;
        orgItemList.add(categoryItem);
        orgItemList.addAll(newItemList);
        items.setValue(orgItemList);
        loading.setValue(false);
        recallSet.remove(GET_CATEGORY_ITEMS);

        checkIsGetItemsCompleted();
        if (getCategoryItemsCount != 0) getCategoryItems();
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
