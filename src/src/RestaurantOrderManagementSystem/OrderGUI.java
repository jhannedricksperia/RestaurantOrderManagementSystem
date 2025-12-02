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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class OrderGUI extends Components {

    private RTextField orderIdField = new RTextField("", 30);
    private RTextField userIdField = new RTextField("Required", 30);
    private RTextField custIdField = new RTextField("Required", 30);
    private String[] orderType = {"SELECT", "DINE-IN", "TAKE-OUT"};
    private JComboBox<String> oTypes = new CustomComboBox<>(orderType);
    private RPanel oType = new RPanel(30);
    private RTextField sAmountField = new RTextField("Required", 30);
    private DateTimeSpinner dateTimes = new DateTimeSpinner();
    private RPanel dateTime = new RPanel(30);
    private String[] orderStat = {"SELECT", "DONE", "PENDING", "CANCELLED"};
    private JComboBox<String> oStatuss = new CustomComboBox<>(orderStat);
    private RPanel oStatus = new RPanel(30);

    private JLabel errorUId, errorCId, errorOtype, errorSA, errorDate, errorOstat;
    private JLabel orderIdFieldlbl = new JLabel("Order ID");
    private JLabel userIdFieldlbl = new JLabel("User ID");
    private JLabel custIdFieldlbl = new JLabel("Customer ID");
    private JLabel orderTypelbl = new JLabel("Order Type");
    private JLabel sAmountFieldlbl = new JLabel("Sub Total");
    private JLabel dateTimelbl = new JLabel("Date and Time");
    private JLabel orderStatlbl = new JLabel("Order Status");
    private final String[] ocolumnNames = {"Order ID", "User ID", "Customer ID", "Order Type", "Sub Total Amount", "Date & Time", "Order Status"};
    private final DefaultTableModel orderTableModel = new DisabledTableModel(ocolumnNames, 0);
    public JTable orderTable = new JTable(orderTableModel);
    private final JPanel orderPanel;

    public OrderGUI() {

        // Retrieve table data
        retrieveOrderTableData();

        DefaultTableCellRenderer centerRenderer = new CustomRowRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer rightRenderer = new CustomRowRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        orderTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Order ID
        orderTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // User ID
        orderTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Customer ID
        orderTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Order Type
        orderTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);  // Sub Total Amount
        orderTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Date & Time
        orderTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); // Order Status
        // Main panel setup
        orderPanel = new JPanel(new BorderLayout());
        orderPanel.setBorder(b);
        orderPanel.setOpaque(false);

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
                sortOrders();
            } else if (choice == 1) {
                likeSearchOrders();
            } else if (choice == 2) {
                retrieveOrderTableData();
            }
        });

        JButton refreshBtn = new BtnCustom(
                new ImageIcon(getClass().getResource("/images/icon/refresh.png")),
                new ImageIcon(getClass().getResource("/images/icon/refreshGreen.png")),
                "Refresh"
        );
        refreshBtn.addActionListener(e -> retrieveOrderTableData());

        headPanel.add(refreshBtn);
        headPanel.add(filterBtn);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(b);
        JScrollPane tableScrollPane = new CScrollPane(orderTable()); // assuming orderTable() returns JTable
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
        createButton.addActionListener(e -> orderCreate());
        updateButton.addActionListener(e -> orderUpdate());
        searchButton.addActionListener(e -> orderSearch());
        deleteButton.addActionListener(e -> orderDelete());

        //Udating componens GUI
        oType.setBorder(b);
        oStatus.setBorder(b);
        dateTime.setBorder(b);

        oType.setLayout(new BorderLayout());
        oType.setBackground(Color.white);
        oTypes.setBackground(Color.white);
        oType.add(oTypes, BorderLayout.CENTER);
        oTypes.setBorder(null);

        dateTime.setLayout(new BorderLayout());
        dateTime.setBackground(Color.white);
        dateTimes.setBackground(Color.white);
        dateTime.add(dateTimes, BorderLayout.CENTER);
        dateTimes.setBorder(null);

        oStatus.setLayout(new BorderLayout());
        oStatus.setBackground(Color.white);
        oStatuss.setBackground(Color.white);
        oStatus.add(oStatuss, BorderLayout.CENTER);
        oStatuss.setBorder(null);

        oType.setPreferredSize(new Dimension(200, 40));
        oStatus.setPreferredSize(new Dimension(200, 40));
        dateTime.setPreferredSize(new Dimension(200, 40));

        // Assemble
        orderPanel.add(headPanel, BorderLayout.NORTH);
        orderPanel.add(tablePanel, BorderLayout.CENTER);
        orderPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }

    public JPanel getOrderPanel() {

        return orderPanel;
    }

    private JScrollPane orderTable() {
        orderTable = Components.updateTable(orderTable);
        orderTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        orderTable.setFillsViewportHeight(true);
        JScrollPane orderPane = new CNavScrollPane(orderTable);

        return orderPane;
    }

    private void retrieveOrderTableData() {
        String sql = "SELECT * FROM ORDER_";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                    rs.getString("OrderID"),
                    rs.getString("UserID"),
                    rs.getString("CustomerID"),
                    rs.getString("OType"),
                    rs.getString("SAmount"),
                    rs.getString("DateTime"),
                    rs.getString("OStatus"),};
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading table data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private final JTextField OrderID = new JTextField();

    private void orderCreate() {

        orderClear();
        int nextID = Database.getNextID("ORDER_");
        if (nextID != -1) {
            OrderID.setText(String.valueOf(nextID));
            OrderID.setEnabled(false); // make it read-only
            OrderID.setDisabledTextColor(Color.BLACK);
        }
        JDialog createDialog = new JDialog((Frame) null, "Create Order", true);
        createDialog.setSize(470, 670);
        createDialog.setLayout(new BorderLayout());
        createDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        createDialog.setLocationRelativeTo(null);

        JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
        mainPanel.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        JLabel title = new JLabel("Create Order");
        title.setFont(b18);
        title.setForeground(Color.white);
        title.setHorizontalAlignment(JLabel.CENTER);

        JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/green.jpg")).getImage());
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(title, BorderLayout.CENTER);
        titlePanel.setBackground(new Color(3, 4, 94));
        titlePanel.setBorder(b);

        JLabel orderIdL = new JLabel("Order ID:");
        JLabel userIdL = new JLabel("User ID:");
        JLabel custIdL = new JLabel("Customer ID:");
        JLabel oTypeLab = new JLabel("Order Type:");
        JLabel sTAmountL = new JLabel("SubTotal Amount:");
        JLabel dateTimeL = new JLabel("Date and Time:");
        JLabel oStatL = new JLabel("Order Status:");
        JLabel[] labels = {orderIdL, userIdL, custIdL, oTypeLab, sTAmountL, dateTimeL, oStatL};

        orderIdFieldlbl.setFont(p14);
        userIdFieldlbl.setFont(p14);
        custIdFieldlbl.setFont(p14);
        orderTypelbl.setFont(p14);
        sAmountFieldlbl.setFont(p14);
        dateTimelbl.setFont(p14);
        orderStatlbl.setFont(p14);

        orderIdField.setText(String.valueOf(nextID));

        orderIdField.setFont(p14);
        orderIdField.setBorder(b);
        userIdField.setFont(p14);
        userIdField.setBorder(b);
        custIdField.setFont(p14);
        custIdField.setBorder(b);
        sAmountField.setFont(p14);
        sAmountField.setBorder(b);
        oType.setFont(p14);
        oStatus.setFont(p14);
        dateTime.setFont(p14);

        orderIdField.setEnabled(false);
        orderIdField.setDisabledTextColor(Color.BLACK);

        userIdField.setEnabled(true);
        custIdField.setEnabled(true);

        sAmountField.setEnabled(true);
        dateTimes.setEnabled(true);
        oTypes.setEnabled(true);
        oStatuss.setEnabled(true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ---------- Column 0: Labels and spacers ----------
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.gridy = 0;

        centerPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        centerPanel.add(orderIdFieldlbl, gbc);
        gbc.gridy++;
        centerPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        centerPanel.add(userIdFieldlbl, gbc);
        gbc.gridy++;
        centerPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        centerPanel.add(custIdFieldlbl, gbc);
        gbc.gridy++;
        centerPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        centerPanel.add(orderTypelbl, gbc);
        gbc.gridy++;
        centerPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        centerPanel.add(sAmountFieldlbl, gbc);
        gbc.gridy++;
        centerPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        centerPanel.add(dateTimelbl, gbc);
        gbc.gridy++;
        centerPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        centerPanel.add(orderStatlbl, gbc);
        gbc.gridy++;
        centerPanel.add(new JLabel(" "), gbc); // Spacer (bottom padding)

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.gridy = 0;

        centerPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        centerPanel.add(orderIdField, gbc);
        gbc.gridy++;
        centerPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        centerPanel.add(userIdField, gbc);
        gbc.gridy++;
        centerPanel.add(errorUId = new JLabel(" "), gbc);
        gbc.gridy++;
        centerPanel.add(custIdField, gbc);
        gbc.gridy++;
        centerPanel.add(errorCId = new JLabel(" "), gbc);
        gbc.gridy++;
        centerPanel.add(oType, gbc);
        gbc.gridy++;
        centerPanel.add(errorOtype = new JLabel(" "), gbc);
        gbc.gridy++;
        centerPanel.add(sAmountField, gbc);
        gbc.gridy++;
        centerPanel.add(errorSA = new JLabel(" "), gbc);
        gbc.gridy++;
        centerPanel.add(dateTime, gbc);
        gbc.gridy++;
        centerPanel.add(errorDate = new JLabel(" "), gbc);
        gbc.gridy++;
        centerPanel.add(oStatus, gbc);
        gbc.gridy++;
        centerPanel.add(errorOstat = new JLabel(" "), gbc);

        errorUId.setFont(p11);
        errorCId.setFont(p11);
        errorOtype.setFont(p11);
        errorSA.setFont(p11);
        errorDate.setFont(p11);
        errorOstat.setFont(p11);

        orderIdFieldlbl.setPreferredSize(new Dimension(120, 35));
        userIdFieldlbl.setPreferredSize(new Dimension(120, 35));
        custIdFieldlbl.setPreferredSize(new Dimension(120, 35));
        orderTypelbl.setPreferredSize(new Dimension(120, 35));
        sAmountFieldlbl.setPreferredSize(new Dimension(120, 35));
        dateTimelbl.setPreferredSize(new Dimension(120, 35));
        orderStatlbl.setPreferredSize(new Dimension(120, 35));

        orderIdField.setPreferredSize(new Dimension(200, 35));
        userIdField.setPreferredSize(new Dimension(200, 35));
        custIdField.setPreferredSize(new Dimension(200, 35));
        sAmountField.setPreferredSize(new Dimension(200, 35));

        JButton createBtn = new BtnGreen("Create", new ImageIcon(getClass().getResource("/images/icon/create.png")));
        JButton cancelBtn = new BtnRed("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(b);

        btnPanel.add(cancelBtn);
        btnPanel.add(createBtn);

        cancelBtn.addActionListener(e -> createDialog.dispose());
        createBtn.addActionListener(e -> {
            String orderId = orderIdField.getText().trim();
            String userId = userIdField.getText().trim();
            String custId = custIdField.getText().trim();
            String otype = oTypes.getSelectedItem().toString();
            String sAmount = sAmountField.getText().trim();
            String datime = dateTimes.getDateTime();
            String oStat = oStatuss.getSelectedItem().toString();

            Boolean checker = false;

            if (userId.isEmpty() || userId.equalsIgnoreCase("Required")) {
                errorUId.setText("User Id is Required");
                errorUId.setForeground(Color.red);
                userIdField.setForeground(Color.red);

                checker = true;
            }
            if (custId.isEmpty() || custId.equalsIgnoreCase("Required")) {
                errorCId.setText("Customer Id is Required");
                errorCId.setForeground(Color.red);
                custIdField.setForeground(Color.red);

                checker = true;
            }
            if (sAmount.isEmpty() || sAmount.equalsIgnoreCase("Required")) {
                errorSA.setText("Subtotal Amount is Required");
                errorSA.setForeground(Color.red);
                sAmountField.setForeground(Color.red);

                checker = true;
            }

            if (otype.equalsIgnoreCase("SELECT")) {
                errorOtype.setText("Order Type is Required");
                errorOtype.setForeground(Color.red);
                oTypes.setForeground(Color.red);

                checker = true;
            }
            if (oStat.equalsIgnoreCase("SELECT")) {
                errorOstat.setText("Order Type is Required");
                errorOstat.setForeground(Color.red);
                oStatuss.setForeground(Color.red);
                checker = true;
            }

            if (checker == true) {
                JOptionPane.showMessageDialog(null, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String sql = "INSERT INTO ORDER_ (UserID, CustomerID, OType, SAmount, DateTime, OStatus) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, userId);
                stmt.setString(2, custId);
                stmt.setString(3, otype);
                stmt.setString(4, sAmount);
                stmt.setString(5, datime);
                stmt.setString(6, oStat);

                stmt.executeUpdate();
                retrieveOrderTableData();
                orderClear();
                JOptionPane.showMessageDialog(createDialog, "Order added successfully.");
                createDialog.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(createDialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        createDialog.setContentPane(mainPanel);
        createDialog.setVisible(true);
    }

    private void orderUpdate() {
        orderClear();
        String inputID = JOptionPane.showInputDialog(this, "Enter Order ID to Update:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Order ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM Order_ WHERE OrderID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();
            String originalUserID = inputID;
            String originalCustID = inputID;

            if (rs.next()) {
                JDialog updateDialog = new JDialog((Frame) null, "Update Order", true);
                updateDialog.setSize(470, 670);
                updateDialog.setLayout(new BorderLayout());
                updateDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                updateDialog.setLocationRelativeTo(null);

                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                JPanel centerPanel = new JPanel(new GridBagLayout());
                centerPanel.setOpaque(false);

                JLabel title = new JLabel("Update Order");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);

                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/orange.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.add(title, BorderLayout.CENTER);
                titlePanel.setBackground(new Color(3, 4, 94));
                titlePanel.setBorder(b);

                JLabel orderIdL = new JLabel("Order ID:");
                JLabel userIdL = new JLabel("User ID:");
                JLabel custIdL = new JLabel("Customer ID:");
                JLabel oTypeLab = new JLabel("Order Type:");
                JLabel sTAmountL = new JLabel("SubTotal Amount:");
                JLabel dateTimeL = new JLabel("Date and Time:");
                JLabel oStatL = new JLabel("Order Status:");

                orderIdField.setText(rs.getString("OrderID"));
                userIdField.setText(rs.getString("UserID"));
                custIdField.setText(rs.getString("CustomerID"));
                oTypes.setSelectedItem(rs.getString("OType"));
                sAmountField.setText(rs.getString("SAmount"));
                dateTimes.setValue(rs.getDate("DateTime"));
                oStatuss.setSelectedItem(rs.getString("OStatus"));

                orderIdFieldlbl.setFont(p14);
                userIdFieldlbl.setFont(p14);
                custIdFieldlbl.setFont(p14);
                orderTypelbl.setFont(p14);
                sAmountFieldlbl.setFont(p14);
                dateTimelbl.setFont(p14);
                orderStatlbl.setFont(p14);

                orderIdField.setText(String.valueOf(inputID));

                orderIdField.setFont(p14);
                orderIdField.setBorder(b);
                userIdField.setFont(p14);
                userIdField.setBorder(b);
                custIdField.setFont(p14);
                custIdField.setBorder(b);
                sAmountField.setFont(p14);
                sAmountField.setBorder(b);
                oType.setFont(p14);
                oStatus.setFont(p14);
                dateTime.setFont(p14);

                orderIdField.setEnabled(false);
                orderIdField.setDisabledTextColor(Color.BLACK);
                userIdField.setEnabled(false);
                custIdField.setEnabled(false);

                sAmountField.setEnabled(true);
                dateTimes.setEnabled(true);
                oTypes.setEnabled(true);
                oStatuss.setEnabled(true);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                // ---------- Column 0: Labels and spacers ----------
                gbc.gridx = 0;
                gbc.weightx = 0;
                gbc.gridwidth = 1;
                gbc.gridy = 0;

                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(orderIdFieldlbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(userIdFieldlbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(custIdFieldlbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(orderTypelbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(sAmountFieldlbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(dateTimelbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(orderStatlbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer (bottom padding)

                gbc.gridx = 1;
                gbc.weightx = 1.0;
                gbc.gridy = 0;

                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(orderIdField, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(userIdField, gbc);
                gbc.gridy++;
                centerPanel.add(errorUId = new JLabel(" "), gbc);
                gbc.gridy++;
                centerPanel.add(custIdField, gbc);
                gbc.gridy++;
                centerPanel.add(errorCId = new JLabel(" "), gbc);
                gbc.gridy++;
                centerPanel.add(oType, gbc);
                gbc.gridy++;
                centerPanel.add(errorOtype = new JLabel(" "), gbc);
                gbc.gridy++;
                centerPanel.add(sAmountField, gbc);
                gbc.gridy++;
                centerPanel.add(errorSA = new JLabel(" "), gbc);
                gbc.gridy++;
                centerPanel.add(dateTime, gbc);
                gbc.gridy++;
                centerPanel.add(errorDate = new JLabel(" "), gbc);
                gbc.gridy++;
                centerPanel.add(oStatus, gbc);
                gbc.gridy++;
                centerPanel.add(errorOstat = new JLabel(" "), gbc);

                errorUId.setFont(p11);
                errorCId.setFont(p11);
                errorOtype.setFont(p11);
                errorSA.setFont(p11);
                errorDate.setFont(p11);
                errorOstat.setFont(p11);

                orderIdFieldlbl.setPreferredSize(new Dimension(120, 35));
                userIdFieldlbl.setPreferredSize(new Dimension(120, 35));
                custIdFieldlbl.setPreferredSize(new Dimension(120, 35));
                orderTypelbl.setPreferredSize(new Dimension(120, 35));
                sAmountFieldlbl.setPreferredSize(new Dimension(120, 35));
                dateTimelbl.setPreferredSize(new Dimension(120, 35));
                orderStatlbl.setPreferredSize(new Dimension(120, 35));

                orderIdField.setPreferredSize(new Dimension(200, 35));
                userIdField.setPreferredSize(new Dimension(200, 35));
                custIdField.setPreferredSize(new Dimension(200, 35));
                sAmountField.setPreferredSize(new Dimension(200, 35));

                JButton updateBtn = new BtnDefault("Update", new ImageIcon(getClass().getResource("/images/icon/update.png")));
                JButton cancelBtn = new BtnRed("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
                JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
                btnPanel.setOpaque(false);
                btnPanel.setBorder(b);

                btnPanel.add(cancelBtn);
                btnPanel.add(updateBtn);

                cancelBtn.addActionListener(e -> updateDialog.dispose());

                updateBtn.addActionListener(e -> {
                    String orderId = orderIdField.getText().trim();
                    String userId = userIdField.getText().trim();
                    String custId = custIdField.getText().trim();
                    String otype = oTypes.getSelectedItem().toString();
                    String sAmount = sAmountField.getText().trim();
                    String datime = dateTimes.getDateTime();
                    String oStat = oStatuss.getSelectedItem().toString();

                    Boolean checker = false;

                    if (userId.isEmpty() || userId.equalsIgnoreCase("Required")) {
                        errorUId.setText("User Id is Required");
                        errorUId.setForeground(Color.red);
                        userIdField.setForeground(Color.red);

                        checker = true;
                    }
                    if (custId.isEmpty() || custId.equalsIgnoreCase("Required")) {
                        errorCId.setText("Customer Id is Required");
                        errorCId.setForeground(Color.red);
                        custIdField.setForeground(Color.red);

                        checker = true;
                    }
                    if (sAmount.isEmpty() || sAmount.equalsIgnoreCase("Required")) {
                        errorSA.setText("Subtotal Amount is Required");
                        errorSA.setForeground(Color.red);
                        sAmountField.setForeground(Color.red);

                        checker = true;
                    }

                    if (otype.equalsIgnoreCase("SELECT")) {
                        errorOtype.setText("Order Type is Required");
                        errorOtype.setForeground(Color.red);
                        oTypes.setForeground(Color.red);

                        checker = true;
                    }
                    if (oStat.equalsIgnoreCase("SELECT")) {
                        errorOstat.setText("Order Type is Required");
                        errorOstat.setForeground(Color.red);
                        oStatuss.setForeground(Color.red);
                        checker = true;
                    }

                    if (checker == true) {
                        JOptionPane.showMessageDialog(null, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try (PreparedStatement updateStmt = conn.prepareStatement(
                            "UPDATE ORDER_ SET UserID = ?, CustomerID = ?, OType = ?, SAmount = ?, DateTime = ?, OStatus = ? WHERE OrderID= ?")) {
                        // Check if UserID exists
                        PreparedStatement checkUser = conn.prepareStatement("SELECT * FROM User_ WHERE UserID = ?");
                        checkUser.setString(1, userId);
                        ResultSet userRs = checkUser.executeQuery();
                        if (!userRs.next()) {
                            JOptionPane.showMessageDialog(updateDialog, "User ID does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Check if CustomerID exists
                        PreparedStatement checkCustomer = conn.prepareStatement("SELECT * FROM CUSTOMER WHERE CustomerID = ?");
                        checkCustomer.setString(1, custId);
                        ResultSet custRs = checkCustomer.executeQuery();
                        if (!custRs.next()) {
                            JOptionPane.showMessageDialog(updateDialog, "Customer ID does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        updateStmt.setString(1, userId);
                        updateStmt.setString(2, custId);
                        updateStmt.setString(3, otype);
                        updateStmt.setString(4, sAmount);
                        updateStmt.setString(5, datime);
                        updateStmt.setString(6, oStat);
                        updateStmt.setString(7, orderId);
                        updateStmt.executeUpdate();

                        JOptionPane.showMessageDialog(updateDialog, "Order ID " + orderId + " updated successfully.");

                        updateDialog.dispose();
                        retrieveOrderTableData();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(updateDialog, "Update failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                updateDialog.add(mainPanel, BorderLayout.CENTER);
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(centerPanel, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);
                updateDialog.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Order ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }

    }

    private void orderSearch() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Order ID to Search:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Order ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM ORDER_ WHERE OrderID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog searchDialog = new JDialog((Frame) null, "Search Order", true);
                searchDialog.setSize(470, 670);
                searchDialog.setLayout(new BorderLayout());
                searchDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                searchDialog.setLocationRelativeTo(null);

                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                JPanel centerPanel = new JPanel(new GridBagLayout());
                centerPanel.setOpaque(false);

                JLabel title = new JLabel("Search Order");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);

                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/blue1.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.add(title, BorderLayout.CENTER);
                titlePanel.setBackground(new Color(3, 4, 94));
                titlePanel.setBorder(b);

                JLabel orderIdL = new JLabel("Order ID:");
                JLabel userIdL = new JLabel("User ID:");
                JLabel custIdL = new JLabel("Customer ID:");
                JLabel oTypeLab = new JLabel("Order Type:");
                JLabel sTAmountL = new JLabel("SubTotal Amount:");
                JLabel dateTimeL = new JLabel("Date and Time:");
                JLabel oStatL = new JLabel("Order Status:");

                orderIdField.setText(rs.getString("OrderID"));
                userIdField.setText(rs.getString("UserID"));
                custIdField.setText(rs.getString("CustomerID"));
                oTypes.setSelectedItem(rs.getString("OType"));
                sAmountField.setText(rs.getString("SAmount"));
                dateTimes.setValue(rs.getDate("DateTime"));
                oStatuss.setSelectedItem(rs.getString("OStatus"));

                orderIdFieldlbl.setFont(p14);
                userIdFieldlbl.setFont(p14);
                custIdFieldlbl.setFont(p14);
                orderTypelbl.setFont(p14);
                sAmountFieldlbl.setFont(p14);
                dateTimelbl.setFont(p14);
                orderStatlbl.setFont(p14);

                orderIdField.setText(String.valueOf(inputID));

                orderIdField.setFont(p14);
                orderIdField.setBorder(b);
                userIdField.setFont(p14);
                userIdField.setBorder(b);
                custIdField.setFont(p14);
                custIdField.setBorder(b);
                sAmountField.setFont(p14);
                sAmountField.setBorder(b);
                oType.setFont(p14);
                oStatus.setFont(p14);
                dateTime.setFont(p14);

                orderIdField.setEnabled(false);
                orderIdField.setDisabledTextColor(Color.BLACK);
                
                userIdField.setEnabled(false);
                custIdField.setEnabled(false);
                sAmountField.setEnabled(false);
                dateTimes.setEnabled(false);
                oTypes.setEnabled(false);
                oStatuss.setEnabled(false);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                // ---------- Column 0: Labels and spacers ----------
                gbc.gridx = 0;
                gbc.weightx = 0;
                gbc.gridwidth = 1;
                gbc.gridy = 0;

                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(orderIdFieldlbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(userIdFieldlbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(custIdFieldlbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(orderTypelbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(sAmountFieldlbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(dateTimelbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(orderStatlbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer (bottom padding)

                gbc.gridx = 1;
                gbc.weightx = 1.0;
                gbc.gridy = 0;

                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(orderIdField, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(userIdField, gbc);
                gbc.gridy++;
                centerPanel.add(errorUId = new JLabel(" "), gbc);
                gbc.gridy++;
                centerPanel.add(custIdField, gbc);
                gbc.gridy++;
                centerPanel.add(errorCId = new JLabel(" "), gbc);
                gbc.gridy++;
                centerPanel.add(oType, gbc);
                gbc.gridy++;
                centerPanel.add(errorOtype = new JLabel(" "), gbc);
                gbc.gridy++;
                centerPanel.add(sAmountField, gbc);
                gbc.gridy++;
                centerPanel.add(errorSA = new JLabel(" "), gbc);
                gbc.gridy++;
                centerPanel.add(dateTime, gbc);
                gbc.gridy++;
                centerPanel.add(errorDate = new JLabel(" "), gbc);
                gbc.gridy++;
                centerPanel.add(oStatus, gbc);
                gbc.gridy++;
                centerPanel.add(errorOstat = new JLabel(" "), gbc);

                errorUId.setFont(p11);
                errorCId.setFont(p11);
                errorOtype.setFont(p11);
                errorSA.setFont(p11);
                errorDate.setFont(p11);
                errorOstat.setFont(p11);

                orderIdFieldlbl.setPreferredSize(new Dimension(120, 35));
                userIdFieldlbl.setPreferredSize(new Dimension(120, 35));
                custIdFieldlbl.setPreferredSize(new Dimension(120, 35));
                orderTypelbl.setPreferredSize(new Dimension(120, 35));
                sAmountFieldlbl.setPreferredSize(new Dimension(120, 35));
                dateTimelbl.setPreferredSize(new Dimension(120, 35));
                orderStatlbl.setPreferredSize(new Dimension(120, 35));

                orderIdField.setPreferredSize(new Dimension(200, 35));
                userIdField.setPreferredSize(new Dimension(200, 35));
                custIdField.setPreferredSize(new Dimension(200, 35));
                sAmountField.setPreferredSize(new Dimension(200, 35));

                orderIdField.setEnabled(false);
                userIdField.setEnabled(false);
                custIdField.setEnabled(false);
                sAmountField.setEnabled(false);
                oTypes.setEnabled(false);
                oStatuss.setEnabled(false);
                dateTimes.setEnabled(false);

                orderIdField.setDisabledTextColor(Color.BLACK);
                userIdField.setDisabledTextColor(Color.BLACK);
                custIdField.setDisabledTextColor(Color.BLACK);
                sAmountField.setDisabledTextColor(Color.BLACK);

                JButton returnBtn = new BtnDefault("Return", new ImageIcon(getClass().getResource("/images/icon/return.png")));
                JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
                btnPanel.setOpaque(false);
                btnPanel.setBorder(b);
                btnPanel.add(returnBtn);

                returnBtn.addActionListener(e -> searchDialog.dispose());
                retrieveOrderTableData();
                searchDialog.add(mainPanel, BorderLayout.CENTER);
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(centerPanel, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);
                searchDialog.setVisible(true);
                
            } else {
                JOptionPane.showMessageDialog(this, "Order ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void orderClear() {

        orderIdField.setDefault();
        userIdField.setDefault();
        custIdField.setDefault();

        sAmountField.setDefault();
        dateTimes.setValue(new Date());
        oTypes.setSelectedIndex(0);  // Selects the first item
        oStatuss.setSelectedIndex(0);

        orderIdField.setForeground(Color.BLACK);
        userIdField.setForeground(Color.BLACK);
        custIdField.setForeground(Color.BLACK);
        sAmountField.setForeground(Color.BLACK);

        oTypes.setForeground(Color.BLACK);
        oStatuss.setForeground(Color.BLACK);
        oTypes.setSelectedIndex(0);
        oStatuss.setSelectedIndex(0);

    }

    private void orderDelete() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Order ID to Delete:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Order ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM ORDER_ WHERE OrderID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog deleteDialog = new JDialog((Frame) null, "Delete Order", true);
                deleteDialog.setSize(470, 670);
                deleteDialog.setLayout(new BorderLayout());
                deleteDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                deleteDialog.setLocationRelativeTo(null);

                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                JPanel centerPanel = new JPanel(new GridBagLayout());
                centerPanel.setOpaque(false);

                JLabel title = new JLabel("Delete Order");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);

                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/red.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.add(title, BorderLayout.CENTER);
                titlePanel.setBackground(new Color(3, 4, 94));
                titlePanel.setBorder(b);

                JLabel orderIdL = new JLabel("Order ID:");
                JLabel userIdL = new JLabel("User ID:");
                JLabel custIdL = new JLabel("Customer ID:");
                JLabel oTypeLab = new JLabel("Order Type:");
                JLabel sTAmountL = new JLabel("SubTotal Amount:");
                JLabel dateTimeL = new JLabel("Date and Time:");
                JLabel oStatL = new JLabel("Order Status:");

                orderIdField.setText(rs.getString("OrderID"));
                userIdField.setText(rs.getString("UserID"));
                custIdField.setText(rs.getString("CustomerID"));
                oTypes.setSelectedItem(rs.getString("OType"));
                sAmountField.setText(rs.getString("SAmount"));
                dateTimes.setValue(rs.getDate("DateTime"));
                oStatuss.setSelectedItem(rs.getString("OStatus"));

                orderIdFieldlbl.setFont(p14);
                userIdFieldlbl.setFont(p14);
                custIdFieldlbl.setFont(p14);
                orderTypelbl.setFont(p14);
                sAmountFieldlbl.setFont(p14);
                dateTimelbl.setFont(p14);
                orderStatlbl.setFont(p14);

                orderIdField.setText(String.valueOf(inputID));

                orderIdField.setFont(p14);
                orderIdField.setBorder(b);
                userIdField.setFont(p14);
                userIdField.setBorder(b);
                custIdField.setFont(p14);
                custIdField.setBorder(b);
                sAmountField.setFont(p14);
                sAmountField.setBorder(b);
                oType.setFont(p14);
                oStatus.setFont(p14);
                dateTime.setFont(p14);

                orderIdField.setEnabled(false);
                orderIdField.setDisabledTextColor(Color.BLACK);
                userIdField.setEnabled(false);
                custIdField.setEnabled(false);

                sAmountField.setEnabled(false);
                dateTimes.setEnabled(false);
                oTypes.setEnabled(false);
                oStatuss.setEnabled(false);
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                // ---------- Column 0: Labels and spacers ----------
                gbc.gridx = 0;
                gbc.weightx = 0;
                gbc.gridwidth = 1;
                gbc.gridy = 0;

                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(orderIdFieldlbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(userIdFieldlbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(custIdFieldlbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(orderTypelbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(sAmountFieldlbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(dateTimelbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(orderStatlbl, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer (bottom padding)

                gbc.gridx = 1;
                gbc.weightx = 1.0;
                gbc.gridy = 0;

                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(orderIdField, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(userIdField, gbc);
                gbc.gridy++;
                centerPanel.add(errorUId = new JLabel(" "), gbc);
                gbc.gridy++;
                centerPanel.add(custIdField, gbc);
                gbc.gridy++;
                centerPanel.add(errorCId = new JLabel(" "), gbc);
                gbc.gridy++;
                centerPanel.add(oType, gbc);
                gbc.gridy++;
                centerPanel.add(errorOtype = new JLabel(" "), gbc);
                gbc.gridy++;
                centerPanel.add(sAmountField, gbc);
                gbc.gridy++;
                centerPanel.add(errorSA = new JLabel(" "), gbc);
                gbc.gridy++;
                centerPanel.add(dateTime, gbc);
                gbc.gridy++;
                centerPanel.add(errorDate = new JLabel(" "), gbc);
                gbc.gridy++;
                centerPanel.add(oStatus, gbc);
                gbc.gridy++;
                centerPanel.add(errorOstat = new JLabel(" "), gbc);

                errorUId.setFont(p11);
                errorCId.setFont(p11);
                errorOtype.setFont(p11);
                errorSA.setFont(p11);
                errorDate.setFont(p11);
                errorOstat.setFont(p11);

                orderIdFieldlbl.setPreferredSize(new Dimension(120, 35));
                userIdFieldlbl.setPreferredSize(new Dimension(120, 35));
                custIdFieldlbl.setPreferredSize(new Dimension(120, 35));
                orderTypelbl.setPreferredSize(new Dimension(120, 35));
                sAmountFieldlbl.setPreferredSize(new Dimension(120, 35));
                dateTimelbl.setPreferredSize(new Dimension(120, 35));
                orderStatlbl.setPreferredSize(new Dimension(120, 35));

                orderIdField.setPreferredSize(new Dimension(200, 35));
                userIdField.setPreferredSize(new Dimension(200, 35));
                custIdField.setPreferredSize(new Dimension(200, 35));
                sAmountField.setPreferredSize(new Dimension(200, 35));

                orderIdField.setEnabled(false);
                userIdField.setEnabled(false);
                custIdField.setEnabled(false);
                sAmountField.setEnabled(false);
                oTypes.setEnabled(false);
                oStatuss.setEnabled(false);
                dateTimes.setEnabled(false);

                orderIdField.setDisabledTextColor(Color.BLACK);
                userIdField.setDisabledTextColor(Color.BLACK);
                custIdField.setDisabledTextColor(Color.BLACK);
                sAmountField.setDisabledTextColor(Color.BLACK);

                JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
                btnPanel.setOpaque(false);
                btnPanel.setBorder(b);
                JButton cancelBtn = new BtnDefault("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
                JButton deleteBtn = new BtnRed("Confirm Delete", new ImageIcon(getClass().getResource("/images/icon/delete.png")));
                btnPanel.add(cancelBtn);
                btnPanel.add(deleteBtn);

                cancelBtn.addActionListener(e -> deleteDialog.dispose());
                deleteBtn.addActionListener(e -> {
                    try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM Order_ WHERE OrderID = ?")) {
                        deleteStmt.setString(1, inputID);
                        deleteStmt.executeUpdate();
                        JOptionPane.showMessageDialog(deleteDialog, "Order No. " + inputID + " was deleted successfully.");
                        retrieveOrderTableData();
                        deleteDialog.dispose();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(deleteDialog, "Delete failed: " + ex.getMessage());
                    }
                });

                deleteDialog.add(mainPanel, BorderLayout.CENTER);
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(centerPanel, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);
                deleteDialog.setVisible(true);
                retrieveOrderTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Order ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void likeSearchOrders() {
        String[] fields = {"OrderID", "UserID", "CustomerID", "OType", "SAmount", "DateTime", "OStatus"};
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

            String sql = "SELECT * FROM ORDER_ WHERE " + field + " LIKE ?";
            try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, keyword);
                try (ResultSet rs = stmt.executeQuery()) {
                    orderTableModel.setRowCount(0); // clear table

                    while (rs.next()) {
                        orderTableModel.addRow(new Object[]{
                            rs.getInt("OrderID"),
                            rs.getInt("UserID"),
                            rs.getInt("CustomerID"),
                            rs.getString("OType"),
                            rs.getDouble("SAmount"),
                            rs.getTimestamp("DateTime"),
                            rs.getString("OStatus")
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

    private void sortOrders() {
        String[] fields = {"OrderID", "UserID", "CustomerID", "OType", "SAmount", "DateTime", "OStatus"};
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

        String sql = "SELECT * FROM ORDER_ ORDER BY " + field + " " + order;

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            orderTableModel.setRowCount(0); // clear table

            while (rs.next()) {
                orderTableModel.addRow(new Object[]{
                    rs.getInt("OrderID"),
                    rs.getInt("UserID"),
                    rs.getInt("CustomerID"),
                    rs.getString("OType"),
                    rs.getDouble("SAmount"),
                    rs.getTimestamp("DateTime"),
                    rs.getString("OStatus")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error sorting: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
