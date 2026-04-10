package com.energy.monitor;

import com.energy.monitor.models.*;
import com.energy.monitor.datastructures.*;

/**
 * Integrated Test Module: Verifies all 7 core files work together.
 * Testing: Appliance, UsageRecord, RoomNode, EnergyStore, HistoryManager,
 * MaxHeap, and RoomTree.
 */
public class TestIntegratedData {
    public static void main(String[] args) {
        System.out.println("=== Starting Integrated Data Test ===\n");

        // 1. Initialize all Data Structures
        EnergyStore store = new EnergyStore();
        HistoryManager history = new HistoryManager();
        MaxHeap ranking = new MaxHeap();
        RoomTree houseHierarchy = new RoomTree("MyHome");

        // 2. Create Appliances (Models)
        Appliance ac = new Appliance("A1", "AC", 2000);
        Appliance fan = new Appliance("A2", "Fan", 75);
        Appliance led = new Appliance("A3", "LED Bulb", 15);

        // 3. Create Usage Records (Models + Logic)
        UsageRecord rec1 = new UsageRecord("R1", ac, 1, 5); // 10.0 kWh
        UsageRecord rec2 = new UsageRecord("R2", fan, 2, 8); // 1.2 kWh (2 fans * 75W * 8h)
        UsageRecord rec3 = new UsageRecord("R3", led, 5, 10); // 0.75 kWh (5 bulbs * 15W * 10h)

        // 4. Populate Data Structures (Integration)

        // Fast Lookup
        store.addUsageRecord(rec1);
        store.addUsageRecord(rec2);
        store.addUsageRecord(rec3);

        // History Log
        history.addRecord(rec1);
        history.addRecord(rec2);
        history.addRecord(rec3);

        // Energy Ranking
        ranking.insert(rec1);
        ranking.insert(rec2);
        ranking.insert(rec3);

        // House Hierarchy
        houseHierarchy.addRoom("MyHome", "LivingRoom");
        houseHierarchy.addRoom("MyHome", "Bedroom");
        houseHierarchy.addApplianceToRoom("LivingRoom", rec1); // AC in Living Room
        houseHierarchy.addApplianceToRoom("LivingRoom", rec3); // LED in Living Room
        houseHierarchy.addApplianceToRoom("Bedroom", rec2); // Fans in Bedroom

        // 5. Verification Output

        System.out.println("--- 1. Quick Lookup (EnergyStore) ---");
        System.out.println("AC Energy: " + store.getUsage("AC").getEnergyKWh() + " kWh");

        System.out.println("\n--- 2. Recent Activity (HistoryManager) ---");
        history.printHistory();

        System.out.println("\n--- 3. Top Energy Consumer (MaxHeap) ---");
        System.out.println(
                "Top: " + ranking.peek().getAppliance().getName() + " (" + ranking.peek().getEnergyKWh() + " kWh)");

        System.out.println("\n--- 4. House Hierarchy (RoomTree) ---");
        displayHierarchy(houseHierarchy.getRoot(), "");

        System.out.println("\n=== Integrated Test Complete ===");
    }

    private static void displayHierarchy(RoomNode node, String indent) {
        System.out.println(indent + "+ " + node.getName());
        for (UsageRecord r : node.getAppliances()) {
            System.out.println(indent + "  - " + r.getAppliance().getName() + " (x" + r.getQuantity() + ")");
        }
        for (RoomNode child : node.getChildren()) {
            displayHierarchy(child, indent + "  ");
        }
    }
}
