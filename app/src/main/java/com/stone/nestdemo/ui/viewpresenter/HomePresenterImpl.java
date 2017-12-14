package com.stone.nestdemo.ui.viewpresenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.stone.nestdemo.storage.model.Device;
import com.stone.nestdemo.storage.model.Structure;
import com.stone.nestdemo.ui.viewmodel.HomeViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class HomePresenterImpl extends BasePresenter<ViewPresenterContract.HomeView, HomeViewModel> implements ViewPresenterContract.HomePresenter {

    private List<Structure> mStructures;
    private List<Device> mDevices;

    private int mSelectedStructurePosition;
    private int mSelectedDevicePosition;
    private boolean mStructuresPositionPending;
    private boolean mDevicesPositionPending;

    public HomePresenterImpl(ViewPresenterContract.HomeView view) {
        super(view);
    }

    @Override
    public void loadHome() {
        subscribeRepositoryOperationStatus();
        viewModel().loadHome();
    }

    @Override
    public void observeStructures() {
        viewModel().subscribeStructures().observe(mView.lifecycleOwner(), this::structuresLoaded);
    }

    @Override
    public void onStructureSelected(int position) {
        Log.d(TAG, "onStructureSelected: " + position);

        boolean ifChanged = mSelectedStructurePosition != position;
        mSelectedStructurePosition = position;

        // Reset device selection if structure has changed
        if (ifChanged) mSelectedDevicePosition = 0;

        if (mStructures != null && position < mStructures.size()) {
            Structure structure = mStructures.get(position);
            String structureId = structure.getStructureId();
            viewModel().subscribeDevicesInStructure(structureId).observe(mView.lifecycleOwner(), this::devicesLoaded);
            loadWeather(structure.getTimeZone());
        } else {
            mStructuresPositionPending = true;
        }
    }

    @Override
    public void onDeviceSelected(int position) {
        Log.d(TAG, "onDeviceSelected: " + position);
        mSelectedDevicePosition = position;
        if (mDevices != null) {
            mView.showDrawer(false);
            Device device = mDevices.get(position);
            mView.showDeviceFragment(device.getDeviceId());
        } else {
            mDevicesPositionPending = true;
        }
    }

    @Override
    public int getSelectedStructurePosition() {
        return mSelectedStructurePosition;
    }

    @Override
    public int getSelectedDevicePosition() {
        return mSelectedDevicePosition;
    }

    private void subscribeRepositoryOperationStatus() {
        viewModel().subscribeRepositoryStatus().observe(mView.lifecycleOwner(), status -> {
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
                        break;
                }
            }
        });
    }

    private void structuresLoaded(List<Structure> structures) {
        Log.d(TAG, "structuresLoaded: " + (structures == null ? "NULL" : structures.size()));
        if (structures != null) {
            mStructures = structures;
            List<String> structureNames = structures.stream().map(Structure::getName).collect(Collectors.toList());
            mView.updateSpinnerItems(structureNames);
            mView.showProgress(false);

            if (mStructuresPositionPending && mSelectedStructurePosition < mStructures.size()) {
                mView.selectStructure(mSelectedStructurePosition);
                mStructuresPositionPending = false;
            }
        }
    }

    private void devicesLoaded(@NonNull List<Device> devices) {
        Log.d(TAG, "devicesLoaded: " + devices.size());
        mDevices = devices;
        mView.updateDrawerItems(devices.stream().map(Device::getName).collect(Collectors.toList()));

        if (mDevicesPositionPending) {
            mView.selectDevice(mSelectedDevicePosition);
            mDevicesPositionPending = false;
        }
    }
}
