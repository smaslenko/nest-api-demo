package com.stone.nestdemo.storage.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Thermostat {
    @PrimaryKey
    private int id;

    private String name;

    private String humidity;

    @SerializedName("target_temperature_c")
    private String targetTemp;

    @SerializedName("ambient_temperature_c")
    private String ambientTemp;

    @SerializedName("is_online")
    private String isOnline;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTargetTemp() {
        return targetTemp;
    }

    public void setTargetTemp(String targetTemp) {
        this.targetTemp = targetTemp;
    }

    public String getAmbientTemp() {
        return ambientTemp;
    }

    public void setAmbientTemp(String ambientTemp) {
        this.ambientTemp = ambientTemp;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }
}
