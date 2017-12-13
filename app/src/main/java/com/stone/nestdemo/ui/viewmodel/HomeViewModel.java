package com.stone.nestdemo.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.stone.nestdemo.repository.HomeRepository;
import com.stone.nestdemo.repository.RepositoryOperationStatusManager;
import com.stone.nestdemo.storage.model.Device;
import com.stone.nestdemo.storage.model.Structure;

import java.util.List;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {

    private HomeRepository mRepository;
    private LiveData<List<Structure>> mStructures;

    @Inject // HomeRepository provided by Dagger
    HomeViewModel(HomeRepository repository) {
        mRepository = repository;
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

    public LiveData<RepositoryOperationStatusManager.Status> subscribeRepositoryStatus() {
        return mRepository.subscribeRepositoryOperationStatus();
    }

    private void initStructures() {
        if (mStructures != null) {
            return;
        }
        mStructures = mRepository.subscribeStructures();
    }
}
