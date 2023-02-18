package com.pd.mvvmapplication.Network;

import com.pd.mvvmapplication.Model.Actor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    @GET("data.php")
    Call<List<Actor>> getAllActors();
}
