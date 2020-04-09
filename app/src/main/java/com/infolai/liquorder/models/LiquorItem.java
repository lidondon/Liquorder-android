package com.infolai.liquorder.models;

import com.google.gson.annotations.SerializedName;

public class LiquorItem {
    @SerializedName("id")
    public int id;
    @SerializedName("brandId")
    public int brandId;
    @SerializedName("bottling")
    public String bottling;
    @SerializedName("capacity")
    public int capacity;
    @SerializedName("name")
    public String name;
    @SerializedName("nameEn")
    public String nameEn;

    public Category category;
    //public boolean isFavorite;
    public int favoriteId = -1;
}
