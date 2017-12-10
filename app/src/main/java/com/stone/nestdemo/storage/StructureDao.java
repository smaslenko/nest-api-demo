package com.stone.nestdemo.storage;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.stone.nestdemo.storage.model.Camera;
import com.stone.nestdemo.storage.model.Thermostat;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface StructureDao {

    @Insert(onConflict = REPLACE)
    void saveThermostat(Thermostat thermostat);

    @Query("SELECT * FROM thermostat WHERE id = :deviceId")
    LiveData<Thermostat> loadThermostat(String deviceId);

    @Insert(onConflict = REPLACE)
    void saveCamera(Camera camera);

    @Query("SELECT * FROM camera WHERE id = :deviceId")
    LiveData<Camera> loadCamera(String deviceId);

    @Query("SELECT * FROM thermostat")
    List<Thermostat> getAllThermostats();

    @Query("SELECT * FROM Camera")
    List<Camera> getAllCameras();


}
