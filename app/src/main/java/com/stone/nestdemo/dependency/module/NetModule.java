package com.stone.nestdemo.dependency.module;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stone.nestdemo.network.ApiClient;
import com.stone.nestdemo.network.WeatherClient;

import java.io.IOException;

import javax.annotation.Nullable;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {

    private final String mBaseUrl;
    private final String mWeatherUrl;
    private final String mAccessToken;

    // Constructor needs one parameter to instantiate.
    public NetModule(String baseUrl, String accessToken, String weatherUrl) {
        mBaseUrl = baseUrl;
        mAccessToken = accessToken;
        mWeatherUrl = weatherUrl;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
            .setLenient()
            .create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient() {
        return new OkHttpClient().newBuilder()
            .authenticator(new Authenticator() {
                @Nullable @Override
                public Request authenticate(@NonNull Route route, @NonNull Response response) throws IOException {
                    Request.Builder builder = response.request().newBuilder().addHeader("Authorization", mAccessToken);
                    return builder.build();
                }
            })
            .retryOnConnectionFailure(true)
            .followRedirects(true)
            .followSslRedirects(true)
            .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
            .baseUrl(mBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    }

    @Provides
    @Singleton
    ApiClient provideApiClient(Retrofit retrofit) {
        return retrofit.create(ApiClient.class);
    }

    @Provides
    @Singleton
    WeatherClient provideWeatherClient(Retrofit retrofit) {
        Retrofit weatherRetrofit = retrofit.newBuilder()
            .baseUrl(mWeatherUrl)
            .client(new OkHttpClient())
            .build();
        return weatherRetrofit.create(WeatherClient.class);
    }

}