package com.fazevaib.shushit;

/**
 * Created by Vaibhav on 27-07-2017.
 * Project: ShushIt
 */

public class WorkLocation {
    String name;
    double lat;
    double lng;

    public WorkLocation(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
