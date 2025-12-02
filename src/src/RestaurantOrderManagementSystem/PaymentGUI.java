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
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PaymentGUI extends Components {

    private RTextField paymentIDField, billIDField, pModeField;
    private JLabel errorBill, errorPMode, errorDTime;
    private DateTimeSpinner dateTimeSpinner;
    private final DefaultTableModel tableModel = new DisabledTableModel(new String[]{"PaymentID", "BillID", "DateTime", "PaymentMode"}, 0);
    private final JTable PaymentTable = Components.updateTable(new JTable(tableModel));
    private int nextID;
    private JLabel paymentIDLabel = new JLabel("Payment ID:");
    private JLabel billIDLabel = new JLabel("Bill ID:");
    private JLabel pModeLabel = new JLabel("Payment Mode:");
    private JLabel dTimeLabel = new JLabel("Date and Time:");
    private JPanel paymentPanel;

    public PaymentGUI() {
        dateTimeSpinner = new DateTimeSpinner();
        dateTimeSpinner.setValue(new Date());
        paymentIDLabel.setFont(p14);
        billIDLabel.setFont(p14);
        pModeLabel.setFont(p14);
        dTimeLabel.setFont(p14);

        DefaultTableCellRenderer centerRenderer = new CustomRowRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        PaymentTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // PaymentID
        PaymentTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // BillID
        PaymentTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // DateTime
        PaymentTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // PaymentMode

        paymentPanel = new JPanel(new BorderLayout());//Rename based on your assigned database
        paymentPanel.setOpaque(false);
        paymentPanel.setBorder(b);

        //Retrieved data from the database
        retrievePaymentTableData();
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
                sortPayments();
            } else if (choice == 1) { // Like Search
                likeSearchPayments();
            } else if (choice == 2) {
                retrievePaymentTableData();
            }
        });

        JButton refreshBtn = new BtnCustom(new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/refresh.png")).getImage()), new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/refreshGreen.png")).getImage()), "Refresh");
        refreshBtn.addActionListener(e -> {
            retrievePaymentTableData();
        });
        JButton printReceiptBtn = new BtnCustom(new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/receiptW.png")).getImage()), new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/receiptB.png")).getImage()), "Print Receipt");
        printReceiptBtn.addActionListener(e -> {
            printReceipt();
        });
        //Panel for the table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(b);
        PaymentTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        PaymentTable.setFillsViewportHeight(true);
        JScrollPane tableScrollPane = new CScrollPane(PaymentTable);
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

        createBtn.addActionListener(e -> createPayment());
        updateBtn.addActionListener(e -> updatePayment());
        deleteBtn.addActionListener(e -> deletePayment());
        searchBtn.addActionListener(e -> searchPayment());

        tablePanel.setPreferredSize(new Dimension(900, 0));
        buttonsPanel.setPreferredSize(new Dimension(0, 100));
        paymentPanel.add(tablePanel, BorderLayout.CENTER);
        paymentPanel.add(buttonsPanel, BorderLayout.SOUTH);
        paymentPanel.add(headPanel, BorderLayout.NORTH);
        headPanel.add(refreshBtn);
        headPanel.add(filterBtn);
        headPanel.add(printReceiptBtn);

    }

    public JPanel getPaymentGUI() {
        return paymentPanel;
    }

    private void createPayment() {

        JDialog createDialog = new JDialog((Frame) null, "Create Payment", true);
        createDialog.setSize(470, 420);
        createDialog.setLayout(new BorderLayout());
        createDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        RPanel dateTimePanel = new RPanel(30);
        dateTimePanel.setBackground(Color.WHITE);
        dateTimePanel.setLayout(new BorderLayout());
        dateTimePanel.setBorder(b);
        dateTimePanel.add(dateTimeSpinner, BorderLayout.CENTER);

        JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
        mainPanel.setLayout(new BorderLayout());

        // Title
        JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/green.jpg")).getImage());
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(3, 4, 94));
        titlePanel.setBorder(b);

        JLabel title = new JLabel("Create Payment");
        title.setFont(b18);
        title.setForeground(Color.white);
        title.setHorizontalAlignment(JLabel.CENTER);

        titlePanel.add(title, BorderLayout.CENTER);

        // Form Panel
        JPanel miniPanel = new JPanel(new GridBagLayout());
        miniPanel.setOpaque(false);
        miniPanel.setBorder(b);

        // Input Fields
        paymentIDField = new RTextField(30);
        paymentIDField.setEnabled(false);
        billIDField = new RTextField("Required", 30);
        pModeField = new RTextField("Cash", 30);
        pModeField.setEnabled(false);
        dateTimePanel = new RPanel(30);
        dateTimePanel.setLayout(new BorderLayout());
        dateTimeSpinner.setEnabled(true);
        dateTimePanel.add(dateTimeSpinner);
        dateTimePanel.setBackground(Color.WHITE);
        dateTimePanel.setBorder(b);

        paymentIDField.setFont(p14);
        billIDField.setFont(p14);
        pModeField.setFont(p14);
        dateTimeSpinner.setFont(p14);

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
        miniPanel.add(paymentIDLabel, gbc);
        gbc.gridy++;
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
        miniPanel.add(pModeLabel, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(dTimeLabel, gbc);
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
        nextID = Database.getNextID("PAYMENT");
        if (nextID != -1) {
            paymentIDField.setText(String.valueOf(nextID));
            paymentIDField.setEnabled(false);
            paymentIDField.setDisabledTextColor(Color.DARK_GRAY);
        }
        miniPanel.add(paymentIDField, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(billIDField, gbc);
        gbc.gridy++;
        errorBill = new JLabel("");
        errorBill.setFont(p11);
        errorBill.setForeground(Color.red);
        miniPanel.add(errorBill, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(pModeField, gbc);
        gbc.gridy++;
        errorPMode = new JLabel("");
        errorPMode.setFont(p11);
        errorPMode.setForeground(Color.red);
        miniPanel.add(errorPMode, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        //miniPanel.add(dateTimeSpinner, gbc);
        miniPanel.add(dateTimePanel, gbc);
        gbc.gridy++;
        errorDTime = new JLabel("");
        errorDTime.setFont(p11);
        errorDTime.setForeground(Color.red);
        miniPanel.add(errorDTime, gbc);
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
            String pModeText = pModeField.getText().trim();
            String dTimeDate = dateTimeSpinner.getDateTime().trim();

            errorBill.setText("");
            errorPMode.setText("");
            errorDTime.setText("");

            billIDField.setForeground(Color.black);
            pModeField.setForeground(Color.black);
            dateTimeSpinner.setForeground(Color.black);

            // Check if required fields are empty or default
            if (billIDText.isEmpty() || billIDText.equalsIgnoreCase("Required")
                    || pModeText.isEmpty() || pModeText.equalsIgnoreCase("")
                    || dTimeDate.isEmpty() || dTimeDate.equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(createDialog, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);

                if (billIDText.isEmpty() || billIDText.equalsIgnoreCase("Required")) {
                    errorBill.setText("Bill ID is required.");
                    billIDField.setForeground(Color.red);
                }

                return; // Stop further execution if there's any error
            }

            try {
                int billID = Integer.parseInt(billIDText);

                String sql = "INSERT INTO PAYMENT (BillID, DateTime, PaymentMode) VALUES (?, ?, ?)";

                try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, billID);
                    stmt.setString(2, dTimeDate);
                    stmt.setString(3, pModeText);
                    stmt.executeUpdate();
                    nextID = Database.getNextID("PAYMENT");
                    if (nextID != -1) {
                        paymentIDField.setText(String.valueOf(nextID));
                        paymentIDField.setEnabled(false);
                        paymentIDField.setDisabledTextColor(Color.DARK_GRAY);
                    }
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Payment has successfully been added", "Payment updated successfully", JOptionPane.INFORMATION_MESSAGE);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            } catch (NumberFormatException ae) {
                JOptionPane.showMessageDialog(createDialog, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            retrievePaymentTableData();
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
        errorBill.setText("");
        errorPMode.setText("");
        errorDTime.setText("");

        billIDField.setForeground(Color.black);
        pModeField.setForeground(Color.black);
        dateTimeSpinner.setForeground(Color.black);

        billIDField.setDefault();
        pModeField.setDefault();
        dateTimeSpinner.setValue(new Date());
    }

    private void deletePayment() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Payment ID to Delete:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Payment ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM Payment WHERE PaymentID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog deleteDialog = new JDialog((Frame) null, "Delete Payment", true);
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
                JLabel title = new JLabel("Delete Payment");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                // Form Panel
                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);

                // Fields (retrieved from database)
                paymentIDField = new RTextField(String.valueOf(rs.getInt("PaymentID")), 30);
                paymentIDField.setFont(p14);
                paymentIDField.setEnabled(false);
                paymentIDField.setDisabledTextColor(Color.DARK_GRAY);
                paymentIDField.setText(rs.getString("PaymentID")); // from ResultSet

                billIDField = new RTextField(String.valueOf(rs.getInt("BillID")), 30);
                billIDField.setFont(p14);
                billIDField.setEnabled(false);
                billIDField.setDisabledTextColor(Color.DARK_GRAY);
                billIDField.setText(rs.getString("BillID"));

                pModeField = new RTextField(String.valueOf(rs.getString("PaymentMode")), 30);
                pModeField.setFont(p14);
                pModeField.setEnabled(false);
                pModeField.setDisabledTextColor(Color.DARK_GRAY);
                pModeField.setText(rs.getString("PaymentMode"));

                dateTimeSpinner = new DateTimeSpinner();
                dateTimeSpinner.setFont(p14);
                dateTimeSpinner.setEnabled(false);
                dateTimeSpinner.setValue(rs.getTimestamp("DateTime"));

                RPanel dateTimePanel = new RPanel(30);
                dateTimePanel.setLayout(new BorderLayout());
                dateTimePanel.add(dateTimeSpinner);
                dateTimePanel.setBackground(Color.WHITE);
                dateTimePanel.setBorder(b);

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
                miniPanel.add(paymentIDLabel, gbc);
                gbc.gridy++;
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
                miniPanel.add(pModeLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(dTimeLabel, gbc);
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
                miniPanel.add(paymentIDField, gbc);
                gbc.gridy++;
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
                miniPanel.add(pModeField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(dateTimePanel, gbc);

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
                    try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM Payment WHERE PaymentID = ?")) {
                        deleteStmt.setString(1, inputID);
                        deleteStmt.executeUpdate();
                        JOptionPane.showMessageDialog(deleteDialog, "Payment No. " + inputID + " was deleted successfully.");
                        retrievePaymentTableData();
                        deleteDialog.dispose();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(deleteDialog, "Delete failed: " + ex.getMessage());
                    }
                });
                deleteDialog.add(mainPanel, BorderLayout.CENTER);
                deleteDialog.setLocationRelativeTo(null);
                deleteDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Payment ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updatePayment() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Payment ID to Update:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Payment ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM PAYMENT WHERE PaymentID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            int paymentIDD = Integer.parseInt(inputID);
            stmt.setInt(1, paymentIDD);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog updateDialog = new JDialog((Frame) null, "Update Payment", true);
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

                JLabel title = new JLabel("Update Payment");
                title.setFont(b18);
                title.setForeground(Color.WHITE);
                title.setHorizontalAlignment(JLabel.CENTER);

                titlePanel.add(title, BorderLayout.CENTER);

                // Fields
                paymentIDField = new RTextField(String.valueOf(rs.getInt("PaymentID")), 30);
                paymentIDField.setFont(p14);
                paymentIDField.setEnabled(false);
                paymentIDField.setDisabledTextColor(Color.DARK_GRAY);
                paymentIDField.setText(rs.getString("PaymentID"));

                billIDField = new RTextField(String.valueOf(rs.getInt("BillID")), 30);
                billIDField.setFont(p14);
                billIDField.setEnabled(false);
                billIDField.setDisabledTextColor(Color.DARK_GRAY);
                billIDField.setText(rs.getString("BillID"));

                pModeField = new RTextField(String.valueOf(rs.getString("PaymentMode")), 30);
                pModeField.setFont(p14);
                pModeField.setEnabled(false);
                pModeField.setText(rs.getString("PaymentMode"));

                dateTimeSpinner = new DateTimeSpinner();
                dateTimeSpinner.setFont(p14);
                dateTimeSpinner.setEnabled(true);  // Allowing modification
                dateTimeSpinner.setValue(rs.getTimestamp("DateTime"));

                RPanel dateTimePanel = new RPanel(30);
                dateTimePanel.setLayout(new BorderLayout());
                dateTimePanel.add(dateTimeSpinner);
                dateTimePanel.setBackground(Color.WHITE);
                dateTimePanel.setBorder(b);

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
                miniPanel.add(paymentIDLabel, gbc);
                gbc.gridy++;
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
                miniPanel.add(pModeLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(dTimeLabel, gbc);
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
                miniPanel.add(paymentIDField, gbc);
                gbc.gridy++;
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
                miniPanel.add(pModeField, gbc);
                gbc.gridy++;
                errorPMode = new JLabel("");
                errorPMode.setFont(p11);
                errorPMode.setForeground(Color.red);
                miniPanel.add(errorPMode, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(dateTimePanel, gbc);
                gbc.gridy++;
                errorDTime = new JLabel("");
                errorDTime.setFont(p11);
                errorDTime.setForeground(Color.red);
                miniPanel.add(errorDTime, gbc);
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
                    String dTimeDate = dateTimeSpinner.getDate();

                    errorPMode.setText("");
                    errorDTime.setText("");
                    billIDField.setForeground(Color.black);
                    pModeField.setForeground(Color.black);
                    dateTimeSpinner.setForeground(Color.black);

                    // Check if required fields are empty or default
                    if (dTimeDate.isEmpty()) {
                        JOptionPane.showMessageDialog(updateDialog, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String sqlU = "UPDATE PAYMENT SET DateTime = ? WHERE PaymentID = ?";

                    try (PreparedStatement updateStmt = conn.prepareStatement(sqlU)) {
                        try {
                            Date selectedDate = (Date) dateTimeSpinner.getValue();
                            updateStmt.setTimestamp(1, new java.sql.Timestamp(selectedDate.getTime()));
                        } catch (ClassCastException | NullPointerException ex) {
                            JOptionPane.showMessageDialog(updateDialog, "Invalid or missing date value.", "Date Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        updateStmt.setInt(2, paymentIDD);

                        int rowsUpdated = updateStmt.executeUpdate();

                        if (rowsUpdated > 0) {
                            JOptionPane.showMessageDialog(updateDialog, "Payment updated successfully.");
                            updateDialog.dispose();
                            retrievePaymentTableData();
                        } else {
                            JOptionPane.showMessageDialog(updateDialog, "No payment found with that ID.", "Update Failed", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(updateDialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                updateDialog.setLocationRelativeTo(null);
                updateDialog.add(mainPanel, BorderLayout.CENTER);
                updateDialog.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Payment ID not found.");
            }

        } catch (SQLException ea) {
            JOptionPane.showMessageDialog(this, "Error: " + ea.getMessage());
        } catch (NumberFormatException ae) {
            JOptionPane.showMessageDialog(null, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchPayment() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Payment ID to search:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Payment ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM Payment WHERE PaymentID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog searchDialog = new JDialog((Frame) null, "Payment No. " + inputID, true);
                searchDialog.setSize(470, 420);
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
                JLabel title = new JLabel("Search Payment");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                // Form Panel
                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);

                // Fields (retrieved from database)
                //paymentIDField = new RTextField(String.valueOf(rs.getInt("PaymentID")),30);
                paymentIDField = new RTextField(rs.getString("PaymentID"), 30);
                paymentIDField.setFont(p14);
                paymentIDField.setEnabled(false);
                paymentIDField.setDisabledTextColor(Color.DARK_GRAY);
                //paymentIDField.setText(rs.getString("PaymentID")); // from ResultSet

                billIDField = new RTextField(String.valueOf(rs.getInt("BillID")), 30);
                billIDField.setFont(p14);
                billIDField.setEnabled(false);
                billIDField.setDisabledTextColor(Color.DARK_GRAY);
                billIDField.setText(rs.getString("BillID"));

                //pModeField = new RTextField(String.valueOf(rs.getDouble("PaymentMode")),30);
                pModeField = new RTextField(rs.getString("PaymentMode"), 30);
                pModeField.setFont(p14);
                pModeField.setEnabled(false);
                pModeField.setDisabledTextColor(Color.DARK_GRAY);
                pModeField.setText(rs.getString("PaymentMode"));

                dateTimeSpinner = new DateTimeSpinner();
                dateTimeSpinner.setFont(p14);
                dateTimeSpinner.setEnabled(false);
                dateTimeSpinner.setValue(rs.getTimestamp("DateTime"));

                RPanel dateTimePanel = new RPanel(30);
                dateTimePanel.setLayout(new BorderLayout());
                dateTimePanel.add(dateTimeSpinner);
                dateTimePanel.setBackground(Color.WHITE);
                dateTimePanel.setBorder(b);

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
                miniPanel.add(paymentIDLabel, gbc);
                gbc.gridy++;
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
                miniPanel.add(pModeLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(dTimeLabel, gbc);
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
                miniPanel.add(paymentIDField, gbc);
                gbc.gridy++;
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
                miniPanel.add(pModeField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(dateTimePanel, gbc);

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
                JOptionPane.showMessageDialog(this, "Payment not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        retrievePaymentTableData();

    }

    private void likeSearchPayments() {
        String[] fields = {"PaymentID", "BillID", "DateTime", "PaymentMode"};
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

            String sql = "SELECT * FROM PAYMENT WHERE " + field + " LIKE ?";
            try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, keyword);
                try (ResultSet rs = stmt.executeQuery()) {
                    tableModel.setRowCount(0); // clear table

                    while (rs.next()) {
                        tableModel.addRow(new Object[]{
                            rs.getString("PaymentID"),
                            rs.getString("BillID"),
                            rs.getString("DateTime"),
                            rs.getString("PaymentMode")
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

    private void sortPayments() {
        String[] fields = {"PaymentID", "BillID", "DateTime", "PaymentMode"};
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

        String sql = "SELECT * FROM PAYMENT ORDER BY " + field + " " + order;

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            tableModel.setRowCount(0); // clear table

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("PaymentID"),
                    rs.getString("BillID"),
                    rs.getString("DateTime"),
                    rs.getString("PaymentMode")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error sorting: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void retrievePaymentTableData() {
        String sql = "SELECT * FROM Payment";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            // Clear existing rows in the table model
            DefaultTableModel model = (DefaultTableModel) PaymentTable.getModel();
            model.setRowCount(0); // Clear old rows

            // Fill the table with fresh data
            while (rs.next()) {
                Object[] row = {
                    rs.getString("PaymentID"),
                    rs.getString("BillID"),
                    rs.getString("DateTime"),
                    rs.getString("PaymentMode")
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading table data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void printReceipt() {
        String input = JOptionPane.showInputDialog(null, "Enter Payment ID:", "Receipt Lookup", JOptionPane.QUESTION_MESSAGE);

        // Check if input was cancelled or left blank
        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Payment ID is required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int paymentID = Integer.parseInt(input.trim());

            // You could optionally validate this against the database here before proceeding
            CashierGUI.printReceipt(paymentID);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Payment ID. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}
