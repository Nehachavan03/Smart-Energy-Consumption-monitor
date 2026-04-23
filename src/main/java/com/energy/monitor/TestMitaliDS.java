package com.energy.monitor;

import com.energy.monitor.models.*;
import com.energy.monitor.datastructures.*;

public class TestMitaliDS {

    public static void main(String[] args) {

        EnergyStore store = new EnergyStore();
        HistoryManager history = new HistoryManager();

        
        Appliance ac = new Appliance("A1", "AC", 1500);
        Appliance tv = new Appliance("A2", "TV", 200);

        
        
        UsageRecord r1 = new UsageRecord("r1", ac, 3, 4); 
        UsageRecord r2 = new UsageRecord("r2", tv, 2, 5); 
        
        store.addUsageRecord(r1);
        store.addUsageRecord(r2);

        
        history.addRecord(r1);
        history.addRecord(r2);

        
        store.printAll();
        history.printHistory();

        System.out.println("\nLast Record:");
        System.out.println(history.getLastRecord());
    }
}
