package com.energy.monitor.ui;

import com.energy.monitor.datastructures.EnergyStore;
import com.energy.monitor.models.Appliance;
import com.energy.monitor.models.UsageRecord;
import com.energy.monitor.services.CarbonCalculator;
import com.energy.monitor.services.EnergyCalculator;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class Dashboard extends JFrame {

    private final EnergyCalculator energyCalculator = new EnergyCalculator();
    private final CarbonCalculator carbonCalculator = new CarbonCalculator();
    private final EnergyStore energyStore = new EnergyStore();

    private final List<RecordEntry> records = new ArrayList<>();
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Room", "Device", "Watts", "Qty", "Hours", "Energy (kWh)", "Carbon (kg)"}, 0);
    private final JLabel totalEnergyLabel = new JLabel();
    private final JLabel totalCarbonLabel = new JLabel();
    private final JLabel highestUsageLabel = new JLabel();
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

        JLabel header = new JLabel("Welcome, " + username + " — Energy Dashboard");
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

        JPanel summaryPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        summaryPanel.setBackground(new Color(26, 42, 66));
        summaryPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(65, 95, 130)), "Summary"));
        totalEnergyLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalEnergyLabel.setForeground(Color.WHITE);
        totalCarbonLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalCarbonLabel.setForeground(Color.WHITE);
        highestUsageLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        highestUsageLabel.setForeground(new Color(200, 220, 240));
        summaryPanel.add(totalEnergyLabel);
        summaryPanel.add(totalCarbonLabel);
        summaryPanel.add(highestUsageLabel);
        rightPanel.add(summaryPanel);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(65, 95, 130)), "Add Room / Device Usage"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel roomLabel = new JLabel("Room:");
        roomLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(roomLabel, gbc);
        JTextField roomField = new JTextField();
        gbc.gridx = 1;
        inputPanel.add(roomField, gbc);

        JLabel deviceLabel = new JLabel("Device:");
        deviceLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(deviceLabel, gbc);
        JComboBox<String> deviceBox = new JComboBox<>(new String[]{"AC", "Fan", "Light", "Refrigerator", "TV", "Custom"});
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

        addButton.addActionListener(e -> addUsageRecord(roomField, deviceBox, wattsField, qtyField, hoursField));
    }

    private void addUsageRecord(JTextField roomField, JComboBox<String> deviceBox, JTextField wattsField, JTextField qtyField, JTextField hoursField) {
        String room = roomField.getText().trim();
        String device = ((String) deviceBox.getSelectedItem());
        String wattsText = wattsField.getText().trim();
        String qtyText = qtyField.getText().trim();
        String hoursText = hoursField.getText().trim();

        if (room.isEmpty() || device == null || wattsText.isEmpty() || qtyText.isEmpty() || hoursText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double watts = Double.parseDouble(wattsText);
            int quantity = Integer.parseInt(qtyText);
            double hours = Double.parseDouble(hoursText);
                String recordId = "R" + (records.size() + 1);
            Appliance appliance = new Appliance(device, watts);
            UsageRecord record = new UsageRecord(recordId, appliance, quantity, hours, room);
            records.add(new RecordEntry(room, record));
            energyStore.addUsageRecord(record);
            tableModel.addRow(new Object[]{room, device, watts, quantity, hours, String.format("%.2f", energyCalculator.calculateEnergy(record)), String.format("%.2f", carbonCalculator.calculateCarbon(record))});
            updateSummary();
            roomField.setText("");
            deviceBox.setSelectedIndex(0);
            wattsField.setText("");
            qtyField.setText("");
            hoursField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Watts, quantity, and hours must be numeric.", "Validation", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSummary() {
        double totalEnergy = records.stream().mapToDouble(entry -> energyCalculator.calculateEnergy(entry.record)).sum();
        double totalCarbon = records.stream().mapToDouble(entry -> carbonCalculator.calculateCarbon(entry.record)).sum();
        totalEnergyLabel.setText("Total energy: " + String.format("%.2f", totalEnergy) + " kWh");
        totalCarbonLabel.setText("Total carbon: " + String.format("%.2f", totalCarbon) + " kg CO2");

        java.util.Optional<RecordEntry> highest = records.stream()
                .max((a, b) -> Double.compare(a.record.getEnergyKWh(), b.record.getEnergyKWh()));
        if (highest.isPresent()) {
            RecordEntry entry = highest.get();
            highestUsageLabel.setText("Highest usage: " + entry.record.getAppliance().getName() + " in " + entry.room + " (" + String.format("%.2f", entry.record.getEnergyKWh()) + " kWh)");
        } else {
            highestUsageLabel.setText("Highest usage: none");
        }
    }

    private static class RecordEntry {
        private final String room;
        private final UsageRecord record;

        public RecordEntry(String room, UsageRecord record) {
            this.room = room;
            this.record = record;
        }
    }
}
