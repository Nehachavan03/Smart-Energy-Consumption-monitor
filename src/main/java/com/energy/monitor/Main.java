package com.energy.monitor;

import com.energy.monitor.models.*;
import com.energy.monitor.services.*;
import com.energy.monitor.datastructures.*;

public class Main {
    public static void main(String[] args) {

        EnergyCalculator ec = new EnergyCalculator();
        CarbonCalculator cc = new CarbonCalculator();
        EnergyStore store = new EnergyStore();

        // Appliance 1 → 1 AC for 5 hours
        Appliance ac = new Appliance("AC", 1500);
        UsageRecord r1 = new UsageRecord("R1", ac, 1, 5.0);

        // Appliance 2 → 2 Fans for 10 hours
        Appliance fan = new Appliance("Fan", 75);
        UsageRecord r2 = new UsageRecord("R2", fan, 2, 10.0);

        // Appliance 3 → 3 Lights for 8 hours
        Appliance light = new Appliance("Light", 20);
        UsageRecord r3 = new UsageRecord("R3", light, 3, 8.0);

        // Store all records
        store.addUsageRecord(r1);
        store.addUsageRecord(r2);
        store.addUsageRecord(r3);

        // Display results
        System.out.println("----- Individual Calculations -----");

        printDetails(r1, ec, cc);
        printDetails(r2, ec, cc);
        printDetails(r3, ec, cc);

        System.out.println("\n----- Stored Data (HashMap) -----");
        store.printAll();
    }

    // Helper method (clean + reusable)
    public static void printDetails(UsageRecord record, EnergyCalculator ec, CarbonCalculator cc) {
        System.out.println(
                record.getAppliance().getName() + " x" + record.getQuantity()
                        + " → Energy: " + ec.calculateEnergy(record) + " kWh"
                        + ", Carbon: " + String.format("%.2f", cc.calculateCarbon(record)) + " kg CO2"
        );
    }
}
