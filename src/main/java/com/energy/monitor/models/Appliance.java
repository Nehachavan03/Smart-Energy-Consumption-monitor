package com.energy.monitor.models;

/**
 * Model class for an electrical appliance.
 */
public class Appliance {
    private String id;
    private String name;
    private double powerWatts;

    // Constructor with name and power
    public Appliance(String name, double powerWatts) {
        this.name = name;
        this.powerWatts = powerWatts;
    }

    // Full constructor
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
        return name + " (" + powerWatts + "W)";
    }
}