package com.stone.nestdemo.storage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.stone.nestdemo.storage.model.Camera;
import com.stone.nestdemo.storage.model.Thermostat;

@Database(entities = {Thermostat.class, Camera.class}, version = 1, exportSchema = false)
public abstract class StructureDatabase extends RoomDatabase {

    public abstract StructureDao structureDao();
}
