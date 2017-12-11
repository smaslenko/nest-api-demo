package com.stone.nestdemo.dependency.module;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stone.nestdemo.network.ApiClient;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {

    private final String mBaseUrl;
    private final String mAccessToken;

    // Constructor needs one parameter to instantiate.
    public NetModule(String baseUrl, String accessToken) {
        mBaseUrl = baseUrl;
        mAccessToken = accessToken;
    }

    @Provides
    @Singleton
    Cache provideHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        return new Cache(application.getCacheDir(), cacheSize);
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
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
            .baseUrl(mBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient(Cache cache) {
        return new OkHttpClient().newBuilder().addNetworkInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder builder = originalRequest.newBuilder().addHeader("Authorization", mAccessToken);
                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        })
            .cache(cache)
            .build();
    }

    @Provides
    @Singleton
    ApiClient provideApiClient(Retrofit retrofit) {
        return retrofit.create(ApiClient.class);
    }

}