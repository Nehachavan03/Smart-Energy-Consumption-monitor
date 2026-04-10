package com.energy.monitor.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;


public class ConsoleUI {

    private final Map<String, String> users = new HashMap<>();

    public ConsoleUI() {
        users.put("admin", "admin");
    }

    public void show() {
        EventQueue.invokeLater(this::createLoginFrame);
    }

    private void createLoginFrame() {
        JFrame frame = new JFrame("Smart Energy Monitor - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 300);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(25, 40, 60));

        JLabel titleLabel = new JLabel("Smart Energy Monitor", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 4, 16));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        frame.add(titlePanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 12, 6, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(userLabel, gbc);

        JTextField userField = new JTextField(18);
        userField.setPreferredSize(new Dimension(220, 28));
        gbc.gridx = 1;
        gbc.gridy = 0;
        centerPanel.add(userField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(18);
        passField.setPreferredSize(new Dimension(220, 28));
        gbc.gridx = 1;
        gbc.gridy = 1;
        centerPanel.add(passField, gbc);

        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Signup");
        loginButton.setBackground(new Color(40, 125, 90));
        loginButton.setForeground(Color.WHITE);
        signupButton.setBackground(new Color(70, 100, 200));
        signupButton.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        centerPanel.add(buttonPanel, gbc);

        frame.add(centerPanel, BorderLayout.CENTER);

        loginButton.addActionListener((ActionEvent e) -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Enter username and password.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (authenticate(username, password)) {
                frame.dispose();
                openDashboard(username);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        signupButton.addActionListener((ActionEvent e) -> createSignupDialog(frame));

        frame.setVisible(true);
    }

    private void createSignupDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Create Account", true);
        dialog.setSize(420, 280);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new GridBagLayout());
        dialog.getContentPane().setBackground(new Color(30, 50, 70));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("New username:");
        nameLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(nameLabel, gbc);

        JTextField nameField = new JTextField(18);
        nameField.setPreferredSize(new Dimension(240, 28));
        gbc.gridx = 1;
        gbc.gridy = 0;
        dialog.add(nameField, gbc);

        JLabel passLabel = new JLabel("New password:");
        passLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(18);
        passField.setPreferredSize(new Dimension(240, 28));
        gbc.gridx = 1;
        gbc.gridy = 1;
        dialog.add(passField, gbc);

        JButton createButton = new JButton("Create Account");
        createButton.setBackground(new Color(40, 125, 90));
        createButton.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        dialog.add(createButton, gbc);

        createButton.addActionListener((ActionEvent e) -> {
            String username = nameField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter both fields.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (registerUser(username, password)) {
                JOptionPane.showMessageDialog(dialog, "Account created successfully. You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Username already exists. Choose another.", "Signup Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private boolean authenticate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    private boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, password);
        return true;
    }

    private void openDashboard(String username) {
        Dashboard dashboard = new Dashboard(username);
        dashboard.showDashboard();
    }
}
