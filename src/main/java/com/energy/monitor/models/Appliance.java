package com.energy.monitor.models;

public class Appliance {

    private String id;
    private String name;
    private double powerWatts;

    public Appliance(String id, String name, double powerWatts) {
        this.id = id;
        this.name = name;
        this.powerWatts = powerWatts;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPowerWatts() {
        return powerWatts;
    }

    @Override
    public String toString() {
        return id + " | " + name + " | " + powerWatts + "W";
    }
}