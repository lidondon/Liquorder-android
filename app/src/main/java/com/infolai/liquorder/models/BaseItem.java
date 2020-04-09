package com.infolai.liquorder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseItem {
    @SerializedName("id")
    public int id;
    @SerializedName("liquorId")
    public int liquorId;
    @SerializedName("unitId")
    public int unitId;
    @SerializedName("liquorBottling")
    public String liquorBottling;
    @SerializedName("liquorCapacity")
    public int liquorCapacity;
    @SerializedName("liquorName")
    public String liquorName;
    @SerializedName("liquorNameEn")
    public String liquorNameEn;
    @SerializedName("price")
    public int price;
    @SerializedName("quantity")
    public int quantity;
    @SerializedName("orderId")
    public int orderId; //in MenuItem, should be 0 for back end
}
