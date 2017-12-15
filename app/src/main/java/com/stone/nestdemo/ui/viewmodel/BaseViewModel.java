package com.stone.nestdemo.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.stone.nestdemo.network.response.Weather;
import com.stone.nestdemo.repository.HomeRepository;
import com.stone.nestdemo.repository.RepositoryOperationStatusManager;

import javax.inject.Inject;

public class BaseViewModel extends ViewModel {

    @Inject
    HomeRepository mRepository;

    public LiveData<RepositoryOperationStatusManager.Status> subscribeRepositoryStatus() {
        return mRepository.subscribeRepositoryOperationStatus();
    }

    public LiveData<Weather> subscribeWeather(String city, boolean forceRefresh) {
        return mRepository.subscribeWeather(city, forceRefresh);
    }

}
