package com.energy.monitor.datastructures;

import com.energy.monitor.models.*;
import java.util.*;

public class EnergyStore {

    private Map<String, UsageRecord> applianceEnergy = new HashMap<>();

    public void addUsageRecord(UsageRecord record) {
        applianceEnergy.put(record.getAppliance().getName(), record);
    }

    public void printAll() {
        for (String key : applianceEnergy.keySet()) {
            System.out.println(key + " -> " +
                    applianceEnergy.get(key).getEnergyKWh() + " kWh");
        }
    }
}