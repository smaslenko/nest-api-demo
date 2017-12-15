package com.stone.nestdemo.ui.viewpresenter;

import android.support.annotation.NonNull;

import com.stone.nestdemo.storage.model.Thermostat;
import com.stone.nestdemo.ui.viewmodel.DeviceViewModel;

public class DevicePresenterImpl extends BasePresenter<ViewPresenterContract.DeviceView, DeviceViewModel> implements ViewPresenterContract.DevicePresenter {

    private final static double TEMPERATURE_STEP = .5;
    private String mDeviceId;
    private Thermostat mThermostat;

    public DevicePresenterImpl(ViewPresenterContract.DeviceView view, String deviceId) {
        super(view);
        mDeviceId = deviceId;
    }

    @Override
    public void observeDevice() {
        subscribeRepositoryOperationStatus();
        viewModel().subscribeThermostat(mDeviceId).observe(mView.lifecycleOwner(), this::update);
    }

    private void update(@NonNull Thermostat thermostat) {
        mThermostat = thermostat;

        viewModel().subscribeTimeZone(thermostat.getStructureId()).observe(mView.lifecycleOwner(), s -> subscribeWeather(s, false));

        mView.updateName(thermostat.getName());
        mView.updateTemperature(thermostat.getTargetTemp());
        mView.updateHumidity(thermostat.getHumidity() + "%");

        mView.showProgress(false);
    }

    @Override
    public void temperaturePlus() {
        increaseTemperature(TEMPERATURE_STEP);
    }

    @Override
    public void temperatureMinus() {
        increaseTemperature(-TEMPERATURE_STEP);
    }

    private void increaseTemperature(double inc) {
        double tempCurr = Double.parseDouble(mThermostat.getTargetTemp());
        double tempNew = tempCurr + inc;
        if (ensureTemperatureRange(tempNew)) {
            mView.showProgress(true);
            viewModel().setTemperature(mThermostat.getDeviceId(), tempNew);
        } else {
            mView.showTemperatureExceedMessage();
        }
    }

    /**
     * Temperature fields only accept specific increments in a fixed range.
     * Fahrenheit must be whole integers between 50 and 90.
     * Celsius must be in increments of 0.5 between 9 and 32.
     *
     * @param temperature value to check
     * @return true if range not exceeded
     */
    private boolean ensureTemperatureRange(double temperature) {
        return temperature >= 9 && temperature <= 32;
    }
}
