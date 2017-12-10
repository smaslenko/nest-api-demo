package com.stone.nestdemo.network;

import com.stone.nestdemo.network.response.Structure;

import javax.inject.Inject;

import retrofit2.Call;

public class ApiClientManager {

    public static final String BASE_URL = "https://developer-api.nest.com";
    public static final String ACCESS_TOKEN = "Bearer c.XPJOWxUIBESorkzWrMOPiVtQMHHfA94oE18NEOATkc1eIYrfEHqeBpyefmI7ibWAzixX70FwCYYUyzAbeFcWe4gSFcj7gDxXGgSYWQTFyQVDQfpT3sY2iZaQLuodQw6S3C67F27EQsUXkli9";

    ApiClient mApiClient;

    @Inject
    public ApiClientManager(ApiClient apiClient) {
        mApiClient = apiClient;
    }

    public Call<Structure> getDevices() {
        return mApiClient.getDevices();
    }

}