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
    void saveAllThermostats(List<Thermostat> thermostats);

    @Insert(onConflict = REPLACE)
    void saveThermostat(Thermostat thermostat);

    @Query("SELECT * FROM Thermostat WHERE structureId = :structureId")
    LiveData<List<Thermostat>> loadThermostatsPerStructure(String structureId);

    @Query("SELECT * FROM Thermostat WHERE deviceId = :deviceId")
    LiveData<Thermostat> loadThermostat(String deviceId);
    //endregion

    //region Cameras
    @Insert(onConflict = REPLACE)
    void saveAllCameras(List<Camera> cameras);

    @Query("SELECT * FROM Camera WHERE structureId = :structureId")
    LiveData<List<Camera>> loadCamerasPerStructure(String structureId);

    @Query("SELECT * FROM Camera")
    LiveData<List<Camera>> loadAllCameras();
    //endregion

    //region Structures
    @Insert(onConflict = REPLACE)
    void saveAllStructures(List<Structure> structures);

    @Query("SELECT * FROM Structure WHERE structureId = :structureId")
    LiveData<Structure> loadStructure(String structureId);

    @Query("SELECT * FROM Structure")
    LiveData<List<Structure>> loadAllStructures();

    @Query("SELECT * FROM Structure")
    List<Structure> checkStructures();
    //endregion

}
