package com.stone.nestdemo.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.stone.nestdemo.dependency.scope.RepositoryScope;
import com.stone.nestdemo.network.ApiClientManager;
import com.stone.nestdemo.network.response.Home;
import com.stone.nestdemo.storage.HomeDao;
import com.stone.nestdemo.storage.model.Camera;
import com.stone.nestdemo.storage.model.Structure;
import com.stone.nestdemo.storage.model.Thermostat;
import com.stone.nestdemo.utils.DaoUtil;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RepositoryScope
public class HomeRepository {

    private static final String TAG = "HomeRepository";
    private final ApiClientManager mApiClientManager;
    private final HomeDao mHomeDao;
    private final Executor mExecutor;

    @Inject HomeRepository(ApiClientManager apiClientManager, HomeDao dao, Executor executor) {
        mApiClientManager = apiClientManager;
        mHomeDao = dao;
        mExecutor = executor;
    }

    public LiveData<List<Structure>> getStructures() {
        // return a LiveData directly from the database.
        return mHomeDao.loadAllStructures();
    }

    public LiveData<List<Thermostat>> getThermostats() {
        // return a LiveData directly from the database.
        return mHomeDao.loadAllThermostats();
    }

    public LiveData<List<Camera>> getCameras() {
        // return a LiveData directly from the database.
        return mHomeDao.loadAllCameras();
    }

    public void refreshHome() {
        Call<Home> call = mApiClientManager.getHome();

        call.enqueue(new Callback<Home>() {
            @Override
            public void onResponse(@NonNull Call<Home> call, @NonNull Response<Home> response) {
                if (response.isSuccessful()) {
                    Home home = response.body();

                    saveHomeToDb(home);

                } else {
                    Log.d(TAG, "" + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Home> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private void saveHomeToDb(Home home) {
        mExecutor.execute(() -> {
            DaoUtil.saveHome(mHomeDao, home);
        });
    }

}
