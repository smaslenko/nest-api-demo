package com.stone.nestdemo.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.stone.nestdemo.dependency.scope.RepositoryScope;
import com.stone.nestdemo.network.ApiClientManager;
import com.stone.nestdemo.network.response.Home;
import com.stone.nestdemo.storage.HomeDao;
import com.stone.nestdemo.storage.model.Device;
import com.stone.nestdemo.storage.model.Structure;
import com.stone.nestdemo.utils.DaoUtil;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RepositoryScope
public class HomeRepository {

    private final ApiClientManager mApiClientManager;
    private final HomeDao mHomeDao;
    private final Executor mExecutor;

    @Inject
    RepositoryOperationStatusManager mRepositoryStatusManager;

    @Inject HomeRepository(ApiClientManager apiClientManager, HomeDao dao, Executor executor) {
        mApiClientManager = apiClientManager;
        mHomeDao = dao;
        mExecutor = executor;
    }

    public LiveData<List<Structure>> subscribeStructures() {
        // return a LiveData directly from the database.
        return mHomeDao.loadAllStructures();
    }

    public LiveData<List<Device>> subscribeDevicesInStructure(String structureId) {
        // return a LiveData directly from the database.
        return DaoUtil.loadDevicesInStructure(mHomeDao, structureId);
    }

    public LiveData<RepositoryOperationStatusManager.Status> subscribeRepositoryOperationStatus() {
        return mRepositoryStatusManager.getStatus();
    }

    public void loadHome() {
        mRepositoryStatusManager.setLoading("Home...");

        Call<Home> call = mApiClientManager.getHome();
        call.enqueue(new Callback<Home>() {
            @Override
            public void onResponse(@NonNull Call<Home> call, @NonNull Response<Home> response) {
                if (response.isSuccessful()) {
                    saveHome(response.body());
                    mRepositoryStatusManager.setSuccess("" + response.raw());

                } else {
                    mRepositoryStatusManager.setError("" + response.raw());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Home> call, @NonNull Throwable t) {
                mRepositoryStatusManager.setError("" + t.getMessage());
            }
        });
    }

    public void checkHomeExistsAndLoad() {
        mExecutor.execute(() -> {
            List<Structure> structures = mHomeDao.checkStructures();
            if (structures == null || structures.isEmpty()) {
                loadHome();
            }
        });
    }

    private void saveHome(Home home) {
        mExecutor.execute(() -> {
            DaoUtil.saveHome(mHomeDao, home);
        });
    }

}
