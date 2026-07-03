package org.yourcompany.yourproject;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class HostelManagementApp extends JFrame {

    private JPanel mainContainer;
    private CardLayout cardLayout;
    private JTabbedPane mainTabs;
    private JPanel dashboardContentPanel;

    private ArrayList<Object[]> studentData;
    private DefaultTableModel studentTableModel;
    private Map<String, Room> roomData;
    private DefaultTableModel roomTableModel;
    private final String[] BRANCHES = {"CSE", "ECE", "MECH", "CIVIL", "EEE", "IT"};

    private final Color ACCENT_COLOR = new Color(0, 119, 182);
    private final Color PRIMARY_COLOR = new Color(245, 245, 245);
    private final Color CARD_BACKGROUND = new Color(255, 255, 255, 230); 
    private final Color HEADER_COLOR = new Color(30, 40, 50);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color ERROR_COLOR = new Color(231, 76, 60);
    private final Color STATS_COLOR = new Color(243, 156, 18);

    public HostelManagementApp() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println("Nimbus L&F failed to load.");
        }

        setTitle("Freshers Block - Hostel Management System");
        setSize(1100, 700);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                showExitConfirmation();
            }
        });

        setLocationRelativeTo(null);

        initializeData();

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        mainContainer.add(createLoginPanel(), "Login");
        mainContainer.add(createDashboardPanel(), "Dashboard");

        add(mainContainer);
        cardLayout.show(mainContainer, "Login");
    }

    private void showExitConfirmation() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit Freshers Block?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void initializeData() {
        roomData = new HashMap<>();
        studentData = new ArrayList<>();
        Random rand = new Random();

        for (int i = 1; i <= 200; i++) {
            String block = (i <= 100) ? "A" : "B";
            String roomNum = block + (i % 100 + 100);
            int capacity = (rand.nextBoolean()) ? 2 : 3;
            roomData.put(roomNum, new Room(roomNum, capacity));
        }

        roomData.get("A101").addOccupant("Alice Smith", "CSE");
        studentData.add(new Object[]{"101", "Alice Smith", "A101", "Paid", "9876543210", "CSE"});
    }

    private JPanel createLoginPanel() {
        BackgroundPanel panel = new BackgroundPanel("https://images.unsplash.com/photo-1517816743773-6e0fd518b4a6?q=80&w=2000&auto=format&fit=crop");
        panel.setLayout(new GridBagLayout());

        JPanel loginCard = new JPanel(new GridBagLayout());
        loginCard.setBackground(new Color(255, 255, 255, 220));
        loginCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ACCENT_COLOR, 2, true),
                new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Freshers Block Login");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginCard.add(titleLabel, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        loginCard.add(new JLabel("Username:"), gbc);
        JTextField userField = new JTextField(15);
        gbc.gridx = 1; loginCard.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        loginCard.add(new JLabel("Password:"), gbc);
        JPasswordField passField = new JPasswordField(15);
        gbc.gridx = 1; loginCard.add(passField, gbc);

        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBackground(ACCENT_COLOR);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        loginCard.add(loginBtn, gbc);

        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            if (user.equals("msrithostel") && pass.equals("hostel123")) {
                refreshDashboardContent();
                cardLayout.show(mainContainer, "Dashboard");
            } else {
                JOptionPane.showMessageDialog(panel, "Invalid Credentials!", "Denied", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(loginCard);
        return panel;
    }

    private JPanel createDashboardPanel() {
        BackgroundPanel dashboard = new BackgroundPanel("https://images.unsplash.com/photo-1557683316-973673baf926?q=80&w=2000&auto=format&fit=crop");
        dashboard.setLayout(new BorderLayout(10, 10));
        dashboard.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 40, 50, 200));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel headerTitle = new JLabel("Freshers Block Management Dashboard");
        headerTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        headerTitle.setForeground(Color.WHITE);
        header.add(headerTitle, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> cardLayout.show(mainContainer, "Login"));
        header.add(logoutBtn, BorderLayout.EAST);

        dashboard.add(header, BorderLayout.NORTH);

        dashboardContentPanel = new JPanel(new BorderLayout(0, 15));
        dashboardContentPanel.setOpaque(false);

        mainTabs = new JTabbedPane();
        mainTabs.addTab("Student Records", createStudentPanel());
        mainTabs.addTab("Room Availability", createRoomPanel());
        mainTabs.addTab("Add New Member", createAddMemberPanel());

        dashboardContentPanel.add(createStatsPanel(), BorderLayout.NORTH);
        dashboardContentPanel.add(mainTabs, BorderLayout.CENTER);

        dashboard.add(dashboardContentPanel, BorderLayout.CENTER);
        return dashboard;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setOpaque(false);

        int totalRooms = roomData.size();
        int totalCap = roomData.values().stream().mapToInt(Room::getCapacity).sum();
        int occupied = studentData.size();

        statsPanel.add(createStatCard("Total Rooms", String.valueOf(totalRooms), ACCENT_COLOR));
        statsPanel.add(createStatCard("Occupancy", occupied + " / " + totalCap, STATS_COLOR));
        statsPanel.add(createStatCard("Available", String.valueOf(totalCap - occupied), SUCCESS_COLOR));

        return statsPanel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(255, 255, 255, 210));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(color, 1), new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font("SansSerif", Font.BOLD, 32));
        valLbl.setForeground(color);
        card.add(valLbl, BorderLayout.CENTER);

        JLabel titLbl = new JLabel(title);
        titLbl.setForeground(HEADER_COLOR);
        card.add(titLbl, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        String[] cols = {"ID", "Name", "Room", "Status", "Phone", "Branch"};
        studentTableModel = new DefaultTableModel(cols, 0);
        for (Object[] row : studentData) studentTableModel.addRow(row);

        JTable table = new JTable(studentTableModel);
        table.getTableHeader().setBackground(ACCENT_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRoomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        String[] cols = {"Room No.", "Capacity", "Occupancy", "Available", "Full?"};
        roomTableModel = new DefaultTableModel(cols, 0);
        updateRoomTableModel();

        JTable table = new JTable(roomTableModel);
        table.getTableHeader().setBackground(ACCENT_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAddMemberPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CARD_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(20);
        JComboBox<String> branchBox = new JComboBox<>(BRANCHES);
        JButton addBtn = new JButton("Add Student");
        addBtn.setBackground(ACCENT_COLOR);
        addBtn.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Branch:"), gbc);
        gbc.gridx = 1; panel.add(branchBox, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; panel.add(addBtn, gbc);

        addBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Student added successfully!");
        });

        return panel;
    }

    private void updateRoomTableModel() {
        roomTableModel.setRowCount(0);
        for (Room room : roomData.values()) {
            roomTableModel.addRow(new Object[]{
                    room.getRoomNumber(), room.getCapacity(), room.getCurrentOccupancy(),
                    room.getAvailableCapacity(), room.isFull() ? "Yes" : "No"
            });
        }
    }

    private void refreshDashboardContent() {
        updateRoomTableModel();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HostelManagementApp().setVisible(true));
    }

    static class BackgroundPanel extends JPanel {
        private Image bgImage;
        public BackgroundPanel(String url) {
            try { bgImage = javax.imageio.ImageIO.read(new java.net.URL(url)); }
            catch (Exception e) { e.printStackTrace(); }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bgImage != null) g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            g.setColor(new Color(0, 0, 0, 60));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    static class Room {
        private String roomNumber;
        private int capacity;
        private Map<String, String> occupants = new HashMap<>();
        public Room(String num, int cap) { this.roomNumber = num; this.capacity = cap; }
        public String getRoomNumber() { return roomNumber; }
        public int getCapacity() { return capacity; }
        public int getCurrentOccupancy() { return occupants.size(); }
        public int getAvailableCapacity() { return capacity - occupants.size(); }
        public boolean isFull() { return occupants.size() >= capacity; }
        public void addOccupant(String name, String branch) { if (!isFull()) occupants.put(name, branch); }
    }
}