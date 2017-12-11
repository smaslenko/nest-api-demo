package com.stone.nestdemo.utils;

import com.stone.nestdemo.network.response.Home;
import com.stone.nestdemo.storage.HomeDao;
import com.stone.nestdemo.storage.model.Camera;
import com.stone.nestdemo.storage.model.Structure;
import com.stone.nestdemo.storage.model.Thermostat;

import java.util.ArrayList;
import java.util.List;

public class DaoUtil {

    public static void saveHome(HomeDao dao, Home home) {
        List<Structure> structures = new ArrayList<>(home.getStructures().values());
        List<Thermostat> thermostats = new ArrayList<>(home.getDevices().getThermostats().values());
        List<Camera> cameras = new ArrayList<>(home.getDevices().getCameras().values());

        dao.saveAllStructures(structures);
        dao.saveAllThermostats(thermostats);
        dao.saveAllCameras(cameras);
    }
}
