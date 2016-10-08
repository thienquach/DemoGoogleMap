package com.thienquach.demogooglemap.model;

/**
 * Created by thien.quach on 10/7/2016.
 */
public class Doctor {

    private String name;
    private String address;
    private String distance;

    public Doctor(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
