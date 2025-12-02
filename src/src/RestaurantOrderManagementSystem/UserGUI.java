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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class UserGUI extends Components {

    private final JPanel user = new JPanel(new BorderLayout());

    private String[] roles = {"SELECT", "ADMIN", "CASHIER"};
    private String[] status = {"SELECT", "ACTIVE", "BLOCKED"};
    private JLabel profilePreviewLabel;

    // JLabel errors
    private JLabel errorID, errorFName, errorMName, errorLName, errorEmail, errorNumber, errorUsername, errorPassword, errorRole, errorStatus, errorProfile, errorSecKey;

    // RTextFields
    private RTextField userIDField;
    private RTextField userFName;
    private RTextField userMName;
    private RTextField userLName;
    private RTextField userEmail;
    private RTextField userContactNumber;
    private RTextField userUsername;
    private RTextField userPassword;
    private RTextField userSecKey;

    // Panels and ComboBoxes
    private RPanel userRole;
    private JComboBox<String> userRoleC;
    private RPanel userStatus;
    private JComboBox<String> userStatusC;

    // File chooser and image
    private JFileChooser pfpChooser;
    private File userImageFile;
    private JLabel userLblImagePath;

    // Table and model
    private String[] userColumnNames = new String[]{
        "UserID", "Role", "FName", "MName", "LName", "Email", "CNumber", "Username", "Password", "ProfilePicture", "SecurityKey", "UStatus"
    };
    private DefaultTableModel userTableModel = new DisabledTableModel(userColumnNames, 0);
    ;
    public JTable userTable = new JTable(userTableModel);

    ;

    public UserGUI() {
        // Main user panel with BorderLayout
        user.setBorder(b);
        user.setOpaque(false);
        DefaultTableCellRenderer centerRenderer = new CustomRowRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Manually assign the center renderer to each column
        userTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);  // UserID
        userTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);  // Role
        userTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);  // FName
        userTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);  // MName
        userTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);  // LName
        userTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);  // Email
        userTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);  // CNumber
        userTable.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);  // Username
        userTable.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);  // Password
        userTable.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);  // ProfilePicture
        userTable.getColumnModel().getColumn(10).setCellRenderer(centerRenderer); // SecurityKey
        userTable.getColumnModel().getColumn(11).setCellRenderer(centerRenderer); // UStatus

        retrieveUserTableData();
        int c = 0;
        // Configure user table column widths
        userTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        userTable.getColumnModel().getColumn(c).setPreferredWidth(100);
        userTable.getColumnModel().getColumn(c++).setPreferredWidth(100);
        userTable.getColumnModel().getColumn(c++).setPreferredWidth(100);
        userTable.getColumnModel().getColumn(c++).setPreferredWidth(100);
        userTable.getColumnModel().getColumn(c++).setPreferredWidth(100);
        userTable.getColumnModel().getColumn(c++).setPreferredWidth(100);
        userTable.getColumnModel().getColumn(c++).setPreferredWidth(150);
        userTable.getColumnModel().getColumn(c++).setPreferredWidth(150);
        userTable.getColumnModel().getColumn(c++).setPreferredWidth(100);
        userTable.getColumnModel().getColumn(c++).setPreferredWidth(100);
        userTable.getColumnModel().getColumn(c++).setPreferredWidth(100);

        // RTextField initialization  
        userIDField = new RTextField("", 30);
        userFName = new RTextField("Required", 30);
        userMName = new RTextField("Optional", 30);
        userLName = new RTextField("Required", 30);
        userEmail = new RTextField("sample@patz.com", 30);
        userContactNumber = new RTextField("09", 30);
        userUsername = new RTextField("Required", 30);
        userPassword = new RTextField("Required", 30);
        userSecKey = new RTextField("Required", 30);

        // Panels and ComboBoxes
        userRole = new RPanel(30);
        userRoleC = new CustomComboBox<>(roles);
        userStatus = new RPanel(30);
        userStatusC = new CustomComboBox<>(status);

        //Error labels
        errorID = new JLabel("");
        errorFName = new JLabel("");
        errorMName = new JLabel("");
        errorLName = new JLabel("");
        errorEmail = new JLabel("");
        errorNumber = new JLabel("");
        errorUsername = new JLabel("");
        errorPassword = new JLabel("");
        errorRole = new JLabel("");
        errorStatus = new JLabel("");
        errorProfile = new JLabel("");
        errorSecKey = new JLabel("");

        errorID.setFont(p11);
        errorFName.setFont(p11);
        errorMName.setFont(p11);
        errorLName.setFont(p11);
        errorEmail.setFont(p11);
        errorNumber.setFont(p11);
        errorUsername.setFont(p11);
        errorPassword.setFont(p11);
        errorRole.setFont(p11);
        errorStatus.setFont(p11);
        errorProfile.setFont(p11);
        errorSecKey.setFont(p11);

        errorID.setForeground(Color.RED);
        errorProfile.setForeground(Color.RED);
        errorRole.setForeground(Color.RED);
        errorFName.setForeground(Color.RED);
        errorMName.setForeground(Color.RED);
        errorLName.setForeground(Color.RED);
        errorEmail.setForeground(Color.RED);
        errorNumber.setForeground(Color.RED);
        errorUsername.setForeground(Color.RED);
        errorPassword.setForeground(Color.RED);
        errorStatus.setForeground(Color.RED);
        errorSecKey.setForeground(Color.RED);

        // File chooser and image
        pfpChooser = new JFileChooser();
        userImageFile = null; // or new File("");
        userLblImagePath = new JLabel();

        //comboBoxes added in RPanel
        userRole.setLayout(new BorderLayout());
        userRole.setBackground(Color.white);
        userRole.add(userRoleC, BorderLayout.CENTER);
        userRole.setBorder(b);

        userStatus.setLayout(new BorderLayout());
        userStatus.setBackground(Color.white);
        userStatus.setBackground(Color.white);
        userStatus.add(userStatusC, BorderLayout.CENTER);
        userStatus.setBorder(b);
        userStatusC.setBorder(null);
        userRoleC.setBorder(null);

        //id field
        int nextID = Database.getNextID("USER_");
        if (nextID != -1) {
            userIDField.setText(String.valueOf(nextID));
            userIDField.setEnabled(false); // make it read-only
            userIDField.setDisabledTextColor(Color.DARK_GRAY);
        }

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
                sortUsers();
            } else if (choice == 1) { // Like Search
                likeSearchUsers();
            } else if (choice == 2) {
                retrieveUserTableData();
            }
        });

        JButton refreshBtn = new BtnCustom(new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/refresh.png")).getImage()), new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/refreshGreen.png")).getImage()), "Refresh");
        refreshBtn.addActionListener(e -> {
            retrieveUserTableData();
        });
        headPanel.add(refreshBtn);
        headPanel.add(filterBtn);

        JLabel titleHeader = new JLabel("USER");
        titleHeader.setFont(Components.b45);
        titleHeader.setBorder(Components.b);
        titleHeader.setForeground(Components.ashBlue);

        // CENTER Panel with left and right sections
        JPanel centerUser = new JPanel(new BorderLayout());
        centerUser.setBackground(new Color(238, 238, 238));
        centerUser.setBorder(Components.b);

        // Right Center Panel - Table panel
        JPanel rightCenter = new JPanel(new BorderLayout());
        rightCenter.setBorder(b);
        rightCenter.setBackground(new Color(238, 238, 238));
        rightCenter.add(userTablePane(), BorderLayout.CENTER);

        // SOUTH Panel - CRUD Buttons
        JPanel southUser = new JPanel(new GridLayout(1, 4, 10, 10));
        southUser.setBackground(new Color(238, 238, 238));
        southUser.setPreferredSize(new Dimension(0, 100));
        southUser.setBorder(Components.b);

        // Create buttons with icons
        Dimension crudBttn = new Dimension(120, 30);

        JButton createButton = new BtnGreen("Create", new ImageIcon(getClass().getResource("/images/icon/create.png")));
        JButton updateButton = new BtnDefault("Update", new ImageIcon(getClass().getResource("/images/icon/update.png")));
        JButton searchButton = new BtnBlue("Search", new ImageIcon(getClass().getResource("/images/icon/searchWhite.png")));
        JButton deleteButton = new BtnRed("Delete", new ImageIcon(getClass().getResource("/images/icon/delete.png")));

        createButton.setPreferredSize(crudBttn);
        updateButton.setPreferredSize(crudBttn);
        searchButton.setPreferredSize(crudBttn);
        deleteButton.setPreferredSize(crudBttn);

        southUser.add(createButton);
        southUser.add(updateButton);
        southUser.add(searchButton);
        southUser.add(deleteButton);

        // Action Listeners for buttons
        createButton.addActionListener(e -> userCreate());
        searchButton.addActionListener(e -> userSearch());
        deleteButton.addActionListener(e -> userDelete());
        updateButton.addActionListener(e -> userUpdate());

        centerUser.add(userTablePane(), BorderLayout.CENTER);

        // Add panels to main user panel
        user.add(centerUser, BorderLayout.CENTER);
        user.add(southUser, BorderLayout.SOUTH);
        user.add(headPanel, BorderLayout.NORTH);

    }

    public JPanel getUserPanel() {

        return user;
    }

    private JScrollPane userTablePane() {
        userTable = Components.updateTable(new JTable(userTableModel));
        userTable.setPreferredScrollableViewportSize(new Dimension(500, 150));
        userTable.setFillsViewportHeight(true);
        JScrollPane userPane = new JScrollPane(userTable);

        return userPane;
    }

    private void userCreate() {

        userStatusC.setSelectedItem("ACTIVE");
        userStatusC.setEnabled(false);

        JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
        mainPanel.setLayout(new BorderLayout());

        RPanel imgPanel = new RPanel(30);
        imgPanel.setLayout(new BorderLayout());
        imgPanel.setBorder(b);
        imgPanel.setBackground(Color.WHITE);
        userLblImagePath = new JLabel("No file selected");
        userLblImagePath.setFont(p12);
        JButton btnBrowse = new BtnBlue("Browse");
        btnBrowse.setFont(b12);
        btnBrowse.setPreferredSize(new Dimension(100, 30));
        btnBrowse.addActionListener(e -> chooseImageFile());
        imgPanel.add(userLblImagePath, BorderLayout.CENTER);
        imgPanel.add(btnBrowse, BorderLayout.EAST);

        JDialog createDialog = new JDialog((Frame) null, "Create User", true);
        createDialog.setSize(470, 670);
        createDialog.setLayout(new BorderLayout());
        createDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        createDialog.setLocationRelativeTo(null);

        JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/green.jpg")).getImage());
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(3, 4, 94));
        titlePanel.setBorder(b);

        JLabel title = new JLabel("Create User");
        title.setFont(b18);
        title.setForeground(Color.white);
        title.setHorizontalAlignment(JLabel.CENTER);

        titlePanel.add(title, BorderLayout.CENTER);

        JPanel miniPanel = new JPanel();
        miniPanel.setLayout(new GridBagLayout());
        miniPanel.setBorder(b);
        miniPanel.setOpaque(false);

        userIDField.setEnabled(false);
        userIDField.setFont(p14);
        userIDField.setBorder(b);

        userIDField.setFont(p14);
        userIDField.setBorder(b);
        userIDField.setDisabledTextColor(Color.GRAY);

        userFName.setFont(p14);
        userFName.setBorder(b);
        userFName.setDisabledTextColor(Color.GRAY);

        userMName.setFont(p14);
        userMName.setBorder(b);
        userMName.setDisabledTextColor(Color.GRAY);

        userLName.setFont(p14);
        userLName.setBorder(b);
        userLName.setDisabledTextColor(Color.GRAY);

        userEmail.setFont(p14);
        userEmail.setBorder(b);
        userEmail.setDisabledTextColor(Color.GRAY);

        userContactNumber.setFont(p14);
        userContactNumber.setBorder(b);
        userContactNumber.setDisabledTextColor(Color.GRAY);

        userUsername.setFont(p14);
        userUsername.setBorder(b);
        userUsername.setDisabledTextColor(Color.GRAY);

        userPassword.setFont(p14);
        userPassword.setBorder(b);
        userPassword.setDisabledTextColor(Color.GRAY);

        userSecKey.setFont(p14);
        userSecKey.setBorder(b);
        userSecKey.setDisabledTextColor(Color.GRAY);

        JLabel userIDl = new JLabel("User ID:");
        JLabel imglbl = new JLabel("Picture :");
        JLabel uRolel = new JLabel("Role:");
        JLabel fNamel = new JLabel("First Name:");
        JLabel mNamel = new JLabel("Middle Name:");
        JLabel lNamel = new JLabel("Last Name:");
        JLabel emaill = new JLabel("Email:");
        JLabel cNumberl = new JLabel("Contact Number:");
        JLabel usernamel = new JLabel("Username:");
        JLabel passwordl = new JLabel("Password:");
        JLabel statlbl = new JLabel("Status :");
        JLabel keylbl = new JLabel("Security Key:");
        Dimension lblD = new Dimension(150, 40);
        userIDl.setFont(p14);
        userIDl.setPreferredSize(lblD);

        uRolel.setFont(p14);
        uRolel.setPreferredSize(lblD);

        imglbl.setFont(p14);
        imglbl.setPreferredSize(lblD);

        fNamel.setFont(p14);
        fNamel.setPreferredSize(lblD);

        mNamel.setFont(p14);
        mNamel.setPreferredSize(lblD);

        lNamel.setFont(p14);
        lNamel.setPreferredSize(lblD);

        emaill.setFont(p14);
        emaill.setPreferredSize(lblD);

        cNumberl.setFont(p14);
        cNumberl.setPreferredSize(lblD);

        usernamel.setFont(p14);
        usernamel.setPreferredSize(lblD);

        passwordl.setFont(p14);
        passwordl.setPreferredSize(lblD);

        statlbl.setFont(p14);
        statlbl.setPreferredSize(lblD);

        keylbl.setFont(p14);
        keylbl.setPreferredSize(lblD);

        // Create the image preview label with default image
        profilePreviewLabel = new JLabel();
        profilePreviewLabel.setPreferredSize(new Dimension(150, 150));
        profilePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        profilePreviewLabel.setHorizontalAlignment(JLabel.CENTER);
        profilePreviewLabel.setVerticalAlignment(JLabel.CENTER);

        // Load default image
        ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/images/profilePicture/defaultProfile.png"));
        Image scaledImage = defaultIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        profilePreviewLabel.setIcon(new ImageIcon(scaledImage));

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
        miniPanel.add(new JLabel(" "), gbc);
        gbc.gridy++;
        miniPanel.add(imglbl, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc);

        gbc.gridy++;
        miniPanel.add(userIDl, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc);

        // uRole label
        gbc.gridy++;
        miniPanel.add(uRolel, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc);

        // First Name label
        gbc.gridy++;
        miniPanel.add(fNamel, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc);

        // Middle Name label
        gbc.gridy++;
        miniPanel.add(mNamel, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc);

        // Last Name label
        gbc.gridy++;
        miniPanel.add(lNamel, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc);

        // Email label
        gbc.gridy++;
        miniPanel.add(emaill, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc);

        // Contact Number label
        gbc.gridy++;
        miniPanel.add(cNumberl, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc);

        // usernamne label
        gbc.gridy++;
        miniPanel.add(usernamel, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc);

        // password label
        gbc.gridy++;
        miniPanel.add(passwordl, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc);

        //status
        gbc.gridy++;
        miniPanel.add(statlbl, gbc);

        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc);

        //status
        gbc.gridy++;
        miniPanel.add(keylbl, gbc);

        gbc.gridy++;
        miniPanel.add(new JLabel(" "), gbc);

        //ADDS INPUT FIELDS AND COMBOBOXES//
        // Configure input fields
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
        miniPanel.add(imgPanel, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(errorProfile, gbc);

        gbc.gridy++;
        //
        miniPanel.add(userIDField, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(errorID, gbc);

        // Role input field
        gbc.gridy++;
        miniPanel.add(userRole, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(errorRole, gbc);

        // First Name input field              
        gbc.gridy++;
        miniPanel.add(userFName, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(errorFName, gbc);

        // Middle Name input field
        gbc.gridy++;
        miniPanel.add(userMName, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(errorMName, gbc);

        // Last Name input field
        gbc.gridy++;
        miniPanel.add(userLName, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(errorLName, gbc);

        // Email input field
        gbc.gridy++;
        miniPanel.add(userEmail, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(errorEmail, gbc);

        // Contact Number input field
        gbc.gridy++;
        miniPanel.add(userContactNumber, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(errorNumber, gbc);

        // username input field
        gbc.gridy++;
        miniPanel.add(userUsername, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(errorUsername, gbc);

        // password input field
        gbc.gridy++;
        miniPanel.add(userPassword, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(errorPassword, gbc);

        //status
        gbc.gridy++;
        miniPanel.add(userStatus, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(errorStatus, gbc);

        //status
        gbc.gridy++;
        miniPanel.add(userSecKey, gbc);

        //fillers
        gbc.gridy++;
        miniPanel.add(errorSecKey, gbc);

        Dimension inputPrefSize = new Dimension(150, 40);
        userIDField.setPreferredSize(inputPrefSize);
        userRole.setPreferredSize(inputPrefSize);
        userFName.setPreferredSize(inputPrefSize);
        userMName.setPreferredSize(inputPrefSize);
        userLName.setPreferredSize(inputPrefSize);
        userContactNumber.setPreferredSize(inputPrefSize);
        userEmail.setPreferredSize(inputPrefSize);
        userUsername.setPreferredSize(inputPrefSize);
        userPassword.setPreferredSize(inputPrefSize);
        userStatus.setPreferredSize(inputPrefSize);
        userSecKey.setPreferredSize(inputPrefSize);
        imgPanel.setPreferredSize(inputPrefSize);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        btnPanel.setBorder(b);
        btnPanel.setOpaque(false);
        JButton cancelBtn = new BtnRed("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
        JButton updateBtn = new BtnGreen("Create", new ImageIcon(getClass().getResource("/images/icon/create.png")));

        cancelBtn.addActionListener(e -> {
            userClear();
            createDialog.dispose();

        });
        updateBtn.addActionListener(e -> {
            clearErrorLabels();

            String uRole = userRoleC.getSelectedItem().toString().trim();
            String uStat = "ACTIVE";
            String firstName = userFName.getText().trim();
            String middleName = userMName.getText().trim();
            String lastName = userLName.getText().trim();
            String contactNumber = userContactNumber.getText().trim();
            String email = userEmail.getText().trim();
            String userName = userUsername.getText().trim();
            String aPassword = userPassword.getText().trim();
            String secKey = userSecKey.getText().trim();
            clearErrorLabels();
            Boolean checker = false;

            if (userImageFile == null || !userImageFile.exists()) {
                errorProfile.setText("Image is Required");
                errorProfile.setForeground(Color.red);
                checker = true;

            }

            if (firstName.isEmpty() || firstName.equalsIgnoreCase("Required")) {
                errorFName.setText("First Name is Required");
                userFName.setForeground(Color.red);
                checker = true;
            }
            if (middleName.isEmpty() || middleName.equalsIgnoreCase("Optional")) {
                middleName = "NULL";
            }

            if (lastName.isEmpty() || lastName.equalsIgnoreCase("Required")) {
                errorLName.setText("Last Name Required");
                userLName.setForeground(Color.red);
                checker = true;
            }

            if (uRole.isEmpty() || uRole.equalsIgnoreCase("SELECT")) {
                errorRole.setText("Role is Required");
                userRoleC.setForeground(Color.red);
                checker = true;

            }

            if (!contactNumber.isEmpty()) {
                if (contactNumber.equals("09")) {
                    contactNumber = "NULL";
                } else {
                    if (!Database.isValidContactNumberLength(contactNumber)) {

                        errorNumber.setText("Invalid phone number. It must be 11 digits long.");
                        errorNumber.setForeground(Color.red);
                        checker = true;

                    }
                    if (!Database.isValidContactNumber(contactNumber)) {

                        errorNumber.setText("Invalid phone number. It must start with '09");
                        errorNumber.setForeground(Color.red);
                        checker = true;
                    }
                }
            }

            if (!email.isEmpty()) {
                if (email.equals("sample@patz.com")) {
                    email = "NULL";
                } else if (!Database.isValidEmail(email)) {
                    errorEmail.setText("Invalid Email Format");
                    errorEmail.setForeground(Color.red);
                    checker = true;
                }
            } else if (email.isEmpty()) {
                email = "NULL";
            }

            if (userName.isEmpty() || userName.equalsIgnoreCase("Required")) {
                errorUsername.setText("Username is Required");
                userUsername.setForeground(Color.red);
                checker = true;

            }
            
            
            if (aPassword.isEmpty() || aPassword.equalsIgnoreCase("Required")) {
                errorPassword.setText("Password is required");
                userPassword.setForeground(Color.red);
                checker = true;
            } else if (aPassword.length() < 8) {
                errorPassword.setText("Password must be at least 8 characters");
                userPassword.setForeground(Color.red);
                checker = true;
            }

            if (!aPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d])[a-zA-Z\\d[^\\s]]+$")) {
                errorPassword.setText("Password must be alphanumeric with symbols");
                userPassword.setForeground(Color.red);
                checker = true;
            } else if (aPassword.length() < 8) {
                errorPassword.setText("Password must be at least 8 characters");
                userPassword.setForeground(Color.red);
                checker = true;
            }

            if (secKey.isEmpty() || secKey.equalsIgnoreCase("Required")) {
                errorSecKey.setText("Security Key is Required");
                userSecKey.setForeground(Color.red);
                checker = true;

            }

            if (secKey.length() > 15) {
                errorSecKey.setText("Invalid length. Must be less than 15 characters long");
                userSecKey.setForeground(Color.red);
                checker = true;
            }

            if (!secKey.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d])[a-zA-Z\\d[^\\s]]+$")) {
                errorSecKey.setText("Security Key must be alphanumeric with symbols");
                userSecKey.setForeground(Color.red);
                checker = true;
            }

            if (checker == true) {
                JOptionPane.showMessageDialog(createDialog, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);

                return;
            }

            String sql = "INSERT INTO User_ (Role, FName, MName, LName, Email, CNumber, UName, Password, UStatus, SecurityKey, ProfilePicture) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                try {
                    conn.setAutoCommit(false);
                    stmt.setString(1, uRole);
                    stmt.setString(2, firstName);
                    stmt.setString(3, middleName);
                    stmt.setString(4, lastName);
                    stmt.setString(5, email);
                    stmt.setString(6, contactNumber);
                    stmt.setString(7, userName);
                    stmt.setString(8, aPassword);
                    stmt.setString(9, uStat);
                    stmt.setString(10, secKey);

                    try (FileInputStream fis = new FileInputStream(userImageFile)) {
                        stmt.setBinaryStream(11, fis, (int) userImageFile.length());
                        stmt.executeUpdate();
                        conn.commit();
                        JOptionPane.showMessageDialog(this, "User added successfully.");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(UserGUI.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(this, "User image file not found. Please select a valid image.", "File Error", JOptionPane.ERROR_MESSAGE);
                        conn.rollback();
                    } catch (IOException ex) {
                        conn.rollback();
                        Logger.getLogger(UserGUI.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(this, "IOException occurred with the User Image.", "File Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            retrieveUserTableData();
            userClear();
            createDialog.dispose();
        });

        btnPanel.add(cancelBtn);
        btnPanel.add(updateBtn);
        CNavScrollPane scrollPane = new CNavScrollPane(miniPanel);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        createDialog.add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        createDialog.setVisible(true);

    }

    private void userDelete() {
        String inputID = JOptionPane.showInputDialog(this, "Enter User ID to Delete:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No User ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM User_ WHERE UserID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Create dialog
                JDialog deleteDialog = new JDialog((Frame) null, "Delete User", true);
                deleteDialog.setSize(470, 670);
                deleteDialog.setLayout(new BorderLayout());
                deleteDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                deleteDialog.setLocationRelativeTo(null);

                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title Panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/red.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);
                JLabel title = new JLabel("Delete User");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setBorder(b);
                miniPanel.setOpaque(false);

                RTextField idField = new RTextField(inputID, 30);
                idField.setFont(p14);
                idField.setBorder(b);
                idField.setEnabled(false);

                profilePreviewLabel = new JLabel();
                profilePreviewLabel.setPreferredSize(new Dimension(150, 150));
                profilePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                profilePreviewLabel.setHorizontalAlignment(JLabel.CENTER);
                profilePreviewLabel.setVerticalAlignment(JLabel.CENTER);

                RTextField uRoleF = new RTextField(rs.getString("Role"), 30);
                RTextField fName = new RTextField(rs.getString("FName"), 30);
                RTextField mName = new RTextField(rs.getString("MName"), 30);
                RTextField lName = new RTextField(rs.getString("LName"), 30);
                RTextField email = new RTextField(rs.getString("Email"), 30);
                RTextField cNumber = new RTextField(rs.getString("CNumber"), 30);
                RTextField username = new RTextField(rs.getString("uName"), 30);
                RTextField password = new RTextField(rs.getString("Password"), 30);
                RTextField userStat = new RTextField(rs.getString("UStatus"), 30);
                RTextField secuKey = new RTextField(rs.getString("SecurityKey"), 30);
                // Retrieve and display the image if it exists
                byte[] imageBytes = rs.getBytes("ProfilePicture"); // Assuming image is stored in a column named "UserImage"
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

                idField.setFont(p14);
                idField.setBorder(b);
                idField.setEnabled(false);
                idField.setDisabledTextColor(Color.GRAY);

                uRoleF.setFont(p14);
                uRoleF.setBorder(b);
                uRoleF.setEnabled(false);
                uRoleF.setDisabledTextColor(Color.GRAY);

                fName.setFont(p14);
                fName.setBorder(b);
                fName.setEnabled(false);
                fName.setDisabledTextColor(Color.GRAY);

                mName.setFont(p14);
                mName.setBorder(b);
                mName.setEnabled(false);
                mName.setDisabledTextColor(Color.GRAY);

                lName.setFont(p14);
                lName.setBorder(b);
                lName.setEnabled(false);
                lName.setDisabledTextColor(Color.GRAY);

                email.setFont(p14);
                email.setBorder(b);
                email.setEnabled(false);
                email.setDisabledTextColor(Color.GRAY);

                cNumber.setFont(p14);
                cNumber.setBorder(b);
                cNumber.setEnabled(false);
                cNumber.setDisabledTextColor(Color.GRAY);

                username.setFont(p14);
                username.setBorder(b);
                username.setEnabled(false);
                username.setDisabledTextColor(Color.GRAY);

                password.setFont(p14);
                password.setBorder(b);
                password.setEnabled(false);
                password.setDisabledTextColor(Color.GRAY);

                userStat.setFont(p14);
                userStat.setBorder(b);
                userStat.setEnabled(false);
                userStat.setDisabledTextColor(Color.GRAY);

                secuKey.setFont(p14);
                secuKey.setBorder(b);
                secuKey.setEnabled(false);
                secuKey.setDisabledTextColor(Color.GRAY);

                JLabel userIDl = new JLabel("User ID:");
                JLabel uRolel = new JLabel("Role:");
                JLabel fNamel = new JLabel("First Name:");
                JLabel mNamel = new JLabel("Middle Name:");
                JLabel lNamel = new JLabel("Last Name:");
                JLabel cNumberl = new JLabel("Contact Number:");
                JLabel emaill = new JLabel("Email:");
                JLabel usernamel = new JLabel("Username:");
                JLabel passwordl = new JLabel("Password:");
                JLabel statlbl = new JLabel("Status :");
                JLabel keylbl = new JLabel("Security Key :");

                Dimension d = new Dimension(150, 40);

                userIDl.setFont(p14);
                userIDl.setVerticalAlignment(SwingConstants.CENTER);
                userIDl.setPreferredSize(d);

                uRolel.setFont(p14);
                uRolel.setVerticalAlignment(SwingConstants.CENTER);
                uRolel.setPreferredSize(d);

                fNamel.setFont(p14);
                fNamel.setVerticalAlignment(SwingConstants.CENTER);
                fNamel.setPreferredSize(d);

                mNamel.setFont(p14);
                mNamel.setVerticalAlignment(SwingConstants.CENTER);
                mNamel.setPreferredSize(d);

                lNamel.setFont(p14);
                lNamel.setVerticalAlignment(SwingConstants.CENTER);
                lNamel.setPreferredSize(d);

                emaill.setFont(p14);
                emaill.setVerticalAlignment(SwingConstants.CENTER);
                emaill.setPreferredSize(d);

                cNumberl.setFont(p14);
                cNumberl.setVerticalAlignment(SwingConstants.CENTER);
                cNumberl.setPreferredSize(d);

                usernamel.setFont(p14);
                usernamel.setVerticalAlignment(SwingConstants.CENTER);
                usernamel.setPreferredSize(d);

                passwordl.setFont(p14);
                passwordl.setVerticalAlignment(SwingConstants.CENTER);
                passwordl.setPreferredSize(d);

                statlbl.setFont(p14);
                statlbl.setVerticalAlignment(SwingConstants.CENTER);
                statlbl.setPreferredSize(d);

                keylbl.setFont(p14);
                keylbl.setVerticalAlignment(SwingConstants.CENTER);
                keylbl.setPreferredSize(d);

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
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(userIDl, gbc);

                // role label
                gbc.gridy++;
                miniPanel.add(uRolel, gbc);

                // First Name label
                gbc.gridy++;
                miniPanel.add(fNamel, gbc);

                // Middle Name label
                gbc.gridy++;
                miniPanel.add(mNamel, gbc);

                // Last Name label
                gbc.gridy++;
                miniPanel.add(lNamel, gbc);

                // Email label
                gbc.gridy++;
                miniPanel.add(emaill, gbc);
                // Contact Number label
                gbc.gridy++;
                miniPanel.add(cNumberl, gbc);

                //username
                gbc.gridy++;
                miniPanel.add(usernamel, gbc);

                //password
                gbc.gridy++;
                miniPanel.add(passwordl, gbc);

                //password
                gbc.gridy++;
                miniPanel.add(statlbl, gbc);

                //password
                gbc.gridy++;
                miniPanel.add(keylbl, gbc);

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

                miniPanel.add(idField, gbc);
                // Roles input field
                gbc.gridy++;
                miniPanel.add(uRoleF, gbc);

                // First Name input field
                gbc.gridy++;
                miniPanel.add(fName, gbc);

                // Middle Name input field
                gbc.gridy++;
                miniPanel.add(mName, gbc);

                // Last Name input field
                gbc.gridy++;
                miniPanel.add(lName, gbc);

                // Email input field
                gbc.gridy++;
                miniPanel.add(email, gbc);

                // Contact Number input field
                gbc.gridy++;
                miniPanel.add(cNumber, gbc);

                gbc.gridy++;
                miniPanel.add(username, gbc);

                gbc.gridy++;
                miniPanel.add(password, gbc);

                gbc.gridy++;
                miniPanel.add(userStat, gbc);

                gbc.gridy++;
                miniPanel.add(secuKey, gbc);

                Dimension inputPrefSize = new Dimension(150, 40);
                idField.setPreferredSize(inputPrefSize);
                uRoleF.setPreferredSize(inputPrefSize);
                fName.setPreferredSize(inputPrefSize);
                mName.setPreferredSize(inputPrefSize);
                lName.setPreferredSize(inputPrefSize);
                cNumber.setPreferredSize(inputPrefSize);
                email.setPreferredSize(inputPrefSize);
                username.setPreferredSize(inputPrefSize);
                password.setPreferredSize(inputPrefSize);
                userStat.setPreferredSize(inputPrefSize);
                secuKey.setPreferredSize(inputPrefSize);

                JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
                btnPanel.setOpaque(false);
                JButton cancelBtn = new BtnDefault("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
                JButton deleteBtn = new BtnRed("Confirm Delete", new ImageIcon(getClass().getResource("/images/icon/delete.png")));
                btnPanel.setBorder(b);
                cancelBtn.addActionListener(e -> deleteDialog.dispose());
                deleteBtn.addActionListener(e -> {
                    try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM User_ WHERE UserID = ?")) {
                        deleteStmt.setString(1, inputID);
                        deleteStmt.executeUpdate();
                        JOptionPane.showMessageDialog(deleteDialog, "User No. " + inputID + " was deleted successfully.");
                        JOptionPane.showMessageDialog(this, "Please logout to apply the changes.", "Information", JOptionPane.INFORMATION_MESSAGE);
                        retrieveUserTableData();
                        deleteDialog.dispose();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(deleteDialog, "Delete failed: " + ex.getMessage());
                    }
                });

                btnPanel.add(cancelBtn);
                btnPanel.add(deleteBtn);
                CNavScrollPane scrollPane = new CNavScrollPane(miniPanel);
                scrollPane.setOpaque(false);
                scrollPane.setBorder(null);
                scrollPane.getViewport().setOpaque(false);

                deleteDialog.add(mainPanel, BorderLayout.CENTER);
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(scrollPane, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);

                deleteDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "User ID not found.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void userClear() {
        int nextID = Database.getNextID("User_");
        if (nextID != -1) {
            userIDField.setText(String.valueOf(nextID));
            userIDField.setEnabled(false); // make it read-only
            userIDField.setDisabledTextColor(Color.DARK_GRAY);
        }
        userFName.setText("Required");
        userMName.setText("");
        userLName.setText("Required");
        userEmail.setDefault();
        userContactNumber.setDefault();
        userUsername.setText("Required");
        userPassword.setText("Required");
        userRoleC.setSelectedIndex(0);
        errorFName.setText(" ");
        errorMName.setText(" ");
        errorLName.setText(" ");
        errorEmail.setText(" ");
        errorNumber.setText(" ");
        errorUsername.setText(" ");
        errorPassword.setText(" ");
        errorRole.setText(" ");
        errorStatus.setText(" ");
        errorProfile.setText(" ");
        errorSecKey.setText(" ");
        userSecKey.setText("Required");
        userRoleC.setForeground(Color.DARK_GRAY);
        userLName.setForeground(Color.DARK_GRAY);
        userFName.setForeground(Color.DARK_GRAY);
        userUsername.setForeground(Color.DARK_GRAY);
        userPassword.setForeground(Color.DARK_GRAY);
        userSecKey.setForeground(Color.DARK_GRAY);
        userImageFile = null;

    }

    private void clearErrorLabels() {
        userFName.setForeground(Color.BLACK);
        userMName.setForeground(Color.BLACK);
        userLName.setForeground(Color.BLACK);
        userContactNumber.setForeground(Color.BLACK);
        userEmail.setForeground(Color.BLACK);
        userUsername.setForeground(Color.BLACK);
        userPassword.setForeground(Color.BLACK);
        userSecKey.setForeground(Color.BLACK);
        userRoleC.setForeground(Color.BLACK);
        errorID.setText(" ");
        errorFName.setText(" ");
        errorMName.setText("");
        errorLName.setText(" ");
        errorEmail.setText(" ");
        errorNumber.setText(" ");
        errorUsername.setText(" ");
        errorPassword.setText(" ");
        errorRole.setText(" ");
        errorStatus.setText(" ");
        errorProfile.setText(" ");
        errorSecKey.setText(" ");

    }

    private void userUpdate() {

        String inputID = JOptionPane.showInputDialog(this, "Enter User ID to Update:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No User ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM User_ WHERE UserID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                JDialog updateDialog = new JDialog((Frame) null, "Update User", true);
                updateDialog.setSize(470, 670);
                updateDialog.setLayout(new BorderLayout());
                updateDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                updateDialog.setLocationRelativeTo(null);

                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/orange.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBackground(new Color(3, 4, 94));
                titlePanel.setBorder(b);

                JLabel title = new JLabel("Update User");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);

                titlePanel.add(title, BorderLayout.CENTER);

                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setBorder(b);
                miniPanel.setOpaque(false);
                // Create the image preview label with default image
                profilePreviewLabel = new JLabel();
                profilePreviewLabel.setPreferredSize(new Dimension(150, 150));
                profilePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                profilePreviewLabel.setHorizontalAlignment(JLabel.CENTER);
                profilePreviewLabel.setVerticalAlignment(JLabel.CENTER);

                RTextField fName = new RTextField(rs.getString("FName"), 30);
                fName.setFont(p14);
                fName.setBorder(b);
                fName.setDisabledTextColor(Color.GRAY);

                RTextField mName = new RTextField(rs.getString("MName"), 30);
                mName.setFont(p14);
                mName.setBorder(b);
                mName.setDisabledTextColor(Color.GRAY);

                RTextField lName = new RTextField(rs.getString("LName"), 30);
                lName.setFont(p14);
                lName.setBorder(b);
                lName.setDisabledTextColor(Color.GRAY);

                RTextField email = new RTextField(rs.getString("Email"), 30);
                email.setFont(p14);
                email.setBorder(b);
                email.setDisabledTextColor(Color.GRAY);

                RTextField cNumber = new RTextField(rs.getString("CNumber"), 30);
                cNumber.setFont(p14);
                cNumber.setBorder(b);
                cNumber.setDisabledTextColor(Color.GRAY);

                RTextField username = new RTextField(rs.getString("UName"), 30);
                username.setFont(p14);
                username.setBorder(b);
                username.setDisabledTextColor(Color.GRAY);

                RTextField password = new RTextField(rs.getString("Password"), 30);
                password.setFont(p14);
                password.setBorder(b);
                password.setDisabledTextColor(Color.GRAY);

                // and for the ID field too:
                RTextField securityIDField = new RTextField(rs.getString("SecurityKey"), 30);
                securityIDField.setEnabled(true);
                securityIDField.setFont(p14);
                securityIDField.setBorder(b);
                securityIDField.setDisabledTextColor(Color.GRAY);

                JComboBox uStatC = new JComboBox(status);
                uStatC.setFont(p14);
                uStatC.setBackground(Color.white);
                uStatC.setBorder(null);
                uStatC.setSelectedItem(rs.getString("UStatus"));
                RPanel uStat = new RPanel(40);
                uStat.setLayout(new BorderLayout());
                uStat.setBackground(Color.white);
                uStatC.setBackground(Color.white);
                uStat.setBorder(b);
                uStat.add(uStatC, BorderLayout.CENTER);

                // Retrieve and display the image if it exists
                byte[] imageBytes = rs.getBytes("ProfilePicture"); // Assuming image is stored in a column named "UserImage"
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
                RTextField idField = new RTextField(inputID, 30);
                idField.setEnabled(false);
                idField.setFont(p14);
                idField.setBorder(b);

                JComboBox<String> uRoleF = new CustomComboBox<>(roles);
                uRoleF.setFont(p14);
                uRoleF.setBorder(null);
                uRoleF.setSelectedItem(rs.getString("Role").toUpperCase());

                uRoleF.setEnabled(false);
                RPanel uRole = new RPanel(40);
                uRole.setLayout(new BorderLayout());
                uRole.setBackground(Color.white);
                uRoleF.setBackground(Color.white);
                uRole.setBorder(b);
                uRole.add(uRoleF, BorderLayout.CENTER);

                JLabel userIDl = new JLabel("User ID:");
                JLabel imagel = new JLabel("Picture:");
                JLabel uRolel = new JLabel("Role:");
                JLabel fNamel = new JLabel("First Name:");
                JLabel mNamel = new JLabel("Middle Name:");
                JLabel lNamel = new JLabel("Last Name:");
                JLabel emaill = new JLabel("Email:");
                JLabel cNumberl = new JLabel("Contact Number:");
                JLabel usernamel = new JLabel("Username:");
                JLabel passwordl = new JLabel("Password:");
                JLabel userstatl = new JLabel("Status:");
                JLabel keyl = new JLabel("Security Key:");

                RPanel imgPanel = new RPanel(30);
                imgPanel.setLayout(new BorderLayout());
                imgPanel.setBorder(b);
                imgPanel.setBackground(Color.WHITE);
                userLblImagePath = new JLabel("Image Loaded");
                userLblImagePath.setFont(p12);
                JButton btnBrowse = new BtnBlue("Browse");
                btnBrowse.setFont(b12);
                btnBrowse.setPreferredSize(new Dimension(100, 30));
                btnBrowse.addActionListener(e -> chooseImageFile());
                imgPanel.setPreferredSize(new Dimension(0, 40));
                imgPanel.add(userLblImagePath, BorderLayout.CENTER);
                imgPanel.add(btnBrowse, BorderLayout.EAST);
                imgPanel.setOpaque(false);

                Dimension labelSize = new Dimension(150, 40);

                imagel.setFont(p14);
                imagel.setPreferredSize(labelSize);

                fNamel.setFont(p14);
                fNamel.setPreferredSize(labelSize);

                mNamel.setFont(p14);
                mNamel.setPreferredSize(labelSize);

                lNamel.setFont(p14);
                lNamel.setPreferredSize(labelSize);

                emaill.setFont(p14);
                emaill.setPreferredSize(labelSize);

                cNumberl.setFont(p14);
                cNumberl.setPreferredSize(labelSize);

                uRolel.setFont(p14);
                uRolel.setPreferredSize(labelSize);

                usernamel.setFont(p14);
                usernamel.setPreferredSize(labelSize);

                passwordl.setFont(p14);
                passwordl.setPreferredSize(labelSize);

                userstatl.setFont(p14);
                userstatl.setPreferredSize(labelSize);

                keyl.setFont(p14);
                keyl.setPreferredSize(labelSize);

                userIDl.setFont(p14);
                userIDl.setPreferredSize(labelSize);

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
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(imagel, gbc);
                //fillers
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                gbc.gridy++;

                miniPanel.add(userIDl, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                // uRole label
                gbc.gridy++;
                miniPanel.add(uRolel, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                // First Name label
                gbc.gridy++;
                miniPanel.add(fNamel, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                // Middle Name label
                gbc.gridy++;
                miniPanel.add(mNamel, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                // Last Name label
                gbc.gridy++;
                miniPanel.add(lNamel, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                // Email label
                gbc.gridy++;
                miniPanel.add(emaill, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                // Contact Number label
                gbc.gridy++;
                miniPanel.add(cNumberl, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                // usernamne label
                gbc.gridy++;
                miniPanel.add(usernamel, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                // password label
                gbc.gridy++;
                miniPanel.add(passwordl, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                //status
                gbc.gridy++;
                miniPanel.add(userstatl, gbc);

                //filler
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                //key
                gbc.gridy++;
                miniPanel.add(keyl, gbc);

                //filler
                gbc.gridy++;
                miniPanel.add(new JLabel(" "), gbc);

                gbc.insets = new Insets(5, 5, 5, 5);

                //ADDS INPUT FIELDS AND COMBOBOXES//
                // Configure input fields
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
                miniPanel.add(imgPanel, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(errorProfile, gbc);

                gbc.gridy++;
                // Customer ID input field
                miniPanel.add(idField, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(errorID, gbc);

                // Role input field
                gbc.gridy++;
                miniPanel.add(uRole, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(errorRole, gbc);

                // First Name input field              
                gbc.gridy++;
                miniPanel.add(fName, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(errorFName, gbc);

                // Middle Name input field
                gbc.gridy++;
                miniPanel.add(mName, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(errorMName, gbc);

                // Last Name input field
                gbc.gridy++;
                miniPanel.add(lName, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(errorLName, gbc);

                // Email input field
                gbc.gridy++;
                miniPanel.add(email, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(errorEmail, gbc);

                // Contact Number input field
                gbc.gridy++;
                miniPanel.add(cNumber, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(errorNumber, gbc);

                // username input field
                gbc.gridy++;
                miniPanel.add(username, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(errorUsername, gbc);

                // password input field
                gbc.gridy++;
                miniPanel.add(password, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(errorPassword, gbc);

                //status
                gbc.gridy++;
                miniPanel.add(uStat, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(errorStatus, gbc);

                //key
                gbc.gridy++;
                miniPanel.add(securityIDField, gbc);

                //fillers
                gbc.gridy++;
                miniPanel.add(errorSecKey, gbc);

                Dimension inputPrefSize = new Dimension(200, 40);
                idField.setPreferredSize(inputPrefSize);
                uRole.setPreferredSize(new Dimension(150, 40));
                fName.setPreferredSize(inputPrefSize);
                mName.setPreferredSize(inputPrefSize);
                lName.setPreferredSize(inputPrefSize);
                cNumber.setPreferredSize(inputPrefSize);
                email.setPreferredSize(inputPrefSize);
                username.setPreferredSize(inputPrefSize);
                password.setPreferredSize(inputPrefSize);
                uStat.setPreferredSize(new Dimension(150, 40));
                imgPanel.setPreferredSize(new Dimension(150, 40));

                JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
                JButton cancelBtn = new BtnRed("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
                JButton updateBtn = new BtnGreen("Update", new ImageIcon(getClass().getResource("/images/icon/create.png")));
                btnPanel.setBorder(b);
                btnPanel.setOpaque(false);
                cancelBtn.addActionListener(e -> {
                    userClear();
                    fName.setForeground(gray);
                    lName.setForeground(gray);
                    mName.setForeground(gray);
                    username.setForeground(gray);
                    password.setForeground(gray);
                    uStatC.setForeground(gray);
                    updateDialog.dispose();
                });
                updateBtn.addActionListener(e -> {
                    clearErrorLabels();
                    String uRoleS = uRoleF.getSelectedItem().toString();
                    String fNameS = fName.getText();
                    String mNameS = mName.getText();
                    String lNameS = lName.getText();
                    String cNum = cNumber.getText();
                    String emailS = email.getText();
                    String userName = username.getText();
                    String aPassword = password.getText();
                    String userStat = uStatC.getSelectedItem().toString();
                    String sKey = securityIDField.getText();
                    clearErrorLabels();
                    Boolean checker = false;

                    if (fNameS.isEmpty() || fNameS.equalsIgnoreCase("Required")) {
                        errorFName.setText("First Name is Required");
                        fName.setForeground(Color.red);
                        checker = true;
                    }

                    if (lNameS.isEmpty() || lNameS.equalsIgnoreCase("Required")) {
                        errorLName.setText("Last Name Required");
                        lName.setForeground(Color.red);
                        checker = true;

                    }

                    if (uRoleS.isEmpty() || uRoleS.equalsIgnoreCase("SELECT")) {
                        errorRole.setText("Role is Required");
                        uRoleF.setForeground(Color.red);
                        checker = true;

                    }
                    
                    if (mNameS.isEmpty() || mNameS.equalsIgnoreCase("Optional")) {
                        mNameS = "NULL";
                    }
                    if (!cNum.isEmpty()) {
                        if (cNum.equals("09") || cNum.equals("NULL")) {
                            cNum = "NULL";
                        } else {
                            if (!Database.isValidContactNumberLength(cNum)) {

                                errorNumber.setText("Contact number must be 11 digits.");
                                errorNumber.setForeground(Color.red);
                                checker = true;
                            }
                            if (!Database.isValidContactNumber(cNum)) {

                                errorNumber.setText("Invalid phone number. It must start with '09'.");
                                errorNumber.setForeground(Color.red);
                                checker = true;
                            }
                        }
                    }

                    if (!emailS.isEmpty()) {
                        if (emailS.equals("sample@patz.com") || emailS.equalsIgnoreCase("NULL")) {
                            emailS = "NULL";
                        } else if (!Database.isValidEmail(emailS)) {
                            JOptionPane.showMessageDialog(this, "Invalid email format.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            errorEmail.setText("Invalid Email Format");
                            errorEmail.setForeground(Color.red);
                            checker = true;
                        }

                    } else if (emailS.isEmpty()) {
                        emailS = "NULL";
                    }

                    if (userName.isEmpty() || userName.equalsIgnoreCase("Required")) {
                        errorUsername.setText("Username is Required");
                        username.setForeground(Color.red);
                        checker = true;

                    }

                    if (aPassword.isEmpty() || aPassword.equalsIgnoreCase("Required")) {
                        errorPassword.setText("Password is required");
                        userPassword.setForeground(Color.red);
                        checker = true;
                    } else if (aPassword.length() < 8) {
                        errorPassword.setText("Password must be at least 8 characters");
                        userPassword.setForeground(Color.red);
                        checker = true;
                    } else if (!aPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d])[a-zA-Z\\d[^\\s]]+$")) {
                        errorPassword.setText("Password must be alphanumeric with symbols");
                        userPassword.setForeground(Color.red);
                        checker = true;
                    }

                    if (userStat.isEmpty() || userStat.equalsIgnoreCase("Required")) {
                        errorStatus.setText("Password is Required");
                        uStat.setForeground(Color.red);
                        checker = true;

                    }

                    if (sKey.isEmpty() || sKey.equalsIgnoreCase("Required")) {
                        errorStatus.setText("Password is Required");
                        userSecKey.setForeground(Color.red);
                        checker = true;

                    }
                    if (sKey.length() > 15) {
                        errorSecKey.setText("Invalid length. Must be less than 15 characters long");
                        userSecKey.setForeground(Color.red);
                        checker = true;
                    }
                    if (!sKey.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d])[a-zA-Z\\d[^\\s]]+$")) {
                        errorSecKey.setText("Security Key must be alphanumeric with symbols");
                        userSecKey.setForeground(Color.red);
                        checker = true;
                    }

                    // Check if any required field is empty or if 'checker' is true
                    if (checker == true) {
                        // Show error message if any required field is not filled
                        JOptionPane.showMessageDialog(updateDialog, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);

                        // Set checker flag to false to indicate an error has been shown
                        checker = false;
                        return; // Exit from the method or action, preventing further processing
                    }

                    if (pfpChooser != null && pfpChooser.getSelectedFile() != null) {
                        userImageFile = pfpChooser.getSelectedFile();

                        // Check if valid PNG file
                        if (userImageFile.exists() && userImageFile.getName().toLowerCase().endsWith(".png")) {
                            // Update with image
                            String updateSql = "UPDATE User_ SET FName = ?, MName = ?, LName = ?, Email = ?, CNumber = ?, UName = ?, Password = ?, Role = ?, UStatus = ?, ProfilePicture = ? , SecurityKey = ? WHERE UserID = ?";

                            try (FileInputStream fis = new FileInputStream(userImageFile); PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

                                updateStmt.setString(1, fNameS);
                                updateStmt.setString(2, mNameS);
                                updateStmt.setString(3, lNameS);
                                updateStmt.setString(4, emailS);
                                updateStmt.setString(5, cNum);
                                updateStmt.setString(6, userName);
                                updateStmt.setString(7, aPassword);
                                updateStmt.setString(8, uRoleS);
                                updateStmt.setString(9, userStat);
                                updateStmt.setBinaryStream(10, fis, (int) userImageFile.length());
                                updateStmt.setString(11, sKey);
                                updateStmt.setString(12, inputID);

                                updateStmt.executeUpdate();

                                JOptionPane.showMessageDialog(updateDialog, "User ID No. " + inputID + " updated successfully.");
                                updateDialog.dispose();
                                retrieveUserTableData();

                            } catch (FileNotFoundException ex) {
                                JOptionPane.showMessageDialog(this, "User image file not found.", "Error", JOptionPane.ERROR_MESSAGE);
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
                        String updateSql = "UPDATE User_ SET FName = ?, MName = ?, LName = ?, Email = ?, CNumber = ?, UName = ?, Password = ?, Role = ?, UStatus = ?,  SecurityKey = ? WHERE UserID = ?";

                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setString(1, fNameS);
                            updateStmt.setString(2, mNameS);
                            updateStmt.setString(3, lNameS);
                            updateStmt.setString(4, emailS);
                            updateStmt.setString(5, cNum);
                            updateStmt.setString(6, userName);
                            updateStmt.setString(7, aPassword);
                            updateStmt.setString(8, uRoleS);
                            updateStmt.setString(9, userStat);
                            updateStmt.setString(10, sKey);
                            updateStmt.setString(11, inputID);

                            updateStmt.executeUpdate();

                            JOptionPane.showMessageDialog(updateDialog, "UserID " + inputID + " updated successfully.");
                            userClear();
                            updateDialog.dispose();
                            retrieveUserTableData();

                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(updateDialog, "Update failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                btnPanel.add(cancelBtn);
                btnPanel.add(updateBtn);
                CNavScrollPane scrollPane = new CNavScrollPane(miniPanel);
                scrollPane.setOpaque(false);
                scrollPane.setBorder(null);
                scrollPane.getViewport().setOpaque(false);
                updateDialog.add(mainPanel, BorderLayout.CENTER);
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(scrollPane, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);
                updateDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "User ID not found.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void userSearch() {

        String inputID = JOptionPane.showInputDialog(this, "Enter User ID to Search:");
        if (inputID == null) {
            return;
        }

        if (inputID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No User ID inputted", "No ID Inputted", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM User_ WHERE UserID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, inputID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JDialog searchDialog = new JDialog((Frame) null, "Search User", true);
                searchDialog.setSize(470, 670);
                searchDialog.setLayout(new BorderLayout());
                searchDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                searchDialog.setLocationRelativeTo(null);

                JPanel mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
                mainPanel.setLayout(new BorderLayout());

                // Title Panel
                JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/blue1.jpg")).getImage());
                titlePanel.setLayout(new BorderLayout());
                titlePanel.setBorder(b);
                JLabel title = new JLabel("Search User");
                title.setFont(b18);
                title.setForeground(Color.white);
                title.setHorizontalAlignment(JLabel.CENTER);
                titlePanel.add(title, BorderLayout.CENTER);

                JPanel miniPanel = new JPanel(new GridBagLayout());
                miniPanel.setBorder(b);
                miniPanel.setOpaque(false);

                RTextField idField = new RTextField(inputID, 30);
                idField.setFont(p14);
                idField.setBorder(b);
                idField.setEnabled(false);

                // Create the image preview label with default image
                profilePreviewLabel = new JLabel();
                profilePreviewLabel.setPreferredSize(new Dimension(150, 150));
                profilePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                profilePreviewLabel.setHorizontalAlignment(JLabel.CENTER);
                profilePreviewLabel.setVerticalAlignment(JLabel.CENTER);

                RTextField uRoleF = new RTextField(rs.getString("Role"), 30);
                RTextField fName = new RTextField(rs.getString("FName"), 30);
                RTextField mName = new RTextField(rs.getString("MName"), 30);
                RTextField lName = new RTextField(rs.getString("LName"), 30);
                RTextField email = new RTextField(rs.getString("Email"), 30);
                RTextField cNumber = new RTextField(rs.getString("CNumber"), 30);
                RTextField username = new RTextField(rs.getString("uName"), 30);
                RTextField password = new RTextField(rs.getString("Password"), 30);
                RTextField userStat = new RTextField(rs.getString("UStatus"), 30);
                RTextField secuKey = new RTextField(rs.getString("SecurityKey"), 30);
                // Retrieve and display the image if it exists
                byte[] imageBytes = rs.getBytes("ProfilePicture"); // Assuming image is stored in a column named "UserImage"
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

                idField.setFont(p14);
                idField.setBorder(b);
                idField.setEnabled(false);
                idField.setDisabledTextColor(Color.GRAY);

                uRoleF.setFont(p14);
                uRoleF.setBorder(b);
                uRoleF.setEnabled(false);
                uRoleF.setDisabledTextColor(Color.GRAY);

                fName.setFont(p14);
                fName.setBorder(b);
                fName.setEnabled(false);
                fName.setDisabledTextColor(Color.GRAY);

                mName.setFont(p14);
                mName.setBorder(b);
                mName.setEnabled(false);
                mName.setDisabledTextColor(Color.GRAY);

                lName.setFont(p14);
                lName.setBorder(b);
                lName.setEnabled(false);
                lName.setDisabledTextColor(Color.GRAY);

                email.setFont(p14);
                email.setBorder(b);
                email.setEnabled(false);
                email.setDisabledTextColor(Color.GRAY);

                cNumber.setFont(p14);
                cNumber.setBorder(b);
                cNumber.setEnabled(false);
                cNumber.setDisabledTextColor(Color.GRAY);

                username.setFont(p14);
                username.setBorder(b);
                username.setEnabled(false);
                username.setDisabledTextColor(Color.GRAY);

                password.setFont(p14);
                password.setBorder(b);
                password.setEnabled(false);
                password.setDisabledTextColor(Color.GRAY);

                userStat.setFont(p14);
                userStat.setBorder(b);
                userStat.setEnabled(false);
                userStat.setDisabledTextColor(Color.GRAY);

                secuKey.setFont(p14);
                secuKey.setBorder(b);
                secuKey.setEnabled(false);
                secuKey.setDisabledTextColor(Color.GRAY);

                JLabel userIDl = new JLabel("User ID:");
                JLabel uRolel = new JLabel("Role:");
                JLabel fNamel = new JLabel("First Name:");
                JLabel mNamel = new JLabel("Middle Name:");
                JLabel lNamel = new JLabel("Last Name:");
                JLabel cNumberl = new JLabel("Contact Number:");
                JLabel emaill = new JLabel("Email:");
                JLabel usernamel = new JLabel("Username:");
                JLabel passwordl = new JLabel("Password:");
                JLabel statlbl = new JLabel("Status :");
                JLabel keylbl = new JLabel("Security Key :");

                Dimension d = new Dimension(150, 40);

                userIDl.setFont(p14);
                userIDl.setVerticalAlignment(SwingConstants.CENTER);
                userIDl.setPreferredSize(d);

                uRolel.setFont(p14);
                uRolel.setVerticalAlignment(SwingConstants.CENTER);
                uRolel.setPreferredSize(d);

                fNamel.setFont(p14);
                fNamel.setVerticalAlignment(SwingConstants.CENTER);
                fNamel.setPreferredSize(d);

                mNamel.setFont(p14);
                mNamel.setVerticalAlignment(SwingConstants.CENTER);
                mNamel.setPreferredSize(d);

                lNamel.setFont(p14);
                lNamel.setVerticalAlignment(SwingConstants.CENTER);
                lNamel.setPreferredSize(d);

                emaill.setFont(p14);
                emaill.setVerticalAlignment(SwingConstants.CENTER);
                emaill.setPreferredSize(d);

                cNumberl.setFont(p14);
                cNumberl.setVerticalAlignment(SwingConstants.CENTER);
                cNumberl.setPreferredSize(d);

                usernamel.setFont(p14);
                usernamel.setVerticalAlignment(SwingConstants.CENTER);
                usernamel.setPreferredSize(d);

                passwordl.setFont(p14);
                passwordl.setVerticalAlignment(SwingConstants.CENTER);
                passwordl.setPreferredSize(d);

                statlbl.setFont(p14);
                statlbl.setVerticalAlignment(SwingConstants.CENTER);
                statlbl.setPreferredSize(d);

                keylbl.setFont(p14);
                keylbl.setVerticalAlignment(SwingConstants.CENTER);
                keylbl.setPreferredSize(d);

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
                miniPanel.add(new JLabel(" "), gbc);
                gbc.gridy++;
                miniPanel.add(userIDl, gbc);

                // role label
                gbc.gridy++;
                miniPanel.add(uRolel, gbc);

                // First Name label
                gbc.gridy++;
                miniPanel.add(fNamel, gbc);

                // Middle Name label
                gbc.gridy++;
                miniPanel.add(mNamel, gbc);

                // Last Name label
                gbc.gridy++;
                miniPanel.add(lNamel, gbc);

                // Email label
                gbc.gridy++;
                miniPanel.add(emaill, gbc);
                // Contact Number label
                gbc.gridy++;
                miniPanel.add(cNumberl, gbc);

                //username
                gbc.gridy++;
                miniPanel.add(usernamel, gbc);

                //password
                gbc.gridy++;
                miniPanel.add(passwordl, gbc);

                //password
                gbc.gridy++;
                miniPanel.add(statlbl, gbc);

                //password
                gbc.gridy++;
                miniPanel.add(keylbl, gbc);

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
                miniPanel.add(idField, gbc);

                // Roles input field
                gbc.gridy++;
                miniPanel.add(uRoleF, gbc);

                // First Name input field
                gbc.gridy++;
                miniPanel.add(fName, gbc);

                // Middle Name input field
                gbc.gridy++;
                miniPanel.add(mName, gbc);

                // Last Name input field
                gbc.gridy++;
                miniPanel.add(lName, gbc);

                // Email input field
                gbc.gridy++;
                miniPanel.add(email, gbc);

                // Contact Number input field
                gbc.gridy++;
                miniPanel.add(cNumber, gbc);

                gbc.gridy++;
                miniPanel.add(username, gbc);

                gbc.gridy++;
                miniPanel.add(password, gbc);

                gbc.gridy++;
                miniPanel.add(userStat, gbc);

                gbc.gridy++;
                miniPanel.add(secuKey, gbc);

                Dimension inputPrefSize = new Dimension(150, 40);
                idField.setPreferredSize(inputPrefSize);
                uRoleF.setPreferredSize(inputPrefSize);
                fName.setPreferredSize(inputPrefSize);
                mName.setPreferredSize(inputPrefSize);
                lName.setPreferredSize(inputPrefSize);
                cNumber.setPreferredSize(inputPrefSize);
                email.setPreferredSize(inputPrefSize);
                username.setPreferredSize(inputPrefSize);
                password.setPreferredSize(inputPrefSize);
                userStat.setPreferredSize(inputPrefSize);
                secuKey.setPreferredSize(inputPrefSize);

                JPanel btnPanel = new JPanel(new GridLayout(1, 1, 10, 10));
                btnPanel.setOpaque(false);
                JButton returnBtn = new BtnDefault("Return", new ImageIcon(getClass().getResource("/images/icon/return.png")));

                btnPanel.setBorder(b);

                returnBtn.addActionListener(e -> searchDialog.dispose());

                btnPanel.add(returnBtn);
                CNavScrollPane scrollPane = new CNavScrollPane(miniPanel);
                scrollPane.setOpaque(false);
                scrollPane.setBorder(null);
                scrollPane.getViewport().setOpaque(false);
                searchDialog.add(mainPanel, BorderLayout.CENTER);
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(scrollPane, BorderLayout.CENTER);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);

                searchDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "User ID not found.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }

    }

    private void retrieveUserTableData() {
        DefaultTableCellRenderer centerRenderer = new CustomRowRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Manually assign the center renderer to each column
        userTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);  // UserID
        userTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);  // Role
        userTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);  // FName
        userTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);  // MName
        userTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);  // LName
        userTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);  // Email
        userTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);  // CNumber
        userTable.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);  // Username
        userTable.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);  // Password
        userTable.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);  // ProfilePicture
        userTable.getColumnModel().getColumn(10).setCellRenderer(centerRenderer); // SecurityKey
        userTable.getColumnModel().getColumn(11).setCellRenderer(centerRenderer); // UStatus

        String sql = "SELECT * FROM User_";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            // Clear existing rows in the table model
            DefaultTableModel model = (DefaultTableModel) userTable.getModel();
            model.setRowCount(0); // Clear old rows

            // Fill the table with fresh data
            while (rs.next()) {
                Object[] row = {
                    rs.getString("UserID"),
                    rs.getString("Role"),
                    rs.getString("FName"),
                    rs.getString("MName"),
                    rs.getString("LName"),
                    rs.getString("Email"),
                    rs.getString("CNumber"),
                    rs.getString("UName"),
                    rs.getString("Password"),
                    rs.getString("ProfilePicture"),
                    rs.getString("SecurityKey"),
                    rs.getString("UStatus")

                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading table data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to let the user select a .png image file and update the profile picture preview
    private void chooseImageFile() {
        pfpChooser = new JFileChooser();

        FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG Images (*.png)", "png");
        pfpChooser.setFileFilter(pngFilter);
        pfpChooser.setAcceptAllFileFilterUsed(false);

        int result = pfpChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File chosen = pfpChooser.getSelectedFile();

            if (chosen.getName().toLowerCase().endsWith(".png")) {
                userImageFile = chosen;

                if (userLblImagePath != null) {
                    userLblImagePath.setText(userImageFile.getName());
                }

                if (profilePreviewLabel != null) {
                    ImageIcon newIcon = new ImageIcon(userImageFile.getAbsolutePath());
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

    private void likeSearchUsers() {
        // list only the fields you want to allow searching
        String[] fields = {
            "CAST(UserID AS VARCHAR)",
            "FName", "MName", "LName",
            "CNumber", "Email",
            "Uname", "Role", "UStatus"
        };

        // friendly labels matching the actual SQL expressions
        String[] labels = {
            "UserID", "First Name", "Middle Name", "Last Name",
            "Contact Number", "Email", "Username", "Role", "Status"
        };

        // choose field
        String label = (String) JOptionPane.showInputDialog(
                this,
                "Select field to search:",
                "Field Selection",
                JOptionPane.PLAIN_MESSAGE,
                null,
                labels,
                labels[0]);
        if (label == null) {
            return;
        }

        // map back to actual SQL
        String field = fields[java.util.Arrays.asList(labels).indexOf(label)];

        // choose match type
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

        // enter keyword
        String keyword = JOptionPane.showInputDialog(this, "Enter keyword:");
        if (keyword == null || keyword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No keyword entered.");
            return;
        }
        keyword = keyword.trim();
        switch (matchType) {
            case "Starts With":
                keyword = keyword + "%";
                break;
            case "Ends With":
                keyword = "%" + keyword;
                break;
            default:
                keyword = "%" + keyword + "%";
                break;
        }

        String sql = "SELECT UserID, FName, MName, LName, CNumber, Email, Uname, Password, Role, ProfilePicture, SecurityKey,UStatus "
                + "FROM USER_ WHERE " + field + " LIKE ?";

        try (
                Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, keyword);
            try (ResultSet rs = stmt.executeQuery()) {
                userTableModel.setRowCount(0);
                while (rs.next()) {
                    userTableModel.addRow(new Object[]{
                        rs.getString("UserID"),
                        rs.getString("Role"),
                        rs.getString("FName"),
                        rs.getString("MName"),
                        rs.getString("LName"),
                        rs.getString("Email"),
                        rs.getString("CNumber"),
                        rs.getString("UName"),
                        rs.getString("Password"),
                        rs.getString("ProfilePicture"),
                        rs.getString("SecurityKey"),
                        rs.getString("UStatus")
                    });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void sortUsers() {
        String[] fields = {
            "UserID", "FName", "MName", "LName",
            "CNumber", "Email", "Uname", "Role", "UStatus"
        };
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

        String sql = "SELECT UserID, FName, MName, LName, CNumber, Email, Uname, Password, Role, ProfilePicture, SecurityKey,UStatus "
                + "FROM USER_ ORDER BY " + field + " " + order;

        try (
                Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            userTableModel.setRowCount(0);
            while (rs.next()) {
                userTableModel.addRow(new Object[]{
                    rs.getString("UserID"),
                    rs.getString("Role"),
                    rs.getString("FName"),
                    rs.getString("MName"),
                    rs.getString("LName"),
                    rs.getString("Email"),
                    rs.getString("CNumber"),
                    rs.getString("UName"),
                    rs.getString("Password"),
                    rs.getString("ProfilePicture"),
                    rs.getString("SecurityKey"),
                    rs.getString("UStatus")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error sorting: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ImageIcon scaleImage(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

}
