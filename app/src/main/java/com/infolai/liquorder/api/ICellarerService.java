package com.infolai.liquorder.api;

import com.infolai.liquorder.models.Cellarer;
import com.infolai.liquorder.models.CellarerMenu;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ICellarerService {
    String ID = "id";

    @GET(Api.Url.GET_CELLARERS)
    Call<List<Cellarer>> getCellarers();

    @GET(Api.Url.GET_CELLARER_ACTIVE_MENU)
    Call<CellarerMenu> getCellarerActiveMenu(@Path(ID) int id);
}
