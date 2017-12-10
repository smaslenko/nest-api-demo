package com.stone.nestdemo.network;

import com.stone.nestdemo.network.response.Structure;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiClient {
    @GET("/devices")
    Call<Structure> getDevices();
}
