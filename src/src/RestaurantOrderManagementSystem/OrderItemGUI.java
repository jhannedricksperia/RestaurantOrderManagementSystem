/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package RestaurantOrderManagementSystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class OrderItemGUI extends Components {

    private RTextField orderID = new RTextField("Required", 30);
    private RTextField menuItemID = new RTextField("Required", 30);
    private RTextField quantity = new RTextField("Required", 30);
    private RTextField totalprice = new RTextField("Required", 30);
    private JLabel errorOrderId, errorMenuItemID, errorQuantity, errorTotalprice;
    private final String[] otcolumnNames = {"Order ID", "Menu Item ID", "Quantity", "Total Price"};
    private final DefaultTableModel orderItemTableModel = new DisabledTableModel(otcolumnNames, 0);
    private JTable orderItemTable = new JTable(orderItemTableModel);
    private final JPanel orderItemPanel;

    public OrderItemGUI() {
        // Main layout panel
        orderItemPanel = new JPanel(new BorderLayout());
        orderItemPanel.setBorder(b);
        orderItemPanel.setOpaque(false);
        // Data table retrieval
        retrieveOrderItemTableData();
        //Sets the font to the text fields
        orderID.setFont(p14);
        menuItemID.setFont(p14);
        quantity.setFont(p14);
        totalprice.setFont(p14);

        orderID.setDisabledTextColor(Color.BLACK);
        menuItemID.setDisabledTextColor(Color.BLACK);
        quantity.setDisabledTextColor(Color.BLACK);
        totalprice.setDisabledTextColor(Color.BLACK);

        errorOrderId = new JLabel(" ");
        errorMenuItemID = new JLabel(" ");
        errorQuantity = new JLabel(" ");
        errorTotalprice = new JLabel(" ");

        // Head panel with Refresh and Filter
        JPanel headPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        headPanel.setOpaque(false);

        JButton filterBtn = new BtnCustom(
                new ImageIcon(getClass().getResource("/images/icon/filterBlack.png")),
                new ImageIcon(getClass().getResource("/images/icon/filterViolet.png")),
                "Filter"
        );
        filterBtn.addActionListener(e -> {
            String[] options = {"Sort", "Like Search", "View All"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "What would you like to do?",
                    "Filter Options",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == 0) {
                sortOrderItem();
            } else if (choice == 1) {
                likeSearchOrderItems();
            } else if (choice == 2) {
                retrieveOrderItemTableData();
            }
        });

        JButton refreshBtn = new BtnCustom(
                new ImageIcon(getClass().getResource("/images/icon/refresh.png")),
                new ImageIcon(getClass().getResource("/images/icon/refreshGreen.png")),
                "Refresh"
        );
        refreshBtn.addActionListener(e -> retrieveOrderItemTableData());

        headPanel.add(refreshBtn);
        headPanel.add(filterBtn);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(b);
        JScrollPane tableScrollPane = new CScrollPane(orderItemTable()); // assuming orderItemTable() returns JTable
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonsPanel.setPreferredSize(new Dimension(0, 100));
        buttonsPanel.setBorder(b);

        JButton createButton = new BtnGreen("Create", new ImageIcon(getClass().getResource("/images/icon/create.png")));
        JButton updateButton = new BtnDefault("Update", new ImageIcon(getClass().getResource("/images/icon/update.png")));
        JButton searchButton = new BtnBlue("Search", new ImageIcon(getClass().getResource("/images/icon/searchWhite.png")));
        JButton deleteButton = new BtnRed("Delete", new ImageIcon(getClass().getResource("/images/icon/delete.png")));

        buttonsPanel.add(createButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(searchButton);
        buttonsPanel.add(deleteButton);

        // Action listeners
        createButton.addActionListener(e -> orderItemCreate());
        updateButton.addActionListener(e -> orderItemUpdate());
        deleteButton.addActionListener(e -> orderItemDelete());
        searchButton.addActionListener(e -> orderItemSearch());

        // Assemble
        orderItemPanel.add(headPanel, BorderLayout.NORTH);
        orderItemPanel.add(tablePanel, BorderLayout.CENTER);
        orderItemPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }

    public JPanel getOrderItemPanel() {

        return orderItemPanel;
    }

    private JScrollPane orderItemTable() {
        orderItemTable = Components.updateTable(orderItemTable);
        orderItemTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        orderItemTable.setFillsViewportHeight(true);
        JScrollPane orderItemPane = new CNavScrollPane(orderItemTable);

        return orderItemPane;
    }

    private void orderItemCreate() {

        JDialog createDialog = new JDialog((Frame) null, "Create Order Item", true);
        createDialog.setSize(470, 490);
        createDialog.setLayout(new BorderLayout());
        createDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        createDialog.setLocationRelativeTo(null);

        JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
        mainPanel.setLayout(new BorderLayout());

        // Title
        JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/green.jpg")).getImage());
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(3, 4, 94));
        titlePanel.setBorder(b);

        JLabel title = new JLabel("Create Order Item");
        title.setFont(b18);
        title.setForeground(Color.white);
        title.setHorizontalAlignment(JLabel.CENTER);

        titlePanel.add(title, BorderLayout.CENTER);

        JPanel miniPanel = new JPanel(new GridBagLayout());
        miniPanel.setOpaque(false);
        miniPanel.setBorder(b);

        JLabel orderIdl = new JLabel("Order ID:");
        JLabel menuIteml = new JLabel("Menu Item ID:");
        JLabel quantityl = new JLabel("Quantity:");
        JLabel totalpricel = new JLabel("Total Price:");

        // Shared preferred size
        Dimension labelSize = new Dimension(120, 35);

// orderIdl
        orderIdl.setFont(p14);
        orderIdl.setPreferredSize(labelSize);

// menuIteml
        menuIteml.setFont(p14);
        menuIteml.setPreferredSize(labelSize);

// quantityl
        quantityl.setFont(p14);
        quantityl.setPreferredSize(labelSize);

// totalpricel
        totalpricel.setFont(p14);
        totalpricel.setPreferredSize(labelSize);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridwidth = 1;
        gbc.weightx = 0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        miniPanel.add(orderIdl, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc);

        gbc.gridy++;
        miniPanel.add(menuIteml, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc);

        gbc.gridy++;
        miniPanel.add(quantityl, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc);

        gbc.gridy++;
        miniPanel.add(totalpricel, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc);

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 1;
        gbc.gridy = 0;
        miniPanel.add(orderID, gbc);
        gbc.gridy++;
        miniPanel.add(errorOrderId = new JLabel(" "), gbc);

        gbc.gridy++;
        miniPanel.add(menuItemID, gbc);
        gbc.gridy++;
        miniPanel.add(errorMenuItemID = new JLabel(" "), gbc);

        gbc.gridy++;
        miniPanel.add(quantity, gbc);
        gbc.gridy++;
        miniPanel.add(errorQuantity = new JLabel(" "), gbc);

        gbc.gridy++;
        miniPanel.add(totalprice, gbc);
        gbc.gridy++;
        miniPanel.add(errorTotalprice = new JLabel(" "), gbc);

        Dimension fieldSize = new Dimension(200, 35);
        orderID.setPreferredSize(fieldSize);
        menuItemID.setPreferredSize(fieldSize);
        quantity.setPreferredSize(fieldSize);
        totalprice.setPreferredSize(fieldSize);

        orderID.setEnabled(true);
        menuItemID.setEnabled(true);
        quantity.setEnabled(true);
        totalprice.setEnabled(true);

        JButton createBtn = new BtnGreen("Create", new ImageIcon(getClass().getResource("/images/icon/create.png")));
        JButton cancelBtn = new BtnRed("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(b);
        btnPanel.add(cancelBtn);
        btnPanel.add(createBtn);

        cancelBtn.addActionListener(e -> {
            orderItemClear();
            createDialog.dispose();
        });
        createBtn.addActionListener(e -> {
            String orderId = orderID.getText();
            String menuItemId = menuItemID.getText();
            String quantityc = quantity.getText();
            String totalPrice = totalprice.getText();

            Boolean checker = false;

            if (orderId.isEmpty() || orderId.equalsIgnoreCase("Required")) {
                errorOrderId.setText("OrderID is Required");
                errorOrderId.setForeground(Color.red);
                errorOrderId.setFont(p11);
                orderID.setForeground(Color.red);
                checker = true;
            }
            if (menuItemId.isEmpty() || menuItemId.equalsIgnoreCase("Required")) {
                errorMenuItemID.setText("MenuItemID is Required");
                errorMenuItemID.setForeground(Color.red);
                errorMenuItemID.setFont(p11);
                menuItemID.setForeground(Color.red);
                checker = true;
            }
            if (quantityc.isEmpty() || quantityc.equalsIgnoreCase("Required")) {
                errorQuantity.setText("Quantity is Required");
                errorQuantity.setForeground(Color.red);
                errorQuantity.setFont(p11);
                quantity.setForeground(Color.red);
                checker = true;
            }

            if (totalPrice.isEmpty() || totalPrice.equalsIgnoreCase("Required")) {
                errorTotalprice.setText("Total Price is Required");
                errorTotalprice.setForeground(Color.red);
                errorTotalprice.setFont(p11);
                totalprice.setForeground(Color.red);
                checker = true;
            }

            if (checker == true) {
                JOptionPane.showMessageDialog(createDialog, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                checker = false;
                return;
            }

            String sql = "INSERT INTO ORDERITEM (OrderID, MenuItemID, Quantity, TotalPrice) VALUES (?, ?, ?, ?)";

            try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, orderId);
                stmt.setString(2, menuItemId);
                stmt.setString(3, quantityc);
                stmt.setString(4, totalPrice);

                stmt.executeUpdate();
                retrieveOrderItemTableData();
                JOptionPane.showMessageDialog(createDialog, "Order added successfully.");
                createDialog.dispose();
                orderItemClear();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(createDialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(miniPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        createDialog.setContentPane(mainPanel);
        createDialog.setVisible(true);
    }

    private void orderItemClear() {
        menuItemID.setDefault();
        orderID.setDefault();
        quantity.setDefault();
        totalprice.setDefault();

        menuItemID.setForeground(gray);
        orderID.setForeground(gray);
        quantity.setForeground(gray);
        totalprice.setForeground(gray);

        errorMenuItemID.setText(" ");
        errorOrderId.setText(" ");
        errorQuantity.setText(" ");
        errorTotalprice.setText(" ");

    }

    private void orderItemUpdate() {
        orderItemClear();

        String inputID = JOptionPane.showInputDialog(this, "Enter Order ID to Update:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Order ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int orderIdd;
        try {
            orderIdd = Integer.parseInt(inputID.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Order ID. Please enter a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String menuItemIDD = JOptionPane.showInputDialog(this, "Enter Menu Item ID to Update:");
        if (menuItemIDD == null) {
            return;
        }

        if (menuItemIDD.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Menu Item ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int menuItemIdd;
        try {
            menuItemIdd = Integer.parseInt(menuItemIDD.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Menu Item ID. Please enter a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM OrderItem WHERE OrderID = ? AND MenuItemID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderIdd);
            stmt.setInt(2, menuItemIdd);
            ResultSet rs = stmt.executeQuery();
            String originalUserID = inputID;
            String originalCustID = inputID;

            if (rs.next()) {

                JDialog updateDialog = new JDialog((Frame) null, "Update Order Item", true);
                updateDialog.setSize(470, 490);
                updateDialog.setLayout(new BorderLayout());
                updateDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                updateDialog.setLocationRelativeTo(null);

                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/orange.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBackground(new Color(3, 4, 94));
                titlePanel.setBorder(b);

                JLabel title = new JLabel("Update Order Item");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);

                titlePanel.add(title, BorderLayout.CENTER);

                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);

                JLabel orderIdl = new JLabel("Order ID:");
                JLabel menuIteml = new JLabel("Menu Item ID:");
                JLabel quantityl = new JLabel("Quantity:");
                JLabel totalpricel = new JLabel("Total Price:");

                orderID.setEnabled(false);
                menuItemID.setEnabled(false);
                quantity.setEnabled(true);
                totalprice.setEnabled(true);

                orderID.setText(rs.getString("OrderID"));
                menuItemID.setText(rs.getString("MenuItemID"));
                quantity.setText(rs.getString("Quantity"));
                totalprice.setText(rs.getString("TotalPrice"));
                orderID.setForeground(Color.DARK_GRAY);
                menuItemID.setForeground(Color.DARK_GRAY);
                quantity.setForeground(Color.DARK_GRAY);
                totalprice.setForeground(Color.DARK_GRAY);

                // Shared Dimension
                Dimension labelSize = new Dimension(120, 35);

// orderIdl
                orderIdl.setFont(p14);
                orderIdl.setPreferredSize(labelSize);

// menuIteml
                menuIteml.setFont(p14);
                menuIteml.setPreferredSize(labelSize);

// quantityl
                quantityl.setFont(p14);
                quantityl.setPreferredSize(labelSize);

// totalpricel
                totalpricel.setFont(p14);
                totalpricel.setPreferredSize(labelSize);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                gbc.gridwidth = 1;
                gbc.weightx = 0;

                gbc.gridx = 0;
                gbc.gridy = 0;
                miniPanel.add(orderIdl, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                gbc.gridy++;
                miniPanel.add(menuIteml, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                gbc.gridy++;
                miniPanel.add(quantityl, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                gbc.gridy++;
                miniPanel.add(totalpricel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.weightx = 1;
                gbc.weighty = 1;

                gbc.gridx = 1;
                gbc.gridy = 0;
                miniPanel.add(orderID, gbc);
                gbc.gridy++;
                miniPanel.add(errorOrderId = new JLabel(" "), gbc);

                gbc.gridy++;
                miniPanel.add(menuItemID, gbc);
                gbc.gridy++;
                miniPanel.add(errorMenuItemID = new JLabel(" "), gbc);

                gbc.gridy++;
                miniPanel.add(quantity, gbc);
                gbc.gridy++;
                miniPanel.add(errorQuantity = new JLabel(" "), gbc);

                gbc.gridy++;
                miniPanel.add(totalprice, gbc);
                gbc.gridy++;
                miniPanel.add(errorTotalprice = new JLabel(" "), gbc);

                Dimension fieldSize = new Dimension(200, 35);
                orderID.setPreferredSize(fieldSize);
                menuItemID.setPreferredSize(fieldSize);
                quantity.setPreferredSize(fieldSize);
                totalprice.setPreferredSize(fieldSize);

                JButton updateBtn = new BtnDefault("Update", new ImageIcon(getClass().getResource("/images/icon/update.png")));
                JButton cancelBtn = new BtnRed("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
                JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
                btnPanel.setOpaque(false);
                btnPanel.setBorder(b);
                btnPanel.add(updateBtn);
                btnPanel.add(cancelBtn);

                cancelBtn.addActionListener(e -> updateDialog.dispose());

                updateBtn.addActionListener(e -> {

                    String orderId = orderID.getText();
                    String menuItemId = menuItemID.getText();
                    String quantityc = quantity.getText();
                    String totalPrice = totalprice.getText();

                    if (orderId.isEmpty()) {
                        JOptionPane.showMessageDialog(updateDialog, "Please input User ID", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (menuItemId.isEmpty()) {
                        JOptionPane.showMessageDialog(updateDialog, "Please input Customer ID", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (quantityc.isEmpty()) {
                        JOptionPane.showMessageDialog(updateDialog, "Please input Sub Total Amount", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (totalPrice.isEmpty()) {
                        JOptionPane.showMessageDialog(updateDialog, "Please input Sub Total Amount", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try (PreparedStatement updateStmt = conn.prepareStatement(
                            "UPDATE ORDERITEM SET Quantity = ?, TotalPrice = ? WHERE OrderID = ? AND MenuItemID = ?")) {
                        updateStmt.setString(1, quantityc);
                        updateStmt.setString(2, totalPrice);
                        updateStmt.setInt(3, orderIdd);
                        updateStmt.setInt(4, menuItemIdd);
                        updateStmt.executeUpdate();
                        JOptionPane.showMessageDialog(updateDialog, "Order ID " + orderId + " updated successfully.");
                        retrieveOrderItemTableData();
                        updateDialog.dispose();

                        updateDialog.dispose();
                        retrieveOrderItemTableData();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(updateDialog, "Update failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }

                });

                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(miniPanel, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);
                updateDialog.setContentPane(mainPanel);
                updateDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Order ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }

    }

    private void orderItemSearch() {
        orderItemClear();

        String inputID = JOptionPane.showInputDialog(this, "Enter Order ID to Update:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Order ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(inputID.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Order ID. Please enter a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String menuItemIDD = JOptionPane.showInputDialog(this, "Enter Menu Item ID to Update:");
        if (menuItemIDD == null) {
            return;
        }

        if (menuItemIDD.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Menu Item ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int menuItemId;
        try {
            menuItemId = Integer.parseInt(menuItemIDD.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Menu Item ID. Please enter a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

// Continue with using orderId and menuItemId...
        String sql = "SELECT * FROM OrderItem WHERE OrderID = ? AND MenuItemID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, menuItemId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                JDialog searchDialog = new JDialog((Frame) null, "Search Order Item", true);
                searchDialog.setSize(470, 490);
                searchDialog.setLayout(new BorderLayout());
                searchDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                searchDialog.setLocationRelativeTo(null);

                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/blue1.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBackground(new Color(3, 4, 94));
                titlePanel.setBorder(b);

                JLabel title = new JLabel("Search Order Item");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);

                titlePanel.add(title, BorderLayout.CENTER);

                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);

                JLabel orderIdl = new JLabel("Order ID:");
                JLabel menuIteml = new JLabel("Menu Item ID:");
                JLabel quantityl = new JLabel("Quantity:");
                JLabel totalpricel = new JLabel("Total Price:");

                orderID.setText(rs.getString("OrderID"));
                menuItemID.setText(rs.getString("MenuItemID"));
                quantity.setText(rs.getString("Quantity"));
                totalprice.setText(rs.getString("TotalPrice"));

                orderID.setEnabled(false);
                menuItemID.setEnabled(false);
                quantity.setEnabled(false);
                totalprice.setEnabled(false);

                // Reusable Dimension object
                Dimension labelSize = new Dimension(120, 35);

// Apply settings manually
                orderIdl.setFont(p14);
                orderIdl.setPreferredSize(labelSize);

                menuIteml.setFont(p14);
                menuIteml.setPreferredSize(labelSize);

                quantityl.setFont(p14);
                quantityl.setPreferredSize(labelSize);

                totalpricel.setFont(p14);
                totalpricel.setPreferredSize(labelSize);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                gbc.gridwidth = 1;
                gbc.weightx = 0;

                gbc.gridx = 0;
                gbc.gridy = 0;
                miniPanel.add(orderIdl, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                gbc.gridy++;
                miniPanel.add(menuIteml, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                gbc.gridy++;
                miniPanel.add(quantityl, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                gbc.gridy++;
                miniPanel.add(totalpricel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.weightx = 1;
                gbc.weighty = 1;

                gbc.gridx = 1;
                gbc.gridy = 0;
                miniPanel.add(orderID, gbc);
                gbc.gridy++;
                miniPanel.add(errorOrderId = new JLabel(" "), gbc);

                gbc.gridy++;
                miniPanel.add(menuItemID, gbc);
                gbc.gridy++;
                miniPanel.add(errorMenuItemID = new JLabel(" "), gbc);

                gbc.gridy++;
                miniPanel.add(quantity, gbc);
                gbc.gridy++;
                miniPanel.add(errorQuantity = new JLabel(" "), gbc);

                gbc.gridy++;
                miniPanel.add(totalprice, gbc);
                gbc.gridy++;
                miniPanel.add(errorTotalprice = new JLabel(" "), gbc);

                Dimension fieldSize = new Dimension(200, 35);
                orderID.setPreferredSize(fieldSize);
                menuItemID.setPreferredSize(fieldSize);
                quantity.setPreferredSize(fieldSize);
                totalprice.setPreferredSize(fieldSize);

                orderID.setForeground(Color.DARK_GRAY);
                menuItemID.setForeground(Color.DARK_GRAY);
                quantity.setForeground(Color.DARK_GRAY);
                totalprice.setForeground(Color.DARK_GRAY);

                orderID.setEnabled(false);
                menuItemID.setEnabled(false);
                quantity.setEnabled(false);
                totalprice.setEnabled(false);

                JButton cancelBtn = new BtnDefault("Return", new ImageIcon(getClass().getResource("/images/icon/return.png")));
                JPanel btnPanel = new JPanel(new GridLayout(1, 1, 10, 10));
                btnPanel.setOpaque(false);
                btnPanel.setBorder(b);
                btnPanel.add(cancelBtn);
                cancelBtn.addActionListener(e -> searchDialog.dispose());
                searchDialog.add(mainPanel, BorderLayout.CENTER);
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(miniPanel, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);
                searchDialog.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Order ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }

        orderItemClear();

    }

    private void orderItemDelete() {
        orderItemClear();

        String inputID = JOptionPane.showInputDialog(this, "Enter Order ID to Update:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Order ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int orderIdd;
        try {
            orderIdd = Integer.parseInt(inputID.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Order ID. Please enter a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String menuItemIDD = JOptionPane.showInputDialog(this, "Enter Menu Item ID to Update:");
        if (menuItemIDD == null) {
            return;
        }

        if (menuItemIDD.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Menu Item ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int menuItemIdd;
        try {
            menuItemIdd = Integer.parseInt(menuItemIDD.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Menu Item ID. Please enter a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM OrderItem WHERE OrderID = ? AND MenuItemID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderIdd);
            stmt.setInt(2, menuItemIdd);
            ResultSet rs = stmt.executeQuery();
            String originalUserID = inputID;
            String originalCustID = inputID;

            if (rs.next()) {

                JDialog deleteDialog = new JDialog((Frame) null, "Delete Order Item", true);
                deleteDialog.setSize(470, 490);
                deleteDialog.setLayout(new BorderLayout());
                deleteDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                deleteDialog.setLocationRelativeTo(null);

                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/red.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBackground(new Color(3, 4, 94));
                titlePanel.setBorder(b);

                JLabel title = new JLabel("Delete Order Item");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);

                titlePanel.add(title, BorderLayout.CENTER);

                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);

                JLabel orderIdl = new JLabel("Order ID:");
                JLabel menuIteml = new JLabel("Menu Item ID:");
                JLabel quantityl = new JLabel("Quantity:");
                JLabel totalpricel = new JLabel("Total Price:");

                orderID.setText(rs.getString("OrderID"));
                menuItemID.setText(rs.getString("MenuItemID"));
                quantity.setText(rs.getString("Quantity"));
                totalprice.setText(rs.getString("TotalPrice"));
                orderID.setForeground(Color.DARK_GRAY);
                menuItemID.setForeground(Color.DARK_GRAY);
                quantity.setForeground(Color.DARK_GRAY);
                totalprice.setForeground(Color.DARK_GRAY);

                orderID.setEnabled(false);
                menuItemID.setEnabled(false);
                quantity.setEnabled(false);
                totalprice.setEnabled(false);

                // Shared preferred size
                Dimension labelSize = new Dimension(120, 35);

                // orderIdl
                orderIdl.setFont(p14);
                orderIdl.setPreferredSize(labelSize);

                // menuIteml
                menuIteml.setFont(p14);
                menuIteml.setPreferredSize(labelSize);

                // quantityl
                quantityl.setFont(p14);
                quantityl.setPreferredSize(labelSize);

                // totalpricel
                totalpricel.setFont(p14);
                totalpricel.setPreferredSize(labelSize);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                gbc.gridwidth = 1;
                gbc.weightx = 0;

                gbc.gridx = 0;
                gbc.gridy = 0;
                miniPanel.add(orderIdl, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                gbc.gridy++;
                miniPanel.add(menuIteml, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                gbc.gridy++;
                miniPanel.add(quantityl, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                gbc.gridy++;
                miniPanel.add(totalpricel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.weightx = 1;
                gbc.weighty = 1;

                gbc.gridx = 1;
                gbc.gridy = 0;
                miniPanel.add(orderID, gbc);
                gbc.gridy++;
                miniPanel.add(errorOrderId = new JLabel(" "), gbc);

                gbc.gridy++;
                miniPanel.add(menuItemID, gbc);
                gbc.gridy++;
                miniPanel.add(errorMenuItemID = new JLabel(" "), gbc);

                gbc.gridy++;
                miniPanel.add(quantity, gbc);
                gbc.gridy++;
                miniPanel.add(errorQuantity = new JLabel(" "), gbc);

                gbc.gridy++;
                miniPanel.add(totalprice, gbc);
                gbc.gridy++;
                miniPanel.add(errorTotalprice = new JLabel(" "), gbc);

                Dimension fieldSize = new Dimension(200, 35);
                orderID.setPreferredSize(fieldSize);
                menuItemID.setPreferredSize(fieldSize);
                quantity.setPreferredSize(fieldSize);
                totalprice.setPreferredSize(fieldSize);

                orderID.setEnabled(false);
                menuItemID.setEnabled(false);
                quantity.setEnabled(false);
                totalprice.setEnabled(false);

                JButton cancelBtn = new BtnDefault("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
                JButton deleteBtn = new BtnRed("Confirm Delete", new ImageIcon(getClass().getResource("/images/icon/delete.png")));
                JPanel btnPanel = new JPanel(new GridLayout(1, 1, 10, 10));
                btnPanel.setOpaque(false);
                btnPanel.setBorder(b);
                btnPanel.add(cancelBtn);
                btnPanel.add(deleteBtn);

                cancelBtn.addActionListener(e -> deleteDialog.dispose());
                deleteBtn.addActionListener(e -> {
                    String sqlDel = "DELETE FROM ORDERITEM WHERE OrderID = ? AND MenuItemID = ?";
                    try (PreparedStatement deleteStmt = conn.prepareStatement(sqlDel)) {
                        deleteStmt.setInt(1, orderIdd);
                        deleteStmt.setInt(2, menuItemIdd);
                        deleteStmt.executeUpdate();
                        JOptionPane.showMessageDialog(deleteDialog, "Order No. " + inputID + " was deleted successfully.");
                        retrieveOrderItemTableData();
                        deleteDialog.dispose();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(deleteDialog, "Delete failed: " + ex.getMessage());
                    }
                });

                deleteDialog.add(mainPanel, BorderLayout.CENTER);
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(miniPanel, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);
                deleteDialog.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Order ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }

        orderItemClear();
    }

    private void retrieveOrderItemTableData() {
        DefaultTableCellRenderer centerRenderer = new CustomRowRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer rightRenderer = new CustomRowRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        // Apply renderers manually
        orderItemTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Order ID
        orderItemTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Menu Item ID
        orderItemTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);  // Quantity
        orderItemTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);  // Total Price
        String sql = "SELECT * FROM ORDERITEM";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            DefaultTableModel model = (DefaultTableModel) orderItemTable.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                    rs.getString("OrderID"),
                    rs.getString("MenuItemID"),
                    rs.getString("Quantity"),
                    rs.getString("TotalPrice")

                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading table data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sortOrderItem() {
        String[] fields = {"OrderID", "MenuItemID", "Quantity", "TotalPrice"};
        String field = (String) JOptionPane.showInputDialog(
                this,
                "Select field to sort by:",
                "Sort Field",
                JOptionPane.PLAIN_MESSAGE,
                null,
                fields,
                fields[0]);

        if (field == null) {
            return;
        }

        String[] orderOptions = {"ASC", "DESC"};
        String order = (String) JOptionPane.showInputDialog(
                this,
                "Sort order?",
                "Sort Direction",
                JOptionPane.PLAIN_MESSAGE,
                null,
                orderOptions,
                orderOptions[0]);

        if (order == null) {
            return;
        }

        String sql = "SELECT * FROM ORDERITEM ORDER BY " + field + " " + order;

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            orderItemTableModel.setRowCount(0); // clear table

            while (rs.next()) {
                orderItemTableModel.addRow(new Object[]{
                    rs.getString("OrderID"),
                    rs.getString("MenuItemID"),
                    rs.getString("Quantity"),
                    rs.getString("TotalPrice")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error sorting: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void likeSearchOrderItems() {
        String[] fields = {"orderID", "MenuItemID", "Quantity", "TotalPrice"};
        String field = (String) JOptionPane.showInputDialog(
                this,
                "Select field to search:",
                "Field Selection",
                JOptionPane.PLAIN_MESSAGE,
                null,
                fields,
                fields[0]);

        if (field == null) {
            return;
        }

        String[] matchTypes = {"Starts With", "Contains", "Ends With"};
        String matchType = (String) JOptionPane.showInputDialog(
                this,
                "Choose match type:",
                "Match Type",
                JOptionPane.PLAIN_MESSAGE,
                null,
                matchTypes,
                matchTypes[1]);

        if (matchType == null) {
            return;
        }

        String keyword = JOptionPane.showInputDialog(this, "Enter keyword:");

        if (keyword != null && !keyword.trim().isEmpty()) {
            keyword = keyword.trim();
            switch (matchType) {
                case "Starts With":
                    keyword = keyword + "%";
                    break;
                case "Ends With":
                    keyword = "%" + keyword;
                    break;
                case "Contains":
                default:
                    keyword = "%" + keyword + "%";
                    break;
            }

            String sql = "SELECT * FROM ORDERITEM WHERE " + field + " LIKE ?";
            try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, keyword);
                try (ResultSet rs = stmt.executeQuery()) {
                    orderItemTableModel.setRowCount(0); // clear table

                    while (rs.next()) {
                        orderItemTableModel.addRow(new Object[]{
                            rs.getString("OrderID"),
                            rs.getString("MenuItemID"),
                            rs.getString("Quantity"),
                            rs.getString("TotalPrice")
                        });
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "No keyword entered.");
        }
    }

}
