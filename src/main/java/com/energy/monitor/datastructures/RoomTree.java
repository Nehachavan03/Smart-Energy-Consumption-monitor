package com.energy.monitor.datastructures;

import com.energy.monitor.models.RoomNode;
import com.energy.monitor.models.UsageRecord;


public class RoomTree {
    private RoomNode root;

    public RoomTree(String rootName) {
        this.root = new RoomNode(rootName);
    }

    public RoomNode getRoot() {
        return root;
    }

    
    public void addRoom(String parentName, String newRoomName) {
        RoomNode parent = findRoom(root, parentName);
        if (parent != null) {
            parent.addChild(new RoomNode(newRoomName));
        } else {
            System.out.println("Parent room " + parentName + " not found!");
        }
    }

    
    public void addApplianceToRoom(String roomName, UsageRecord record) {
        RoomNode room = findRoom(root, roomName);
        if (room != null) {
            room.addAppliance(record);
        } else {
            System.out.println("Room " + roomName + " not found!");
        }
    }

    
    public void removeApplianceFromRoom(String roomName, UsageRecord record) {
        RoomNode room = findRoom(root, roomName);
        if (room != null) {
            room.removeAppliance(record);
        } else {
            System.out.println("Room " + roomName + " not found!");
        }
    }

    
    public void deleteRoom(String name) {
        if (root.getName().equalsIgnoreCase(name)) {
            System.out.println("Cannot delete root room!");
            return;
        }
        deleteRoomRecursive(root, name);
    }

    private boolean deleteRoomRecursive(RoomNode current, String targetName) {
        for (RoomNode child : current.getChildren()) {
            if (child.getName().equalsIgnoreCase(targetName)) {
                current.removeChild(child);
                return true;
            }
            if (deleteRoomRecursive(child, targetName))
                return true;
        }
        return false;
    }

    
    public RoomNode findRoom(RoomNode current, String name) {
        if (current.getName().equalsIgnoreCase(name))
            return current;
        for (RoomNode child : current.getChildren()) {
            RoomNode found = findRoom(child, name);
            if (found != null)
                return found;
        }
        return null;
    }
}
