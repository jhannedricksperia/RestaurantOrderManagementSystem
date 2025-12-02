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
import java.awt.Image;
import java.awt.Insets;
import java.sql.Statement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class MenuItemGUI extends Components {

    private final JTextField menuItemIDField = new JTextField();
    private JComboBox<String> menuCatID;
    private JComboBox<String> menuItemStat;
    private RTextField meCatId, mstat, menuItemId, menuItemName, menuItemUnitP, menuItemNoServe;
    private JLabel menuItemIdL, menuCatIdL, menuItemNameL, uPriceL, mINoSerL, mIStatL, itemPic;
    private JLabel errormenuCatId, errormenuItemName, erroruPrice, errormINoSer, errormIStat, erroritemPic;
    private RPanel mCid, mIStat, img;
    private JLabel lblImagePath;
    private File imageFile;
    private final String[] menuItemNames = {"MI ID", "MI Cat ID", "MI Name", "Unit Price", "No. Serving", "MI Status"};
    private final DefaultTableModel menuItemTableModel = new DisabledTableModel(menuItemNames, 0);
    public JTable menuItemTable = new JTable(menuItemTableModel);
    private JLabel profilePreviewLabel;
    // File chooser and image
    private JFileChooser pfpChooser;
    private JLabel userLblImagePath;

    private final JPanel menuItemPanel = new JPanel(new BorderLayout());

    public MenuItemGUI() {
        // Retrieve initial data
        retrieveMenuItemTableData();
        allignedTable();

        // Set up auto-incremented MenuItem ID
        int nextID = Database.getNextID("MENUITEM");
        if (nextID != -1) {
            menuItemIDField.setText(String.valueOf(nextID));
            menuItemIDField.setEnabled(false);
            menuItemIDField.setDisabledTextColor(Color.DARK_GRAY);
        }

        // File chooser and image
        imageFile = null; // or new File("");
        userLblImagePath = new JLabel();

        // Panel setup
        menuItemPanel.setLayout(new BorderLayout());
        menuItemPanel.setBorder(b);
        menuItemPanel.setOpaque(false);

        // Head panel with Filter and Refresh
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
                sortMenuItems();
            } else if (choice == 1) {
                likeSearchMenuItems();
            } else if (choice == 2) {
                retrieveMenuItemTableData();
            }
        });

        JButton refreshBtn = new BtnCustom(
                new ImageIcon(getClass().getResource("/images/icon/refresh.png")),
                new ImageIcon(getClass().getResource("/images/icon/refreshGreen.png")),
                "Refresh"
        );
        refreshBtn.addActionListener(e -> retrieveMenuItemTableData());

        headPanel.add(refreshBtn);
        headPanel.add(filterBtn);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(b);
        JScrollPane tableScrollPane = new CNavScrollPane(menuItemTable()); // Assuming menuItemTable() returns a JTable
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // CRUD Buttons
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

        // Add button actions
        createButton.addActionListener(e -> menuItemCreate());
        updateButton.addActionListener(e -> menuItemUpdate());
        searchButton.addActionListener(e -> menuItemSearch());
        deleteButton.addActionListener(e -> menuItemDelete());

        // Assemble final layout
        menuItemPanel.add(headPanel, BorderLayout.NORTH);
        menuItemPanel.add(tablePanel, BorderLayout.CENTER);
        menuItemPanel.add(southPanel, BorderLayout.SOUTH);
    }

    public JPanel getMenuItemPanel() {

        return menuItemPanel;
    }

    private JScrollPane menuItemTable() {
        menuItemTable = Components.updateTable(menuItemTable);
        menuItemTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        menuItemTable.setFillsViewportHeight(true);
        JScrollPane menuItemPane = new CNavScrollPane(menuItemTable);

        return menuItemPane;
    }
    private void allignedTable(){
            DefaultTableCellRenderer centerRenderer = new CustomRowRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

            DefaultTableCellRenderer rightRenderer = new CustomRowRenderer();
            rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

            menuItemTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // MI ID
            menuItemTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // MI Cat ID
            menuItemTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // MI Name
            menuItemTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);  // Unit Price
            menuItemTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // No. Serving
            menuItemTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // MI Status
    }
    private void retrieveMenuItemTableData() {
        String sql = "SELECT * FROM MENUITEM";
         allignedTable();
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            DefaultTableModel model = (DefaultTableModel) menuItemTable.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("MenuItemID"),
                    rs.getInt("MICatID"),
                    rs.getString("MIName"),
                    String.format("%.2f", rs.getDouble("UPrice")),
                    rs.getString("NServing"),
                    rs.getString("MIStatus"),};
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading table data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void menuItemCreate() {
        menuItemId = new RTextField(30);
        int nextID = Database.getNextID("MENUITEM");
        if (nextID != -1) {
            menuItemId.setText(String.valueOf(nextID));
            menuItemId.setEnabled(false); // make it read-only
            menuItemId.setDisabledTextColor(Color.DARK_GRAY);
        }
        JDialog createDialog = new JDialog((Frame) null, "Create Menu Item", true);
        createDialog.setSize(550, 735);
        createDialog.setLayout(new BorderLayout());
        createDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
        mainPanel.setLayout(new BorderLayout());

        // Title Panel
        JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/green.jpg")).getImage());
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(b);

        JLabel title = new JLabel("Create Menu Item");
        title.setFont(b18);
        title.setForeground(Color.white);
        title.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(title, BorderLayout.CENTER);

        // Form Panel
        JPanel miniPanel = new JPanel(new GridBagLayout());
        miniPanel.setOpaque(false);
        miniPanel.setBorder(b);

        //============ FIELDS/COMBO BOX =============
        menuItemId.setText(String.valueOf(nextID));

        mCid = new RPanel(30);
        mCid.setLayout(new BorderLayout());
        String[] menuCat = getAllMenuCategoryIDs();
        menuCatID = new CustomComboBox<>(menuCat);
        mCid.add(menuCatID, BorderLayout.CENTER);
        mCid.setBorder(b);
        mCid.setPreferredSize(new Dimension(0, 40));
        mCid.setBackground(Color.WHITE);
        menuCatID.setBackground(Color.WHITE);
        menuCatID.setFont(p14);
        menuCatID.setBorder(null);

        menuItemName = new RTextField(" Required", 30);

        menuItemUnitP = new RTextField("0.00 (Required)", 30);
        menuItemNoServe = new RTextField("0.00 (Required)", 30);

        mIStat = new RPanel(30);
        mIStat.setLayout(new BorderLayout());
        String[] menuItemStatus = {"SELECT", "AVAILABLE", "UNAVAILABLE"};
        menuItemStat = new CustomComboBox<>(menuItemStatus);
        mIStat.add(menuItemStat, BorderLayout.CENTER);
        mIStat.setBorder(b);
        mIStat.setPreferredSize(new Dimension(0, 40));
        mIStat.setBackground(Color.WHITE);
        menuItemStat.setBackground(Color.WHITE);
        menuItemStat.setFont(p14);
        menuItemStat.setBorder(null);

        img = new RPanel(30);
        img.setLayout(new BorderLayout());
        lblImagePath = new JLabel("No file selected");
        lblImagePath.setFont(p14);
        JButton btnBrowse = new BtnBlue("Browse");
        btnBrowse.addActionListener(e -> chooseImageFile());
        btnBrowse.setPreferredSize(new Dimension(100, 30));
        btnBrowse.setFont(b12);
        img.add(lblImagePath, BorderLayout.CENTER);
        img.add(btnBrowse, BorderLayout.EAST);
        img.setBorder(b);
        img.setPreferredSize(new Dimension(0, 40));
        img.setBackground(Color.WHITE);
        lblImagePath.setFont(p14);

        // Create the image preview label with default image
        profilePreviewLabel = new JLabel();
        profilePreviewLabel.setPreferredSize(new Dimension(150, 150));
        profilePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        profilePreviewLabel.setHorizontalAlignment(JLabel.CENTER);
        profilePreviewLabel.setVerticalAlignment(JLabel.CENTER);
        // Load default image
        ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/images/profilePicture/defaultmenuitem.png"));
        Image scaledImage = defaultIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        profilePreviewLabel.setIcon(new ImageIcon(scaledImage));

        // shared preferred size
        Dimension sharedSize = new Dimension(0, 32);

        // menuItemId
        menuItemId.setFont(p14);
        menuItemId.setBorder(b);
        menuItemId.setPreferredSize(sharedSize);

        // menuItemName
        menuItemName.setFont(p14);
        menuItemName.setBorder(b);
        menuItemName.setPreferredSize(sharedSize);

        // menuItemUnitP
        menuItemUnitP.setFont(p14);
        menuItemUnitP.setBorder(b);
        menuItemUnitP.setPreferredSize(sharedSize);

        // menuItemNoServe
        menuItemNoServe.setFont(p14);
        menuItemNoServe.setBorder(b);
        menuItemNoServe.setPreferredSize(sharedSize);

        //=============== LABELS ================
        menuItemIdL = new JLabel("Menu Item ID:");
        menuCatIdL = new JLabel("Menu Category ID:");
        menuItemNameL = new JLabel("Menu Item Name:");
        uPriceL = new JLabel("Unit Price:");
        mINoSerL = new JLabel("Serving No.:");
        mIStatL = new JLabel("Menu Item Status:");
        itemPic = new JLabel("Item Picture:");

        // shared preferred size for all labels
        Dimension labelSize = new Dimension(145, 32);

        // menuItemIdL
        menuItemIdL.setFont(p14);
        menuItemIdL.setPreferredSize(labelSize);

        // menuCatIdL
        menuCatIdL.setFont(p14);
        menuCatIdL.setPreferredSize(labelSize);

        // menuItemNameL
        menuItemNameL.setFont(p14);
        menuItemNameL.setPreferredSize(labelSize);

        // uPriceL
        uPriceL.setFont(p14);
        uPriceL.setPreferredSize(labelSize);

        // mINoSerL
        mINoSerL.setFont(p14);
        mINoSerL.setPreferredSize(labelSize);

        // mIStatL
        mIStatL.setFont(p14);
        mIStatL.setPreferredSize(labelSize);

        // itemPic
        itemPic.setFont(p14);
        itemPic.setPreferredSize(labelSize);
        errormenuCatId = new JLabel("");
        errormenuItemName = new JLabel("");

        erroruPrice = new JLabel("");
        errormINoSer = new JLabel("");
        errormIStat = new JLabel("");
        erroritemPic = new JLabel("");

        // errormenuCatId
        errormenuCatId.setFont(p11);
        errormenuCatId.setForeground(Color.red);

        // errormenuItemName
        errormenuItemName.setFont(p11);
        errormenuItemName.setForeground(Color.red);

        // erroruPrice
        erroruPrice.setFont(p11);
        erroruPrice.setForeground(Color.red);

        // errormINoSer
        errormINoSer.setFont(p11);
        errormINoSer.setForeground(Color.red);

        // errormIStat
        errormIStat.setFont(p11);
        errormIStat.setForeground(Color.red);

        // erroritemPic
        erroritemPic.setFont(p11);
        erroritemPic.setForeground(Color.red);

        // GridBagConstraints setup
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;

        //ADDS LABELS//
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        // Customer ID label
        gbc.gridx = 0;
        gbc.gridy = 0;
        miniPanel.add(new JLabel(" "), gbc);//img
        gbc.gridwidth = 2; // Make it span 2 columns
        gbc.weightx = 1;   // To istribute space evenly
        gbc.gridy++;
        miniPanel.add(profilePreviewLabel, gbc);
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        //fillers
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(itemPic, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(menuItemIdL, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(menuCatIdL, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(menuItemNameL, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(uPriceL, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(mINoSerL, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(mIStatL, gbc);

        //======== FIELDS ========
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.gridy = 1;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(img, gbc);
        gbc.gridy++;
        miniPanel.add(erroritemPic, gbc);
        gbc.gridy++;
        miniPanel.add(menuItemId, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(mCid, gbc);
        gbc.gridy++;
        miniPanel.add(errormenuCatId, gbc);
        gbc.gridy++;
        miniPanel.add(menuItemName, gbc);
        gbc.gridy++;
        miniPanel.add(errormenuItemName, gbc);
        gbc.gridy++;
        miniPanel.add(menuItemUnitP, gbc);
        gbc.gridy++;
        miniPanel.add(erroruPrice, gbc);
        gbc.gridy++;
        miniPanel.add(menuItemNoServe, gbc);
        gbc.gridy++;
        miniPanel.add(errormINoSer, gbc);
        gbc.gridy++;
        miniPanel.add(mIStat, gbc);
        gbc.gridy++;
        miniPanel.add(errormIStat, gbc);

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
            errormenuCatId.setText("");
            errormenuItemName.setText("");
            erroruPrice.setText("");
            errormINoSer.setText("");
            errormIStat.setText("");
            erroritemPic.setText("");

            String mItemId = menuItemId.getText().trim();
            String mCatId = menuCatID.getSelectedItem().toString().split(" - ")[0];
            String mName = menuItemName.getText().trim();
            String mUprice = menuItemUnitP.getText().trim();
            String mNoServe = menuItemNoServe.getText().trim();
            String mStat = menuItemStat.getSelectedItem().toString();
            System.out.println(mStat);

            boolean hasError = false;
            if (mCatId.equals("SELECT")) {
                errormenuCatId.setText("Menu Category ID is required.");
                menuCatID.setForeground(Color.red);
                hasError = true;
            }
            menuCatID.setForeground(Color.BLACK);

            if (mName.equals("Required") || mName.length() > 25) {
                errormenuItemName.setText("Menu Item Name is required.");
                menuItemName.setForeground(Color.red);
                hasError = true;
            }
            if (mName.length() > 25) {
                errormenuItemName.setText("Menu Item Name should not exceed 25 characters");
                menuItemName.setForeground(Color.red);
                hasError = true;
            }
            if (mUprice.equals("0.00 (Required)")) {
                erroruPrice.setText("Unit Price is required.");
                menuItemUnitP.setForeground(Color.red);
                hasError = true;
            }
            if (mNoServe.equals("0.00 (Required)")) {
                errormINoSer.setText("Serving No. is required.");
                menuItemNoServe.setForeground(Color.red);
                hasError = true;
            }
            if (mStat.equals("SELECT")) {
                errormIStat.setText("Menu Item Status is required.");
                menuItemStat.setForeground(Color.red);
                hasError = true;
            }
            menuItemStat.setForeground(Color.BLACK);
            if (imageFile == null || !imageFile.exists()) {
                erroritemPic.setText("Image is required.");
                lblImagePath.setForeground(Color.red);
                lblImagePath.setText("Required");
                hasError = true;
            }
            lblImagePath.setForeground(Color.BLACK);
            if (hasError) {
                JOptionPane.showMessageDialog(createDialog, "Please fix the errors in the form.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {

                try {
                    if (imageFile == null || !imageFile.exists()) {
                        erroritemPic.setText("Image is required.");
                        lblImagePath.setForeground(Color.red);

                    }
                    int mCatIdNum = 0;
                    int mServeNum = 0;
                    double mUpriceNum = 0.0;

                    // Parse mCatId
                    try {
                        String selectedCat = menuCatID.getSelectedItem().toString();
                        mCatIdNum = Integer.parseInt(mCatId);
                    } catch (NumberFormatException | NullPointerException ae) {
                        JOptionPane.showMessageDialog(this, "Invalid Category selection.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Parse mUprice
                    try {
                        mUpriceNum = Double.parseDouble(mUprice);
                    } catch (NumberFormatException ae) {
                        JOptionPane.showMessageDialog(this, "Invalid Unit Price. Please enter a valid decimal number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Parse NumServe
                    try {
                        mServeNum = Integer.parseInt(mNoServe);
                    } catch (NumberFormatException ae) {
                        JOptionPane.showMessageDialog(this, "Invalid number of seats. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return; // stop further execution
                    }

                    if (mServeNum <= 0) {
                        JOptionPane.showMessageDialog(this, "Invalid number of seats. Please enter a number greater than 0.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        errormINoSer.setText("Please enter a number greater than 0.");
                        menuItemNoServe.setForeground(Color.red);
                        return; // stop further execution
                    }

                    String sql = "INSERT INTO MENUITEM (MICatID, MIName, UPrice, NServing, MIStatus, ItemPicture) VALUES (?, ?, ?, ?,?,?)";
                    try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setInt(1, mCatIdNum);
                        stmt.setString(2, mName);
                        stmt.setDouble(3, mUpriceNum);
                        stmt.setInt(4, mServeNum);
                        stmt.setString(5, mStat);

                        try (FileInputStream fis = new FileInputStream(imageFile)) {
                            // Set the image as binary stream in the PreparedStatement
                            stmt.setBinaryStream(6, fis, (int) imageFile.length());
                            stmt.executeUpdate();
                            fis.close();
                        } catch (FileNotFoundException ex) {
                            // Log the error in the console for debugging purposes
                            Logger.getLogger(UserGUI.class.getName()).log(Level.SEVERE, null, ex);

                            // Show an error message to the user using a dialog box
                            JOptionPane.showMessageDialog(this, "Menu Item image file not found. Please select a valid image.", "File Error", JOptionPane.ERROR_MESSAGE);

                            // Exit the method to prevent further execution since the file couldn't be read
                            return;
                        } catch (IOException ex) {
                            // Show an error message to the user using a dialog box
                            JOptionPane.showMessageDialog(this, "IOException occured with the User Image.", "File Error", JOptionPane.ERROR_MESSAGE);
                            Logger.getLogger(UserGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        retrieveMenuItemTableData();
                        JOptionPane.showMessageDialog(createDialog, "Menu Item added successfully.");
                        createDialog.dispose();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(createDialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (NumberFormatException ae) {
                    JOptionPane.showMessageDialog(createDialog, "Please enter valid numeric values in Unit Price and Serving Number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        CNavScrollPane scrollPane = new CNavScrollPane(miniPanel);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        createDialog.add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        createDialog.setLocationRelativeTo(null);
        createDialog.setVisible(true);

    }

    private void chooseImageFile() {
        pfpChooser = new JFileChooser();

        FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG Images (*.png)", "png");
        pfpChooser.setFileFilter(pngFilter);
        pfpChooser.setAcceptAllFileFilterUsed(false);

        int result = pfpChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File chosen = pfpChooser.getSelectedFile();

            if (chosen.getName().toLowerCase().endsWith(".png")) {
                imageFile = chosen;

                if (userLblImagePath != null) {
                    userLblImagePath.setText(imageFile.getName());
                }

                if (profilePreviewLabel != null) {
                    ImageIcon newIcon = new ImageIcon(imageFile.getAbsolutePath());
                    Image scaledImage = newIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    profilePreviewLabel.setIcon(new ImageIcon(scaledImage));
                }
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a .png file only.",
                        "Invalid File Type",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    private void menuItemUpdate() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Menu Item ID to Update:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Menu Item ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String sql = "SELECT * FROM MENUITEM WHERE MenuItemID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog updateDialog = new JDialog((Frame) null, "Update Menu Item", true);
                updateDialog.setSize(550, 735);
                updateDialog.setLayout(new BorderLayout());
                updateDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title Panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/orange.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);

                JLabel title = new JLabel("Update Menu Item");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                // Create the image preview label with default image
                profilePreviewLabel = new JLabel();
                profilePreviewLabel.setPreferredSize(new Dimension(150, 150));
                profilePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                profilePreviewLabel.setHorizontalAlignment(JLabel.CENTER);
                profilePreviewLabel.setVerticalAlignment(JLabel.CENTER);

                // Form Panel
                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);

                //============ FIELDS/COMBO BOX =============
                menuItemId = new RTextField(30);
                menuItemId.setText(rs.getString("MenuItemID"));

                mCid = new RPanel(30);
                mCid.setLayout(new BorderLayout());
                String[] menuCat = getAllMenuCategoryIDs();
                menuCatID = new CustomComboBox<>(menuCat);
                mCid.add(menuCatID, BorderLayout.CENTER);
                mCid.setBorder(b);
                mCid.setPreferredSize(new Dimension(0, 40));
                mCid.setBackground(Color.WHITE);
                menuCatID.setBackground(Color.WHITE);
                menuCatID.setFont(p14);
                menuCatID.setBorder(null);
                menuCatID.setSelectedItem(rs.getString("MICatID"));

                menuItemName = new RTextField(" Required", 30);
                menuItemName.setText(rs.getString("MIName"));

                menuItemUnitP = new RTextField("0.00 (Required)", 30);
                menuItemUnitP.setText(rs.getString("UPrice"));
                menuItemNoServe = new RTextField("0.00 (Required)", 30);
                menuItemNoServe.setText(rs.getString("NServing"));

                mIStat = new RPanel(30);
                mIStat.setLayout(new BorderLayout());
                String[] menuItemStatus = {"AVAILABLE", "UNAVAILABLE"};
                menuItemStat = new CustomComboBox<>(menuItemStatus);
                mIStat.add(menuItemStat, BorderLayout.CENTER);
                mIStat.setBorder(b);
                mIStat.setPreferredSize(new Dimension(0, 40));
                mIStat.setBackground(Color.WHITE);
                menuItemStat.setBackground(Color.WHITE);
                menuItemStat.setFont(p14);
                menuItemStat.setBorder(null);
                menuItemStat.setSelectedItem(rs.getString("MIStatus"));
                String statusFromDB = rs.getString("MIStatus").trim().toUpperCase();
                menuItemStat.setSelectedItem(statusFromDB); // Make sure this value matches exactly

                img = new RPanel(30);
                img.setLayout(new BorderLayout());
                lblImagePath = new JLabel("No file selected");
                lblImagePath.setFont(p14);
                JButton btnBrowse = new BtnBlue("Browse");
                btnBrowse.addActionListener(e -> chooseImageFile());
                btnBrowse.setPreferredSize(new Dimension(100, 30));
                btnBrowse.setFont(b12);
                img.add(lblImagePath, BorderLayout.CENTER);
                img.add(btnBrowse, BorderLayout.EAST);
                img.setBorder(b);
                img.setPreferredSize(new Dimension(0, 40));
                img.setBackground(Color.WHITE);
                lblImagePath.setFont(p14);

                // Retrieve and display the image if it exists
                byte[] imageBytes = rs.getBytes("ItemPicture"); // Assuming image is stored in a column named "UserImage"
                if (imageBytes != null && imageBytes.length > 0) {
                    ImageIcon imageIcon = new ImageIcon(imageBytes);
                    Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    profilePreviewLabel.setIcon(new ImageIcon(image));
                } else {
                    // If no image is found, display the default image
                    ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/images/profilePicture/defaultProfile.png"));
                    Image scaledImage = defaultIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    profilePreviewLabel.setIcon(new ImageIcon(scaledImage));
                }
// Shared preferred size
                Dimension textFieldSize = new Dimension(0, 32);

// menuItemId
                menuItemId.setFont(p14);
                menuItemId.setBorder(b);
                menuItemId.setPreferredSize(textFieldSize);
                menuItemId.setForeground(Color.BLACK);

// menuItemName
                menuItemName.setFont(p14);
                menuItemName.setBorder(b);
                menuItemName.setPreferredSize(textFieldSize);
                menuItemName.setForeground(Color.BLACK);

// menuItemUnitP
                menuItemUnitP.setFont(p14);
                menuItemUnitP.setBorder(b);
                menuItemUnitP.setPreferredSize(textFieldSize);
                menuItemUnitP.setForeground(Color.BLACK);

// menuItemNoServe
                menuItemNoServe.setFont(p14);
                menuItemNoServe.setBorder(b);
                menuItemNoServe.setPreferredSize(textFieldSize);
                menuItemNoServe.setForeground(Color.BLACK);
                menuItemId.setEnabled(false);
                menuItemId.setForeground(Color.gray);

                //=============== LABELS ================
                menuItemIdL = new JLabel("Menu Item ID:");
                menuCatIdL = new JLabel("Menu Category ID:");
                menuItemNameL = new JLabel("Menu Item Name:");
                uPriceL = new JLabel("Unit Price:");
                mINoSerL = new JLabel("Serving No.:");
                mIStatL = new JLabel("Menu Item Status:");
                itemPic = new JLabel("Item Picture:");
// Shared preferred size
                Dimension labelSize = new Dimension(145, 32);

// menuItemIdL
                menuItemIdL.setFont(p14);
                menuItemIdL.setPreferredSize(labelSize);

// menuCatIdL
                menuCatIdL.setFont(p14);
                menuCatIdL.setPreferredSize(labelSize);

// menuItemNameL
                menuItemNameL.setFont(p14);
                menuItemNameL.setPreferredSize(labelSize);

// uPriceL
                uPriceL.setFont(p14);
                uPriceL.setPreferredSize(labelSize);

// mINoSerL
                mINoSerL.setFont(p14);
                mINoSerL.setPreferredSize(labelSize);

// mIStatL
                mIStatL.setFont(p14);
                mIStatL.setPreferredSize(labelSize);

// itemPic
                itemPic.setFont(p14);
                itemPic.setPreferredSize(labelSize);

                errormenuCatId = new JLabel("");
                errormenuItemName = new JLabel("");
                erroruPrice = new JLabel("");
                errormINoSer = new JLabel("");
                errormIStat = new JLabel("");
                erroritemPic = new JLabel("");

                // errormenuCatId
                errormenuCatId.setFont(p11);
                errormenuCatId.setForeground(Color.red);

// errormenuItemName
                errormenuItemName.setFont(p11);
                errormenuItemName.setForeground(Color.red);

// erroruPrice
                erroruPrice.setFont(p11);
                erroruPrice.setForeground(Color.red);

// errormINoSer
                errormINoSer.setFont(p11);
                errormINoSer.setForeground(Color.red);

// errormIStat
                errormIStat.setFont(p11);
                errormIStat.setForeground(Color.red);

// erroritemPic
                erroritemPic.setFont(p11);
                erroritemPic.setForeground(Color.red);

                GridBagConstraints gbc = new GridBagConstraints();
                //ADDS LABELS//
                gbc.insets = new Insets(3, 3, 3, 3);
                gbc.gridx = 0;
                gbc.fill = GridBagConstraints.NONE;
                gbc.anchor = GridBagConstraints.CENTER;
                gbc.weightx = 0;
                // Customer ID label
                gbc.gridx = 0;
                gbc.gridy = 0;
                miniPanel.add(new JLabel(" "), gbc);//img
                gbc.gridwidth = 2; // Make it span 2 columns
                gbc.weightx = 1;   // To istribute space evenly
                gbc.gridy++;
                miniPanel.add(profilePreviewLabel, gbc);
                gbc.gridwidth = 1;
                gbc.weightx = 0;
                gbc.anchor = GridBagConstraints.WEST;
                //fillers
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(itemPic, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(menuItemIdL, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(menuCatIdL, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(menuItemNameL, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(uPriceL, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(mINoSerL, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(mIStatL, gbc);
                gbc.gridy++;

                //======== FIELDS ========
                gbc.gridx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.weightx = 1.0;
                gbc.gridy = 1;

                miniPanel.add(new JLabel(" "), gbc);
                //fillers
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                // img
                gbc.gridy++;
                miniPanel.add(img, gbc);
                gbc.gridy++;
                miniPanel.add(erroritemPic, gbc);
                gbc.gridy++;
                //fillers
                miniPanel.add(menuItemId, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(mCid, gbc);
                gbc.gridy++;
                miniPanel.add(errormenuCatId, gbc);
                gbc.gridy++;
                miniPanel.add(menuItemName, gbc);
                gbc.gridy++;
                miniPanel.add(errormenuItemName, gbc);
                gbc.gridy++;
                miniPanel.add(menuItemUnitP, gbc);
                gbc.gridy++;
                miniPanel.add(erroruPrice, gbc);
                gbc.gridy++;
                miniPanel.add(menuItemNoServe, gbc);
                gbc.gridy++;
                miniPanel.add(errormINoSer, gbc);
                gbc.gridy++;
                miniPanel.add(mIStat, gbc);
                gbc.gridy++;
                miniPanel.add(errormIStat, gbc);

                JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
                btnPanel.setBorder(b);
                btnPanel.setOpaque(false);
                JButton cancelBtn = new BtnRed("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
                JButton updateBtn = new BtnDefault("Update", new ImageIcon(getClass().getResource("/images/icon/update.png")));
                btnPanel.add(cancelBtn);
                btnPanel.add(updateBtn);

                cancelBtn.addActionListener(e -> updateDialog.dispose());
                updateBtn.addActionListener(e -> {
                    errormenuCatId.setText("");
                    errormenuItemName.setText("");
                    erroruPrice.setText("");
                    errormINoSer.setText("");
                    errormIStat.setText("");
                    erroritemPic.setText("");

                    String mItemId = menuItemId.getText().trim();
                    String mCatId = menuCatID.getSelectedItem().toString().split(" - ")[0];
                    String mName = menuItemName.getText().trim();
                    String mUprice = menuItemUnitP.getText().trim();
                    String mNoServe = menuItemNoServe.getText().trim();
                    String mStat = menuItemStat.getSelectedItem().toString();

                    boolean hasError = false;
                    if (mCatId.equals("SELECT")) {
                        errormenuCatId.setText("Menu Category ID is required.");
                        menuCatID.setForeground(Color.red);
                        hasError = true;
                    }
                    menuCatID.setForeground(Color.BLACK);

                    if (mName.equals("Required") || mName.length() > 25) {
                        errormenuItemName.setText("Menu Item Name is required.");
                        menuItemName.setForeground(Color.red);
                        hasError = true;
                    }
                    if (mName.length() > 25) {
                        errormenuItemName.setText("Menu Item Name should not exceed 25 characters");
                        menuItemName.setForeground(Color.red);
                        hasError = true;
                    }
                    if (mUprice.equals("0.00 (Required)")) {
                        erroruPrice.setText("Unit Price is required.");
                        menuItemUnitP.setForeground(Color.red);
                        hasError = true;
                    }
                    if (mNoServe.equals("0.00 (Required)")) {
                        errormINoSer.setText("Serving No. is required.");
                        menuItemNoServe.setForeground(Color.red);
                        hasError = true;
                    }
                    if (mStat.equals("SELECT")) {
                        errormIStat.setText("Menu Item Status is required.");
                        menuItemStat.setForeground(Color.red);
                        hasError = true;
                    }
                    menuItemStat.setForeground(Color.BLACK);

                    lblImagePath.setForeground(Color.BLACK);
                    if (hasError) {
                        JOptionPane.showMessageDialog(updateDialog, "Please fix the errors in the form.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int mItemIdNum = 0;
                    int mCatIdNum = 0;
                    int mServeNum = 0;
                    double mUpriceNum = 0.0;

                    // Parse mItemId
                    try {
                        mItemIdNum = Integer.parseInt(mItemId);
                    } catch (NumberFormatException ae) {
                        JOptionPane.showMessageDialog(this, "Invalid Item ID. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return; // stop further execution
                    }

                    // Parse mCatId
                    try {
                        String selectedCat = menuCatID.getSelectedItem().toString();
                        mCatIdNum = Integer.parseInt(mCatId);
                    } catch (NumberFormatException | NullPointerException ae) {
                        JOptionPane.showMessageDialog(this, "Invalid Category selection.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Parse mUprice
                    try {
                        mUpriceNum = Double.parseDouble(mUprice);
                    } catch (NumberFormatException ae) {
                        JOptionPane.showMessageDialog(this, "Invalid Unit Price. Please enter a valid decimal number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Parse NumServe
                    try {
                        mServeNum = Integer.parseInt(mNoServe);
                    } catch (NumberFormatException ae) {
                        JOptionPane.showMessageDialog(this, "Invalid number of seats. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return; // stop further execution
                    }

                    if (mServeNum <= 0) {
                        JOptionPane.showMessageDialog(this, "Invalid number of seats. Please enter a number greater than 0.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        errormINoSer.setText("Please enter a number greater than 0.");
                        menuItemNoServe.setForeground(Color.red);
                        return; // stop further execution
                    }
if (pfpChooser != null && pfpChooser.getSelectedFile() != null) {
    imageFile = pfpChooser.getSelectedFile();

    // Check if valid PNG file
    if (imageFile.exists() && imageFile.getName().toLowerCase().endsWith(".png")) {
        // Update with image
        String updateSql = "UPDATE MENUITEM SET MICatID = ?, MIName = ?, UPrice = ?, NServing = ?, MIStatus = ?, ItemPicture = ? WHERE MenuItemID = ?";

        try (FileInputStream fis = new FileInputStream(imageFile);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            updateStmt.setInt(1, mCatIdNum);
            updateStmt.setString(2, mName);
            updateStmt.setDouble(3, mUpriceNum);
            updateStmt.setInt(4, mServeNum);
            updateStmt.setString(5, mStat);
            updateStmt.setBinaryStream(6, fis, (int) imageFile.length());
            updateStmt.setInt(7, mItemIdNum);

            updateStmt.executeUpdate();

            JOptionPane.showMessageDialog(updateDialog, "Menu Item ID No. " + mItemId + " updated successfully.");
            updateDialog.dispose();
            retrieveMenuItemTableData();

        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Menu item image file not found.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(updateDialog, "Update failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(MenuItemGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    } else {
        JOptionPane.showMessageDialog(this, "Invalid image file. Only .png files are allowed.", "Invalid File", JOptionPane.WARNING_MESSAGE);
    }

} else {
    // Update without image
    String updateSql = "UPDATE MENUITEM SET MICatID = ?, MIName = ?, UPrice = ?, NServing = ?, MIStatus = ? WHERE MenuItemID = ?";

    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
        updateStmt.setInt(1, mCatIdNum);
        updateStmt.setString(2, mName);
        updateStmt.setDouble(3, mUpriceNum);
        updateStmt.setInt(4, mServeNum);
        updateStmt.setString(5, mStat);
        updateStmt.setInt(6, mItemIdNum);

        updateStmt.executeUpdate();

        JOptionPane.showMessageDialog(updateDialog, "Menu Item ID " + mItemId + " updated successfully.");
        updateDialog.dispose();
        retrieveMenuItemTableData();

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(updateDialog, "Update failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
                });
                CNavScrollPane scrollPane = new CNavScrollPane(miniPanel);
                scrollPane.setOpaque(false);
                scrollPane.setBorder(null);
                scrollPane.getViewport().setOpaque(false);
                updateDialog.add(mainPanel, BorderLayout.CENTER);
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(scrollPane, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);
                updateDialog.setLocationRelativeTo(null);
                updateDialog.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Menu Item ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }

    }

    private void menuItemSearch() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Menu Item ID to Search:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Menu Item ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM MENUITEM WHERE MenuItemID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog searchDialog = new JDialog((Frame) null, "Search Menu Item", true);
                searchDialog.setSize(550, 665);
                searchDialog.setLayout(new BorderLayout());
                searchDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title Panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/blue1.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);

                JLabel title = new JLabel("Search Menu Item");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                // Form Panel
                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);

                //============ FIELDS/COMBO BOX =============
                menuItemId = new RTextField(30);
                menuItemId.setText(rs.getString("MenuItemID"));
                meCatId = new RTextField(30);
                meCatId.setText(rs.getString("MICatID"));
                menuItemName = new RTextField(30);
                menuItemName.setText(rs.getString("MIName"));
                menuItemUnitP = new RTextField(30);
                menuItemUnitP.setText(rs.getString("UPrice"));
                menuItemNoServe = new RTextField(30);
                menuItemNoServe.setText(rs.getString("NServing"));
                mstat = new RTextField(30);
                mstat.setText(rs.getString("MIStatus"));

                // Shared preferred size
                Dimension textFieldSize = new Dimension(0, 32);

// menuItemId
                menuItemId.setFont(p14);
                menuItemId.setBorder(b);
                menuItemId.setPreferredSize(textFieldSize);
                menuItemId.setDisabledTextColor(Color.BLACK);
                menuItemId.setEnabled(false);

// meCatId
                meCatId.setFont(p14);
                meCatId.setBorder(b);
                meCatId.setPreferredSize(textFieldSize);
                meCatId.setDisabledTextColor(Color.BLACK);
                meCatId.setEnabled(false);

// menuItemName
                menuItemName.setFont(p14);
                menuItemName.setBorder(b);
                menuItemName.setPreferredSize(textFieldSize);
                menuItemName.setDisabledTextColor(Color.BLACK);
                menuItemName.setEnabled(false);

// menuItemUnitP
                menuItemUnitP.setFont(p14);
                menuItemUnitP.setBorder(b);
                menuItemUnitP.setPreferredSize(textFieldSize);
                menuItemUnitP.setDisabledTextColor(Color.BLACK);
                menuItemUnitP.setEnabled(false);

// menuItemNoServe
                menuItemNoServe.setFont(p14);
                menuItemNoServe.setBorder(b);
                menuItemNoServe.setPreferredSize(textFieldSize);
                menuItemNoServe.setDisabledTextColor(Color.BLACK);
                menuItemNoServe.setEnabled(false);

// mstat
                mstat.setFont(p14);
                mstat.setBorder(b);
                mstat.setPreferredSize(textFieldSize);
                mstat.setDisabledTextColor(Color.BLACK);
                mstat.setEnabled(false);

                //=============== LABELS ================
                menuItemIdL = new JLabel("Menu Item ID:");
                menuCatIdL = new JLabel("Menu Category ID:");
                menuItemNameL = new JLabel("Menu Item Name:");
                uPriceL = new JLabel("Unit Price:");
                mINoSerL = new JLabel("Serving No.:");
                mIStatL = new JLabel("Menu Item Status:");
                itemPic = new JLabel("Item Picture:");

                // Create the image preview label with default image
                profilePreviewLabel = new JLabel();
                profilePreviewLabel.setPreferredSize(new Dimension(150, 150));
                profilePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                profilePreviewLabel.setHorizontalAlignment(JLabel.CENTER);
                profilePreviewLabel.setVerticalAlignment(JLabel.CENTER);

                // Retrieve and display the image if it exists
                byte[] imageBytes = rs.getBytes("ItemPicture"); // Assuming image is stored in a column named "UserImage"
                if (imageBytes != null && imageBytes.length > 0) {
                    ImageIcon imageIcon = new ImageIcon(imageBytes);
                    Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    profilePreviewLabel.setIcon(new ImageIcon(image));
                } else {
                    // If no image is found, display the default image
                    ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/images/profilePicture/defaultProfile.png"));
                    Image scaledImage = defaultIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    profilePreviewLabel.setIcon(new ImageIcon(scaledImage));
                }

                // Shared preferred size
                Dimension labelSize = new Dimension(145, 32);

// menuItemIdL
                menuItemIdL.setFont(p14);
                menuItemIdL.setPreferredSize(labelSize);

// menuCatIdL
                menuCatIdL.setFont(p14);
                menuCatIdL.setPreferredSize(labelSize);

// menuItemNameL
                menuItemNameL.setFont(p14);
                menuItemNameL.setPreferredSize(labelSize);

// uPriceL
                uPriceL.setFont(p14);
                uPriceL.setPreferredSize(labelSize);

// mINoSerL
                mINoSerL.setFont(p14);
                mINoSerL.setPreferredSize(labelSize);

// mIStatL
                mIStatL.setFont(p14);
                mIStatL.setPreferredSize(labelSize);

// itemPic
                itemPic.setFont(p14);
                itemPic.setPreferredSize(labelSize);

                // GridBagConstraints setup
                GridBagConstraints gbc = new GridBagConstraints();

                gbc.fill = GridBagConstraints.BOTH;

                //ADDS LABELS//
                gbc.insets = new Insets(3, 3, 3, 3);
                gbc.gridx = 0;
                gbc.fill = GridBagConstraints.NONE;
                gbc.anchor = GridBagConstraints.CENTER;
                gbc.weightx = 0;
                // Customer ID label
                gbc.gridx = 0;
                gbc.gridy = 0;
                miniPanel.add(new JLabel(" "), gbc);//img
                gbc.gridwidth = 2; // Make it span 2 columns
                gbc.weightx = 1;   // To istribute space evenly
                gbc.gridy++;
                miniPanel.add(profilePreviewLabel, gbc);
                gbc.gridwidth = 1;
                gbc.weightx = 0;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(menuItemIdL, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(menuCatIdL, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(menuItemNameL, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(uPriceL, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(mINoSerL, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(mIStatL, gbc);

                //======== FIELDS ========
                gbc.gridx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.weightx = 1.0;
                gbc.gridy = 1;
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                //fillers
                gbc.gridy++;
                miniPanel.add(menuItemId, gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(meCatId, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(menuItemName, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(menuItemUnitP, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(menuItemNoServe, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(mstat, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer

                JButton returnBtn = new BtnDefault("Return", new ImageIcon(getClass().getResource("/images/icon/return.png")));
                JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
                btnPanel.setOpaque(false);
                btnPanel.setBorder(b);
                btnPanel.add(returnBtn);

                returnBtn.addActionListener(e -> searchDialog.dispose());
                retrieveMenuItemTableData();
                CNavScrollPane scrollPane = new CNavScrollPane(miniPanel);
                scrollPane.setOpaque(false);
                scrollPane.setBorder(null);
                scrollPane.getViewport().setOpaque(false);
                searchDialog.add(mainPanel, BorderLayout.CENTER);
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(scrollPane, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);
                searchDialog.setLocationRelativeTo(null);
                searchDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Menu Item ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void menuItemDelete() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Menu Item ID to Delete:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Menu Item ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM MENUITEM WHERE MenuItemID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog deleteDialog = new JDialog((Frame) null, "Delete Menu Item", true);
                deleteDialog.setSize(550, 665);
                deleteDialog.setLayout(new BorderLayout());
                deleteDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title Panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/red.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);

                JLabel title = new JLabel("Delete Menu Item");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                // Form Panel
                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);

                profilePreviewLabel = new JLabel();
                profilePreviewLabel.setPreferredSize(new Dimension(150, 150));
                profilePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                profilePreviewLabel.setHorizontalAlignment(JLabel.CENTER);
                profilePreviewLabel.setVerticalAlignment(JLabel.CENTER);

                //============ FIELDS/COMBO BOX =============
                menuItemId = new RTextField(30);
                menuItemId.setText(rs.getString("MenuItemID"));
                meCatId = new RTextField(30);
                meCatId.setText(rs.getString("MICatID"));
                menuItemName = new RTextField(30);
                menuItemName.setText(rs.getString("MIName"));
                menuItemUnitP = new RTextField(30);
                menuItemUnitP.setText(rs.getString("UPrice"));
                menuItemNoServe = new RTextField(30);
                menuItemNoServe.setText(rs.getString("NServing"));
                mstat = new RTextField(30);
                mstat.setText(rs.getString("MIStatus"));

                // Retrieve and display the image if it exists
                byte[] imageBytes = rs.getBytes("ItemPicture"); // Assuming image is stored in a column named "UserImage"
                if (imageBytes != null && imageBytes.length > 0) {
                    ImageIcon imageIcon = new ImageIcon(imageBytes);
                    Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    profilePreviewLabel.setIcon(new ImageIcon(image));
                } else {
                    // If no image is found, display the default image
                    ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/images/profilePicture/defaultProfile.png"));
                    Image scaledImage = defaultIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    profilePreviewLabel.setIcon(new ImageIcon(scaledImage));
                }
// Shared preferred size
                Dimension textFieldSize = new Dimension(0, 32);

// menuItemId
                menuItemId.setFont(p14);
                menuItemId.setBorder(b);
                menuItemId.setPreferredSize(textFieldSize);
                menuItemId.setDisabledTextColor(Color.BLACK);
                menuItemId.setEnabled(false);

// meCatId
                meCatId.setFont(p14);
                meCatId.setBorder(b);
                meCatId.setPreferredSize(textFieldSize);
                meCatId.setDisabledTextColor(Color.BLACK);
                meCatId.setEnabled(false);

// menuItemName
                menuItemName.setFont(p14);
                menuItemName.setBorder(b);
                menuItemName.setPreferredSize(textFieldSize);
                menuItemName.setDisabledTextColor(Color.BLACK);
                menuItemName.setEnabled(false);

// menuItemUnitP
                menuItemUnitP.setFont(p14);
                menuItemUnitP.setBorder(b);
                menuItemUnitP.setPreferredSize(textFieldSize);
                menuItemUnitP.setDisabledTextColor(Color.BLACK);
                menuItemUnitP.setEnabled(false);

// menuItemNoServe
                menuItemNoServe.setFont(p14);
                menuItemNoServe.setBorder(b);
                menuItemNoServe.setPreferredSize(textFieldSize);
                menuItemNoServe.setDisabledTextColor(Color.BLACK);
                menuItemNoServe.setEnabled(false);

// mstat
                mstat.setFont(p14);
                mstat.setBorder(b);
                mstat.setPreferredSize(textFieldSize);
                mstat.setDisabledTextColor(Color.BLACK);
                mstat.setEnabled(false);

                //=============== LABELS ================
                menuItemIdL = new JLabel("Menu Item ID:");
                menuCatIdL = new JLabel("Menu Category ID:");
                menuItemNameL = new JLabel("Menu Item Name:");
                uPriceL = new JLabel("Unit Price:");
                mINoSerL = new JLabel("Serving No.:");
                mIStatL = new JLabel("Menu Item Status:");
                itemPic = new JLabel("Item Picture:");

                // Shared preferred size
                Dimension labelSize = new Dimension(145, 32);

// menuItemIdL
                menuItemIdL.setFont(p14);
                menuItemIdL.setPreferredSize(labelSize);

// menuCatIdL
                menuCatIdL.setFont(p14);
                menuCatIdL.setPreferredSize(labelSize);

// menuItemNameL
                menuItemNameL.setFont(p14);
                menuItemNameL.setPreferredSize(labelSize);

// uPriceL
                uPriceL.setFont(p14);
                uPriceL.setPreferredSize(labelSize);

// mINoSerL
                mINoSerL.setFont(p14);
                mINoSerL.setPreferredSize(labelSize);

// mIStatL
                mIStatL.setFont(p14);
                mIStatL.setPreferredSize(labelSize);

// itemPic
                itemPic.setFont(p14);
                itemPic.setPreferredSize(labelSize);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.BOTH;

                //ADDS LABELS//
                gbc.insets = new Insets(3, 3, 3, 3);
                gbc.gridx = 0;
                gbc.fill = GridBagConstraints.NONE;
                gbc.anchor = GridBagConstraints.CENTER;
                gbc.weightx = 0;
                // Customer ID label
                gbc.gridx = 0;
                gbc.gridy = 0;
                miniPanel.add(new JLabel(" "), gbc);//img
                gbc.gridwidth = 2; // Make it span 2 columns
                gbc.weightx = 1;   // To istribute space evenly
                gbc.gridy++;
                miniPanel.add(profilePreviewLabel, gbc);
                gbc.gridwidth = 1;
                gbc.weightx = 0;
                gbc.anchor = GridBagConstraints.WEST;
                //fillers
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(menuItemIdL, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(menuCatIdL, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(menuItemNameL, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(uPriceL, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(mINoSerL, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(mIStatL, gbc);

                //======== FIELDS ========
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.gridx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.weightx = 1.0;
                gbc.gridy = 1;

                miniPanel.add(new JLabel(" "), gbc);
                //fillers
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);
                // img
                gbc.gridy++;
                miniPanel.add(menuItemId, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(meCatId, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(menuItemName, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(menuItemUnitP, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(menuItemNoServe, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(mstat, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer

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
                    try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM MENUITEM WHERE MenuItemID = ?")) {
                        deleteStmt.setString(1, inputID);
                        deleteStmt.executeUpdate();
                        JOptionPane.showMessageDialog(deleteDialog, "Menu Item No. " + inputID + " was deleted successfully.");
                        retrieveMenuItemTableData();
                        deleteDialog.dispose();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(deleteDialog, "Delete failed: " + ex.getMessage());
                    }
                });
                cancelBtn.addActionListener(e -> deleteDialog.dispose());
                CNavScrollPane scrollPane = new CNavScrollPane(miniPanel);
                scrollPane.setOpaque(false);
                scrollPane.setBorder(null);
                scrollPane.getViewport().setOpaque(false);
                deleteDialog.add(mainPanel, BorderLayout.CENTER);
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(scrollPane, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);
                deleteDialog.setLocationRelativeTo(null);
                deleteDialog.setVisible(true);
                retrieveMenuItemTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Menu Item ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }

    }

    private void likeSearchMenuItems() {
        String[] fields = {"MenuItemID", "MICatID", "MIName", "UPrice", "NServing", "MIStatus"};
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

            String sql = "SELECT * FROM MENUITEM WHERE " + field + " LIKE ?";
            try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, keyword);
                try (ResultSet rs = stmt.executeQuery()) {
                    menuItemTableModel.setRowCount(0); // clear table

                    while (rs.next()) {
                        menuItemTableModel.addRow(new Object[]{
                            rs.getInt("MenuItemID"),
                            rs.getInt("MICatID"),
                            rs.getString("MIName"),
                            rs.getBigDecimal("UPrice"),
                            rs.getInt("NServing"),
                            rs.getString("MIStatus")
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

    private void sortMenuItems() {
        String[] fields = {"MenuItemID", "MICatID", "MIName", "UPrice", "NServing", "MIStatus"};
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

        String sql = "SELECT * FROM MENUITEM ORDER BY " + field + " " + order;

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            menuItemTableModel.setRowCount(0); // clear table

            while (rs.next()) {
                menuItemTableModel.addRow(new Object[]{
                    rs.getInt("MenuItemID"),
                    rs.getInt("MICatID"),
                    rs.getString("MIName"),
                    rs.getBigDecimal("UPrice"),
                    rs.getInt("NServing"),
                    rs.getString("MIStatus")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error sorting: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String[] getAllMenuCategoryIDs() {
        List<String> categoryIDList = new ArrayList<>();
        String query = "SELECT MICatID, CatDes FROM MENUITEMCATEGORY";

        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("MICatID");
                String name = rs.getString("CatDes");
                categoryIDList.add(id + " - " + name); // format: "1 - MEAL"
            }

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }

        if (categoryIDList.isEmpty()) {
            return new String[]{"SELECT"};
        }

        return categoryIDList.toArray(new String[0]);
    }
}
