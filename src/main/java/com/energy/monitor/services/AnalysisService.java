package com.energy.monitor.services;

import com.energy.monitor.models.*;
import java.util.*;

public class AnalysisService {

    public List<UsageRecord> getTopConsumers(Map<String, UsageRecord> data, int k) {
        List<UsageRecord> list = new ArrayList<>(data.values());

        list.sort((a, b) -> Double.compare(b.getEnergyKWh(), a.getEnergyKWh()));

        return list.subList(0, Math.min(k, list.size()));
    }

    public double calculateRoomTotal(RoomNode room) {
        double total = 0;

        for (UsageRecord r : room.getAppliances()) {
            total += r.getEnergyKWh();
        }

        return total;
    }
}