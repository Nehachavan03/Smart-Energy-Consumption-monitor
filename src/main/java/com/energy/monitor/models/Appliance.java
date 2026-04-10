package com.energy.monitor.models;

public class Appliance {

    private String id;
    private String name;
    private double powerWatts;

    // Constructor (your version)
    public Appliance(String name, double powerWatts) {
        this.name = name;
        this.powerWatts = powerWatts;
    }

    // Constructor (for test files)
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
}