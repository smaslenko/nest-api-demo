package com.stone.nestdemo.storage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.stone.nestdemo.storage.model.Camera;
import com.stone.nestdemo.storage.model.Structure;
import com.stone.nestdemo.storage.model.Thermostat;

@Database(entities = {Thermostat.class, Camera.class, Structure.class}, version = 3, exportSchema = false)
public abstract class HomeDatabase extends RoomDatabase {

    public abstract HomeDao getHomeDao();
}
