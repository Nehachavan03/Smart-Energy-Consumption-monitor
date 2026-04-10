package com.energy.monitor.models;

/**
 * Model class for an energy usage record.
 */
public class UsageRecord {
    private String recordId;
    private Appliance appliance;
    private int quantity;
    private double hoursUsed;
    private String room;

    public UsageRecord(String recordId, Appliance appliance, int quantity, double hoursUsed) {
        this.recordId = recordId;
        this.appliance = appliance;
        this.quantity = quantity;
        this.hoursUsed = hoursUsed;
        this.room = "Unknown";
    }

    public UsageRecord(String recordId, Appliance appliance, int quantity, double hoursUsed, String room) {
        this.recordId = recordId;
        this.appliance = appliance;
        this.quantity = quantity;
        this.hoursUsed = hoursUsed;
        this.room = room;
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

    public String getRoom() {
        return room;
    }

    public double getEnergyKWh() {
        if (appliance == null)
            return 0;
        return (appliance.getPowerWatts() * quantity * hoursUsed) / 1000.0;
    }

    @Override
    public String toString() {
        return String.format("%s | %s x%d | %.1f hrs | %.2f kWh | Room: %s",
                recordId, (appliance != null ? appliance.getName() : "Unknown"), quantity, hoursUsed, getEnergyKWh(),
                room);
    }
}
