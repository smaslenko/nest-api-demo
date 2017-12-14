package com.stone.nestdemo.ui.viewmodel;

import android.arch.lifecycle.LiveData;

import com.stone.nestdemo.repository.RepositoryOperationStatusManager;
import com.stone.nestdemo.storage.model.Thermostat;

import javax.inject.Inject;

public class DeviceViewModel extends BaseViewModel {

    @Inject DeviceViewModel() {
    }

    public void loadDevice(String deviceId) {
        mRepository.loadThermostat(deviceId);
    }

    public LiveData<Thermostat> subscribeThermostat(String deviceId) {
        return mRepository.subscribeThermostat(deviceId);
    }

    public LiveData<RepositoryOperationStatusManager.Status> subscribeRepositoryStatus() {
        return mRepository.subscribeRepositoryOperationStatus();
    }
}
