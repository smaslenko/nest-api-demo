package com.stone.nestdemo.repository;

import android.arch.lifecycle.LiveData;

import com.stone.nestdemo.network.ApiClientManager;
import com.stone.nestdemo.network.response.Structure;
import com.stone.nestdemo.storage.StructureDao;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StructureRepository {

    ApiClientManager mApiClientManager;
    StructureDao mDao;

    @Inject
    public StructureRepository(ApiClientManager apiClientManager, StructureDao dao) {
        mApiClientManager = apiClientManager;
        mDao = dao;
    }

    public LiveData<Structure> getThermostats() {
        refreshData();
        // return a LiveData directly from the database.
//        return mDao.load(userId);
    }

    private void refreshData() {

    }

}
