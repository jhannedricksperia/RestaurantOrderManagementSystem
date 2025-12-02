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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class CashPaymentGUI extends Components {

    private RTextField cPaymentIDField, paymentIDField, cashTenderedField, changeField;
    private JLabel errorChange, errorCashTendered, errorPaymentID;
    private final DefaultTableModel tableModel = new DisabledTableModel(new String[]{"C_PaymentID", "PaymentID", "Cash Tendered", "Change"}, 0);
    private final JTable cashPaymentTable = Components.updateTable(new JTable(tableModel));
    private int nextID;
    private JLabel cPaymentIDLabel = new JLabel("Cash Payment ID:");
    private JLabel paymentIDLabel = new JLabel("Payment ID:");
    private JLabel cashTenderedLabel = new JLabel("Cash Tendered:");
    private JLabel changeLabel = new JLabel("Change:");
    private JPanel cashPaymentPanel;

    public CashPaymentGUI() {
        cPaymentIDLabel.setFont(p14);
        paymentIDLabel.setFont(p14);
        cashTenderedLabel.setFont(p14);
        changeLabel.setFont(p14);

        DefaultTableCellRenderer centerRenderer = new CustomRowRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer rightRenderer = new CustomRowRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        cashPaymentTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // C_PaymentID
        cashPaymentTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // PaymentID
        cashPaymentTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);  // Cash Tendered
        cashPaymentTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);  // Change

        cashPaymentPanel = new JPanel(new BorderLayout());
        cashPaymentPanel.setOpaque(false);
        cashPaymentPanel.setBorder(b);

        //Retrieved data from the database
        retrieveCashPaymentTableData();
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
                sortCashPayments();
            } else if (choice == 1) { // Like Search
                likeSearchCashPayments();
            } else if (choice == 2) {
                retrieveCashPaymentTableData();
            }
        });

        JButton refreshBtn = new BtnCustom(new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/refresh.png")).getImage()), new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/refreshGreen.png")).getImage()), "Refresh");
        refreshBtn.addActionListener(e -> {
            retrieveCashPaymentTableData();
        });

        //Panel for the table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(b);
        cashPaymentTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        cashPaymentTable.setFillsViewportHeight(true);
        JScrollPane tableScrollPane = new CScrollPane(cashPaymentTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

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

        createBtn.addActionListener(e -> createCashPayment());
        updateBtn.addActionListener(e -> updateCashPayment());
        deleteBtn.addActionListener(e -> deleteCashPayment());
        searchBtn.addActionListener(e -> searchCashPayment());

        tablePanel.setPreferredSize(new Dimension(900, 0));
        buttonsPanel.setPreferredSize(new Dimension(0, 100));
        cashPaymentPanel.add(tablePanel, BorderLayout.CENTER);
        cashPaymentPanel.add(buttonsPanel, BorderLayout.SOUTH);
        cashPaymentPanel.add(headPanel, BorderLayout.NORTH);
        headPanel.add(refreshBtn);
        headPanel.add(filterBtn);

    }

    public JPanel getCashPaymentPanel() {
        return cashPaymentPanel;
    }

    private void createCashPayment() {

        JDialog createDialog = new JDialog((Frame) null, "Create Cash Payment", true);
        createDialog.setSize(470, 420);
        createDialog.setLayout(new BorderLayout());
        createDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
        mainPanel.setLayout(new BorderLayout());

        // Title
        JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/green.jpg")).getImage());
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(3, 4, 94));
        titlePanel.setBorder(b);

        JLabel title = new JLabel("Create Cash Payment");
        title.setFont(b18);
        title.setForeground(Color.white);
        title.setHorizontalAlignment(JLabel.CENTER);

        titlePanel.add(title, BorderLayout.CENTER);

        // Form Panel
        JPanel miniPanel = new JPanel(new GridBagLayout());
        miniPanel.setOpaque(false);
        miniPanel.setBorder(b);

        // Input Fields
        cPaymentIDField = new RTextField(30);
        cPaymentIDField.setEnabled(false);
        paymentIDField = new RTextField("Required", 30);
        cashTenderedField = new RTextField("0.00 (Required)", 30);
        changeField = new RTextField("0.00 (Required)", 30);

        cPaymentIDField.setFont(p14);
        paymentIDField.setFont(p14);
        cashTenderedField.setFont(p14);
        changeField.setFont(p14);

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
        miniPanel.add(cPaymentIDLabel, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(paymentIDLabel, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(cashTenderedLabel, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(changeLabel, gbc);
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
        nextID = Database.getNextID("CASHPAYMENT");
        if (nextID != -1) {
            cPaymentIDField.setText(String.valueOf(nextID));
            cPaymentIDField.setEnabled(false);
            cPaymentIDField.setDisabledTextColor(Color.BLACK);
        }
        miniPanel.add(cPaymentIDField, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(paymentIDField, gbc);
        gbc.gridy++;
        errorPaymentID = new JLabel("");
        errorPaymentID.setFont(p11);
        errorPaymentID.setForeground(Color.red);
        miniPanel.add(errorPaymentID, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(cashTenderedField, gbc);
        gbc.gridy++;
        errorCashTendered = new JLabel("");
        errorCashTendered.setFont(p11);
        errorCashTendered.setForeground(Color.red);
        miniPanel.add(errorCashTendered, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(changeField, gbc);
        gbc.gridy++;
        errorChange = new JLabel("");
        errorChange.setFont(p11);
        errorChange.setForeground(Color.red);
        miniPanel.add(errorChange, gbc);
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
            String paymentIDText = paymentIDField.getText().trim();
            String cashTenderedText = cashTenderedField.getText().trim();
            String changeText = changeField.getText().trim();

            errorPaymentID.setText("");
            errorCashTendered.setText("");
            errorChange.setText("");

            paymentIDField.setForeground(Color.black);
            cashTenderedField.setForeground(Color.black);
            changeField.setForeground(Color.black);
            boolean hasError = false;
            // Check if required fields are empty or default
            if (paymentIDText.isEmpty() || paymentIDText.equalsIgnoreCase("Required")
                    || cashTenderedText.isEmpty() || cashTenderedText.equalsIgnoreCase("0.00 (Required)")
                    || changeText.isEmpty() || changeText.equalsIgnoreCase("0.00 (Required)")) {

                if (paymentIDText.isEmpty() || paymentIDText.equalsIgnoreCase("Required")) {
                    errorPaymentID.setText("Payment ID is required.");
                    paymentIDField.setForeground(Color.red);
                    hasError = true;
                }

                if (cashTenderedText.isEmpty() || cashTenderedText.equalsIgnoreCase("0.00 (Required)")) {
                    errorCashTendered.setText("Cash Tendered is required.");
                    cashTenderedField.setForeground(Color.red);
                    hasError = true;
                }

                if (changeText.isEmpty() || changeText.equalsIgnoreCase("0.00 (Required)")) {
                    errorChange.setText("Change is required.");
                    changeField.setForeground(Color.red);
                    hasError = true;
                }

                if (hasError) {
                    JOptionPane.showMessageDialog(createDialog, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                return; // Stop further execution if there's any error
            }

            try {
                int paymentID = Integer.parseInt(paymentIDText);
                double cashTendered = Double.parseDouble(cashTenderedText);
                double change = Double.parseDouble(changeText);

                if (change >= cashTendered) {
                    errorChange.setText("Change must be less than the cash tendered.");
                    changeField.setForeground(Color.red);
                    hasError = true;
                }

                if (change <= 0) {
                    errorChange.setText("Change must be greater than 0");
                    changeField.setForeground(Color.red);
                    hasError = true;
                }
                if (cashTendered <= 0) {
                    errorChange.setText("Cash tendered must be greater than 0");
                    changeField.setForeground(Color.red);
                    hasError = true;
                }

                if (hasError) {
                    JOptionPane.showMessageDialog(createDialog, "Please check the invalid fields.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String sql = "INSERT INTO CASHPAYMENT (PaymentID, CashTendered, Change) VALUES (?, ?, ?)";

                try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, paymentID);
                    stmt.setDouble(2, cashTendered);
                    stmt.setDouble(3, change);
                    stmt.executeUpdate();
                    nextID = Database.getNextID("CASHPAYMENT");
                    if (nextID != -1) {
                        cPaymentIDField.setText(String.valueOf(nextID));
                        cPaymentIDField.setEnabled(false);
                        cPaymentIDField.setDisabledTextColor(Color.BLACK);
                    }
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Cash Payment has successfully been added", "Cash Payment updated successfully", JOptionPane.INFORMATION_MESSAGE);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            } catch (NumberFormatException ae) {
                JOptionPane.showMessageDialog(createDialog, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            retrieveCashPaymentTableData();
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
        errorPaymentID.setText("");
        errorCashTendered.setText("");
        errorChange.setText("");

        paymentIDField.setForeground(Color.black);
        cashTenderedField.setForeground(Color.black);
        changeField.setForeground(Color.black);

        paymentIDField.setDefault();
        cashTenderedField.setDefault();
        changeField.setDefault();
    }

    private void deleteCashPayment() {
        String inputID = JOptionPane.showInputDialog(this, "Enter CashPayment ID to Delete:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No CashPayment ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM CashPayment WHERE C_PaymentID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog deleteDialog = new JDialog((Frame) null, "Delete Cash Payment", true);
                deleteDialog.setSize(470, 420);
                deleteDialog.setLayout(new BorderLayout());
                deleteDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                // Main Panel with background
                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title Panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/red.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);
                JLabel title = new JLabel("Delete Cash Payment");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                // Form Panel
                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);

                // Fields (retrieved from database)
                cPaymentIDField = new RTextField(String.valueOf(rs.getInt("C_PaymentID")), 30);
                cPaymentIDField.setFont(p14);
                cPaymentIDField.setEnabled(false);
                cPaymentIDField.setDisabledTextColor(Color.BLACK);
                cPaymentIDField.setText(rs.getString("C_PaymentID")); // from ResultSet

                paymentIDField = new RTextField(String.valueOf(rs.getInt("PaymentID")), 30);
                paymentIDField.setFont(p14);
                paymentIDField.setEnabled(false);
                paymentIDField.setDisabledTextColor(Color.BLACK);
                paymentIDField.setText(rs.getString("PaymentID"));

                cashTenderedField = new RTextField(String.valueOf(rs.getDouble("CashTendered")), 30);
                cashTenderedField.setFont(p14);
                cashTenderedField.setEnabled(false);
                cashTenderedField.setDisabledTextColor(Color.BLACK);
                cashTenderedField.setText(rs.getString("CashTendered"));

                changeField = new RTextField(String.valueOf(rs.getDouble("Change")), 30);
                changeField.setFont(p14);
                changeField.setEnabled(false);
                changeField.setDisabledTextColor(Color.BLACK);
                changeField.setText(rs.getString("Change"));

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
                miniPanel.add(cPaymentIDLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(paymentIDLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(cashTenderedLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(changeLabel, gbc);
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
                miniPanel.add(cPaymentIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(paymentIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(cashTenderedField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(changeField, gbc);

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
                    try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM CashPayment WHERE C_PaymentID = ?")) {
                        deleteStmt.setString(1, inputID);
                        deleteStmt.executeUpdate();
                        JOptionPane.showMessageDialog(deleteDialog, "CashPayment No. " + inputID + " was deleted successfully.");
                        retrieveCashPaymentTableData();
                        deleteDialog.dispose();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(deleteDialog, "Delete failed: " + ex.getMessage());
                    }
                });
                deleteDialog.add(mainPanel, BorderLayout.CENTER);
                deleteDialog.setLocationRelativeTo(null);
                deleteDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "CashPayment ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateCashPayment() {
        String inputID = JOptionPane.showInputDialog(this, "Enter CashPayment ID to Update:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No CashPayment ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM CASHPAYMENT WHERE C_PaymentID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            int cPaymentID = Integer.parseInt(inputID);
            stmt.setInt(1, cPaymentID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog updateDialog = new JDialog((Frame) null, "Update Cash Payment", true);
                updateDialog.setSize(470, 420);
                updateDialog.setLayout(new BorderLayout());
                updateDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                // Main background panel
                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/orange.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);

                JLabel title = new JLabel("Update Cash Payment");
                title.setFont(b18);
                title.setForeground(Color.WHITE);
                title.setHorizontalAlignment(JLabel.CENTER);

                titlePanel.add(title, BorderLayout.CENTER);

                // Fields
                cPaymentIDField = new RTextField(String.valueOf(rs.getInt("C_PaymentID")), 30);
                cPaymentIDField.setFont(p14);
                cPaymentIDField.setEnabled(false);
                cPaymentIDField.setDisabledTextColor(Color.BLACK);
                cPaymentIDField.setText(rs.getString("C_PaymentID"));

                paymentIDField = new RTextField(String.valueOf(rs.getInt("PaymentID")), 30);
                paymentIDField.setFont(p14);
                paymentIDField.setEnabled(false);
                paymentIDField.setDisabledTextColor(Color.BLACK);
                paymentIDField.setText(rs.getString("PaymentID"));

                cashTenderedField = new RTextField(String.valueOf(rs.getDouble("CashTendered")), 30);
                cashTenderedField.setFont(p14);
                cashTenderedField.setEnabled(true);  // Allowing modification
                cashTenderedField.setDisabledTextColor(Color.BLACK);
                cashTenderedField.setText(rs.getString("CashTendered"));

                changeField = new RTextField(String.valueOf(rs.getDouble("Change")), 30);
                changeField.setFont(p14);
                changeField.setEnabled(true);  // Allowing modification
                changeField.setDisabledTextColor(Color.BLACK);
                changeField.setText(rs.getString("Change"));

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
                miniPanel.add(cPaymentIDLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(paymentIDLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(cashTenderedLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(changeLabel, gbc);
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
                miniPanel.add(cPaymentIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(paymentIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(cashTenderedField, gbc);
                gbc.gridy++;
                errorCashTendered = new JLabel("");
                errorCashTendered.setFont(p11);
                errorCashTendered.setForeground(Color.red);
                miniPanel.add(errorCashTendered, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(changeField, gbc);
                gbc.gridy++;
                errorChange = new JLabel("");
                errorChange.setFont(p11);
                errorChange.setForeground(Color.red);
                miniPanel.add(errorChange, gbc);
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
                    String paymentIDText = paymentIDField.getText().trim();
                    String cashTenderedText = cashTenderedField.getText().trim();
                    String changeText = changeField.getText().trim();

                    errorCashTendered.setText("");
                    errorChange.setText("");
                    paymentIDField.setForeground(Color.black);
                    cashTenderedField.setForeground(Color.black);
                    changeField.setForeground(Color.black);

                    // Check if required fields are empty or default
                    if (paymentIDText.isEmpty() || cashTenderedText.isEmpty() || changeText.isEmpty()) {
                        JOptionPane.showMessageDialog(updateDialog, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        double cashTendered = Double.parseDouble(cashTenderedText);
                        double change = Double.parseDouble(changeText);
                        boolean hasError = false;
                        if (change >= cashTendered) {
                            errorChange.setText("Change must be less than the cash tendered.");
                            changeField.setForeground(Color.red);
                            hasError = true;
                        }

                        if (change <= 0) {
                            errorChange.setText("Change must be greater than 0");
                            changeField.setForeground(Color.red);
                            hasError = true;
                        }
                        if (cashTendered <= 0) {
                            errorChange.setText("Cash tendered must be greater than 0");
                            changeField.setForeground(Color.red);
                            hasError = true;
                        }

                        if (hasError) {
                            JOptionPane.showMessageDialog(updateDialog, "Please check the invalid fields.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Update the database
                        try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE CashPayment SET CashTendered = ?, Change = ? WHERE C_PaymentID = ?")) {
                            updateStmt.setDouble(1, cashTendered);
                            updateStmt.setDouble(2, change);
                            updateStmt.setInt(3, Integer.parseInt(inputID)); // Use the input ID for update
                            updateStmt.executeUpdate();
                            JOptionPane.showMessageDialog(updateDialog, "CashPayment No. " + inputID + " was updated.");
                            retrieveCashPaymentTableData(); // Refresh data
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
                JOptionPane.showMessageDialog(this, "CashPayment ID not found.");
            }

        } catch (SQLException ea) {
            JOptionPane.showMessageDialog(this, "Error: " + ea.getMessage());
        } catch (NumberFormatException ae) {
            JOptionPane.showMessageDialog(null, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchCashPayment() {
        String inputID = JOptionPane.showInputDialog(this, "Enter CashPayment ID to search:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No CashPayment ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM CashPayment WHERE C_PaymentID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog searchDialog = new JDialog((Frame) null, "Cash Payment No. " + inputID, true);
                searchDialog.setSize(470, 420);
                searchDialog.setLayout(new BorderLayout());
                searchDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                searchDialog.setLocationRelativeTo(null);

                // Main Panel with background
                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title Panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/blue2.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);
                JLabel title = new JLabel("Search Cash Payment");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                // Form Panel
                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);

                // Fields (retrieved from database)
                cPaymentIDField = new RTextField(String.valueOf(rs.getInt("C_PaymentID")), 30);
                cPaymentIDField.setFont(p14);
                cPaymentIDField.setEnabled(false);
                cPaymentIDField.setDisabledTextColor(Color.BLACK);
                cPaymentIDField.setText(rs.getString("C_PaymentID")); // from ResultSet

                paymentIDField = new RTextField(String.valueOf(rs.getInt("PaymentID")), 30);
                paymentIDField.setFont(p14);
                paymentIDField.setEnabled(false);
                paymentIDField.setDisabledTextColor(Color.BLACK);
                paymentIDField.setText(rs.getString("PaymentID"));

                cashTenderedField = new RTextField(String.valueOf(rs.getDouble("CashTendered")), 30);
                cashTenderedField.setFont(p14);
                cashTenderedField.setEnabled(false);
                cashTenderedField.setDisabledTextColor(Color.BLACK);
                cashTenderedField.setText(rs.getString("CashTendered"));

                changeField = new RTextField(String.valueOf(rs.getDouble("Change")), 30);
                changeField.setFont(p14);
                changeField.setEnabled(false);
                changeField.setDisabledTextColor(Color.BLACK);
                changeField.setText(rs.getString("Change"));

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
                miniPanel.add(cPaymentIDLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(paymentIDLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(cashTenderedLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(changeLabel, gbc);
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
                miniPanel.add(cPaymentIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(paymentIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(cashTenderedField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(changeField, gbc);

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
                JOptionPane.showMessageDialog(this, "CashPayment not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        retrieveCashPaymentTableData();

    }

    private void likeSearchCashPayments() {
        String[] fields = {"C_PaymentID", "PaymentID", "CashTendered", "Change"};
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

            String sql = "SELECT * FROM CASHPAYMENT WHERE " + field + " LIKE ?";
            try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, keyword);
                try (ResultSet rs = stmt.executeQuery()) {
                    tableModel.setRowCount(0); // clear table

                    while (rs.next()) {
                        tableModel.addRow(new Object[]{
                            rs.getString("C_PaymentID"),
                            rs.getString("PaymentID"),
                            rs.getString("CashTendered"),
                            rs.getString("Change")
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

    private void sortCashPayments() {
        String[] fields = {"C_PaymentID", "PaymentID", "CashTendered", "Change"};
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

        String sql = "SELECT * FROM CASHPAYMENT ORDER BY " + field + " " + order;

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            tableModel.setRowCount(0); // clear table

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("C_PaymentID"),
                    rs.getString("PaymentID"),
                    rs.getString("CashTendered"),
                    rs.getString("Change")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error sorting: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void retrieveCashPaymentTableData() {
        String sql = "SELECT * FROM CashPayment";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            // Clear existing rows in the table model
            DefaultTableModel model = (DefaultTableModel) cashPaymentTable.getModel();
            model.setRowCount(0); // Clear old rows

            // Fill the table with fresh data
            while (rs.next()) {
                Object[] row = {
                    rs.getString("C_PaymentID"),
                    rs.getString("PaymentID"),
                    rs.getString("CashTendered"),
                    rs.getString("Change")
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading table data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
