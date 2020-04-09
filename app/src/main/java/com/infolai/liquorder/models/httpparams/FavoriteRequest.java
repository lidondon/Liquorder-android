package com.infolai.liquorder.models.httpparams;

import com.google.gson.annotations.SerializedName;

public class FavoriteRequest {
    @SerializedName("liquorId")
    public int liquorId;
    @SerializedName("favoriteId")
    public  int favoriteId;

    public FavoriteRequest(int lId, int fId) {
        liquorId = lId;
        favoriteId = fId;
    }
}
