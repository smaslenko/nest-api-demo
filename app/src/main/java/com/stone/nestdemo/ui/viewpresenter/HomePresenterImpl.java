package com.stone.nestdemo.ui.viewpresenter;

import com.stone.nestdemo.storage.model.Device;
import com.stone.nestdemo.storage.model.Structure;

import java.util.List;
import java.util.stream.Collectors;

public class HomePresenterImpl implements ViewPresenterContract.HomePresenter{

    private ViewPresenterContract.HoveView mView;
    private List<Device> mDevices;

    public HomePresenterImpl(ViewPresenterContract.HoveView view) {
        mView = view;
    }

    @Override
    public void loadHome() {

    }

    @Override
    public void loadThermostats(String structureId) {

    }

    @Override
    public void loadCameras(String structureId) {

    }

    @Override public void onStructuresLoaded(List<Structure> structures) {
        if (structures != null) {
            List<String> structureNames = structures.stream().map(Structure::getName).collect(Collectors.toList());
            mView.updateSpinnerItems(structureNames);
            mView.showProgress(false);
        }
    }

    @Override public void onStructureSelected(int position) {

    }

}
