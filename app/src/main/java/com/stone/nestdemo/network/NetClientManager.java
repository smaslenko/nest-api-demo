package com.stone.nestdemo.network;

import com.stone.nestdemo.network.response.Home;
import com.stone.nestdemo.network.response.Weather;
import com.stone.nestdemo.storage.model.Thermostat;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class NetClientManager {

    public static final String BASE_URL = "https://developer-api.nest.com";
    public static final String ACCESS_TOKEN = "Bearer c.bsfxVCiWzYOMcl9wZse0EnAYku6j2OLoi2uE5yNVxhB3LsGP2kcdDac4frEin1jADTpxQJOvh5e1PqDwnF71BckNLUTmGJj6UoEld0vrGSiL7VKvYElg7eRC2dfEEql2Z0svUY5XxeTuEOgD";
    public static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String WEATHER_API_KEY = "30b66c22490dd2c0da584b66b4fa5ea1";

    private final ApiClient mApiClient;
    private final WeatherClient mWeatherClient;

    @Inject NetClientManager(ApiClient apiClient, WeatherClient weatherClient) {
        mApiClient = apiClient;
        mWeatherClient = weatherClient;
    }

    public Call<Home> getHome() {
        return mApiClient.getHome();
    }

    public Call<Thermostat> getThermostat(String deviceId) {
        return mApiClient.getThermostat(deviceId);
    }

    public Call<Weather> getWeather(String city) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("APPID", WEATHER_API_KEY);
        params.put("q", city);
        params.put("units", "metric");

        return mWeatherClient.getWeather(params);
    }
}
