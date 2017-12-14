package com.stone.nestdemo.ui.viewpresenter;

import com.stone.nestdemo.storage.model.Thermostat;
import com.stone.nestdemo.ui.viewmodel.DeviceViewModel;

public class DevicePresenterImpl extends BasePresenter<ViewPresenterContract.DeviceView, DeviceViewModel> implements ViewPresenterContract.DevicePresenter {

    private String mDeviceId;

    public DevicePresenterImpl(ViewPresenterContract.DeviceView view, String deviceId) {
        super(view);
        mDeviceId = deviceId;
    }

    @Override
    public void loadThermostat() {
        viewModel().loadDevice(mDeviceId);
    }

    @Override
    public void observeDevice() {
        viewModel().subscribeThermostat(mDeviceId).observe(mView.lifecycleOwner(), this::updateAll);
    }

    private void updateAll(Thermostat thermostat) {
        if (thermostat == null) return;

//        loadWeather();

        mView.updateName(thermostat.getName());
        mView.updateTemperature(thermostat.getTargetTemp());
        mView.updateHumidity(thermostat.getHumidity());
    }

    @Override
    public void temperaturePlus() {

    }

    @Override
    public void temperatureMinus() {

    }
}
