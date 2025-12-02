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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class CustomerGUI extends Components {

    private RTextField customerIDField = new RTextField(30);
    private RTextField fNameField = new RTextField("Required",30);
    private RTextField mNameField = new RTextField("Optional", 30);
    private RTextField lNameField = new RTextField("Required", 30);
    private RTextField cNumberField = new RTextField("09", 30);
    private DefaultTableModel tableModel = new DisabledTableModel(new String[]{"CustomerID", "FName", "MName", "LName", "CNumber"}, 0);
    private JTable customerTable = Components.updateTable(new JTable(tableModel));
    private int nextID;
    private JPanel customerPanel;
    private JLabel labelCustomerID;
    private JLabel labelFName;
    private JLabel labelMName;
    private JLabel labelLName;
    private JLabel labelCNumber;
    private JLabel errorFName;
    private JLabel errorLName;
    private JLabel errorContact;

    public CustomerGUI() {

        DefaultTableCellRenderer centerRenderer = new CustomRowRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        customerTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // CustomerID
        customerTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // FName
        customerTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // MName
        customerTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // LName
        customerTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // CNumber

        // Set up panel
        customerPanel = new JPanel(new BorderLayout());
        customerPanel.setBorder(b);
        customerPanel.setOpaque(false);
        // Field labels
        labelCustomerID = new JLabel("Customer ID:");
        labelCustomerID.setFont(p14);

        labelFName = new JLabel("First Name:");
        labelFName.setFont(p14);

        labelMName = new JLabel("Middle Name:");
        labelMName.setFont(p14);

        labelLName = new JLabel("Last Name:");
        labelLName.setFont(p14);

        labelCNumber = new JLabel("Contact Number:");
        labelCNumber.setFont(p14);

        // Error labels
        errorFName = new JLabel();
        errorFName.setFont(p14);

        errorLName = new JLabel();
        errorLName.setFont(p14);

        errorContact = new JLabel();
        errorContact.setFont(p14);

        // Retrieve data
        retrieveCustomerTableData();

        // Customized table data
        customerTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        customerTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        customerTable.getColumnModel().getColumn(1).setPreferredWidth(20);
        customerTable.getColumnModel().getColumn(2).setPreferredWidth(20);
        customerTable.getColumnModel().getColumn(3).setPreferredWidth(20);
        customerTable.getColumnModel().getColumn(4).setPreferredWidth(20);
        customerTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        customerTable.setFillsViewportHeight(true);

        // Head panel with filter and refresh buttons
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
                sortCustomers();
            } else if (choice == 1) {
                likeSearchCustomers();
            } else if (choice == 2) {
                retrieveCustomerTableData();
            }
        });

        JButton refreshBtn = new BtnCustom(
                new ImageIcon(getClass().getResource("/images/icon/refresh.png")),
                new ImageIcon(getClass().getResource("/images/icon/refreshGreen.png")),
                "Refresh"
        );
        refreshBtn.addActionListener(e -> retrieveCustomerTableData());

        headPanel.add(refreshBtn);
        headPanel.add(filterBtn);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(b);
        JScrollPane tableScrollPane = new CScrollPane(customerTable);
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
        createButton.addActionListener(e -> createCustomer());
        updateButton.addActionListener(e -> updateCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());
        searchButton.addActionListener(e -> customerSearch());

        // Add all to main panel
        customerPanel.add(headPanel, BorderLayout.NORTH);
        customerPanel.add(tablePanel, BorderLayout.CENTER);
        customerPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }

    public JPanel getCustomerPanel() {
        return customerPanel;
    }

    private void createCustomer() {
        errorFName.setText(" ");
        errorLName.setText(" ");
        errorContact.setText(" ");
  
        
        JDialog createDialog = new JDialog((Frame) null, "Create Customer", true);
        createDialog.setSize(470, 530);
        createDialog.setLayout(new BorderLayout());
        createDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
        mainPanel.setLayout(new BorderLayout());

        // Title Panel
        JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/orange.jpg")).getImage());
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(b);

        JLabel title = new JLabel("Create Customer");
        title.setFont(b18);
        title.setForeground(Color.white);
        title.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(title, BorderLayout.CENTER);

        // Form Panel
        JPanel miniPanel = new JPanel(new GridBagLayout());
        miniPanel.setOpaque(false);
        miniPanel.setBorder(b);

        // Fields
        nextID = Database.getNextID("CUSTOMER");
        if (nextID != -1) {
            customerIDField.setText(String.valueOf(nextID));
            customerIDField.setEnabled(false);
            customerIDField.setDisabledTextColor(Color.DARK_GRAY);
        }
        customerIDField.setFont(p14);
        fNameField.setFont(p14);
        mNameField.setFont(p14);
        lNameField.setFont(p14);
        cNumberField.setFont(p14);
        
        customerIDField.setBorder(b);
        fNameField.setBorder(b);
        mNameField.setBorder(b);
        lNameField.setBorder(b);
        cNumberField.setBorder(b);

        errorFName.setFont(p11);
        errorFName.setForeground(Color.red);
        errorLName.setFont(p11);
        errorLName.setForeground(Color.red);
        errorContact.setFont(p11);
        errorContact.setForeground(Color.red);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ==== LABELS COLUMN (x = 0) ====
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.gridy = 0;
        miniPanel.add(new JLabel(""), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(labelCustomerID, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(labelFName, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(labelMName, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(labelLName, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(labelCNumber, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer

        // ==== FIELDS COLUMN (x = 1) ====
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        miniPanel.add(new JLabel(""), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(new JLabel(""), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(customerIDField, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(fNameField, gbc);
        gbc.gridy++;
        miniPanel.add(errorFName, gbc);
        gbc.gridy++;
        miniPanel.add(mNameField, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(lNameField, gbc);
        gbc.gridy++;
        miniPanel.add(errorLName, gbc);
        gbc.gridy++;
        miniPanel.add(cNumberField, gbc);
        gbc.gridy++;
        miniPanel.add(errorContact, gbc);

        // Buttons Panel
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(b);
        JButton cancelBtn = new BtnRed("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
        JButton createBtn = new BtnGreen("Create", new ImageIcon(getClass().getResource("/images/icon/create.png")));
        btnPanel.add(cancelBtn);
        btnPanel.add(createBtn);

        cancelBtn.addActionListener(e -> {
            customerClear();
            createDialog.dispose();});

        createBtn.addActionListener(e -> {
            // Clear previous errors
            errorFieldReset();

            String fname = fNameField.getText().trim();
            String mname = mNameField.getText().trim();
            String lname = lNameField.getText().trim();
            String cNumber = cNumberField.getText().trim();

            boolean hasError = false;
            if (fname.equals("Required") || fname.isEmpty()) {
                errorFName.setText("First name is required.");
                fNameField.setForeground(Color.RED);
                hasError = true;
            }
            if (lname.equals("Required") || lname.isEmpty()) {
                errorLName.setText("Last name is required.");
                lNameField.setForeground(Color.RED);
                hasError = true;
            }

            if (mname.equals("Optional") || mname.isEmpty()) {
                mname = "NULL";
            }

            if (!cNumber.equalsIgnoreCase("09")) {
                if (!Database.isValidContactNumberLength(cNumber)) {
                    errorContact.setText("Contact number must be 11 digits.");
                    hasError = true;
                } else if (!Database.isValidContactNumber(cNumber)) {
                    errorContact.setText("Contact number must start with '09'.");
                    hasError = true;
                }
            }else if(cNumber.equalsIgnoreCase("09") || !cNumber.isEmpty()){
               cNumber="NULL"; 
            }

            if (hasError) {
                JOptionPane.showMessageDialog(createDialog, "Please fix the errors in the form.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "INSERT INTO Customer (FName, MName, LName, CNumber) VALUES (?, ?, ?, ?)";
            try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, fname);
                stmt.setString(2, mname.isEmpty() ? null : mname);
                stmt.setString(3, lname);
                stmt.setString(4, cNumber.isEmpty() ? null : cNumber);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Customer added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                retrieveCustomerTableData();
                customerClear();
                createDialog.dispose();

                // Refresh ID
                nextID = Database.getNextID("CUSTOMER");
                if (nextID != -1) {
                    customerIDField.setText(String.valueOf(nextID));
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Assemble
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(miniPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        createDialog.add(mainPanel);
        createDialog.setLocationRelativeTo(null);
        createDialog.setVisible(true);
    }

    private void deleteCustomer() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Customer ID to Delete:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Customer ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM Customer WHERE CustomerID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog deleteDialog = new JDialog((Frame) null, "Delete Customer", true);
                deleteDialog.setSize(470, 530);
                deleteDialog.setLayout(new BorderLayout());
                deleteDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                // Main Panel with background
                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title Panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/red.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);
                JLabel title = new JLabel("Delete Customer");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                // Form Panel
                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);

                // Fields (retrieved from database)
                customerIDField = new RTextField(rs.getString("CustomerID"), 30);
                fNameField = new RTextField(rs.getString("FName"), 30);
                mNameField = new RTextField(rs.getString("MName"), 30);
                lNameField = new RTextField(rs.getString("LName"), 30);
                cNumberField = new RTextField(rs.getString("CNumber"), 30);

                customerIDField.setFont(p14);
                customerIDField.setEnabled(false);
                customerIDField.setDisabledTextColor(Color.DARK_GRAY);
                customerIDField.setBorder(b);

                fNameField.setFont(p14);
                fNameField.setEnabled(false);
                fNameField.setDisabledTextColor(Color.DARK_GRAY);
                fNameField.setBorder(b);

                mNameField.setFont(p14);
                mNameField.setEnabled(false);
                mNameField.setDisabledTextColor(Color.DARK_GRAY);
                mNameField.setBorder(b);

                lNameField.setFont(p14);
                lNameField.setEnabled(false);
                lNameField.setDisabledTextColor(Color.DARK_GRAY);
                lNameField.setBorder(b);

                cNumberField.setFont(p14);
                cNumberField.setEnabled(false);
                cNumberField.setDisabledTextColor(Color.DARK_GRAY);
                cNumberField.setBorder(b);

                JLabel customerIDLabel = new JLabel("Customer ID:");
                customerIDLabel.setFont(p14);
                JLabel firstNameLabel = new JLabel("First Name:");
                firstNameLabel.setFont(p14);
                JLabel middleNameLabel = new JLabel("Middle Name:");
                middleNameLabel.setFont(p14);
                JLabel lastNameLabel = new JLabel("Last Name:");
                lastNameLabel.setFont(p14);
                JLabel contactNumberLabel = new JLabel("Contact Number:");
                contactNumberLabel.setFont(p14);

                // Layout
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 0;

                // Labels - Left Column
                gbc.gridx = 0;
                gbc.gridy = 0;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(customerIDLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(firstNameLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(middleNameLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(lastNameLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(contactNumberLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);

                // Inputs - Right Column
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.weightx = 1.0;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(customerIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(fNameField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(mNameField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(lNameField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(cNumberField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                
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
                    try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM Customer WHERE CustomerID = ?")) {
                        deleteStmt.setString(1, inputID);
                        deleteStmt.executeUpdate();
                        JOptionPane.showMessageDialog(deleteDialog, "Customer No. " + inputID + " was deleted successfully.");
                        retrieveCustomerTableData();
                        deleteDialog.dispose();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(deleteDialog, "Delete failed: " + ex.getMessage());
                    }
                });

                deleteDialog.add(mainPanel, BorderLayout.CENTER);
                deleteDialog.setLocationRelativeTo(null);
                deleteDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Customer ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateCustomer() {
        
        errorFieldReset();
        
        String inputID = JOptionPane.showInputDialog(this, "Enter Customer ID to Update:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Customer ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM CUSTOMER WHERE CustomerID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            int customerID = Integer.parseInt(inputID);
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog updateDialog = new JDialog((Frame) null, "Update Customer", true);
                updateDialog.setSize(470, 530);
                updateDialog.setLayout(new BorderLayout());
                updateDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                // Main background panel
                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/orange.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);

                JLabel title = new JLabel("Update Customer");
                title.setFont(b18);
                title.setForeground(Color.WHITE);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                // Fields
                RTextField customerIDField = new RTextField(String.valueOf(rs.getInt("CustomerID")), 30);
                customerIDField.setFont(p14);
                customerIDField.setEnabled(false);
                customerIDField.setDisabledTextColor(Color.DARK_GRAY);

                fNameField = new RTextField(rs.getString("FName"), 30);
                fNameField.setFont(p14);

                mNameField = new RTextField(rs.getString("MName"), 30);
                mNameField.setFont(p14);

                lNameField = new RTextField(rs.getString("LName"), 30);
                lNameField.setFont(p14);

                cNumberField = new RTextField(rs.getString("CNumber"), 30);
                cNumberField.setFont(p14);
                customerIDField.setText(String.valueOf(rs.getInt("CustomerID")));
                fNameField.setText(rs.getString("FName"));
                mNameField.setText(rs.getString("MName"));
                lNameField.setText(rs.getString("LName"));
                cNumberField.setText(rs.getString("CNumber"));
                
                cNumberField.setBorder(b);
                customerIDField.setBorder(b);
                fNameField.setBorder(b);
                mNameField.setBorder(b);
                lNameField.setBorder(b);
                // Error labels


                // Mini panel
                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.HORIZONTAL;

                // ==== LABELS COLUMN (x = 0) ====
                gbc.gridx = 0;
                gbc.weightx = 0;
                gbc.gridy = 0;
                miniPanel.add(new JLabel(""), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(labelCustomerID, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(labelFName, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(labelMName, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(labelLName, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(labelCNumber, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer

                // ==== FIELDS COLUMN (x = 1) ====
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.weightx = 1.0;
                miniPanel.add(new JLabel(""), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(customerIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(fNameField, gbc);
                gbc.gridy++;
                miniPanel.add(errorFName, gbc);
                gbc.gridy++;
                miniPanel.add(mNameField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(lNameField, gbc);
                gbc.gridy++;
                miniPanel.add(errorLName, gbc);
                gbc.gridy++;
                miniPanel.add(cNumberField, gbc);
                gbc.gridy++;
                miniPanel.add(errorContact, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer

                // Button panel
                JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
                btnPanel.setBorder(b);
                btnPanel.setOpaque(false);

                JButton cancelBtn = new BtnRed("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
                JButton updateBtn = new BtnDefault("Update", new ImageIcon(getClass().getResource("/images/icon/update.png")));

                btnPanel.add(cancelBtn);
                btnPanel.add(updateBtn);

                // Assemble components
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(miniPanel, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);

                cancelBtn.addActionListener(e -> updateDialog.dispose());

                updateBtn.addActionListener(e -> {
                    String fName = fNameField.getText().trim();
                    String mName = mNameField.getText().trim();
                    String lName = lNameField.getText().trim();
                    String cNumber = cNumberField.getText().trim();

                    // Clear previous errors
                    errorFName.setText(" ");
                    errorLName.setText(" ");
                    errorContact.setText(" ");

                     boolean hasError = false;
                    if (fName.equals("Required") || fName.isEmpty()) {
                        errorFName.setText("First name is required.");
                        hasError = true;
                    }
                    if (lName.equals("Required") || lName.isEmpty()) {
                        errorLName.setText("Last name is required.");
                        hasError = true;
                    }

                    if (mName.equals("Optional") || mName.isEmpty()) {
                        mName = "NULL";
                    }

                    if (!cNumber.equalsIgnoreCase("09")) {
                        if (!Database.isValidContactNumberLength(cNumber)) {
                            errorContact.setText("Contact number must be 11 digits.");
                            hasError = true;
                        } else if (!Database.isValidContactNumber(cNumber)) {
                            errorContact.setText("Contact number must start with '09'.");
                            hasError = true;
                        }
                    }else if(cNumber.equalsIgnoreCase("09") || cNumber.equalsIgnoreCase("NULL")  || !cNumber.isEmpty()){
                       cNumber="NULL"; 
                    }

                    if (hasError) {
                        JOptionPane.showMessageDialog(updateDialog, "Please fix the errors in the form.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try (PreparedStatement updateStmt = conn.prepareStatement(
                            "UPDATE CUSTOMER SET FName=?, MName=?, LName=?, CNumber=? WHERE CustomerID=?")) {

                        updateStmt.setString(1, fName);
                        updateStmt.setString(2, mName.isEmpty() ? null : mName);
                        updateStmt.setString(3, lName);
                        updateStmt.setString(4, cNumber.isEmpty() ? null : cNumber);
                        updateStmt.setInt(5, customerID);

                        int rowsAffected = updateStmt.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(updateDialog, "Customer #" + customerID + " updated successfully!");
                            retrieveCustomerTableData(); // Refresh table data
                            customerClear();
                            updateDialog.dispose();
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(updateDialog, "Update failed: " + ex.getMessage());
                    }

                });

                updateDialog.add(mainPanel);
                updateDialog.setLocationRelativeTo(null);
                updateDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Customer ID not found");
            }
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void customerSearch() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Customer ID to Search:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Customer ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM Customer WHERE CustomerID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog searchDialog = new JDialog((Frame) null, "Search Customer", true);
                searchDialog.setSize(470, 530);
                searchDialog.setLayout(new BorderLayout());
                searchDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                // Main Panel with background
                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title Panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/blue2.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);
                JLabel title = new JLabel("Customer Details");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                // Form Panel
                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);

                // Fields (retrieved from database)
                RTextField customerIDField = new RTextField(rs.getString("CustomerID"), 30);
                RTextField fNameField = new RTextField(rs.getString("FName"), 30);
                RTextField mNameField = new RTextField(rs.getString("MName"), 30);
                RTextField lNameField = new RTextField(rs.getString("LName"), 30);
                RTextField cNumberField = new RTextField(rs.getString("CNumber"), 30);
                
                

                customerIDField.setFont(p14);
                customerIDField.setEnabled(false);
                customerIDField.setBorder(b);
                customerIDField.setDisabledTextColor(Color.DARK_GRAY);

                fNameField.setFont(p14);
                fNameField.setEnabled(false);
                fNameField.setBorder(b);
                fNameField.setDisabledTextColor(Color.DARK_GRAY);

                mNameField.setFont(p14);
                mNameField.setEnabled(false);
                mNameField.setBorder(b);
                mNameField.setDisabledTextColor(Color.DARK_GRAY);

                lNameField.setFont(p14);
                lNameField.setEnabled(false);
                lNameField.setBorder(b);
                lNameField.setDisabledTextColor(Color.DARK_GRAY);

                cNumberField.setFont(p14);
                cNumberField.setEnabled(false);
                cNumberField.setBorder(b);
                cNumberField.setDisabledTextColor(Color.DARK_GRAY);

                JLabel customerIDLabel = new JLabel("Customer ID:");
                customerIDLabel.setFont(p14);
                JLabel firstNameLabel = new JLabel("First Name:");
                firstNameLabel.setFont(p14);
                JLabel middleNameLabel = new JLabel("Middle Name:");
                middleNameLabel.setFont(p14);
                JLabel lastNameLabel = new JLabel("Last Name:");
                lastNameLabel.setFont(p14);
                JLabel contactNumberLabel = new JLabel("Contact Number:");
                contactNumberLabel.setFont(p14);

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
                miniPanel.add(customerIDLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(firstNameLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(middleNameLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(lastNameLabel, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(contactNumberLabel, gbc);
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
                miniPanel.add(customerIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(fNameField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(mNameField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(lNameField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(cNumberField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc);

                // Button
                JPanel btnPanel = new JPanel(new GridLayout(1, 1, 10, 10));
                btnPanel.setOpaque(false);
                btnPanel.setBorder(b);
                JButton returnBtn = new BtnDefault("Return", new ImageIcon(getClass().getResource("/images/icon/return.png")));
                btnPanel.add(returnBtn);

                // Add panels to main panel
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(miniPanel, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);

                returnBtn.addActionListener(e -> searchDialog.dispose());

                searchDialog.add(mainPanel, BorderLayout.CENTER);
                searchDialog.setLocationRelativeTo(null);
                searchDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Customer ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void customerClear() {
        fNameField.setText("");
        mNameField.setText("");
        lNameField.setText("");
        cNumberField.setText("09"); // Reset to "09" as a default start
        int nextID = Database.getNextID("Customer");
        if (nextID != -1) {
            customerIDField.setText(String.valueOf(nextID));
        }

    }
    
    private void errorFieldReset(){
        errorFName.setText(" ");
        errorLName.setText(" ");
        fNameField.setForeground(Color.BLACK);
        lNameField.setForeground(Color.BLACK);
    }

    private void retrieveCustomerTableData() {
        String sql = "SELECT * FROM Customer";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            // Clear existing rows in the table model
            DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
            model.setRowCount(0); // Clear old rows

            // Fill the table with fresh data
            while (rs.next()) {
                Object[] row = {
                    rs.getString("CustomerID"),
                    rs.getString("FName"),
                    rs.getString("MName"),
                    rs.getString("LName"),
                    rs.getString("CNumber"),};
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading table data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void likeSearchCustomers() {
        String[] fields = {"CustomerID", "FName", "MName", "LName", "CNumber"};
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

            String sql = "SELECT * FROM CUSTOMER WHERE " + field + " LIKE ?";
            try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, keyword);
                try (ResultSet rs = stmt.executeQuery()) {
                    tableModel.setRowCount(0); // clear table

                    while (rs.next()) {
                        tableModel.addRow(new Object[]{
                            rs.getInt("CustomerID"),
                            rs.getString("FName"),
                            rs.getString("MName"),
                            rs.getString("LName"),
                            rs.getString("CNumber")
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

    private void sortCustomers() {
        String[] fields = {"CustomerID", "FName", "MName", "LName", "CNumber"};
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

        String sql = "SELECT * FROM CUSTOMER ORDER BY " + field + " " + order;

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            tableModel.setRowCount(0); // clear table

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("CustomerID"),
                    rs.getString("FName"),
                    rs.getString("MName"),
                    rs.getString("LName"),
                    rs.getString("CNumber")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error sorting: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
