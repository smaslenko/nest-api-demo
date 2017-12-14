package com.stone.nestdemo.network.response;

import com.google.gson.annotations.SerializedName;

public class Weather {

    /*"main": {
        "temp": 280.32,
            "pressure": 1012,
            "humidity": 81,
            "temp_min": 279.15,
            "temp_max": 281.15
    },
    */

    @SerializedName("main")
    private Main main;

    public Main getMain() {
        return main;
    }

    public class Main {
        Double temp;
        Double humidity;

        public Double getTemp() {
            return temp;
        }

        public Double getHumidity() {
            return humidity;
        }
    }

}
