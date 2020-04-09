package com.infolai.liquorder.models;

import com.google.gson.annotations.SerializedName;

public class MenuItem extends BaseItem {
    @SerializedName("isConsumerFav")
    public boolean isConsumerFav;
    @SerializedName("itemDesc")
    public String itemDesc;

    public Category category;

    public MenuItem clone() {
        MenuItem result = new MenuItem();

        result.id = id;
        result.liquorId = liquorId;
        result.liquorBottling = liquorBottling;
        result.liquorCapacity = liquorCapacity;
        result.liquorName = liquorName;
        result.liquorNameEn = liquorNameEn;
        result.price = price;
        result.quantity = quantity;
        result.orderId = orderId;
        result.isConsumerFav = isConsumerFav;
        result.itemDesc = itemDesc;

        return result;
    }

    public OrderItem toOrderItem() {
        OrderItem result = new OrderItem();

        result.liquorId = liquorId;
        result.liquorBottling = liquorBottling;
        result.liquorCapacity = liquorCapacity;
        result.liquorName = liquorName;
        result.liquorNameEn = liquorNameEn;
        result.price = price;
        result.quantity = quantity;
        result.orderId = orderId;
        result.menuitemDesc = itemDesc;

        return result;
    }

}
