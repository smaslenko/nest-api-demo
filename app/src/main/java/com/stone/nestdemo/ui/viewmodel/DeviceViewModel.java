package com.stone.nestdemo.ui.viewmodel;

import android.arch.lifecycle.LiveData;

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

    public LiveData<String> subscribeTimeZone(String structureId) {
        return mRepository.subscribeTimeZone(structureId);
    }

    public void setTemperature(String deviceId, double temperature) {
        mRepository.putTemperature(deviceId, temperature, "");
    }
}
