package com.stone.nestdemo.network.request;

import com.google.gson.annotations.SerializedName;

public class TargetTemperatureRequest {

    public TargetTemperatureRequest(double targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    @SerializedName("target_temperature_c")
    private double targetTemperature;

    public double getTargetTemperature() {
        return targetTemperature;
    }
}
