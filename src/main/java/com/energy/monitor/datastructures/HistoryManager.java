package com.energy.monitor.datastructures;

import com.energy.monitor.models.UsageRecord;
import java.util.LinkedList;

public class HistoryManager {

    private LinkedList<UsageRecord> history = new LinkedList<>();

    public void addRecord(UsageRecord record) {
        history.add(record);
    }

    public void printHistory() {
        for (UsageRecord r : history) {
            System.out.println(r);
        }
    }

    public UsageRecord getLastRecord() {
        if (history.isEmpty())
            return null;
        return history.getLast();
    }
}