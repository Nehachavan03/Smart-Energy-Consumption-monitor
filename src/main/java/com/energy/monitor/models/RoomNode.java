package com.energy.monitor.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Model: Represents a node in the hierarchical room structure.
 * Stores room name, associated appliances, and sub-rooms.
 */
public class RoomNode {
    private String name;
    private List<UsageRecord> appliances;
    private List<RoomNode> children;

    public RoomNode(String name) {
        this.name = name;
        this.appliances = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<UsageRecord> getAppliances() {
        return appliances;
    }

    public List<RoomNode> getChildren() {
        return children;
    }

    public void addAppliance(UsageRecord record) {
        this.appliances.add(record);
    }

    public void removeAppliance(UsageRecord record) {
        this.appliances.remove(record);
    }

    public void addChild(RoomNode child) {
        this.children.add(child);
    }

    public void removeChild(RoomNode child) {
        this.children.remove(child);
    }

    @Override
    public String toString() {
        return name + " (Appliances: " + appliances.size() + ", Sub-rooms: " + children.size() + ")";
    }
}
