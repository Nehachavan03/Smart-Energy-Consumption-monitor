package com.energy.monitor;

import com.energy.monitor.models.*;
import com.energy.monitor.datastructures.*;

public class TestMitaliDS {

    public static void main(String[] args) {

        EnergyStore store = new EnergyStore();
        HistoryManager history = new HistoryManager();

        // Create appliances
        Appliance ac = new Appliance("A1", "AC", 1500);
        Appliance tv = new Appliance("A2", "TV", 200);

        // Create usage
        // Create usage (WITH quantity)
        UsageRecord r1 = new UsageRecord("r1", ac, 3, 4); // 3 ACs → 18 kWh
        UsageRecord r2 = new UsageRecord("r2", tv, 2, 5); // 2 TVs → 2 kWh
        // Store in HashMap
        store.addUsageRecord(r1);
        store.addUsageRecord(r2);

        // Store in LinkedList
        history.addRecord(r1);
        history.addRecord(r2);

        // Output
        store.printAll();
        history.printHistory();

        System.out.println("\nLast Record:");
        System.out.println(history.getLastRecord());
    }
}
