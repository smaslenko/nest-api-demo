package com.stone.nestdemo.network;

import com.stone.nestdemo.network.response.Home;
import com.stone.nestdemo.storage.model.Thermostat;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.http.GET;

@Singleton
public interface ApiClient {
    @GET("/")
    Call<Home> getHome();

    @GET("/devices/thermostats/{deviceId}")
    Call<Thermostat> getThermostat(String deviceId);

    @GET("/structures/{structureId}/postal_code")
    Call<String> getPostalCode(String structureId);
}
