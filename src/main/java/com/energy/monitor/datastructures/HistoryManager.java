package com.energy.monitor.datastructures;

import com.energy.monitor.models.UsageRecord;
import java.util.LinkedList;

public class HistoryManager {

    // LinkedList → maintains order
    private LinkedList<UsageRecord> usageHistory = new LinkedList<>();

    public void addRecord(UsageRecord record) {
        usageHistory.add(record);
    }

    public void printHistory() {
        System.out.println("---- Usage History (LinkedList) ----");
        for (UsageRecord r : usageHistory) {
            System.out.println("[" +
                    r.getAppliance().getName() + ", " +
                    r.getEnergyKWh() + "]");
        }
    }

    public UsageRecord getLastRecord() {
        return usageHistory.isEmpty() ? null : usageHistory.getLast();
    }
}