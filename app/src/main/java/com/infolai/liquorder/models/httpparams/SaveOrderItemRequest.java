package com.infolai.liquorder.models.httpparams;

import com.google.gson.annotations.SerializedName;
import com.infolai.liquorder.models.OrderItem;

public class SaveOrderItemRequest {
    @SerializedName("itemsToUpdate")
    public OrderItem[] itemsToUpdate;
    @SerializedName("itemIdsToDelete")
    public int[] itemIdsToDelete;
}
