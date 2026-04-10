package com.energy.monitor.services;

import com.energy.monitor.models.UsageRecord;

public class CarbonCalculator {

    public double computeCarbonKG(UsageRecord record) {
        return record.getEnergyKWh() * 0.82;
    }
}
