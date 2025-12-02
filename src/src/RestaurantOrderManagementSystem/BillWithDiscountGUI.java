package RestaurantOrderManagementSystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BillWithDiscountGUI extends Components {

    private RTextField billIDField, discountIDField, lVatField, dAmountField;
    private JLabel errorDiscount, errortVat, errordAmount, errorBill;
    private final DefaultTableModel tableModel = new DisabledTableModel(new String[]{"BillID", "DiscountID", "LessVAT", "DiscountAmount"}, 0);
    private final JTable billDiscountTable = Components.updateTable(new JTable(tableModel));
    private int nextID;
    private JLabel billIDLabel = new JLabel("Bill ID:");
    private JLabel discountIDLabel = new JLabel("Discount ID:");
    private JLabel lVatLabel = new JLabel("Less Vat:");
    private JLabel dAmountLabel = new JLabel("Discount Amount:");
    private JPanel billDiscountPanel;

    public BillWithDiscountGUI() {
        billIDLabel.setFont(p14);
        discountIDLabel.setFont(p14);
        lVatLabel.setFont(p14);
        dAmountLabel.setFont(p14);

        DefaultTableCellRenderer centerRenderer = new CustomRowRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer rightRenderer = new CustomRowRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        billDiscountTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // BillID
        billDiscountTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // DiscountID
        billDiscountTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);  // LessVAT
        billDiscountTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);  // DiscountAmount

        billDiscountPanel = new JPanel(new BorderLayout());//Rename based on your assigned database
        billDiscountPanel.setOpaque(false);
        billDiscountPanel.setBorder(b);

        //Retrieved data from the database
        retrieveBillDiscountTableData();
        JPanel headPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        headPanel.setOpaque(false);
        JButton filterBtn = new BtnCustom(new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/filterBlack.png")).getImage()), new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/filterViolet.png")).getImage()), "Filter");
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

            if (choice == 0) { // Sort
                sortBillDiscount();
            } else if (choice == 1) { // Like Search
                likeSearchBillDiscount();
            } else if (choice == 2) {
                retrieveBillDiscountTableData();
            }
        });

        JButton refreshBtn = new BtnCustom(new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/refresh.png")).getImage()), new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/refreshGreen.png")).getImage()), "Refresh");
        refreshBtn.addActionListener(e -> {
            retrieveBillDiscountTableData();
        });

        //Panel for the table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(b);
        billDiscountTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        billDiscountTable.setFillsViewportHeight(true);
        JScrollPane tableScrollPane = new CScrollPane(billDiscountTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        tableScrollPane.setVisible(true);

        //Panel for the buttons
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonsPanel.setPreferredSize(new Dimension(0, 50));
        buttonsPanel.setBorder(b);
        JButton createBtn = new BtnGreen("Create", new ImageIcon(getClass().getResource("/images/icon/create.png")));
        JButton updateBtn = new BtnDefault("Update", new ImageIcon(getClass().getResource("/images/icon/update.png")));
        JButton searchBtn = new BtnBlue("Search", new ImageIcon(getClass().getResource("/images/icon/searchWhite.png")));
        JButton deleteBtn = new BtnRed("Delete", new ImageIcon(getClass().getResource("/images/icon/delete.png")));

        buttonsPanel.add(createBtn);
        buttonsPanel.add(updateBtn);
        buttonsPanel.add(searchBtn);
        buttonsPanel.add(deleteBtn);

        createBtn.addActionListener(e -> createBillDiscount());
        updateBtn.addActionListener(e -> updateBillDiscount());
        deleteBtn.addActionListener(e -> deleteBillDiscount());
        searchBtn.addActionListener(e -> searchBillDiscount());

        tablePanel.setPreferredSize(new Dimension(900, 0));
        buttonsPanel.setPreferredSize(new Dimension(0, 100));
        billDiscountPanel.add(tablePanel, BorderLayout.CENTER);
        billDiscountPanel.add(buttonsPanel, BorderLayout.SOUTH);
        billDiscountPanel.add(headPanel, BorderLayout.NORTH);
        headPanel.add(refreshBtn);
        headPanel.add(filterBtn);

    }

    public JPanel getBillWithDiscountGUI() {
        return billDiscountPanel;
    }

    private void createBillDiscount() {

        JDialog createDialog = new JDialog((Frame) null, "Create Bill With Discount", true);
        createDialog.setSize(575, 420);
        createDialog.setLayout(new BorderLayout());
        createDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
        mainPanel.setLayout(new BorderLayout());

        // Title
        JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/green.jpg")).getImage());
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(3, 4, 94));
        titlePanel.setBorder(b);

        JLabel title = new JLabel("Create Bill With Discount");
        title.setFont(b18);
        title.setForeground(Color.white);
        title.setHorizontalAlignment(JLabel.CENTER);

        titlePanel.add(title, BorderLayout.CENTER);

        // Form Panel
        JPanel miniPanel = new JPanel(new GridBagLayout());
        miniPanel.setOpaque(false);
        miniPanel.setBorder(b);

        // Input Fields
        billIDField = new RTextField("Required", 30);
        billIDField.setEnabled(true);
        discountIDField = new RTextField("Required", 30);
        lVatField = new RTextField("0.00 (Required)", 30);
        dAmountField = new RTextField("0.00 (Required)", 30);

        billIDField.setFont(p14);
        discountIDField.setFont(p14);
        lVatField.setFont(p14);
        dAmountField.setFont(p14);

        // Layout with GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        // LABELS
        gbc.gridx = 0;
        gbc.gridy = 0;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(billIDLabel, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(discountIDLabel, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(lVatLabel, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(dAmountLabel, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);

        // INPUTS
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        nextID = Database.getNextID("BILLWITHDISCOUNT");
        if (nextID != -1) {
            billIDField.setText(String.valueOf(nextID));
            billIDField.setEnabled(false);
            billIDField.setDisabledTextColor(Color.DARK_GRAY);
        }
        miniPanel.add(billIDField, gbc);
        gbc.gridy++;
        errorBill = new JLabel("");
        errorBill.setFont(p11);
        errorBill.setForeground(Color.red);
        miniPanel.add(errorBill, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(discountIDField, gbc);
        gbc.gridy++;
        errordAmount = new JLabel("");
        errordAmount.setFont(p11);
        errordAmount.setForeground(Color.red);
        miniPanel.add(errordAmount, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(lVatField, gbc);
        gbc.gridy++;
        errortVat = new JLabel("");
        errortVat.setFont(p11);
        errortVat.setForeground(Color.red);
        miniPanel.add(errortVat, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(dAmountField, gbc);
        gbc.gridy++;
        errorDiscount = new JLabel("");
        errorDiscount.setFont(p11);
        errorDiscount.setForeground(Color.red);
        miniPanel.add(errorDiscount, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(b);
        JButton cancelBtn = new BtnRed("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
        JButton createBtn = new BtnGreen("Create", new ImageIcon(getClass().getResource("/images/icon/create.png")));
        btnPanel.add(cancelBtn);
        btnPanel.add(createBtn);
        cancelBtn.addActionListener(e -> createDialog.dispose());

        createBtn.addActionListener(e -> {
            String billIDText = billIDField.getText().trim();
            String discountIDText = discountIDField.getText().trim();
            String lVatText = lVatField.getText().trim();
            String dAmountText = dAmountField.getText().trim();
            errorBill.setText("");
            errordAmount.setText("");
            errortVat.setText("");
            errorDiscount.setText("");

            discountIDField.setForeground(Color.black);
            lVatField.setForeground(Color.black);
            dAmountField.setForeground(Color.black);
            boolean hasError = false;
            if (discountIDText.length() > 20) {
                errordAmount.setText("Invalid Discount ID length, please input characters no more than 20.");
                discountIDField.setForeground(Color.red);
                hasError = true;
            }

            // Check if required fields are empty or default
            if (discountIDText.isEmpty() || discountIDText.equalsIgnoreCase("Required")
                    || lVatText.isEmpty() || lVatText.equalsIgnoreCase("0.00 (Required)")
                    || dAmountText.isEmpty() || dAmountText.equalsIgnoreCase("0.00 (Required)")) {
                JOptionPane.showMessageDialog(createDialog, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                
                if (billIDText.isEmpty() || billIDText.equalsIgnoreCase("Required")) {
                    errorBill.setText("Bill ID is required.");
                    billIDField.setForeground(Color.red);
                    hasError = true;
                }
                
                if (discountIDText.isEmpty() || discountIDText.equalsIgnoreCase("Required")) {
                    errordAmount.setText("Discount ID is required.");
                    discountIDField.setForeground(Color.red);
                    hasError = true;
                }

                if (lVatText.isEmpty() || lVatText.equalsIgnoreCase("0.00 (Required)")) {
                    errortVat.setText("Less Vat is required.");
                    lVatField.setForeground(Color.red);
                    hasError = true;
                }

                if (dAmountText.isEmpty() || dAmountText.equalsIgnoreCase("0.00 (Required)")) {
                    errorDiscount.setText("Discount Amount is required.");
                    dAmountField.setForeground(Color.red);
                    hasError = true;
                }

                if (hasError) {
                    JOptionPane.showMessageDialog(createDialog, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                return; // Stop further execution if there's any error
            }

            try {
                int billIDD = Integer.parseInt(billIDText);
                int discountID = Integer.parseInt(discountIDText);
                double lessVat = Double.parseDouble(lVatText);
                double dAmount = Double.parseDouble(dAmountText);

                if (dAmount > lessVat) {
                    errorDiscount.setText("Invalid Discount amount. Must not be greater than less VAT.");
                    dAmountField.setForeground(Color.red);
                    hasError = true;
                }

                if (dAmount <= 0) {
                    errorDiscount.setText("Discount Amount must be greater than 0.");
                    dAmountField.setForeground(Color.red);
                    hasError = true;
                }
                if (lessVat <= 0) {
                    errortVat.setText("Less VAT Amount must be greater than 0.");
                    lVatField.setForeground(Color.red);
                    hasError = true;
                }

                if (hasError) {
                    JOptionPane.showMessageDialog(createDialog, "Please check the invalid fields.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String sql = "INSERT INTO BILLWITHDISCOUNT (BillID, DiscountID, LessVAT, DiscountAmount) VALUES (?, ?, ?, ?)";

                try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, billIDD);
                    stmt.setInt(2, discountID);
                    stmt.setDouble(3, lessVat);
                    stmt.setDouble(4, dAmount);
                    stmt.executeUpdate();
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Bill With Discount has successfully been added", "Bill With Discount updated successfully", JOptionPane.INFORMATION_MESSAGE);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            } catch (NumberFormatException ae) {
                JOptionPane.showMessageDialog(createDialog, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            retrieveBillDiscountTableData();
        });

        // Assemble panels
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(miniPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        createDialog.add(mainPanel);
        createDialog.setLocationRelativeTo(null);
        createDialog.setVisible(true);
    }

    private void clearFields() {
        errordAmount.setText("");
        errortVat.setText("");
        errorDiscount.setText("");

        discountIDField.setForeground(Color.black);
        lVatField.setForeground(Color.black);
        dAmountField.setForeground(Color.black);
        
        billIDField.setDefault();
        discountIDField.setDefault();
        lVatField.setDefault();
        dAmountField.setDefault();
    }

    private void deleteBillDiscount() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Bill ID to Delete:");
        if (inputID == null) {
            return; // User cancelled
        }

        inputID = inputID.trim();
        if (inputID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Bill ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int billIDD;
        try {
            billIDD = Integer.parseInt(inputID);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Bill ID. Please enter a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ask for Discount ID
        String discountID = JOptionPane.showInputDialog(this, "Enter Discount ID to delete:");
        if (discountID == null || discountID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Discount ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM BILLWITHDISCOUNT WHERE BillID = ? AND DiscountID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, billIDD);
            stmt.setString(2, discountID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog deleteDialog = new JDialog((Frame) null, "Delete Bill With Discount", true);
                deleteDialog.setSize(575, 420);
                deleteDialog.setLayout(new BorderLayout());
                deleteDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                // Main Panel with background
                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title Panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/red.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);
                JLabel title = new JLabel("Delete Bill With Discount");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                // Form Panel
                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);

                // Fields (retrieved from database)
                billIDField = new RTextField(String.valueOf(rs.getInt("BillID")), 30);
                billIDField.setFont(p14);
                billIDField.setEnabled(false);
                billIDField.setDisabledTextColor(Color.DARK_GRAY);
                billIDField.setText(rs.getString("BillID")); // from ResultSet

                discountIDField = new RTextField(String.valueOf(rs.getInt("DiscountID")), 30);
                discountIDField.setFont(p14);
                discountIDField.setEnabled(false);
                discountIDField.setDisabledTextColor(Color.DARK_GRAY);
                discountIDField.setText(rs.getString("DiscountID"));

                lVatField = new RTextField(String.valueOf(rs.getDouble("LessVAT")), 30);
                lVatField.setFont(p14);
                lVatField.setEnabled(false);
                lVatField.setDisabledTextColor(Color.DARK_GRAY);
                lVatField.setText(rs.getString("LessVAT"));

                dAmountField = new RTextField(String.valueOf(rs.getDouble("DiscountAmount")), 30);
                dAmountField.setFont(p14);
                dAmountField.setEnabled(false);
                dAmountField.setDisabledTextColor(Color.DARK_GRAY);
                dAmountField.setText(rs.getString("DiscountAmount"));

                // Layout
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 0;
                // Labels
                gbc.gridx = 0;
                gbc.gridy = 0;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(billIDLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(discountIDLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(lVatLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(dAmountLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);

                // Inputs
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.weightx = 1.0;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(billIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(discountIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(lVatField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(dAmountField, gbc);

                // Buttons
                JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
                btnPanel.setOpaque(false);
                btnPanel.setBorder(b);
                JButton cancelBtn = new BtnDefault("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
                JButton deleteBtn = new BtnRed("Confirm Delete", new ImageIcon(getClass().getResource("/images/icon/delete.png")));
                btnPanel.add(cancelBtn);
                btnPanel.add(deleteBtn);
                // Add panels to main panel
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(miniPanel, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);

                cancelBtn.addActionListener(e -> deleteDialog.dispose());
                deleteBtn.addActionListener(e -> {
                    try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM BILLWITHDISCOUNT WHERE BillID = ? AND DiscountID = ?")) {
                        deleteStmt.setInt(1, billIDD);
                        deleteStmt.setString(2, discountID);
                        deleteStmt.executeUpdate();
                        JOptionPane.showMessageDialog(deleteDialog, "Bill No. " + billIDD + " with Discount ID No. " + discountID + " was deleted successfully.");
                        retrieveBillDiscountTableData();
                        deleteDialog.dispose();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(deleteDialog, "Delete failed: " + ex.getMessage());
                    }
                });
                deleteDialog.add(mainPanel, BorderLayout.CENTER);
                deleteDialog.setLocationRelativeTo(null);
                deleteDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Bill ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateBillDiscount() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Bill ID to Update:");
        if (inputID == null) {
            return; // User cancelled
        }

        inputID = inputID.trim();
        if (inputID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Bill ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int billIDD;
        try {
            billIDD = Integer.parseInt(inputID);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Bill ID. Please enter a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ask for Discount ID
        String discountID = JOptionPane.showInputDialog(this, "Enter Discount ID to apply:");
        if (discountID == null || discountID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Discount ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String sql = "SELECT * FROM BILLWITHDISCOUNT WHERE BillID = ? AND DiscountID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, billIDD);
            stmt.setString(2, discountID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog updateDialog = new JDialog((Frame) null, "Update Bill With Discount", true);
                updateDialog.setSize(575, 420);
                updateDialog.setLayout(new BorderLayout());
                updateDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                // Main background panel
                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/orange.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);

                JLabel title = new JLabel("Update Bill");
                title.setFont(b18);
                title.setForeground(Color.WHITE);
                title.setHorizontalAlignment(JLabel.CENTER);

                titlePanel.add(title, BorderLayout.CENTER);

                // Fields
                billIDField = new RTextField(String.valueOf(rs.getInt("BillID")), 30);
                billIDField.setFont(p14);
                billIDField.setEnabled(false);
                billIDField.setDisabledTextColor(Color.DARK_GRAY);
                billIDField.setText(rs.getString("BillID"));

                discountIDField = new RTextField(String.valueOf(rs.getInt("DiscountID")), 30);
                discountIDField.setFont(p14);
                discountIDField.setEnabled(false);
                discountIDField.setDisabledTextColor(Color.DARK_GRAY);
                discountIDField.setText(rs.getString("DiscountID"));

                lVatField = new RTextField(String.valueOf(rs.getDouble("LessVAT")), 30);
                lVatField.setFont(p14);
                lVatField.setEnabled(true);  // Allowing modification
                lVatField.setText(rs.getString("LessVAT"));

                dAmountField = new RTextField(String.valueOf(rs.getDouble("DiscountAmount")), 30);
                dAmountField.setFont(p14);
                dAmountField.setEnabled(true);  // Allowing modification
                dAmountField.setText(rs.getString("DiscountAmount"));

                // Mini panel using your gbc setup
                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);
                // Layout with GridBagConstraints
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 0;
                // LABELS
                gbc.gridx = 0;
                gbc.gridy = 0;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(billIDLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(discountIDLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(lVatLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(dAmountLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);

                // INPUTS
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.weightx = 1.0;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(billIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(discountIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(lVatField, gbc);
                gbc.gridy++;
                errortVat = new JLabel("");
                errortVat.setFont(p11);
                errortVat.setForeground(Color.red);
                miniPanel.add(errortVat, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(dAmountField, gbc);
                gbc.gridy++;
                errorDiscount = new JLabel("");
                errorDiscount.setFont(p11);
                errorDiscount.setForeground(Color.red);
                miniPanel.add(errorDiscount, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);

                // ========== BUTTON PANEL ========== 
                JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
                btnPanel.setBorder(b);
                btnPanel.setOpaque(false);
                JButton cancelBtn = new BtnRed("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
                JButton updateBtn = new BtnDefault("Update", new ImageIcon(getClass().getResource("/images/icon/update.png")));
                btnPanel.add(cancelBtn);
                btnPanel.add(updateBtn);

                // ========== ASSEMBLE ========== 
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(miniPanel, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);

                cancelBtn.addActionListener(e -> updateDialog.dispose());
                updateBtn.addActionListener(e -> {
                    String discountIDText = discountIDField.getText().trim();
                    String lVatText = lVatField.getText().trim();
                    String dAmountText = dAmountField.getText().trim();

                    errortVat.setText("");
                    errorDiscount.setText("");
                    discountIDField.setForeground(Color.black);
                    lVatField.setForeground(Color.black);
                    dAmountField.setForeground(Color.black);

                    // Check if required fields are empty or default
                    // Validation checks
                    if (discountIDText.isEmpty() || lVatText.isEmpty() || dAmountText.isEmpty()) {
                        JOptionPane.showMessageDialog(updateDialog, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);

                        if (discountIDText.isEmpty()) {
                            discountIDField.setForeground(Color.red);
                        }
                        if (lVatText.isEmpty()) {
                            errortVat.setText("Less VAT is required.");
                            lVatField.setForeground(Color.red);
                        }
                        if (dAmountText.isEmpty()) {
                            errorDiscount.setText("Discount Amount is required.");
                            dAmountField.setForeground(Color.red);
                        }
                        return;
                    }

                    // Length validation for Discount ID
                    if (discountIDText.length() > 20) {
                        JOptionPane.showMessageDialog(updateDialog, "Discount ID must not exceed 20 characters.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        discountIDField.setForeground(Color.red);
                        return;
                    }

                    try {
                        double lessVat = Double.parseDouble(lVatText);
                        double dAmount = Double.parseDouble(dAmountText);
                        boolean hasError = false;
                        
                        if (dAmount > lessVat) {
                            errorDiscount.setText("Invalid Discount amount. Must not be greater than less VAT.");
                            dAmountField.setForeground(Color.red);
                            hasError = true;
                        }

                        if (dAmount <= 0) {
                            errorDiscount.setText("Discount Amount must be greater than 0.");
                            dAmountField.setForeground(Color.red);
                            hasError = true;
                        }
                        if (lessVat <= 0) {
                            errortVat.setText("Less VAT Amount must be greater than 0.");
                            lVatField.setForeground(Color.red);
                            hasError = true;
                        }

                        if (hasError) {
                            JOptionPane.showMessageDialog(updateDialog, "Please check the invalid fields.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Update the database
                        try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE BILLWITHDISCOUNT SET LessVAT = ?, DiscountAmount = ? WHERE BillID = ? AND DiscountID = ?")) {

                            updateStmt.setDouble(1, lessVat);        // LessVAT
                            updateStmt.setDouble(2, dAmount);        // DiscountAmount
                            updateStmt.setInt(3, billIDD); //Bill ID
                            updateStmt.setString(4, discountID); // DiscountID is text
                            updateStmt.executeUpdate();
                            JOptionPane.showMessageDialog(updateDialog, "Bill No. " + billIDD + " with Discount ID No. " + discountID + " was updated.");
                            retrieveBillDiscountTableData(); // Refresh data
                            updateDialog.dispose();
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(updateDialog, "Update failed: " + ex.getMessage());
                        }
                    } catch (NumberFormatException ae) {
                        JOptionPane.showMessageDialog(updateDialog, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                updateDialog.setLocationRelativeTo(null);
                updateDialog.add(mainPanel, BorderLayout.CENTER);
                updateDialog.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Bill ID not found.");
            }

        } catch (SQLException ea) {
            JOptionPane.showMessageDialog(this, "Error: " + ea.getMessage());
        } catch (NumberFormatException ae) {
            JOptionPane.showMessageDialog(null, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchBillDiscount() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Bill ID to Update:");
        if (inputID == null) {
            return; // User cancelled
        }

        inputID = inputID.trim();
        if (inputID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Bill ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int billIDD;
        try {
            billIDD = Integer.parseInt(inputID);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Bill ID. Please enter a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ask for Discount ID
        String discountID = JOptionPane.showInputDialog(this, "Enter Discount ID to apply:");
        if (discountID == null || discountID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Discount ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM BILLWITHDISCOUNT WHERE BillID = ? AND DiscountID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, billIDD);
            stmt.setString(2, discountID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog searchDialog = new JDialog((Frame) null, "Bill No. " + inputID, true);
                searchDialog.setSize(575, 420);
                searchDialog.setLayout(new BorderLayout());
                searchDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                searchDialog.setLocationRelativeTo(null);

                // Main Panel with background
                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title Panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/blue1.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);
                JLabel title = new JLabel("Search Bill");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                // Form Panel
                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);

                // Fields (retrieved from database)
                billIDField = new RTextField(String.valueOf(rs.getInt("BillID")), 30);
                billIDField.setFont(p14);
                billIDField.setEnabled(false);
                billIDField.setDisabledTextColor(Color.DARK_GRAY);
                billIDField.setText(rs.getString("BillID")); // from ResultSet

                discountIDField = new RTextField(String.valueOf(rs.getInt("DiscountID")), 30);
                discountIDField.setFont(p14);
                discountIDField.setEnabled(false);
                discountIDField.setDisabledTextColor(Color.DARK_GRAY);
                discountIDField.setText(rs.getString("DiscountID"));

                lVatField = new RTextField(String.valueOf(rs.getDouble("LessVAT")), 30);
                lVatField.setFont(p14);
                lVatField.setEnabled(false);
                lVatField.setDisabledTextColor(Color.DARK_GRAY);
                lVatField.setText(rs.getString("LessVAT"));

                dAmountField = new RTextField(String.valueOf(rs.getDouble("DiscountAmount")), 30);
                dAmountField.setFont(p14);
                dAmountField.setEnabled(false);
                dAmountField.setDisabledTextColor(Color.DARK_GRAY);
                dAmountField.setText(rs.getString("DiscountAmount"));

                // Layout
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 0;

                // Labels
                gbc.gridx = 0;
                gbc.gridy = 0;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(billIDLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(discountIDLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(lVatLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(dAmountLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);

                // Inputs
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.weightx = 1.0;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(billIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(discountIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(lVatField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(dAmountField, gbc);

                // Buttons
                JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
                btnPanel.setOpaque(false);
                btnPanel.setBorder(b);
                JButton returnBtn = new BtnDefault("Return", new ImageIcon(getClass().getResource("/images/icon/return.png")));
                btnPanel.add(returnBtn);

                returnBtn.addActionListener(e -> {
                    searchDialog.dispose();
                });
                // Add panels to main panel
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(miniPanel, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);
                searchDialog.add(mainPanel, BorderLayout.CENTER);
                searchDialog.setLocationRelativeTo(null);
                searchDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Bill not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        retrieveBillDiscountTableData();

    }

    private void likeSearchBillDiscount() {
        String[] fields = {"BillID", "DiscountID", "LessVAT", "DiscountAmount"};
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

            String sql = "SELECT * FROM BILLWITHDISCOUNT WHERE " + field + " LIKE ?";
            try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, keyword);
                try (ResultSet rs = stmt.executeQuery()) {
                    tableModel.setRowCount(0); // clear table

                    while (rs.next()) {
                        tableModel.addRow(new Object[]{
                            rs.getString("BillID"),
                            rs.getString("DiscountID"),
                            rs.getString("LessVAT"),
                            rs.getString("DiscountAmount")
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

    private void sortBillDiscount() {
        String[] fields = {"BillID", "DiscountID", "LessVAT", "DiscountAmount"};
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

        String sql = "SELECT * FROM BILLWITHDISCOUNT ORDER BY " + field + " " + order;

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            tableModel.setRowCount(0); // clear table

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("BillID"),
                    rs.getString("DiscountID"),
                    rs.getString("LessVAT"),
                    rs.getString("DiscountAmount")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error sorting: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void retrieveBillDiscountTableData() {
        String sql = "SELECT * FROM BILLWITHDISCOUNT";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            // Clear existing rows in the table model
            DefaultTableModel model = (DefaultTableModel) billDiscountTable.getModel();
            model.setRowCount(0); // Clear old rows

            // Fill the table with fresh data
            while (rs.next()) {
                Object[] row = {
                    rs.getString("BillID"),
                    rs.getString("DiscountID"),
                    rs.getString("LessVAT"),
                    rs.getString("DiscountAmount")
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading table data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
