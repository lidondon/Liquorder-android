package com.infolai.liquorder.models;

import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("id")
    public int id;
    @SerializedName("merchantId")
    public int cellarerId;
    @SerializedName("merchantName")
    public String cellarerName;
    @SerializedName("merchantSN")
    public String cellarerSN;
    @SerializedName("formNumber")
    public String formNumber;
    @SerializedName("rejectReason")
    public String rejectReason;
    @SerializedName("orderStatus")
    public String orderStatus;
    @SerializedName("createDateTime")
    public String createDateTime;
}
