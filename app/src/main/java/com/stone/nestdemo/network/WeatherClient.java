package com.stone.nestdemo.network;

import com.stone.nestdemo.network.response.Weather;

import java.util.Map;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

@Singleton
public interface WeatherClient {

    //api.openweathermap.org/data/2.5/weather?APPID=30b66c22490dd2c0da584b66b4fa5ea1&q=London
    @GET("weather")
    Call<Weather> getWeather(@QueryMap Map<String, String> params);
}