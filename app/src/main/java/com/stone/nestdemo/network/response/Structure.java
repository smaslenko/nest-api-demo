package com.stone.nestdemo.network.response;

import com.google.gson.annotations.SerializedName;
import com.stone.nestdemo.storage.model.Camera;
import com.stone.nestdemo.storage.model.Thermostat;

import java.util.Map;

public class Structure {

    @SerializedName("thermostats")
    private Map<String, Thermostat> thermostats;
    @SerializedName("cameras")
    private Map<String, Camera> cameras;

    public Map<String, Thermostat> getThermostats() {
        return thermostats;
    }

    public Map<String, Camera> getCameras() {
        return cameras;
    }
}
