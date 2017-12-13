package com.stone.nestdemo.ui.viewpresenter;

import android.support.annotation.NonNull;

import com.stone.nestdemo.storage.model.Device;
import com.stone.nestdemo.storage.model.Structure;

import java.util.List;
import java.util.stream.Collectors;

public class HomePresenterImpl implements ViewPresenterContract.HomePresenter {

    private ViewPresenterContract.HomeView mView;
    private List<Structure> mStructures;
    private List<Device> mDevices;

    public HomePresenterImpl(ViewPresenterContract.HomeView view) {
        mView = view;
    }

    @Override
    public void loadHome() {
        subscribeRepositoryOperationStatus();
        mView.homeViewModel().loadHome();
    }

    @Override
    public void observeStructures() {
        mView.homeViewModel().subscribeStructures().observe(mView.lifecycleOwner(), this::structuresLoaded);
    }

    @Override
    public void onStructureSelected(int position) {
        String structureId = mStructures.get(position).getStructureId();
        mView.homeViewModel().subscribeDevicesInStructure(structureId).observe(mView.lifecycleOwner(), this::devicesLoaded);
    }

    @Override
    public void onDeviceSelected(int position) {
        Device device = mDevices.get(position);

        mView.showDrawer(false);
    }

    private void subscribeRepositoryOperationStatus() {
        mView.homeViewModel().subscribeRepositoryStatus().observe(mView.lifecycleOwner(), status -> {
            if (status != null) {
                switch (status) {
                    case Error:
                        mView.showProgress(false);
                        mView.showError(status.getMessage());
                        break;
                    case Loading:
                        mView.showProgress(true);
                        break;
                    case Success:
                        mView.showProgress(false);
                        mView.showDrawer(false);
                        break;
                }
            }
        });
    }

    private void structuresLoaded(List<Structure> structures) {
        if (structures != null) {
            mStructures = structures;
            List<String> structureNames = structures.stream().map(Structure::getName).collect(Collectors.toList());
            mView.updateSpinnerItems(structureNames);
            mView.showProgress(false);
        }
    }

    private void devicesLoaded(@NonNull List<Device> devices) {
        mDevices = devices;
        mView.updateDrawerItems(devices.stream().map(Device::getName).collect(Collectors.toList()));
    }

}
