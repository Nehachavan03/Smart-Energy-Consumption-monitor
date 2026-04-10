package com.energy.monitor.datastructures;

import com.energy.monitor.models.UsageRecord;
import java.util.ArrayList;

/**
 * Data Structure: Pure Max-Heap implementation.
 * Performs insert, delete, and peek operations based on energy consumption.
 */
public class MaxHeap {
    private ArrayList<UsageRecord> heap;

    public MaxHeap() {
        this.heap = new ArrayList<>();
    }

    public void insert(UsageRecord record) {
        heap.add(record);
        siftUp(heap.size() - 1);
    }

    public UsageRecord poll() {
        if (heap.isEmpty())
            return null;
        UsageRecord top = heap.get(0);
        UsageRecord last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            siftDown(0);
        }
        return top;
    }

    public UsageRecord peek() {
        return heap.isEmpty() ? null : heap.get(0);
    }

    public int size() {
        return heap.size();
    }

    /**
     * Removes a specific record from the heap.
     * Note: This is an O(n) operation as it requires searching for the element.
     */
    public boolean remove(UsageRecord record) {
        int index = -1;
        for (int i = 0; i < heap.size(); i++) {
            if (heap.get(i).equals(record)) {
                index = i;
                break;
            }
        }
        if (index == -1)
            return false;

        UsageRecord last = heap.remove(heap.size() - 1);
        if (index < heap.size()) {
            heap.set(index, last);
            siftUp(index);
            siftDown(index);
        }
        return true;
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap.get(index).getEnergyKWh() > heap.get(parent).getEnergyKWh()) {
                swap(index, parent);
                index = parent;
            } else
                break;
        }
    }

    private void siftDown(int index) {
        int left, right, largest;
        while (true) {
            left = 2 * index + 1;
            right = 2 * index + 2;
            largest = index;

            if (left < heap.size() && heap.get(left).getEnergyKWh() > heap.get(largest).getEnergyKWh()) {
                largest = left;
            }
            if (right < heap.size() && heap.get(right).getEnergyKWh() > heap.get(largest).getEnergyKWh()) {
                largest = right;
            }

            if (largest != index) {
                swap(index, largest);
                index = largest;
            } else
                break;
        }
    }

    private void swap(int i, int j) {
        UsageRecord temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}
