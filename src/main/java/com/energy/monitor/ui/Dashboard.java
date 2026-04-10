package com.energy.monitor.ui;

import com.energy.monitor.datastructures.EnergyStore;
import com.energy.monitor.models.*;
import com.energy.monitor.services.CarbonCalculator;
import com.energy.monitor.services.EnergyCalculator;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.energy.monitor.datastructures.*;
import com.energy.monitor.services.AnalysisService;

public class Dashboard extends JFrame {

    private final EnergyCalculator energyCalculator = new EnergyCalculator();
    private final CarbonCalculator carbonCalculator = new CarbonCalculator();
    private final EnergyStore energyStore = new EnergyStore();
    private final MaxHeap usageHeap = new MaxHeap();
    private final RoomTree roomTree = new RoomTree("Home");
    private final AnalysisService analysisService = new AnalysisService();

    private final List<RecordEntry> records = new ArrayList<>();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[] { "Room", "Device", "Watts", "Qty", "Hours", "Energy (kWh)", "Carbon (kg)" }, 0);
    private final JLabel totalEnergyLabel = new JLabel();
    private final JLabel totalCarbonLabel = new JLabel();
    private final JLabel highestUsageLabel = new JLabel();
    private final JTextArea roomTotalsArea = new JTextArea(5, 20);
    private final JComboBox<String> roomBox = new JComboBox<>(
            new String[] { "Living Room", "Bedroom", "Kitchen", "Bathroom", "Office", "Dining Room" });
    private final Map<String, Double> deviceWatts = new HashMap<>();

    public Dashboard(String username) {
        super("Smart Energy Monitor Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeDeviceWattMap();
        buildUI(username);
        updateSummary();
    }

    public void showDashboard() {
        EventQueue.invokeLater(() -> {
            pack();
            setSize(1000, 640);
            setLocationRelativeTo(null);
            setVisible(true);
        });
    }

    private void initializeDeviceWattMap() {
        deviceWatts.put("AC", 1500.0);
        deviceWatts.put("Fan", 75.0);
        deviceWatts.put("Light", 20.0);
        deviceWatts.put("Refrigerator", 200.0);
        deviceWatts.put("TV", 120.0);
        deviceWatts.put("Custom", 0.0);
    }

    private void buildUI(String username) {
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(new Color(20, 35, 55));

        setTitle("Welcome, " + username + " - Energy Dashboard");
        JLabel header = new JLabel("Welcome, " + username + " - Energy Dashboard");
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(190, 30, 45));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            dispose();
            new ConsoleUI().show();
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 8, 16));
        topPanel.add(header, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(14, 14));
        mainPanel.setOpaque(false);

        JTable usageTable = new JTable(tableModel);
        usageTable.setRowHeight(28);
        usageTable.setGridColor(new Color(80, 110, 150));
        usageTable.setShowGrid(true);
        usageTable.setBackground(new Color(245, 250, 255));
        usageTable.setForeground(Color.DARK_GRAY);
        usageTable.getTableHeader().setBackground(new Color(45, 70, 95));
        usageTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(usageTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(80, 110, 150)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel summaryPanel = new JPanel(new GridLayout(3, 1, 12, 12));
        summaryPanel.setBackground(new Color(30, 41, 59)); // Premium Slate Blue
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(71, 85, 105)), "Summary", 0,
                        0, null, Color.WHITE),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)));

        totalEnergyLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        totalEnergyLabel.setForeground(Color.WHITE);
        totalCarbonLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        totalCarbonLabel.setForeground(Color.WHITE);
        highestUsageLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        highestUsageLabel.setForeground(new Color(200, 220, 240));
        summaryPanel.add(totalEnergyLabel);
        summaryPanel.add(totalCarbonLabel);
        summaryPanel.add(highestUsageLabel);

        roomTotalsArea.setBackground(new Color(15, 23, 42)); // Deep Navy
        roomTotalsArea.setForeground(new Color(56, 189, 248)); // Vibrant Blue
        roomTotalsArea.setEditable(false);
        roomTotalsArea.setFont(new Font("Consolas", Font.BOLD, 13));
        JScrollPane roomScroll = new JScrollPane(roomTotalsArea);
        roomScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 65, 85)),
                "Room Totals", 0, 0, null, Color.WHITE));
        roomScroll.setOpaque(false);
        roomScroll.getViewport().setOpaque(false);

        rightPanel.add(summaryPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(roomScroll);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(30, 41, 59)); // Matching Slate Blue
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(71, 85, 105)),
                "Add Room / Device Usage", 0, 0, null, Color.WHITE));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel roomLabel = new JLabel("Room:");
        roomLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(roomLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(roomBox, gbc);

        JLabel deviceLabel = new JLabel("Device:");
        deviceLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(deviceLabel, gbc);
        JComboBox<String> deviceBox = new JComboBox<>(
                new String[] { "AC", "Fan", "Light", "Refrigerator", "TV", "Custom" });
        deviceBox.setBackground(Color.WHITE);
        gbc.gridx = 1;
        inputPanel.add(deviceBox, gbc);

        JLabel wattsLabel = new JLabel("Watts:");
        wattsLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(wattsLabel, gbc);
        JTextField wattsField = new JTextField();
        gbc.gridx = 1;
        inputPanel.add(wattsField, gbc);

        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(qtyLabel, gbc);
        JTextField qtyField = new JTextField();
        gbc.gridx = 1;
        inputPanel.add(qtyField, gbc);

        JLabel hoursLabel = new JLabel("Hours:");
        hoursLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(hoursLabel, gbc);
        JTextField hoursField = new JTextField();
        gbc.gridx = 1;
        inputPanel.add(hoursField, gbc);

        JButton addButton = new JButton("Add Usage");
        addButton.setBackground(new Color(40, 125, 90));
        addButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        inputPanel.add(addButton, gbc);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        rightPanel.add(inputPanel);
        rightPanel.add(Box.createVerticalGlue());

        mainPanel.add(rightPanel, BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);

        deviceBox.addActionListener(e -> {
            String selected = (String) deviceBox.getSelectedItem();
            if (selected != null && !selected.equals("Custom")) {
                wattsField.setText(String.format("%.0f", deviceWatts.get(selected)));
                wattsField.setEditable(false);
            } else {
                wattsField.setText("");
                wattsField.setEditable(true);
            }
        });

        addButton.addActionListener(e -> addUsageRecord(roomBox, deviceBox, wattsField, qtyField, hoursField));
    }

    private void addUsageRecord(JComboBox<String> roomBox, JComboBox<String> deviceBox, JTextField wattsField,
            JTextField qtyField, JTextField hoursField) {
        String room = (String) roomBox.getSelectedItem();
        String device = ((String) deviceBox.getSelectedItem());
        String wattsText = wattsField.getText().trim();
        String qtyText = qtyField.getText().trim();
        String hoursText = hoursField.getText().trim();

        if (room == null || room.isEmpty() || device == null || wattsText.isEmpty() || qtyText.isEmpty()
                || hoursText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double watts = Double.parseDouble(wattsText);
            int quantity = Integer.parseInt(qtyText);
            double hours = Double.parseDouble(hoursText);
            String recordId = "R" + (records.size() + 1);
            Appliance appliance = new Appliance(device, watts);
            UsageRecord record = new UsageRecord(recordId, appliance, quantity, hours, room);
            records.add(new RecordEntry(record));
            energyStore.addUsageRecord(record);
            usageHeap.insert(record); // Alg Integration: Add to Heap

            // Alg Integration: Add to RoomTree
            if (roomTree.findRoom(roomTree.getRoot(), room) == null) {
                roomTree.addRoom("Home", room);
            }
            roomTree.addApplianceToRoom(room, record);

            tableModel.addRow(new Object[] { room, device, watts, quantity, hours,
                    String.format("%.2f", energyCalculator.computeEnergyKWh(record)),
                    String.format("%.2f", carbonCalculator.computeCarbonKG(record)) });
            updateSummary();
            deviceBox.setSelectedIndex(0);
            wattsField.setText("");
            qtyField.setText("");
            hoursField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Watts, quantity, and hours must be numeric.", "Validation",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSummary() {
        double totalEnergy = records.stream().mapToDouble(entry -> energyCalculator.computeEnergyKWh(entry.record))
                .sum();
        double totalCarbon = records.stream().mapToDouble(entry -> carbonCalculator.computeCarbonKG(entry.record))
                .sum();
        totalEnergyLabel.setText("Total energy: " + String.format("%.2f", totalEnergy) + " kWh");
        totalCarbonLabel.setText("Total carbon: " + String.format("%.2f", totalCarbon) + " kg CO2");

        // Alg Integration: Use MaxHeap for top usage
        UsageRecord top = usageHeap.peek();
        if (top != null) {
            highestUsageLabel.setText("Highest usage: " + top.getAppliance().getName() + " ("
                    + String.format("%.2f", top.getEnergyKWh()) + " kWh)");
        } else {
            highestUsageLabel.setText("Highest usage: none");
        }

        // Alg Integration: Room-wise summary from RoomTree
        StringBuilder roomSummary = new StringBuilder();
        for (RoomNode child : roomTree.getRoot().getChildren()) {
            double roomTotal = analysisService.calculateRoomTotal(child);
            roomSummary.append(String.format("%-15s: %.2f kWh\n", child.getName(), roomTotal));
        }
        roomTotalsArea.setText(roomSummary.toString());
    }

    private static class RecordEntry {
        private final UsageRecord record;

        public RecordEntry(UsageRecord record) {
            this.record = record;
        }
    }
}
