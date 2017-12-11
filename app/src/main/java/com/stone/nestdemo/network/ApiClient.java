package com.stone.nestdemo.network;

import com.stone.nestdemo.network.response.Home;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.http.GET;

@Singleton
public interface ApiClient {
    @GET("/")
    Call<Home> getHome();
}
