package RestaurantOrderManagementSystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class MenuItemCategoryGUI extends Components {

    private RTextField miCatId = new RTextField(30);
    private RTextField catDes = new RTextField("Required", 30);
    private final String[] mCcolumnNames = {"Menu Item Category ID", "Category Description"};
    private final DefaultTableModel mCTableModel = new DisabledTableModel(mCcolumnNames, 0);
    public JTable mCTable = new JTable(mCTableModel);

    private final JPanel menuItemCategoryPanel = new JPanel(new BorderLayout());

    public MenuItemCategoryGUI() {
        DefaultTableCellRenderer centerRenderer = new CustomRowRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        mCTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Menu Item Category ID
        mCTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Category Description

        // Retrieve data
        retrieveMenuItemCatTableData();

        // Set up the main panel
        menuItemCategoryPanel.setLayout(new BorderLayout());
        menuItemCategoryPanel.setBorder(b);
        menuItemCategoryPanel.setOpaque(false);

        // Head panel (top bar) with filter and refresh
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
                sortMenuItemCategories();
            } else if (choice == 1) {
                likeSearchMenuItemCategories();
            } else if (choice == 2) {
                retrieveMenuItemCatTableData();
            }
        });

        JButton refreshBtn = new BtnCustom(
                new ImageIcon(getClass().getResource("/images/icon/refresh.png")),
                new ImageIcon(getClass().getResource("/images/icon/refreshGreen.png")),
                "Refresh"
        );
        refreshBtn.addActionListener(e -> retrieveMenuItemCatTableData());

        headPanel.add(refreshBtn);
        headPanel.add(filterBtn);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(b);
        JScrollPane tableScrollPane = new CScrollPane(mCTable()); // assuming mCTable() returns JTable
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel southPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        southPanel.setPreferredSize(new Dimension(0, 100));
        southPanel.setBorder(b);

        JButton createButton = new BtnGreen("Create", new ImageIcon(getClass().getResource("/images/icon/create.png")));
        JButton updateButton = new BtnDefault("Update", new ImageIcon(getClass().getResource("/images/icon/update.png")));
        JButton searchButton = new BtnBlue("Search", new ImageIcon(getClass().getResource("/images/icon/searchWhite.png")));
        JButton deleteButton = new BtnRed("Delete", new ImageIcon(getClass().getResource("/images/icon/delete.png")));

        // Shared preferred size
        Dimension buttonSize = new Dimension(120, 30);

// createButton
        createButton.setPreferredSize(buttonSize);
        southPanel.add(createButton);

// updateButton
        updateButton.setPreferredSize(buttonSize);
        southPanel.add(updateButton);

// searchButton
        searchButton.setPreferredSize(buttonSize);
        southPanel.add(searchButton);

// deleteButton
        deleteButton.setPreferredSize(buttonSize);
        southPanel.add(deleteButton);

        // Add button listeners
        createButton.addActionListener(e -> menuItemCatCreate());
        updateButton.addActionListener(e -> menuItemCatUpdate());
        searchButton.addActionListener(e -> menuItemCatSearch());
        deleteButton.addActionListener(e -> menuItemCatDelete());

        // Assemble components
        menuItemCategoryPanel.add(headPanel, BorderLayout.NORTH);
        menuItemCategoryPanel.add(tablePanel, BorderLayout.CENTER);
        menuItemCategoryPanel.add(southPanel, BorderLayout.SOUTH);
    }

    public JPanel getMenuItemCategoryPanel() {

        return menuItemCategoryPanel;
    }

    private JScrollPane mCTable() {
        mCTable = Components.updateTable(mCTable);
        JScrollPane mCPane = new CNavScrollPane(mCTable);
        mCTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        mCTable.setFillsViewportHeight(true);

        return mCPane;
    }

    private void retrieveMenuItemCatTableData() {
        String sql = "SELECT * FROM MENUITEMCATEGORY";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            DefaultTableModel model = (DefaultTableModel) mCTable.getModel();
            model.setRowCount(0);

            DefaultTableCellRenderer centerRenderer = new CustomRowRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

            mCTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Menu Item Category ID
            mCTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Category Description

            while (rs.next()) {
                Object[] row = {
                    rs.getString("MICatID"),
                    rs.getString("CatDes"),};
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading table data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void menuItemCatCreate() {
        int nextID = Database.getNextID("MENUITEMCATEGORY");
        if (nextID != -1) {
            miCatId.setText(String.valueOf(nextID));
            miCatId.setEnabled(false); // make it read-only
            miCatId.setDisabledTextColor(Color.DARK_GRAY);
        }

        JDialog createDialog = new JDialog((Frame) null, "Create Menu Item Category", true);
        createDialog.setSize(500, 300);
        createDialog.setLayout(new BorderLayout());
        createDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        createDialog.setLocationRelativeTo(null);

        JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
        mainPanel.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBorder(b);

        // Title Panel
        JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/green.jpg")).getImage());
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(b);

        JLabel title = new JLabel("Create Menu Item Category");
        title.setFont(b18);
        title.setForeground(Color.white);
        title.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(title, BorderLayout.CENTER);

        JLabel menuCatIdL = new JLabel("Menu Item Category ID:");
        JLabel menuCatDesL = new JLabel("Category Description:");
        JLabel errorDes = new JLabel("");
        errorDes.setFont(p11);
        errorDes.setForeground(Color.red);

        // menuCatIdL
        menuCatIdL.setFont(p14);

// menuCatDesL
        menuCatDesL.setFont(p14);

        miCatId = new RTextField(30);  // Set the next available MICatID
        miCatId.setText(String.valueOf(nextID));
        catDes = new RTextField("Required", 30);

        // Shared preferred size
        Dimension textFieldSize = new Dimension(0, 32);

// miCatId
        miCatId.setFont(p14);
        miCatId.setBorder(b);
        miCatId.setPreferredSize(textFieldSize);

// catDes
        catDes.setFont(p14);
        catDes.setBorder(b);
        catDes.setPreferredSize(textFieldSize);

        miCatId.setEnabled(false); // Disable editing of the ID field
        miCatId.setDisabledTextColor(Color.GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.gridy = 0;
        centerPanel.add(menuCatIdL, gbc);
        gbc.gridy++;
        centerPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        centerPanel.add(menuCatDesL, gbc);

        //======== FIELDS ========
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        centerPanel.add(miCatId, gbc);
        gbc.gridy++;
        centerPanel.add(new JLabel(), gbc);
        gbc.gridy++;
        centerPanel.add(catDes, gbc);
        gbc.gridy++;
        centerPanel.add(errorDes, gbc);

        // Buttons Panel
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(b);
        JButton cancelBtn = new BtnRed("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
        JButton createBtn = new BtnGreen("Create", new ImageIcon(getClass().getResource("/images/icon/create.png")));
        btnPanel.add(cancelBtn);
        btnPanel.add(createBtn);

        cancelBtn.addActionListener(e -> createDialog.dispose());
        createBtn.addActionListener(e -> {
            errorDes.setText("");
            String menuCatDes = catDes.getText().trim();

            boolean hasError = false;
            if (menuCatDes.equals("Required")) {
                errorDes.setText("Description is required.");
                catDes.setForeground(Color.red);
                hasError = true;
            }

            if (hasError) {
                JOptionPane.showMessageDialog(createDialog, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "INSERT INTO MENUITEMCATEGORY (CatDes) VALUES (?)";

            try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, menuCatDes);
                stmt.executeUpdate();

                retrieveMenuItemCatTableData();
                JOptionPane.showMessageDialog(createDialog, "Menu Item Category added successfully.");
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

    private void menuItemCatUpdate() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Menu Item Category ID to Update:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Menu Item Category ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM MENUITEMCATEGORY WHERE MICatID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog updateDialog = new JDialog((Frame) null, "Update Menu Item Category", true);
                updateDialog.setSize(500, 300);
                updateDialog.setLayout(new BorderLayout());
                updateDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                updateDialog.setLocationRelativeTo(null);

                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                JPanel centerPanel = new JPanel();
                centerPanel.setOpaque(false);
                centerPanel.setLayout(new GridBagLayout());
                centerPanel.setBorder(b);

                // Title Panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/orange.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);

                JLabel title = new JLabel("Update Menu Item Category");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                JLabel menuCatIdL = new JLabel("Menu Item Category ID:");
                JLabel menuCatDesL = new JLabel("Category Description:");
                JLabel errorDes = new JLabel("");
                errorDes.setForeground(Color.red);
                // menuCatIdL
                menuCatIdL.setFont(p14);

// menuCatDesL
                menuCatDesL.setFont(p14);

                RTextField miCatId = new RTextField(30);
                int miCatID = rs.getInt("MICatID");
                miCatId.setText(String.valueOf(miCatID));// Set the next available MICatID
                RTextField menuCatDesField = new RTextField(" Required", 30);
                menuCatDesField.setText(rs.getString("CatDes"));
                // Shared preferred size
                Dimension textFieldSize = new Dimension(0, 32);

// miCatId
                miCatId.setFont(p14);
                miCatId.setBorder(b);
                miCatId.setPreferredSize(textFieldSize);

// menuCatDesField
                menuCatDesField.setFont(p14);
                menuCatDesField.setBorder(b);
                menuCatDesField.setPreferredSize(textFieldSize);

                miCatId.setEnabled(false); // Disable editing of the ID field
                miCatId.setDisabledTextColor(Color.GRAY);
                menuCatDesField.setForeground(Color.BLACK);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.BOTH;

                gbc.gridwidth = 1;
                gbc.gridx = 0;
                gbc.weightx = 0;
                gbc.gridy = 0;
                centerPanel.add(menuCatIdL, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(menuCatDesL, gbc);

                //======== FIELDS ========
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.weightx = 1.0;
                centerPanel.add(miCatId, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(), gbc);
                gbc.gridy++;
                centerPanel.add(menuCatDesField, gbc);
                gbc.gridy++;
                centerPanel.add(errorDes, gbc);
                JButton cancelBtn = new BtnRed("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
                JButton updateBtn = new BtnGreen("Update", new ImageIcon(getClass().getResource("/images/icon/create.png")));
                JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
                btnPanel.setOpaque(false);
                btnPanel.setBorder(b);
                btnPanel.add(cancelBtn);
                btnPanel.add(updateBtn);

                cancelBtn.addActionListener(e -> updateDialog.dispose());

                updateBtn.addActionListener(e -> {
                    String catdes = menuCatDesField.getText().trim();

                    boolean hasError = false;
                    if (catdes.equals("Required")) {
                        errorDes.setText("Description is required.");
                        menuCatDesField.setForeground(Color.red);
                        hasError = true;
                    }

                    if (hasError) {
                        JOptionPane.showMessageDialog(updateDialog, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try (PreparedStatement updateStmt = conn.prepareStatement(
                            "UPDATE MENUITEMCATEGORY SET CatDes = ? WHERE MICatID= ?")) {
                        updateStmt.setString(1, catdes);
                        updateStmt.setInt(2, miCatID);
                        updateStmt.executeUpdate();

                        JOptionPane.showMessageDialog(updateDialog, "Menu Item Category ID No. " + miCatID + " updated successfully.");

                        updateDialog.dispose();
                        retrieveMenuItemCatTableData();
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
                JOptionPane.showMessageDialog(this, "Menu Item Category ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void menuItemCatSearch() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Menu Item Category ID to Search:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Menu Item Category ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM MENUITEMCATEGORY WHERE MICatID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog searchDialog = new JDialog((Frame) null, "Search Menu Item Category", true);
                searchDialog.setSize(500, 300);
                searchDialog.setLayout(new BorderLayout());
                searchDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                searchDialog.setLocationRelativeTo(null);

                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                JPanel centerPanel = new JPanel();
                centerPanel.setOpaque(false);
                centerPanel.setLayout(new GridBagLayout());
                centerPanel.setBorder(b);

                // Title Panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/blue1.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);

                JLabel title = new JLabel("Search Menu Item Category");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                JLabel menuCatIdL = new JLabel("Menu Item Category ID:");
                JLabel menuCatDesL = new JLabel("Category Description:");
// menuCatIdL
                menuCatIdL.setFont(p14);

// menuCatDesL
                menuCatDesL.setFont(p14);

                RTextField miCatId = new RTextField(30);
                miCatId.setText(rs.getString("MICatID"));// Set the next available MICatID
                RTextField menuCatDesField = new RTextField(" Required", 30);
                menuCatDesField.setText(rs.getString("CatDes"));
                JTextField[] textFields = {miCatId, menuCatDesField};
                // miCatId
                miCatId.setFont(p14);
                miCatId.setBorder(b);
                miCatId.setPreferredSize(new Dimension(0, 32));
                miCatId.setEnabled(false);
                miCatId.setDisabledTextColor(Color.BLACK);

// menuCatDesField
                menuCatDesField.setFont(p14);
                menuCatDesField.setBorder(b);
                menuCatDesField.setPreferredSize(new Dimension(0, 32));
                menuCatDesField.setEnabled(false);
                menuCatDesField.setDisabledTextColor(Color.BLACK);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.BOTH;

                gbc.gridwidth = 1;
                gbc.gridx = 0;
                gbc.weightx = 0;
                gbc.gridy = 0;
                centerPanel.add(menuCatIdL, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(menuCatDesL, gbc);

                //======== FIELDS ========
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.weightx = 1.0;
                centerPanel.add(miCatId, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(), gbc);
                gbc.gridy++;
                centerPanel.add(menuCatDesField, gbc);

                JButton returnBtn = new BtnDefault("Return", new ImageIcon(getClass().getResource("/images/icon/return.png")));
                JPanel btnPanel = new JPanel(new GridLayout(1, 1, 10, 10));
                btnPanel.setOpaque(false);
                btnPanel.setBorder(b);
                btnPanel.add(returnBtn);

                returnBtn.addActionListener(e -> searchDialog.dispose());
                retrieveMenuItemCatTableData();
                searchDialog.add(mainPanel, BorderLayout.CENTER);
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(centerPanel, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);
                searchDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Menu Item ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void menuItemCatDelete() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Menu Item Category ID to Delete:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Menu Item Category ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM MENUITEMCATEGORY WHERE MICatID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog deleteDialog = new JDialog((Frame) null, "Delete Menu Item Category", true);
                deleteDialog.setSize(500, 300);
                deleteDialog.setLayout(new BorderLayout());
                deleteDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                deleteDialog.setLocationRelativeTo(null);

                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                JPanel centerPanel = new JPanel();
                centerPanel.setOpaque(false);
                centerPanel.setLayout(new GridBagLayout());
                centerPanel.setBorder(b);

                // Title Panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/red.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);

                JLabel title = new JLabel("Delete Menu Item Category");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                JLabel menuCatIdL = new JLabel("Menu Item Category ID:");
                JLabel menuCatDesL = new JLabel("Category Description:");
                // menuCatIdL
                menuCatIdL.setFont(p14);

// menuCatDesL
                menuCatDesL.setFont(p14);

                RTextField miCatId = new RTextField(30);
                miCatId.setText(rs.getString("MICatID"));// Set the next available MICatID
                RTextField menuCatDesField = new RTextField(" Required", 30);
                menuCatDesField.setText(rs.getString("CatDes"));
                // Shared preferred size
                Dimension textFieldSize = new Dimension(0, 32);

// miCatId
                miCatId.setFont(p14);
                miCatId.setBorder(b);
                miCatId.setPreferredSize(textFieldSize);
                miCatId.setEnabled(false);
                miCatId.setDisabledTextColor(Color.BLACK);

// menuCatDesField
                menuCatDesField.setFont(p14);
                menuCatDesField.setBorder(b);
                menuCatDesField.setPreferredSize(textFieldSize);
                menuCatDesField.setEnabled(false);
                menuCatDesField.setDisabledTextColor(Color.BLACK);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.BOTH;

                gbc.gridwidth = 1;
                gbc.gridx = 0;
                gbc.weightx = 0;
                gbc.gridy = 0;
                centerPanel.add(menuCatIdL, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                centerPanel.add(menuCatDesL, gbc);

                //======== FIELDS ========
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.weightx = 1.0;
                centerPanel.add(miCatId, gbc);
                gbc.gridy++;
                centerPanel.add(new JLabel(), gbc);
                gbc.gridy++;
                centerPanel.add(menuCatDesField, gbc);

                // Buttons
                JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
                btnPanel.setOpaque(false);
                btnPanel.setBorder(b);
                JButton cancelBtn = new BtnDefault("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
                JButton deleteBtn = new BtnRed("Confirm Delete", new ImageIcon(getClass().getResource("/images/icon/delete.png")));
                btnPanel.add(cancelBtn);
                btnPanel.add(deleteBtn);

                cancelBtn.addActionListener(e -> deleteDialog.dispose());
                deleteBtn.addActionListener(e -> {
                    try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM MENUITEMCATEGORY WHERE MICatID = ?")) {
                        deleteStmt.setString(1, inputID);
                        deleteStmt.executeUpdate();
                        JOptionPane.showMessageDialog(deleteDialog, "Menu Item Category No. " + inputID + " was deleted successfully.");
                        retrieveMenuItemCatTableData();
                        deleteDialog.dispose();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(deleteDialog, "Delete failed: " + ex.getMessage());
                    }
                });
                cancelBtn.addActionListener(e -> deleteDialog.dispose());

                deleteDialog.add(mainPanel, BorderLayout.CENTER);
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(centerPanel, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);
                deleteDialog.setVisible(true);
                retrieveMenuItemCatTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Menu Item Category ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void likeSearchMenuItemCategories() {
        String[] fields = {"MICatID", "CatDes"};
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

            String sql = "SELECT * FROM MENUITEMCATEGORY WHERE " + field + " LIKE ?";
            try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, keyword);
                try (ResultSet rs = stmt.executeQuery()) {
                    mCTableModel.setRowCount(0); // clear table

                    while (rs.next()) {
                        mCTableModel.addRow(new Object[]{
                            rs.getInt("MICatID"),
                            rs.getString("CatDes")
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

    private void sortMenuItemCategories() {
        String[] fields = {"MICatID", "CatDes"};
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

        String sql = "SELECT * FROM MENUITEMCATEGORY ORDER BY " + field + " " + order;

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            mCTableModel.setRowCount(0); // clear table

            while (rs.next()) {
                mCTableModel.addRow(new Object[]{
                    rs.getInt("MICatID"),
                    rs.getString("CatDes")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error sorting: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
