package com.stone.nestdemo.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.stone.nestdemo.repository.HomeRepository;
import com.stone.nestdemo.storage.model.Camera;
import com.stone.nestdemo.storage.model.Structure;
import com.stone.nestdemo.storage.model.Thermostat;

import java.util.List;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {

    private HomeRepository mRepository;
    private LiveData<List<Structure>> mStructures;
    private LiveData<List<Thermostat>> mThermostats;
    private LiveData<List<Camera>> mCameras;

    @Inject // HomeRepository provided by Dagger
    HomeViewModel(HomeRepository repository) {
        mRepository = repository;
    }

    public void initAll() {
        initStructures();
        initThermostats();
        initCameras();

        if (isEmpty()) {
            refreshHome();
        }
    }

    public void refreshHome() {
        mRepository.refreshHome();
    }

    private void initStructures() {
        if (mStructures != null) {
            return;
        }
        mStructures = mRepository.getStructures();
    }

    private void initThermostats() {
        if (mThermostats != null) {
            return;
        }
        mThermostats = mRepository.getThermostats();
    }

    private void initCameras() {
        if (mCameras != null) {
            return;
        }
        mCameras = mRepository.getCameras();
    }

    private boolean isEmpty() {
        return mStructures.getValue() == null || mStructures.getValue().isEmpty();
    }

    public LiveData<List<Structure>> getStructures() {
        return mStructures;
    }

    public LiveData<List<Thermostat>> getThermostats() {
        return mThermostats;
    }

    public LiveData<List<Camera>> getCameras() {
        return mCameras;
    }
}
