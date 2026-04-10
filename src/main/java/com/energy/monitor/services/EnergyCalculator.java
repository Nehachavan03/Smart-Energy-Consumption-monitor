package com.energy.monitor.services;

import com.energy.monitor.models.UsageRecord;

public class EnergyCalculator {

    public double computeEnergyKWh(UsageRecord record) {
        return record.getEnergyKWh();
    }
}
