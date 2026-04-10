package com.energy.monitor;

import com.energy.monitor.models.*;
import com.energy.monitor.datastructures.*;
import com.energy.monitor.services.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class TestNehaModule {
    public static void main(String[] args) {
        System.out.println("=== Starting Neha's Module Verification ===\n");

        // 1. Test Room Hierarchy
        RoomTree tree = new RoomTree("MainHouse");
        tree.addRoom("MainHouse", "FirstFloor");
        tree.addRoom("FirstFloor", "Kitchen");

        Appliance fridge = new Appliance("F1", "Refrigerator", 300);
        UsageRecord record1 = new UsageRecord("R1", fridge, 1, 24); // 7.2 kWh
        tree.addApplianceToRoom("Kitchen", record1);

        RoomNode kitchen = tree.findRoom(tree.getRoot(), "Kitchen");
        if (kitchen != null && kitchen.getAppliances().size() == 1) {
            System.out.println("[SUCCESS] Room hierarchy and appliance linking verified.");
        } else {
            System.out.println("[FAILURE] Room hierarchy or appliance linking failed.");
        }

        // 2. Test MaxHeap
        MaxHeap heap = new MaxHeap();
        Appliance tv = new Appliance("T1", "TV", 100);
        UsageRecord record2 = new UsageRecord("R2", tv, 1, 5); // 0.5 kWh

        heap.insert(record2); // 0.5
        heap.insert(record1); // 7.2

        if (heap.peek().getEnergyKWh() == 7.2) {
            System.out.println("[SUCCESS] Max-Heap ranking verified (Top consumer correctly identified).");
        } else {
            System.out.println("[FAILURE] Max-Heap ranking failed.");
        }

        // 3. Test AnalysisService
        AnalysisService service = new AnalysisService();
        Map<String, UsageRecord> mockData = new HashMap<>();
        mockData.put("F1", record1);
        mockData.put("T1", record2);

        List<UsageRecord> top = service.getTopConsumers(mockData, 1);
        if (top.size() == 1 && top.get(0).getAppliance().getName().equals("Refrigerator")) {
            System.out.println("[SUCCESS] Service ranking (Top-K) verified.");
        } else {
            System.out.println("[FAILURE] Service ranking (Top-K) failed.");
        }

        double kitchenTotal = service.calculateRoomTotal(kitchen);
        if (kitchenTotal == 7.2) {
            System.out.println("[SUCCESS] Room-wise energy aggregation verified.");
        } else {
            System.out.println("[FAILURE] Room-wise energy aggregation failed.");
        }

        // 4. Test Deletion
        System.out.println("\nTesting Deletions...");

        // Delete appliance from room
        tree.removeApplianceFromRoom("Kitchen", record1);
        if (kitchen.getAppliances().isEmpty()) {
            System.out.println("[SUCCESS] Appliance deletion from room verified.");
        }

        // Delete record from heap
        heap.remove(record1);
        if (heap.size() == 1 && heap.peek().equals(record2)) {
            System.out.println("[SUCCESS] Specific record deletion from Heap verified.");
        }

        // Delete room from tree
        tree.deleteRoom("Kitchen");
        if (tree.findRoom(tree.getRoot(), "Kitchen") == null) {
            System.out.println("[SUCCESS] Room deletion from hierarchy verified.");
        }

        System.out.println("\n=== Verification Complete ===");
    }
}
