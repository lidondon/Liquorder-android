package com.infolai.liquorder.models;

import com.google.gson.annotations.SerializedName;

public class FavoriteItem {
    @SerializedName("id")
    public int id;
    @SerializedName("liquorId")
    public int liquorId;
    @SerializedName("bottling")
    public String bottling;
    @SerializedName("capacity")
    public int capacity;
    @SerializedName("name")
    public String name;
    @SerializedName("nameEn")
    public String nameEn;
}
