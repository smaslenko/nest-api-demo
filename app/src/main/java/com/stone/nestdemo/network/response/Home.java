package com.stone.nestdemo.network.response;

import com.stone.nestdemo.storage.model.Camera;
import com.stone.nestdemo.storage.model.Structure;
import com.stone.nestdemo.storage.model.Thermostat;

import java.util.Map;

public class Home {

    private Devices devices;
    private Map<String, Structure> structures;

    public Devices getDevices() {
        return devices;
    }

    public Map<String, Structure> getStructures() {
        return structures;
    }

    public class Devices {
        private Map<String, Thermostat> thermostats;
        private Map<String, Camera> cameras;

        public Map<String, Thermostat> getThermostats() {
            return thermostats;
        }

        public Map<String, Camera> getCameras() {
            return cameras;
        }

    }
}
