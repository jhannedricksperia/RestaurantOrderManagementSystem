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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BillGUI extends Components {

    private RTextField billIDField, orderIDField, vAmountField, vSalesField, tAmountField;
    private String[] bStatusF = {"SELECT", "PAID", "UNPAID", "CANCELLED"};
    private JComboBox<String> bStatusCmb = new CustomComboBox<>(bStatusF);
    private RPanel bStatusPanel = new RPanel(30);
    private JLabel errorOrder, errorvAmount, errorDTime, errorvSales, errortAmount, errorbStatus;
    private DateTimeSpinner dateTimeS = new DateTimeSpinner();
    private RPanel dateTimePanel = new RPanel(30);
    private final DefaultTableModel tableModel = new DisabledTableModel(new String[]{"BillID ", "OrderID ", "DateTime", "VATAmount", "VATSales", "TotalAmount", "BStatus"}, 0);
    private final JTable billTable = Components.updateTable(new JTable(tableModel));
    private int nextID;
    private JLabel billIDLabel = new JLabel("Bill ID:");
    private JLabel orderIDLabel = new JLabel("Order ID:");
    private JLabel dTimeLabel = new JLabel("Date and Time:");
    private JLabel vAmountLabel = new JLabel("Vat Amount:");
    private JLabel vSalesLabel = new JLabel("Vat Sales:");
    private JLabel tAmountLabel = new JLabel("Total Amount:");
    private JLabel bStatusLabel = new JLabel("Bill Status:");
    private JPanel billPanel;

    public BillGUI() {
        billIDLabel.setFont(p14);
        orderIDLabel.setFont(p14);
        dTimeLabel.setFont(p14);
        vAmountLabel.setFont(p14);
        vSalesLabel.setFont(p14);
        tAmountLabel.setFont(p14);
        bStatusLabel.setFont(p14);

        //Make the columns right alligned
        DefaultTableCellRenderer rightRenderer = new CustomRowRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        //Make the columns centered alligned
        DefaultTableCellRenderer centerRenderer = new CustomRowRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        //RPanels
        bStatusPanel.setLayout(new BorderLayout());
        bStatusPanel.setBorder(b);
        bStatusPanel.setBackground(Color.WHITE);
        bStatusCmb.setFont(p14);
        bStatusCmb.setForeground(Color.BLACK);
        bStatusPanel.add(bStatusCmb, BorderLayout.CENTER);
        bStatusCmb.setBorder(null);

        dateTimePanel.setLayout(new BorderLayout());
        dateTimePanel.setBorder(b);
        dateTimePanel.setBackground(Color.WHITE);
        dateTimeS.setForeground(Color.BLACK);
        dateTimeS.setFont(p14);
        dateTimePanel.add(dateTimeS, BorderLayout.CENTER);
        dateTimeS.setBorder(null);

        // Create the table
        billTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // BillID
        billTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // OrderID
        billTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // DateTime
        billTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); // BStatus

        billTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer); // VATAmount
        billTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer); // VATSales
        billTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer); // TotalAmount

        billPanel = new JPanel(new BorderLayout());//Rename based on your assigned database
        billPanel.setOpaque(false);
        billPanel.setBorder(b);

        //Retrieved data from the database
        retrieveBillTableData();
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
                likeSearchBill();
            } else if (choice == 2) {
                retrieveBillTableData();
            }
        });

        JButton refreshBtn = new BtnCustom(new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/refresh.png")).getImage()), new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/refreshGreen.png")).getImage()), "Refresh");
        refreshBtn.addActionListener(e -> {
            retrieveBillTableData();
        });

        //Panel for the table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(b);
        billTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        billTable.setFillsViewportHeight(true);
        JScrollPane tableScrollPane = new CScrollPane(billTable);
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

        createBtn.addActionListener(e -> createBill());
        updateBtn.addActionListener(e -> updateBill());
        deleteBtn.addActionListener(e -> deleteBill());
        searchBtn.addActionListener(e -> searchBill());

        tablePanel.setPreferredSize(new Dimension(900, 0));
        buttonsPanel.setPreferredSize(new Dimension(0, 100));
        billPanel.add(tablePanel, BorderLayout.CENTER);
        billPanel.add(buttonsPanel, BorderLayout.SOUTH);
        billPanel.add(headPanel, BorderLayout.NORTH);
        headPanel.add(refreshBtn);
        headPanel.add(filterBtn);

    }

    public JPanel getBillPanel() {
        return billPanel;
    }

    private void createBill() {

        JDialog createDialog = new JDialog((Frame) null, "Create Bill", true);
        createDialog.setSize(470, 620);
        createDialog.setLayout(new BorderLayout());
        createDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
        mainPanel.setLayout(new BorderLayout());

        // Title
        JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/green.jpg")).getImage());
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(3, 4, 94));
        titlePanel.setBorder(b);

        JLabel title = new JLabel("Create Bill");
        title.setFont(b18);
        title.setForeground(Color.white);
        title.setHorizontalAlignment(JLabel.CENTER);

        titlePanel.add(title, BorderLayout.CENTER);

        // Form Panel
        JPanel miniPanel = new JPanel(new GridBagLayout());
        miniPanel.setOpaque(false);
        miniPanel.setBorder(b);

        // Input Fields
        billIDField = new RTextField(30);
        billIDField.setEnabled(false);
        orderIDField = new RTextField("(Required)", 30);
        vAmountField = new RTextField("0.00 (Required)", 30);
        vSalesField = new RTextField("0.00 (Required)", 30);
        tAmountField = new RTextField("0.00 (Required)", 30);

        dateTimeS.setForeground(Color.BLACK);
        dateTimeS.setEnabled(true);
        bStatusCmb.setForeground(Color.BLACK);
        bStatusCmb.setEnabled(true);

        billIDField.setFont(p14);
        orderIDField.setFont(p14);
        vAmountField.setFont(p14);
        vSalesField.setFont(p14);
        tAmountField.setFont(p14);

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
        miniPanel.add(orderIDLabel, gbc);
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
        gbc.gridy++;
        miniPanel.add(vAmountLabel, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(vSalesLabel, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(tAmountLabel, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(bStatusLabel, gbc);
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
        nextID = Database.getNextID("BILL");
        if (nextID != -1) {
            billIDField.setText(String.valueOf(nextID));
            billIDField.setEnabled(false);
            billIDField.setDisabledTextColor(Color.BLACK);
        }
        miniPanel.add(billIDField, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(orderIDField, gbc);
        gbc.gridy++;
        errorOrder = new JLabel("");
        errorOrder.setFont(p11);
        errorOrder.setForeground(Color.red);
        miniPanel.add(errorOrder, gbc);
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
        gbc.gridy++;
        miniPanel.add(vAmountField, gbc);
        gbc.gridy++;
        errorvAmount = new JLabel("");
        errorvAmount.setFont(p11);
        errorvAmount.setForeground(Color.red);
        miniPanel.add(errorvAmount, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(vSalesField, gbc);
        gbc.gridy++;
        errorvSales = new JLabel("");
        errorvSales.setFont(p11);
        errorvSales.setForeground(Color.red);
        miniPanel.add(errorvSales, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(tAmountField, gbc);
        gbc.gridy++;
        errortAmount = new JLabel("");
        errortAmount.setFont(p11);
        errortAmount.setForeground(Color.red);
        miniPanel.add(errortAmount, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        miniPanel.add(bStatusPanel, gbc);
        gbc.gridy++;
        errorbStatus = new JLabel("");
        errorbStatus.setFont(p11);
        errorbStatus.setForeground(Color.red);
        miniPanel.add(errorbStatus, gbc);
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
            String orderIDText = orderIDField.getText().trim();
            String dateTimeText = dateTimeS.getDateTime().trim();
            String vAmountText = vAmountField.getText().trim();
            String salesText = vSalesField.getText().trim();
            String tAmountText = tAmountField.getText().trim();
            String bstatusText = bStatusCmb.getSelectedItem().toString();

            errorOrder.setText("");
            errorvAmount.setText("");
            errorDTime.setText("");
            errorvSales.setText("");
            errortAmount.setText("");
            errorbStatus.setText("");

            orderIDField.setForeground(Color.black);
            dateTimeS.setForeground(Color.black);
            vAmountField.setForeground(Color.black);
            vSalesField.setForeground(Color.black);
            tAmountField.setForeground(Color.black);
            bStatusCmb.setForeground(Color.black);

            // Check if required fields are empty or default
            if (orderIDText.isEmpty() || orderIDText.equalsIgnoreCase("(Required)")
                    || vAmountText.isEmpty() || vAmountText.equalsIgnoreCase("0.00 (Required)")
                    || salesText.isEmpty() || salesText.equalsIgnoreCase("0.00 (Required)")
                    || tAmountText.isEmpty() || tAmountText.equalsIgnoreCase("0.00 (Required)")
                    || bstatusText.isEmpty() || bstatusText.equalsIgnoreCase("(Required)")) {
                JOptionPane.showMessageDialog(createDialog, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);

                if (orderIDText.isEmpty() || orderIDText.equalsIgnoreCase("(Required)")) {
                    errorOrder.setText("Order ID is required.");
                    orderIDField.setForeground(Color.red);
                }

                if (vAmountText.isEmpty() || vAmountText.equalsIgnoreCase("0.00 (Required)")) {
                    errorvAmount.setText("Vat Amount is required.");
                    vAmountField.setForeground(Color.red);
                }

                if (salesText.isEmpty() || salesText.equalsIgnoreCase("0.00 (Required)")) {
                    errorvSales.setText("Vat Sales is required.");
                    vSalesField.setForeground(Color.red);
                }

                if (tAmountText.isEmpty() || tAmountText.equalsIgnoreCase("0.00 (Required)")) {
                    errortAmount.setText("Total Amount is required.");
                    tAmountField.setForeground(Color.red);
                }

                if (bstatusText.isEmpty() || bstatusText.equalsIgnoreCase("(Required)")) {
                    errorbStatus.setText("Bill Status is required.");
                    bStatusCmb.setForeground(Color.red);
                }

                return; // Stop further execution if there's any error
            }

            try {
                int orderID = Integer.parseInt(orderIDText);
                String dateTime = dateTimeText;
                double vAmount = Double.parseDouble(vAmountText);
                double vSales = Double.parseDouble(salesText);
                double tAmount = Double.parseDouble(tAmountText);
                String bStatus = bstatusText;

                // Additional error checks
                if (vAmount <= 0) {
                    errorvAmount.setText("VAT Amount must be greater than 0.");
                    vAmountField.setForeground(Color.red);
                    return;
                }

                if (vSales <= 0) {
                    errorvSales.setText("VAT Sales must be greater than 0.");
                    vSalesField.setForeground(Color.red);
                    return;
                }

                if (tAmount <= 0) {
                    errortAmount.setText("Total Amount must be greater than 0.");
                    tAmountField.setForeground(Color.red);
                    return;
                }

                if (vAmount > vSales) {
                    errorvAmount.setText("VAT Amount cannot be more than VAT Sales.");
                    vAmountField.setForeground(Color.red);
                    return;
                }

                if (vAmount > tAmount) {
                    errorvAmount.setText("VAT Amount cannot be more than Total Amount.");
                    vAmountField.setForeground(Color.red);
                    return;
                }

                String sql = "INSERT INTO BILL (OrderID , DateTime, VATAmount, VATSales, TotalAmount, BStatus) VALUES (?, ?, ?, ?, ?, ?)";

                try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, orderID);
                    stmt.setString(2, dateTime);
                    stmt.setDouble(3, vAmount);
                    stmt.setDouble(4, vSales);
                    stmt.setDouble(5, tAmount);
                    stmt.setString(6, bStatus);
                    stmt.executeUpdate();
                    nextID = Database.getNextID("CASHPAYMENT");
                    if (nextID != -1) {
                        billIDField.setText(String.valueOf(nextID));
                        billIDField.setEnabled(false);
                        billIDField.setDisabledTextColor(Color.BLACK);
                    }
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Bill has successfully been added", "Bill updated successfully", JOptionPane.INFORMATION_MESSAGE);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            } catch (NumberFormatException ae) {
                JOptionPane.showMessageDialog(createDialog, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            retrieveBillTableData();
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
        nextID = Database.getNextID("BILL");
        if (nextID != -1) {
            billIDField.setText(String.valueOf(nextID));
        }
        orderIDField.setText("");
        vAmountField.setText("");
        vSalesField.setText("");
        tAmountField.setText("");

        orderIDField.setForeground(Color.black);
        vAmountField.setForeground(Color.black);
        vSalesField.setForeground(Color.black);
        tAmountField.setForeground(Color.black);
        bStatusCmb.setForeground(Color.black);
        bStatusCmb.setSelectedIndex(0);
        dateTimeS.setForeground(Color.BLACK);
        dateTimeS.setValue(new Date());

        orderIDField.setDefault();
        vAmountField.setDefault();
        vSalesField.setDefault();
        tAmountField.setDefault();
    }

    private void deleteBill() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Bill ID to Delete:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Bill ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM BILL WHERE BillID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog deleteDialog = new JDialog((Frame) null, "Delete Bill", true);
                deleteDialog.setSize(470, 620);
                deleteDialog.setLayout(new BorderLayout());
                deleteDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                // Main Panel with background
                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title Panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/red.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);
                JLabel title = new JLabel("Delete Bill");
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
                billIDField.setDisabledTextColor(Color.BLACK);
                billIDField.setText(rs.getString("BillID")); // from ResultSet

                orderIDField = new RTextField(String.valueOf(rs.getDouble("OrderID")), 30);
                orderIDField.setFont(p14);
                orderIDField.setEnabled(false);
                orderIDField.setDisabledTextColor(Color.BLACK);
                orderIDField.setText(rs.getString("OrderID"));

                dateTimeS.setEnabled(false);
                dateTimeS.setValue(rs.getTimestamp("DateTime"));

                vAmountField = new RTextField(String.valueOf(rs.getDouble("VATAmount")), 30);
                vAmountField.setFont(p14);
                vAmountField.setEnabled(false);
                vAmountField.setDisabledTextColor(Color.BLACK);
                vAmountField.setText(rs.getString("VATAmount"));

                vSalesField = new RTextField(String.valueOf(rs.getDouble("VATSales")), 30);
                vSalesField.setFont(p14);
                vSalesField.setEnabled(false);
                vSalesField.setDisabledTextColor(Color.BLACK);
                vSalesField.setText(rs.getString("VATSales"));

                tAmountField = new RTextField(String.valueOf(rs.getDouble("TotalAmount")), 30);
                tAmountField.setFont(p14);
                tAmountField.setEnabled(false);
                tAmountField.setDisabledTextColor(Color.BLACK);
                tAmountField.setText(rs.getString("TotalAmount"));

                bStatusCmb.setEnabled(false);
                bStatusCmb.setSelectedItem(rs.getString("BStatus"));

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
                miniPanel.add(orderIDLabel, gbc);
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
                gbc.gridy++;
                miniPanel.add(vAmountLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(vSalesLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(tAmountLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(bStatusLabel, gbc);
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
                miniPanel.add(orderIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(dateTimePanel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(vAmountField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(vSalesField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(tAmountField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(bStatusPanel, gbc);

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
                    try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM Bill WHERE BillID = ?")) {
                        deleteStmt.setString(1, inputID);
                        deleteStmt.executeUpdate();
                        JOptionPane.showMessageDialog(deleteDialog, "Bill No. " + inputID + " was deleted successfully.");
                        retrieveBillTableData();
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

    private void updateBill() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Bill ID to Update:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Bill ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM BILL WHERE BillID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            int billID = Integer.parseInt(inputID);
            stmt.setInt(1, billID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog updateDialog = new JDialog((Frame) null, "Update Bill", true);
                updateDialog.setSize(470, 620);
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
                billIDField.setDisabledTextColor(Color.BLACK);
                billIDField.setText(rs.getString("BillID")); // from ResultSet

                orderIDField = new RTextField(String.valueOf(rs.getDouble("OrderID")), 30);
                orderIDField.setFont(p14);
                orderIDField.setEnabled(false);
                orderIDField.setDisabledTextColor(Color.BLACK);
                orderIDField.setText(rs.getString("OrderID"));

                dateTimeS.setFont(p14);
                dateTimeS.setEnabled(true);
                dateTimeS.setValue(rs.getTimestamp("DateTime"));

                vAmountField = new RTextField(String.valueOf(rs.getDouble("VATAmount")), 30);
                vAmountField.setFont(p14);
                vAmountField.setEnabled(true);
                vAmountField.setDisabledTextColor(Color.BLACK);
                vAmountField.setText(rs.getString("VATAmount"));

                vSalesField = new RTextField(String.valueOf(rs.getDouble("VATSales")), 30);
                vSalesField.setFont(p14);
                vSalesField.setEnabled(true);
                vSalesField.setDisabledTextColor(Color.BLACK);
                vSalesField.setText(rs.getString("VATSales"));

                tAmountField = new RTextField(String.valueOf(rs.getDouble("TotalAmount")), 30);
                tAmountField.setFont(p14);
                tAmountField.setEnabled(true);
                tAmountField.setDisabledTextColor(Color.BLACK);
                tAmountField.setText(rs.getString("TotalAmount"));

                bStatusCmb.setEnabled(true);
                bStatusCmb.setSelectedItem(rs.getString("BStatus"));

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
                miniPanel.add(orderIDLabel, gbc);
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
                gbc.gridy++;
                miniPanel.add(vAmountLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(vSalesLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(tAmountLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(bStatusLabel, gbc);
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
                miniPanel.add(orderIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
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
                gbc.gridy++;
                miniPanel.add(vAmountField, gbc);
                gbc.gridy++;
                errorvAmount = new JLabel("");
                errorvAmount.setFont(p11);
                errorvAmount.setForeground(Color.red);
                miniPanel.add(errorvAmount, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(vSalesField, gbc);
                gbc.gridy++;
                errorvSales = new JLabel("");
                errorvSales.setFont(p11);
                errorvSales.setForeground(Color.red);
                miniPanel.add(errorvSales, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(tAmountField, gbc);
                gbc.gridy++;
                errortAmount = new JLabel("");
                errortAmount.setFont(p11);
                errortAmount.setForeground(Color.red);
                miniPanel.add(errortAmount, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(bStatusPanel, gbc);
                gbc.gridy++;
                errorbStatus = new JLabel("");
                errorbStatus.setFont(p11);
                errorbStatus.setForeground(Color.red);
                miniPanel.add(errorbStatus, gbc);
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
                    String orderIDText = orderIDField.getText().trim();
                    String dateTimeText = dateTimeS.getDateTime().trim();
                    String vAmountText = vAmountField.getText().trim();
                    String salesText = vSalesField.getText().trim();
                    String tAmountText = tAmountField.getText().trim();
                    String bstatusText = bStatusCmb.getSelectedItem().toString();

                    errorOrder.setText("");
                    errorvAmount.setText("");
                    errorDTime.setText("");
                    errorvSales.setText("");
                    errortAmount.setText("");
                    errorbStatus.setText("");

                    orderIDField.setForeground(Color.black);
                    dateTimeS.setForeground(Color.black);
                    vAmountField.setForeground(Color.black);
                    vSalesField.setForeground(Color.black);
                    tAmountField.setForeground(Color.black);
                    bStatusCmb.setForeground(Color.black);

                    // Check if required fields are empty or default
                    if (orderIDText.isEmpty() || dateTimeText.isEmpty() || vAmountText.isEmpty() || salesText.isEmpty() || tAmountText.isEmpty() || bstatusText.isEmpty()) {
                        JOptionPane.showMessageDialog(updateDialog, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        int orderID = Integer.parseInt(orderIDText);
                        String dateTime = dateTimeText;
                        double vAmount = Double.parseDouble(vAmountText);
                        double vSales = Double.parseDouble(salesText);
                        double tAmount = Double.parseDouble(tAmountText);
                        String bStatus = bstatusText;

                        if (vAmount <= 0) {
                            errorvAmount.setText("VAT Amount must be greater than 0.");
                            vAmountField.setForeground(Color.red);
                            return;
                        }

                        if (vSales <= 0) {
                            errorvSales.setText("VAT Sales must be greater than 0.");
                            vSalesField.setForeground(Color.red);
                            return;
                        }

                        if (tAmount <= 0) {
                            errortAmount.setText("Total Amount must be greater than 0.");
                            tAmountField.setForeground(Color.red);
                            return;
                        }

                        if (vAmount > vSales) {
                            errorvAmount.setText("VAT Amount cannot be more than VAT Sales.");
                            vAmountField.setForeground(Color.red);
                            return;
                        }

                        if (vAmount > tAmount) {
                            errorvAmount.setText("VAT Amount cannot be more than Total Amount.");
                            vAmountField.setForeground(Color.red);
                            return;
                        }

                        // Update the database
                        try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE BILL SET OrderID = ?, DateTime = ?, VATAmount = ?, VATSales = ?, TotalAmount = ?, BStatus = ? WHERE BillID = ?")) {
                            updateStmt.setInt(1, orderID);
                            updateStmt.setString(2, dateTime);
                            updateStmt.setDouble(3, vAmount);
                            updateStmt.setDouble(4, vSales);
                            updateStmt.setDouble(5, tAmount);
                            updateStmt.setString(6, bStatus);
                            updateStmt.setInt(7, Integer.parseInt(inputID)); // Use the input ID for update
                            updateStmt.executeUpdate();
                            JOptionPane.showMessageDialog(updateDialog, "Bill No. " + inputID + " was updated.");
                            retrieveBillTableData(); // Refresh data
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

    private void searchBill() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Bill ID to search:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Bill ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM BILL WHERE BillID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog searchDialog = new JDialog((Frame) null, "Bill No. " + inputID, true);
                searchDialog.setSize(470, 620);
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
                billIDField.setDisabledTextColor(Color.BLACK);
                billIDField.setText(rs.getString("BillID")); // from ResultSet

                orderIDField = new RTextField(String.valueOf(rs.getDouble("OrderID")), 30);
                orderIDField.setFont(p14);
                orderIDField.setEnabled(false);
                orderIDField.setDisabledTextColor(Color.BLACK);
                orderIDField.setText(rs.getString("OrderID"));

                dateTimeS.setEnabled(false);
                dateTimeS.setValue(rs.getTimestamp("DateTime"));

                vAmountField = new RTextField(String.valueOf(rs.getDouble("VATAmount")), 30);
                vAmountField.setFont(p14);
                vAmountField.setEnabled(false);
                vAmountField.setDisabledTextColor(Color.BLACK);
                vAmountField.setText(rs.getString("VATAmount"));

                vSalesField = new RTextField(String.valueOf(rs.getDouble("VATSales")), 30);
                vSalesField.setFont(p14);
                vSalesField.setEnabled(false);
                vSalesField.setDisabledTextColor(Color.BLACK);
                vSalesField.setText(rs.getString("VATSales"));

                tAmountField = new RTextField(String.valueOf(rs.getDouble("TotalAmount")), 30);
                tAmountField.setFont(p14);
                tAmountField.setEnabled(false);
                tAmountField.setDisabledTextColor(Color.BLACK);
                tAmountField.setText(rs.getString("TotalAmount"));

                bStatusCmb.setEnabled(false);
                //bStatusField.setDisabledTextColor(Color.BLACK);
                bStatusCmb.setSelectedItem(rs.getString("BStatus"));

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
                miniPanel.add(orderIDLabel, gbc);
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
                gbc.gridy++;
                miniPanel.add(vAmountLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(vSalesLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(tAmountLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(bStatusLabel, gbc);
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
                miniPanel.add(orderIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(dateTimePanel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(vAmountField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(vSalesField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(tAmountField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(bStatusPanel, gbc);

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
        retrieveBillTableData();

    }

    private void likeSearchBill() {
        String[] fields = {"BillID", "OrderID", "DateTime", "VATAmount", "VATSales", "TotalAmount", "BStatus"};
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

            String sql = "SELECT * FROM BILL WHERE " + field + " LIKE ?";
            try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, keyword);
                try (ResultSet rs = stmt.executeQuery()) {
                    tableModel.setRowCount(0); // clear table

                    while (rs.next()) {
                        tableModel.addRow(new Object[]{
                            rs.getString("BillID"),
                            rs.getString("OrderID"),
                            rs.getString("DateTime"),
                            rs.getString("VATAmount"),
                            rs.getString("VATSales"),
                            rs.getString("TotalAmount"),
                            rs.getString("BStatus")
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
        String[] fields = {"BillID", "OrderID", "DateTime", "VATAmount", "VATSales", "TotalAmount", "BStatus"};
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

        String sql = "SELECT * FROM BILL ORDER BY " + field + " " + order;

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            tableModel.setRowCount(0); // clear table

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("BillID"),
                    rs.getString("OrderID"),
                    rs.getString("DateTime"),
                    rs.getString("VATAmount"),
                    rs.getString("VATSales"),
                    rs.getString("TotalAmount"),
                    rs.getString("BStatus")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error sorting: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void retrieveBillTableData() {
        String sql = "SELECT * FROM BILL";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            // Clear existing rows in the table model
            DefaultTableModel model = (DefaultTableModel) billTable.getModel();
            model.setRowCount(0); // Clear old rows

            // Fill the table with fresh data
            while (rs.next()) {
                Object[] row = {
                    rs.getString("BillID"),
                    rs.getString("OrderID"),
                    rs.getString("DateTime"),
                    rs.getString("VATAmount"),
                    rs.getString("VATSales"),
                    rs.getString("TotalAmount"),
                    rs.getString("BStatus")
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading table data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
