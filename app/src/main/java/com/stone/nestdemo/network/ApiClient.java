package com.stone.nestdemo.network;

import com.stone.nestdemo.network.request.TargetTemperatureRequest;
import com.stone.nestdemo.network.response.Home;
import com.stone.nestdemo.storage.model.Thermostat;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

@Singleton
public interface ApiClient {
    @GET("/")
    Call<Home> getHome();

    @GET("/devices/thermostats/{deviceId}")
    Call<Thermostat> getThermostat(String deviceId);

    @PUT("/devices/thermostats/{deviceId}")
    Call<TargetTemperatureRequest> putTemperature(@Path("deviceId") String deviceId, @Body TargetTemperatureRequest body);

    @PUT Call<TargetTemperatureRequest> putTemperatureRedirect(@Url String url, @Body TargetTemperatureRequest request);

    @GET("/structures/{structureId}/postal_code")
    Call<String> getPostalCode(String structureId);
}
