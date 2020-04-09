package com.infolai.liquorder.models.httpparams;

import com.google.gson.annotations.SerializedName;

public class CreateOrderRequest {
    @SerializedName("merchantId")
    public int cellarerId;
    @SerializedName("orderitemRequest")
    public SaveOrderItemRequest orderitemRequest;
}
