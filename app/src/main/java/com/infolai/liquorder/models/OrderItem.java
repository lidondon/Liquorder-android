package com.infolai.liquorder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderItem extends BaseItem {
    @SerializedName("menuitemDesc")
    public String menuitemDesc;
    @Expose(deserialize = false)
    @SerializedName("categoryId")
    public int categoryId;
    @SerializedName("categoryName")
    public String categoryName;

    public OrderItem clone() {
        OrderItem result = new OrderItem();

        result.id = id;
        result.liquorId = liquorId;
        result.liquorBottling = liquorBottling;
        result.liquorCapacity = liquorCapacity;
        result.liquorName = liquorName;
        result.liquorNameEn = liquorNameEn;
        result.price = price;
        result.quantity = quantity;
        result.orderId = orderId;
        result.menuitemDesc = menuitemDesc;
        result.categoryId = categoryId;
        result.categoryName = categoryName;

        return result;
    }
}
