package com.stone.nestdemo.storage.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Structure {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("structure_id")
    private String structureId;

    private String name;

    @SerializedName("time_zone")
    private String timeZone;

//    private List<String> thermostats;
//
//    private List<String> cameras;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

//    public List<String> getThermostats() {
//        return thermostats;
//    }
//
//    public List<String> getCameras() {
//        return cameras;
//    }
}
