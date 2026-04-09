package com.energy.monitor.services;

import com.energy.monitor.datastructures.MaxHeap;
import com.energy.monitor.models.RoomNode;
import com.energy.monitor.models.UsageRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service: Orchestration layer for analysis and reporting.
 * Coordinates between calculators and data stores to process and rank usage
 * data.
 */
public class AnalysisService {

    /**
     * Uses MaxHeap to find the top K energy consumers.
     */
    public List<UsageRecord> getTopConsumers(Map<String, UsageRecord> allData, int k) {
        MaxHeap heap = new MaxHeap();
        for (UsageRecord record : allData.values()) {
            heap.insert(record);
        }

        List<UsageRecord> topK = new ArrayList<>();
        for (int i = 0; i < k && heap.size() > 0; i++) {
            topK.add(heap.poll());
        }
        return topK;
    }

    /**
     * Recursively calculates total energy for a room and all its sub-rooms.
     * This fulfills the 'Intelligence' requirement for room-wise analysis.
     */
    public double calculateRoomTotal(RoomNode room) {
        double total = 0;
        for (UsageRecord r : room.getAppliances()) {
            total += r.getEnergyKWh();
        }
        for (RoomNode child : room.getChildren()) {
            total += calculateRoomTotal(child);
        }
        return total;
    }
}
