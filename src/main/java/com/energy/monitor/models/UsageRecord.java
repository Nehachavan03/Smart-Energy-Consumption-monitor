package com.energy.monitor.models;

public class UsageRecord {

    private String recordId;
    private Appliance appliance;
    private int quantity; // NEW: number of appliances
    private double hoursUsed;

    public UsageRecord(String recordId, Appliance appliance, int quantity, double hoursUsed) {
        this.recordId = recordId;
        this.appliance = appliance;
        this.quantity = quantity;
        this.hoursUsed = hoursUsed;
    }

    public String getRecordId() {
        return recordId;
    }

    public Appliance getAppliance() {
        return appliance;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getHoursUsed() {
        return hoursUsed;
    }

    // Updated energy calculation
    public double getEnergyKWh() {
        return (appliance.getPowerWatts() * quantity * hoursUsed) / 1000.0;
    }

    @Override
    public String toString() {
        return recordId + " | " +
                appliance.getName() + " x" + quantity + " | " +
                hoursUsed + " hrs | " +
                getEnergyKWh() + " kWh";
    }
}
