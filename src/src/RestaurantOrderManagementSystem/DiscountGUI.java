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
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

public class DiscountGUI extends Components {

    private final String[] discountNames = {"DisID", "FName", "Mname", "LName", "Expdate", "DisCat", "Mun", "Prov"};
    private final DefaultTableModel discountTableModel = new DisabledTableModel(discountNames, 0);
    private JTable discountTable = Components.updateTable(new JTable(discountTableModel));
    private RTextField disIDField, fnameField, mnameField, lnameField, muniField, provField, category, disdate;
    private DateSpinner disDate = new DateSpinner();
    private String[] disCat = {"SELECT", "SENIOR CITIZEN", "PWD"};
    private JComboBox<String> catBox = new CustomComboBox<>(disCat);
    private RPanel date, discat;
    private JLabel discountId;
    private JLabel disfname;
    private JLabel dismname;
    private JLabel dislname;
    private JLabel disEdate;
    private JLabel disMun;
    private JLabel disProv;
    private JLabel disCategory;
    private JLabel errorDisId;
    private JLabel errordisfname;
    private JLabel errordislname;
    private JLabel errordisMun;
    private JLabel errordisProv;
    private JLabel errordisCategory;

    private final JPanel discountPanel;

    public DiscountGUI() {
        // Retrieve initial data
        retrieveDiscountTableData();
        discountTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        DefaultTableCellRenderer centerRenderer = new CustomRowRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        discountTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // DisID
        discountTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // FName
        discountTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Mname
        discountTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // LName
        discountTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Expdate
        discountTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // DisCat
        discountTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); // Mun
        discountTable.getColumnModel().getColumn(7).setCellRenderer(centerRenderer); // Prov

        // Set up main panel
        discountPanel = new JPanel(new BorderLayout());
        discountPanel.setBorder(b);
        discountPanel.setOpaque(false);

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
                sortDiscounts();
            } else if (choice == 1) {
                likeSearchDiscounts();
            } else if (choice == 2) {
                retrieveDiscountTableData();
            }
        });

        JButton refreshBtn = new BtnCustom(
                new ImageIcon(getClass().getResource("/images/icon/refresh.png")),
                new ImageIcon(getClass().getResource("/images/icon/refreshGreen.png")),
                "Refresh"
        );
        refreshBtn.addActionListener(e -> retrieveDiscountTableData());

        headPanel.add(refreshBtn);
        headPanel.add(filterBtn);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(b);
        JScrollPane tableScrollPane = new CScrollPane(discountTable()); // assuming discountTable() returns JTable
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
        createButton.addActionListener(e -> discountCreate());
        updateButton.addActionListener(e -> discountUpdate());
        searchButton.addActionListener(e -> discountSearch());
        deleteButton.addActionListener(e -> discountDelete());

        // Assemble components
        discountPanel.add(headPanel, BorderLayout.NORTH);
        discountPanel.add(tablePanel, BorderLayout.CENTER);
        discountPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }

    public JPanel getDiscountPanel() {
        return discountPanel;
    }

    private JScrollPane discountTable() {
        discountTable = Components.updateTable(discountTable);
        discountTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        discountTable.setFillsViewportHeight(true);
        JScrollPane discountPane = new JScrollPane(discountTable);
        retrieveDiscountTableData();
        return discountPane;
    }

    private void retrieveDiscountTableData() {
        String sql = "SELECT * FROM DISCOUNT";

        try (Connection conn = (Connection) Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            DefaultTableModel model = (DefaultTableModel) discountTable.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                    rs.getString("DiscountID"),
                    rs.getString("FName"),
                    rs.getString("MName"),
                    rs.getString("LName"),
                    rs.getString("EDate"),
                    rs.getString("DCategory"),
                    rs.getString("Mun"),
                    rs.getString("Prov"),};
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading table data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void discountCreate() {
        JDialog createDialog = new JDialog((Frame) null, "Create Discount", true);
        createDialog.setSize(500, 710);
        createDialog.setLayout(new BorderLayout());
        createDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
        mainPanel.setLayout(new BorderLayout());

        // Title Panel
        JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/green.jpg")).getImage());
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(b);

        JLabel title = new JLabel("Create Discount");
        title.setFont(b18);
        title.setForeground(Color.white);
        title.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(title, BorderLayout.CENTER);

        // Form Panel
        JPanel miniPanel = new JPanel(new GridBagLayout());
        miniPanel.setOpaque(false);
        miniPanel.setBorder(b);

        disIDField = new RTextField(" Required", 30);
        fnameField = new RTextField(" Required", 30);
        mnameField = new RTextField(" Optional", 30);
        lnameField = new RTextField(" Required", 30);
        disDate.setValue(new Date());
        muniField = new RTextField(" Required", 30);
        provField = new RTextField(" Required", 30);

        discountId = new JLabel("Discount ID:");
        disfname = new JLabel("First Name:");
        dismname = new JLabel("Middle Name:");
        dislname = new JLabel("Last Name:");
        disEdate = new JLabel("Expiry Date:");
        disMun = new JLabel("Municipality:");
        disProv = new JLabel("Province:");
        disCategory = new JLabel("Category:");
        disDate.setValue(new Date());
        errorDisId = new JLabel(" ");
        errordisfname = new JLabel(" ");
        errordislname = new JLabel(" ");
        errordisMun = new JLabel(" ");
        errordisProv = new JLabel(" ");
        errordisCategory = new JLabel(" ");

        disIDField.setBorder(b);
        fnameField.setBorder(b);
        mnameField.setBorder(b);
        lnameField.setBorder(b);
        disDate.setBorder(b);
        muniField.setBorder(b);
        provField.setBorder(b);

        // Shared sizes
        Dimension labelSize = new Dimension(110, 32);
        Dimension textFieldSize = new Dimension(0, 32);

// Labels
        discountId.setFont(p14);
        discountId.setPreferredSize(labelSize);

        disfname.setFont(p14);
        disfname.setPreferredSize(labelSize);

        dismname.setFont(p14);
        dismname.setPreferredSize(labelSize);

        dislname.setFont(p14);
        dislname.setPreferredSize(labelSize);

        disMun.setFont(p14);
        disMun.setPreferredSize(labelSize);

        disProv.setFont(p14);
        disProv.setPreferredSize(labelSize);

        disEdate.setFont(p14);
        disEdate.setPreferredSize(labelSize);

        disCategory.setFont(p14);
        disCategory.setPreferredSize(labelSize);

// Error Labels
        errorDisId.setFont(p11);
        errorDisId.setForeground(Color.red);

        errordisfname.setFont(p11);
        errordisfname.setForeground(Color.red);

        errordislname.setFont(p11);
        errordislname.setForeground(Color.red);

        errordisMun.setFont(p11);
        errordisMun.setForeground(Color.red);

        errordisProv.setFont(p11);
        errordisProv.setForeground(Color.red);

        errordisCategory.setFont(p11);
        errordisCategory.setForeground(Color.red);

// Text Fields
        disIDField.setFont(p14);
        disIDField.setBorder(b);
        disIDField.setPreferredSize(textFieldSize);

        fnameField.setFont(p14);
        fnameField.setBorder(b);
        fnameField.setPreferredSize(textFieldSize);

        mnameField.setFont(p14);
        mnameField.setBorder(b);
        mnameField.setPreferredSize(textFieldSize);

        lnameField.setFont(p14);
        lnameField.setBorder(b);
        lnameField.setPreferredSize(textFieldSize);

        muniField.setFont(p14);
        muniField.setBorder(b);
        muniField.setPreferredSize(textFieldSize);

        provField.setFont(p14);
        provField.setBorder(b);
        provField.setPreferredSize(textFieldSize);

        date = new RPanel(30);
        date.setLayout(new BorderLayout());
        date.add(disDate, BorderLayout.CENTER);
        disDate.setEnabled(true);
        date.setBorder(b);
        date.setPreferredSize(new Dimension(0, 32));
        date.setBackground(Color.WHITE);
        disDate.setBackground(Color.WHITE);
        disDate.setFont(p14);
        disDate.setBorder(null);

        discat = new RPanel(30);
        discat.setLayout(new BorderLayout());
        discat.add(catBox, BorderLayout.CENTER);
        catBox.setEnabled(true);
        discat.setBorder(b);
        discat.setPreferredSize(new Dimension(0, 40));
        catBox.setBackground(Color.WHITE);
        discat.setBackground(Color.WHITE);
        catBox.setFont(p14);
        catBox.setBorder(null);

        fnameField.setEnabled(true);
        mnameField.setEnabled(true);
        lnameField.setEnabled(true);
        muniField.setEnabled(true);
        provField.setEnabled(true);
        catBox.setEnabled(true);
        disDate.setEnabled(true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.gridy = 0;
        miniPanel.add(discountId, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(disfname, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(dismname, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(dislname, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(disEdate, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(disCategory, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(disMun, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(disProv, gbc);

        //======== FIELDS ========
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        miniPanel.add(disIDField, gbc);
        gbc.gridy++;
        miniPanel.add(errorDisId, gbc);
        gbc.gridy++;
        miniPanel.add(fnameField, gbc);
        gbc.gridy++;
        miniPanel.add(errordisfname, gbc);
        gbc.gridy++;
        miniPanel.add(mnameField, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(lnameField, gbc);
        gbc.gridy++;
        miniPanel.add(errordislname, gbc);
        gbc.gridy++;
        miniPanel.add(date, gbc);
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc); // Spacer
        gbc.gridy++;
        miniPanel.add(discat, gbc);
        gbc.gridy++;
        miniPanel.add(errordisCategory, gbc);
        gbc.gridy++;
        miniPanel.add(muniField, gbc);
        gbc.gridy++;
        miniPanel.add(errordisMun, gbc);
        gbc.gridy++;
        miniPanel.add(provField, gbc);
        gbc.gridy++;
        miniPanel.add(errordisProv, gbc);

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
            errorDisId.setText(" ");
            errordisfname.setText(" ");
            errordislname.setText(" ");
            errordisMun.setText(" ");
            errordisProv.setText(" ");
            errordisCategory.setText(" ");

            String disId = disIDField.getText().trim();
            String firstName = fnameField.getText().trim();
            String middleName = mnameField.getText().trim();
            String lastName = lnameField.getText().trim();
            String expDate = disDate.getDate().trim();
            String municipality = muniField.getText().trim();
            String province = provField.getText().trim();
            String category = catBox.getSelectedItem().toString();

            if (disId.length() > 20) {
                JOptionPane.showMessageDialog(createDialog, "Discount ID should not exceed 20 alphanumeric characters..", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean hasError = false;
            if (disId.equals("Required")) {
                errorDisId.setText("Discount ID is required.");
                disIDField.setForeground(Color.red);
                hasError = true;
            }
            if (firstName.equals("Required")) {
                errordisfname.setText("First name is required.");
                fnameField.setForeground(Color.red);
                hasError = true;
            }
            if (middleName.equals("Optional")) {
                middleName = "NULL";
            }
            if (lastName.equals("Required")) {
                errordislname.setText("Last name is required.");
                lnameField.setForeground(Color.red);
                hasError = true;
            }
            if (municipality.equals("Required")) {
                errordisMun.setText("Municipality is required.");
                muniField.setForeground(Color.red);
                hasError = true;
            }
            if (province.equals("Required")) {
                errordisProv.setText("Province is required.");
                provField.setForeground(Color.red);
                hasError = true;
            }
            if (category.equals("SELECT")) {
                errordisCategory.setText("Discount Category is required.");
                catBox.setForeground(Color.red);
                hasError = true;
            }
            catBox.setForeground(Color.BLACK);
            if (hasError) {
                JOptionPane.showMessageDialog(createDialog, "Please fix the errors in the form.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "INSERT INTO DISCOUNT (DiscountID, FName, MName, LName, EDate, DCategory, Mun, Prov) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, disId);
                stmt.setString(2, firstName);
                stmt.setString(3, middleName);
                stmt.setString(4, lastName);
                stmt.setString(5, expDate);
                stmt.setString(6, category);
                stmt.setString(7, municipality);
                stmt.setString(8, province);

                stmt.executeUpdate();
                retrieveDiscountTableData();
                JOptionPane.showMessageDialog(createDialog, "Discount added successfully.");
                createDialog.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(createDialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(miniPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        createDialog.add(mainPanel);
        createDialog.setLocationRelativeTo(null);
        createDialog.setVisible(true);
    }

    private void discountUpdate() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Discount ID to Update:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Discount ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM Discount WHERE DiscountID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();
            String originalDiscountID = inputID; // save the original DiscountID

            if (rs.next()) {
                JDialog updateDialog = new JDialog((Frame) null, "Update Discount", true);
                updateDialog.setSize(500, 710);
                updateDialog.setLayout(new BorderLayout());
                updateDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title Panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/orange.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);

                JLabel title = new JLabel("Update Discount");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                // Form Panel
                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);

                disIDField = new RTextField(" Required", 30);
                disIDField.setText(rs.getString("DiscountID"));

                fnameField = new RTextField(" Required", 30);
                fnameField.setText(rs.getString("FName"));

                mnameField = new RTextField(" Optional", 30);
                mnameField.setText(rs.getString("MName"));

                lnameField = new RTextField(" Required", 30);
                lnameField.setText(rs.getString("LName"));

                disDate.setValue(new Date());
                disDate.setValue(rs.getDate("EDate"));
                muniField = new RTextField(" Required", 30);
                muniField.setText(rs.getString("Mun"));

                provField = new RTextField(" Required", 30);
                provField.setText(rs.getString("Prov"));

                catBox.setSelectedItem(rs.getString("DCategory"));

                discountId = new JLabel("Discount ID:");
                disfname = new JLabel("First Name:");
                dismname = new JLabel("Middle Name:");
                dislname = new JLabel("Last Name:");
                disEdate = new JLabel("Expiry Date:");
                disMun = new JLabel("Municipality:");
                disProv = new JLabel("Province:");
                disCategory = new JLabel("Category:");

                errorDisId = new JLabel(" ");
                errordisfname = new JLabel(" ");
                errordislname = new JLabel(" ");
                errordisMun = new JLabel(" ");
                errordisProv = new JLabel(" ");
                errordisCategory = new JLabel(" ");

                // Shared dimensions
                Dimension labelSize = new Dimension(110, 32);
                Dimension textFieldSize = new Dimension(0, 32);

// Labels
                discountId.setFont(p14);
                discountId.setPreferredSize(labelSize);

                disfname.setFont(p14);
                disfname.setPreferredSize(labelSize);

                dismname.setFont(p14);
                dismname.setPreferredSize(labelSize);

                dislname.setFont(p14);
                dislname.setPreferredSize(labelSize);

                disMun.setFont(p14);
                disMun.setPreferredSize(labelSize);

                disProv.setFont(p14);
                disProv.setPreferredSize(labelSize);

                disEdate.setFont(p14);
                disEdate.setPreferredSize(labelSize);

                disCategory.setFont(p14);
                disCategory.setPreferredSize(labelSize);

// Error Labels
                errorDisId.setFont(p11);
                errorDisId.setForeground(Color.red);

                errordisfname.setFont(p11);
                errordisfname.setForeground(Color.red);

                errordislname.setFont(p11);
                errordislname.setForeground(Color.red);

                errordisMun.setFont(p11);
                errordisMun.setForeground(Color.red);

                errordisProv.setFont(p11);
                errordisProv.setForeground(Color.red);

                errordisCategory.setFont(p11);
                errordisCategory.setForeground(Color.red);

// Text Fields
                disIDField.setFont(p14);
                disIDField.setBorder(b);
                disIDField.setPreferredSize(textFieldSize);
                disIDField.setForeground(Color.BLACK);

                fnameField.setFont(p14);
                fnameField.setBorder(b);
                fnameField.setPreferredSize(textFieldSize);
                fnameField.setForeground(Color.BLACK);

                mnameField.setFont(p14);
                mnameField.setBorder(b);
                mnameField.setPreferredSize(textFieldSize);
                mnameField.setForeground(Color.BLACK);

                lnameField.setFont(p14);
                lnameField.setBorder(b);
                lnameField.setPreferredSize(textFieldSize);
                lnameField.setForeground(Color.BLACK);

                muniField.setFont(p14);
                muniField.setBorder(b);
                muniField.setPreferredSize(textFieldSize);
                muniField.setForeground(Color.BLACK);

                provField.setFont(p14);
                provField.setBorder(b);
                provField.setPreferredSize(textFieldSize);
                provField.setForeground(Color.BLACK);

                date = new RPanel(30);
                date.setLayout(new BorderLayout());
                disDate.setEnabled(true);
                date.add(disDate, BorderLayout.CENTER);
                date.setBorder(new EmptyBorder(5, 5, 5, 5));
                date.setPreferredSize(new Dimension(0, 35));
                date.setBackground(Color.WHITE);
                disDate.setBackground(Color.WHITE);
                disDate.setFont(p14);
                disDate.setBorder(null);

                discat = new RPanel(30);
                discat.setLayout(new BorderLayout());
                discat.add(catBox, BorderLayout.CENTER);
                catBox.setEnabled(true);
                discat.setBorder(new EmptyBorder(5, 5, 5, 5));
                discat.setPreferredSize(new Dimension(0, 35));
                catBox.setBackground(Color.WHITE);
                discat.setBackground(Color.WHITE);
                catBox.setFont(p14);
                catBox.setBorder(null);

                fnameField.setEnabled(true);
                mnameField.setEnabled(true);
                lnameField.setEnabled(true);
                muniField.setEnabled(true);
                provField.setEnabled(true);
                catBox.setEnabled(true);
                disDate.setEnabled(true);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.BOTH;

                gbc.gridwidth = 1;
                gbc.gridx = 0;
                gbc.weightx = 0;
                gbc.gridy = 0;
                miniPanel.add(discountId, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(disfname, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(dismname, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(dislname, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(disEdate, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(disCategory, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(disMun, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(disProv, gbc);

                //======== FIELDS ========
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.weightx = 1.0;
                miniPanel.add(disIDField, gbc);
                gbc.gridy++;
                miniPanel.add(errorDisId, gbc);
                gbc.gridy++;
                miniPanel.add(fnameField, gbc);
                gbc.gridy++;
                miniPanel.add(errordisfname, gbc);
                gbc.gridy++;
                miniPanel.add(mnameField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(lnameField, gbc);
                gbc.gridy++;
                miniPanel.add(errordislname, gbc);
                gbc.gridy++;
                miniPanel.add(date, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(discat, gbc);
                gbc.gridy++;
                miniPanel.add(errordisCategory, gbc);
                gbc.gridy++;
                miniPanel.add(muniField, gbc);
                gbc.gridy++;
                miniPanel.add(errordisMun, gbc);
                gbc.gridy++;
                miniPanel.add(provField, gbc);
                gbc.gridy++;
                miniPanel.add(errordisProv, gbc);

                // Buttons Panel
                JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
                btnPanel.setBorder(b);
                btnPanel.setOpaque(false);
                JButton cancelBtn = new BtnRed("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
                JButton updateBtn = new BtnDefault("Update", new ImageIcon(getClass().getResource("/images/icon/update.png")));
                btnPanel.add(cancelBtn);
                btnPanel.add(updateBtn);

                cancelBtn.addActionListener(e -> updateDialog.dispose());
                updateBtn.addActionListener(e -> {
                    errorDisId.setText(" ");
                    errordisfname.setText(" ");
                    errordislname.setText(" ");
                    errordisMun.setText(" ");
                    errordisProv.setText(" ");
                    errordisCategory.setText(" ");

                    String disId = disIDField.getText().trim();
                    String firstName = fnameField.getText().trim();
                    String middleName = mnameField.getText().trim();
                    String lastName = lnameField.getText().trim();
                    String expDate = disDate.getDate().trim();
                    String municipality = muniField.getText().trim();
                    String province = provField.getText().trim();
                    String category = catBox.getSelectedItem().toString();

                    if (disId.length() > 20) {
                        JOptionPane.showMessageDialog(updateDialog, "Discount ID should not exceed 20 alphanumeric characters..", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    boolean hasError = false;
                    if (disId.equals("Required")) {
                        errorDisId.setText("Discount ID is required.");
                        disIDField.setForeground(Color.red);
                        hasError = true;
                    }
                    if (firstName.equals("Required")) {
                        errordisfname.setText("First name is required.");
                        fnameField.setForeground(Color.red);
                        hasError = true;
                    }
                    if (middleName.equals("Optional")) {
                        middleName = "NULL";
                    }
                    if (lastName.equals("Required")) {
                        errordislname.setText("Last name is required.");
                        lnameField.setForeground(Color.red);
                        hasError = true;
                    }
                    if (municipality.equals("Required")) {
                        errordisMun.setText("Municipality is required.");
                        muniField.setForeground(Color.red);
                        hasError = true;
                    }
                    if (province.equals("Required")) {
                        errordisProv.setText("Province is required.");
                        provField.setForeground(Color.red);
                        hasError = true;
                    }
                    if (category.equals("SELECT")) {
                        errordisCategory.setText("Discount Category is required.");
                        catBox.setForeground(Color.red);
                        hasError = true;
                    }
                    catBox.setForeground(Color.BLACK);
                    if (hasError) {
                        JOptionPane.showMessageDialog(updateDialog, "Please fix the errors in the form.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try (PreparedStatement updateStmt = conn.prepareStatement(
                            "UPDATE Discount SET DiscountID = ?, FName = ?, MName = ?, LName = ?, EDate = ?, DCategory = ?, Mun = ?, Prov = ? WHERE DiscountID = ?")) {
                        updateStmt.setString(1, disId);
                        updateStmt.setString(2, firstName);
                        updateStmt.setString(3, middleName);
                        updateStmt.setString(4, lastName);
                        updateStmt.setString(5, expDate);
                        updateStmt.setString(6, category);
                        updateStmt.setString(7, municipality);
                        updateStmt.setString(8, province);
                        updateStmt.setString(9, originalDiscountID);
                        updateStmt.executeUpdate();

                        JOptionPane.showMessageDialog(updateDialog, "Discount ID " + disId + " updated successfully.");
                        updateDialog.dispose();
                        retrieveDiscountTableData();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(updateDialog, "Update failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                updateDialog.add(mainPanel, BorderLayout.CENTER);
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(miniPanel, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);
                updateDialog.setLocationRelativeTo(null);
                updateDialog.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Discount ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void discountSearch() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Discount ID to Search:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Discount ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM Discount WHERE DiscountID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog searchDialog = new JDialog((Frame) null, "Search Discount", true);
                searchDialog.setSize(500, 710);
                searchDialog.setLayout(new BorderLayout());
                searchDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title Panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/blue1.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);

                JLabel title = new JLabel("Search Discount");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                // Form Panel
                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);
                RTextField categoryField = new RTextField(rs.getString("DCategory"), 30);
                categoryField.setBackground(Color.WHITE);
                categoryField.setEnabled(false);
                categoryField.setFont(p14);

                RTextField dateField = new RTextField(rs.getString("EDate"), 30);
                dateField.setBackground(Color.WHITE);
                dateField.setEnabled(false);
                dateField.setFont(p14);

                disIDField = new RTextField(rs.getString("DiscountID"), 30);
                fnameField = new RTextField(rs.getString("FName"), 30);
                mnameField = new RTextField(rs.getString("MName"), 30);
                lnameField = new RTextField(rs.getString("LName"), 30);
                muniField = new RTextField(rs.getString("Mun"), 30);
                provField = new RTextField(rs.getString("Prov"), 30);
                category = new RTextField(rs.getString("DCategory"), 30);

                disIDField.setBorder(b);
                fnameField.setBorder(b);
                mnameField.setBorder(b);
                lnameField.setBorder(b);
                disDate.setBorder(b);
                muniField.setBorder(b);
                provField.setBorder(b);

                discountId = new JLabel("Discount ID:");
                disfname = new JLabel("First Name:");
                dismname = new JLabel("Middle Name:");
                dislname = new JLabel("Last Name:");
                disEdate = new JLabel("Expiry Date:");
                disMun = new JLabel("Municipality:");
                disProv = new JLabel("Province:");
                disCategory = new JLabel("Category:");
// Shared dimensions
                Dimension labelSize = new Dimension(110, 32);
                Dimension textFieldSize = new Dimension(0, 32);

// Labels
                discountId.setFont(p14);
                discountId.setPreferredSize(labelSize);

                disfname.setFont(p14);
                disfname.setPreferredSize(labelSize);

                dismname.setFont(p14);
                dismname.setPreferredSize(labelSize);

                dislname.setFont(p14);
                dislname.setPreferredSize(labelSize);

                disMun.setFont(p14);
                disMun.setPreferredSize(labelSize);

                disProv.setFont(p14);
                disProv.setPreferredSize(labelSize);

                disEdate.setFont(p14);
                disEdate.setPreferredSize(labelSize);

                disCategory.setFont(p14);
                disCategory.setPreferredSize(labelSize);

                disEdate.setFont(p14);
                disEdate.setPreferredSize(labelSize);

                disCategory.setFont(p14);
                disCategory.setPreferredSize(labelSize);

// Text Fields
                disIDField.setFont(p14);
                disIDField.setBorder(b);
                disIDField.setPreferredSize(textFieldSize);
                disIDField.setEnabled(false);
                disIDField.setDisabledTextColor(Color.BLACK);

                fnameField.setFont(p14);
                fnameField.setBorder(b);
                fnameField.setPreferredSize(textFieldSize);
                fnameField.setEnabled(false);
                fnameField.setDisabledTextColor(Color.BLACK);

                mnameField.setFont(p14);
                mnameField.setBorder(b);
                mnameField.setPreferredSize(textFieldSize);
                mnameField.setEnabled(false);
                mnameField.setDisabledTextColor(Color.BLACK);

                lnameField.setFont(p14);
                lnameField.setBorder(b);
                lnameField.setPreferredSize(textFieldSize);
                lnameField.setEnabled(false);
                lnameField.setDisabledTextColor(Color.BLACK);

                muniField.setFont(p14);
                muniField.setBorder(b);
                muniField.setPreferredSize(textFieldSize);
                muniField.setEnabled(false);
                muniField.setDisabledTextColor(Color.BLACK);

                provField.setFont(p14);
                provField.setBorder(b);
                provField.setPreferredSize(textFieldSize);
                provField.setEnabled(false);
                provField.setDisabledTextColor(Color.BLACK);

                disIDField.setEnabled(false);
                fnameField.setEnabled(false);
                mnameField.setEnabled(false);
                lnameField.setEnabled(false);
                muniField.setEnabled(false);
                provField.setEnabled(false);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.BOTH;

                gbc.gridwidth = 1;
                gbc.gridx = 0;
                gbc.weightx = 0;
                gbc.gridy = 0;
                miniPanel.add(discountId, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(disfname, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(dismname, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(dislname, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(disEdate, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(disCategory, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(disMun, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(disProv, gbc);

                //======== FIELDS ========
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.weightx = 1.0;
                miniPanel.add(disIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(fnameField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(mnameField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(lnameField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(dateField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(categoryField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(muniField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(provField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer

                // Buttons
                JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
                btnPanel.setOpaque(false);
                btnPanel.setBorder(b);
                JButton returnBtn = new BtnDefault("Return", new ImageIcon(getClass().getResource("/images/icon/return.png")));
                btnPanel.add(returnBtn);

                returnBtn.addActionListener(e -> searchDialog.dispose());
                retrieveDiscountTableData();
                searchDialog.add(mainPanel, BorderLayout.CENTER);
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(miniPanel, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);
                searchDialog.setLocationRelativeTo(null);
                searchDialog.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Discount ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }

    }

    private void discountDelete() {
        String inputID = JOptionPane.showInputDialog(this, "Enter Discount ID to Delete:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Discount ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM Discount WHERE DiscountID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog deleteDialog = new JDialog((Frame) null, "Delete Discount", true);
                deleteDialog.setSize(500, 710);
                deleteDialog.setLayout(new BorderLayout());
                deleteDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title Panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/red.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);

                JLabel title = new JLabel("Delete Discount");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                // Form Panel
                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setOpaque(false);
                miniPanel.setBorder(b);

                RTextField categoryField = new RTextField(rs.getString("DCategory"), 30);
                categoryField.setBackground(Color.WHITE);
                 categoryField.setFont(p14);

                RTextField dateField = new RTextField(rs.getString("EDate"), 30);
                dateField.setBackground(Color.WHITE);
                dateField.setEnabled(false);
                dateField.setFont(p14);
                
                disIDField = new RTextField(rs.getString("DiscountID"), 30);
                fnameField = new RTextField(rs.getString("FName"), 30);
                mnameField = new RTextField(rs.getString("MName"), 30);
                lnameField = new RTextField(rs.getString("LName"), 30);
                muniField = new RTextField(rs.getString("Mun"), 30);
                provField = new RTextField(rs.getString("Prov"), 30);

                disIDField.setBorder(b);
                fnameField.setBorder(b);
                mnameField.setBorder(b);
                lnameField.setBorder(b);
                disDate.setBorder(b);
                muniField.setBorder(b);
                provField.setBorder(b);
                discountId = new JLabel("Discount ID:");
                disfname = new JLabel("First Name:");
                dismname = new JLabel("Middle Name:");
                dislname = new JLabel("Last Name:");
                disEdate = new JLabel("Expiry Date:");
                disMun = new JLabel("Municipality:");
                disProv = new JLabel("Province:");
                disCategory = new JLabel("Category:");
// Shared dimensions
                Dimension labelSize = new Dimension(110, 32);
                Dimension textFieldSize = new Dimension(0, 32);

// Labels
                discountId.setFont(p14);
                discountId.setPreferredSize(labelSize);

                disfname.setFont(p14);
                disfname.setPreferredSize(labelSize);

                dismname.setFont(p14);
                dismname.setPreferredSize(labelSize);

                dislname.setFont(p14);
                dislname.setPreferredSize(labelSize);

                disMun.setFont(p14);
                disMun.setPreferredSize(labelSize);

                disProv.setFont(p14);
                disProv.setPreferredSize(labelSize);

                disEdate.setFont(p14);
                disEdate.setPreferredSize(labelSize);

                disCategory.setFont(p14);
                disCategory.setPreferredSize(labelSize);

// Text Fields (disabled, styled)
                disIDField.setFont(p14);
                disIDField.setBorder(b);
                disIDField.setPreferredSize(textFieldSize);
                disIDField.setEnabled(false);
                disIDField.setDisabledTextColor(Color.BLACK);

                fnameField.setFont(p14);
                fnameField.setBorder(b);
                fnameField.setPreferredSize(textFieldSize);
                fnameField.setEnabled(false);
                fnameField.setDisabledTextColor(Color.BLACK);

                mnameField.setFont(p14);
                mnameField.setBorder(b);
                mnameField.setPreferredSize(textFieldSize);
                mnameField.setEnabled(false);
                mnameField.setDisabledTextColor(Color.BLACK);

                lnameField.setFont(p14);
                lnameField.setBorder(b);
                lnameField.setPreferredSize(textFieldSize);
                lnameField.setEnabled(false);
                lnameField.setDisabledTextColor(Color.BLACK);

                muniField.setFont(p14);
                muniField.setBorder(b);
                muniField.setPreferredSize(textFieldSize);
                muniField.setEnabled(false);
                muniField.setDisabledTextColor(Color.BLACK);

                provField.setFont(p14);
                provField.setBorder(b);
                provField.setPreferredSize(textFieldSize);
                provField.setEnabled(false);
                provField.setDisabledTextColor(Color.BLACK);

                date = new RPanel(30);
                date.setLayout(new BorderLayout());
                date.add(disDate, BorderLayout.CENTER);
                date.setBorder(b);
                date.setPreferredSize(new Dimension(0, 32));
                date.setBackground(Color.WHITE);
                disDate.setBackground(Color.WHITE);
                disDate.setFont(p14);
                disDate.setBorder(null);

                discat = new RPanel(30);
                discat.setLayout(new BorderLayout());
                discat.add(catBox, BorderLayout.CENTER);
                discat.setBorder(b);
                discat.setPreferredSize(new Dimension(0, 40));
                catBox.setBackground(Color.WHITE);
                discat.setBackground(Color.WHITE);
                catBox.setFont(p14);
                catBox.setBorder(null);

                catBox.setEnabled(false);
                disDate.setEnabled(false);
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.BOTH;

                gbc.gridwidth = 1;
                gbc.gridx = 0;
                gbc.weightx = 0;
                gbc.gridy = 0;
                miniPanel.add(discountId, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(disfname, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(dismname, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(dislname, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(disEdate, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(disCategory, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(disMun, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(disProv, gbc);

                //======== FIELDS ========
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.weightx = 1.0;
                miniPanel.add(disIDField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(fnameField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(mnameField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(lnameField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(dateField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(categoryField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(muniField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer
                gbc.gridy++;
                miniPanel.add(provField, gbc);
                gbc.gridy++;
                miniPanel.add(new JLabel(""), gbc); // Spacer

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
                    try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM Discount WHERE DiscountID = ?")) {
                        deleteStmt.setString(1, inputID);
                        deleteStmt.executeUpdate();
                        JOptionPane.showMessageDialog(deleteDialog, "Discount No. " + inputID + " was deleted successfully.");
                        retrieveDiscountTableData();
                        deleteDialog.dispose();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(deleteDialog, "Delete failed: " + ex.getMessage());
                    }
                });
                cancelBtn.addActionListener(e -> deleteDialog.dispose());

                deleteDialog.add(mainPanel, BorderLayout.CENTER);
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(miniPanel, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);
                deleteDialog.setLocationRelativeTo(null);
                deleteDialog.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Discount ID not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }

    }

    private void likeSearchDiscounts() {
        String[] fields = {"DiscountID", "FName", "MName", "LName", "EDate", "DCategory", "Mun", "Prov"};
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

            String sql = "SELECT * FROM DISCOUNT WHERE " + field + " LIKE ?";
            try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, keyword);
                try (ResultSet rs = stmt.executeQuery()) {
                    discountTableModel.setRowCount(0); // clear table

                    while (rs.next()) {
                        discountTableModel.addRow(new Object[]{
                            rs.getString("DiscountID"),
                            rs.getString("FName"),
                            rs.getString("MName"),
                            rs.getString("LName"),
                            rs.getDate("EDate"),
                            rs.getString("DCategory"),
                            rs.getString("Mun"),
                            rs.getString("Prov")
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

    private void sortDiscounts() {
        String[] fields = {"DiscountID", "FName", "MName", "LName", "EDate", "DCategory", "Mun", "Prov"};
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

        String sql = "SELECT * FROM DISCOUNT ORDER BY " + field + " " + order;

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            discountTableModel.setRowCount(0); // clear table

            while (rs.next()) {
                discountTableModel.addRow(new Object[]{
                    rs.getString("DiscountID"),
                    rs.getString("FName"),
                    rs.getString("MName"),
                    rs.getString("LName"),
                    rs.getDate("EDate"),
                    rs.getString("DCategory"),
                    rs.getString("Mun"),
                    rs.getString("Prov")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error sorting: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
