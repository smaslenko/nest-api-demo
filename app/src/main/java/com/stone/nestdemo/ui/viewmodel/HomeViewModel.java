package com.stone.nestdemo.ui.viewmodel;

import android.arch.lifecycle.LiveData;

import com.stone.nestdemo.storage.model.Device;
import com.stone.nestdemo.storage.model.Structure;

import java.util.List;

import javax.inject.Inject;

public class HomeViewModel extends BaseViewModel {

    private LiveData<List<Structure>> mStructures;

    @Inject HomeViewModel() {
    }

    public void init() {
        initStructures();
        mRepository.checkHomeExistsAndLoad();
    }

    public void loadHome() {
        mRepository.loadHome();
    }

    public LiveData<List<Device>> subscribeDevicesInStructure(String structureId) {
        return mRepository.subscribeDevicesInStructure(structureId);
    }

    public LiveData<List<Structure>> subscribeStructures() {
        return mStructures;
    }

    private void initStructures() {
        if (mStructures != null) {
            return;
        }
        mStructures = mRepository.subscribeStructures();
    }
}
