package com.stone.nestdemo.storage;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.stone.nestdemo.storage.model.Camera;
import com.stone.nestdemo.storage.model.Structure;
import com.stone.nestdemo.storage.model.Thermostat;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface HomeDao {

    //region Thermostats
    @Insert(onConflict = REPLACE)
    void saveThermostat(Thermostat thermostat);

    @Insert(onConflict = REPLACE)
    void saveAllThermostats(List<Thermostat> thermostat);

    @Query("SELECT * FROM Thermostat WHERE id = :deviceId")
    LiveData<Thermostat> loadThermostat(String deviceId);

    @Query("SELECT * FROM Thermostat")
    LiveData<List<Thermostat>> loadAllThermostats();
    //endregion

    //region Cameras
    @Insert(onConflict = REPLACE)
    void saveCamera(Camera camera);

    @Insert(onConflict = REPLACE)
    void saveAllCameras(List<Camera> cameras);

    @Query("SELECT * FROM Camera WHERE id = :deviceId")
    LiveData<Camera> loadCamera(String deviceId);

    @Query("SELECT * FROM Camera")
    LiveData<List<Camera>> loadAllCameras();
    //endregion

    //region Structures
    @Insert(onConflict = REPLACE)
    void saveAllStructures(List<Structure> structures);

    @Query("SELECT * FROM Structure WHERE id = :structureId")
    LiveData<Structure> loadStructure(String structureId);

    @Query("SELECT * FROM Structure")
    LiveData<List<Structure>> loadAllStructures();
    //endregion

}
