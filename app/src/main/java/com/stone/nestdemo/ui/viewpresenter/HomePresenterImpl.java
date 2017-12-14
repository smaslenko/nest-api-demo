package com.stone.nestdemo.ui.viewpresenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.stone.nestdemo.storage.model.Device;
import com.stone.nestdemo.storage.model.Structure;

import java.util.List;
import java.util.stream.Collectors;

public class HomePresenterImpl implements ViewPresenterContract.HomePresenter {
    private static final String TAG = "HomePresenterImpl";

    private ViewPresenterContract.HomeView mView;
    private List<Structure> mStructures;
    private List<Device> mDevices;

    private int mSelectedStructurePosition;
    private int mSelectedDevicePosition;
    private boolean mStructuresPositionPending;
    private boolean mDevicesPositionPending;

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
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        String methodNamePrev = Thread.currentThread().getStackTrace()[4].getMethodName();
        Log.d(TAG, methodNamePrev + "->" + methodName + "->onStructureSelected: " + position);

        boolean ifChanged = mSelectedStructurePosition != position;
        mSelectedStructurePosition = position;

        // Reset device selection if structure has changed
        if (ifChanged) mSelectedDevicePosition = 0;

        if (mStructures != null) {
            String structureId = mStructures.get(position).getStructureId();
            mView.homeViewModel().subscribeDevicesInStructure(structureId).observe(mView.lifecycleOwner(), this::devicesLoaded);
        } else {
            mStructuresPositionPending = true;
        }
    }

    @Override
    public void onDeviceSelected(int position) {
        Log.d(TAG, "onDeviceSelected: " + position);
        mSelectedDevicePosition = position;
        if (mDevices != null) {
            Device device = mDevices.get(position);
            mView.showDrawer(false);
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
        Log.d(TAG, "structuresLoaded: " + (structures == null ? "NULL" : structures.size()));
        if (structures != null) {
            mStructures = structures;
            List<String> structureNames = structures.stream().map(Structure::getName).collect(Collectors.toList());
            mView.updateSpinnerItems(structureNames);
            mView.showProgress(false);

            if (mStructuresPositionPending) {
                mView.selectStructure(mSelectedStructurePosition);
                mStructuresPositionPending = false;
            }
        }
    }

    private void devicesLoaded(@NonNull List<Device> devices) {
        Log.d(TAG, "devicesLoaded: " + devices.size());
        mDevices = devices;
        mView.updateDrawerItems(devices.stream().map(device -> {
            String prefix = device.getClass().getSimpleName() + " ";
            return prefix + device.getName();
        }).collect(Collectors.toList()));

        if (mDevicesPositionPending) {
            mView.selectDevice(mSelectedDevicePosition);
            mDevicesPositionPending = false;
        }
    }

}
