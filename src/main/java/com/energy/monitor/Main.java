package com.energy.monitor;

import com.energy.monitor.models.*;
import com.energy.monitor.services.*;
import com.energy.monitor.datastructures.*;
import com.energy.monitor.ui.*;

import javax.swing.SwingUtilities;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        // ===== Initialize Backend Services =====
        EnergyCalculator energyCalculator = new EnergyCalculator();
        CarbonCalculator carbonCalculator = new CarbonCalculator();
        AnalysisService analysisService = new AnalysisService();

        // ===== Initialize Data Structures =====
        EnergyStore store = new EnergyStore();
        RoomTree roomTree = new RoomTree("Home");
        HistoryManager historyManager = new HistoryManager();
        MaxHeap usageHeap = new MaxHeap();

        // ===== Create Appliances and Usage Records =====
        Appliance ac = new Appliance("AC", 1500);
        Appliance fan = new Appliance("Fan", 75);
        Appliance light = new Appliance("Light", 20);

        UsageRecord r1 = new UsageRecord("R1", ac, 1, 5.0, "Living Room");
        UsageRecord r2 = new UsageRecord("R2", fan, 2, 10.0, "Bedroom");
        UsageRecord r3 = new UsageRecord("R3", light, 3, 8.0, "Kitchen");

        // ===== Store Records =====
        store.addUsageRecord(r1);
        store.addUsageRecord(r2);
        store.addUsageRecord(r3);

        historyManager.addRecord(r1);
        historyManager.addRecord(r2);
        historyManager.addRecord(r3);

        usageHeap.insert(r1);
        usageHeap.insert(r2);
        usageHeap.insert(r3);

        // ===== Build room structure =====
        roomTree.addRoom("Home", "Living Room");
        roomTree.addRoom("Home", "Bedroom");
        roomTree.addRoom("Home", "Kitchen");

        roomTree.addApplianceToRoom("Living Room", r1);
        roomTree.addApplianceToRoom("Bedroom", r2);
        roomTree.addApplianceToRoom("Kitchen", r3);

        // ===== Display backend results =====
        System.out.println("----- Individual Calculations -----");
        printDetails(r1, energyCalculator, carbonCalculator);
        printDetails(r2, energyCalculator, carbonCalculator);
        printDetails(r3, energyCalculator, carbonCalculator);

        double totalEnergy = energyCalculator.computeEnergyKWh(r1)
                + energyCalculator.computeEnergyKWh(r2)
                + energyCalculator.computeEnergyKWh(r3);
        double totalCarbon = carbonCalculator.computeCarbonKG(r1)
                + carbonCalculator.computeCarbonKG(r2)
                + carbonCalculator.computeCarbonKG(r3);

        System.out.println("\n----- Total Summary -----");
        System.out.println("Total Energy Consumption: " + String.format("%.2f", totalEnergy) + " kWh");
        System.out.println("Carbon Footprint: " + String.format("%.2f", totalCarbon) + " kg CO2");

        System.out.println("\n----- Top Usage Record -----");
        UsageRecord top = usageHeap.peek();
        if (top != null) {
            printDetails(top, energyCalculator, carbonCalculator);
        }

        System.out.println("\n----- Room Energy Totals -----");
        RoomNode living = roomTree.findRoom(roomTree.getRoot(), "Living Room");
        if (living != null) {
            System.out.println(
                    "Living Room total: " + String.format("%.2f", analysisService.calculateRoomTotal(living)) + " kWh");
        }

        RoomNode bedroomNode = roomTree.findRoom(roomTree.getRoot(), "Bedroom");
        if (bedroomNode != null) {
            System.out.println("Bedroom total: "
                    + String.format("%.2f", analysisService.calculateRoomTotal(bedroomNode)) + " kWh");
        }

        RoomNode kitchen = roomTree.findRoom(roomTree.getRoot(), "Kitchen");
        if (kitchen != null) {
            System.out.println(
                    "Kitchen total: " + String.format("%.2f", analysisService.calculateRoomTotal(kitchen)) + " kWh");
        }

        Map<String, UsageRecord> recordMap = new HashMap<String, UsageRecord>();
        recordMap.put(r1.getRecordId(), r1);
        recordMap.put(r2.getRecordId(), r2);
        recordMap.put(r3.getRecordId(), r3);

        System.out.println("\n----- Top Consumers -----");
        List<UsageRecord> topConsumers = analysisService.getTopConsumers(recordMap, 3);
        for (UsageRecord record : topConsumers) {
            printDetails(record, energyCalculator, carbonCalculator);
        }

        // ===== Launch frontend UI =====
        SwingUtilities.invokeLater(() -> new ConsoleUI().show());
    }

    // Helper method (clean + reusable)
    public static void printDetails(UsageRecord record, EnergyCalculator ec, CarbonCalculator cc) {
        System.out.println(
                record.getAppliance().getName() + " x" + record.getQuantity()
                        + " -> Energy: " + String.format("%.2f", ec.computeEnergyKWh(record)) + " kWh"
                        + ", Carbon: " + String.format("%.2f", cc.computeCarbonKG(record)) + " kg CO2");
    }
}