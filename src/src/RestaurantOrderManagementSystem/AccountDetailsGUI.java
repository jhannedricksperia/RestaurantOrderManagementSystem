package RestaurantOrderManagementSystem;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class AccountDetailsGUI extends Components {

    private RTextField userIDField, fNameField, mNameField, lNameField,
            cNumberField, emailField, unameField, roleField, statusField;
    private JPasswordField passwordField;
    private JButton editButton, changePasswordButton, changePictureButton, revertPictureButton;
    private JLabel profilePictureLabel;
    private boolean isEditMode = false;
    private int currentUserID;
    private JPanel mainPanel;
    private File selectedPictureFile;

    public AccountDetailsGUI(int userID) {
        this.currentUserID = userID;
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(50, 70, 50, 100));

        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setOpaque(false);
        JPanel picPanel = new JPanel(new BorderLayout());
        picPanel.setPreferredSize(new Dimension(200, 250));
        picPanel.setOpaque(false);
        profilePictureLabel = new JLabel("No Image", SwingConstants.CENTER);
        profilePictureLabel.setFont(p18);
        profilePictureLabel.setPreferredSize(new Dimension(200, 200));
        profilePictureLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        picPanel.add(profilePictureLabel, BorderLayout.CENTER);

        JPanel btnPanelPic = new JPanel(new GridLayout(1, 2, 10, 10));
        btnPanelPic.setOpaque(false);
        btnPanelPic.setBorder(b);

        Insets in = new Insets(2, 2, 2, 2);
        // Change button
        changePictureButton = new BtnGreen("Update", new ImageIcon(getClass().getResource("/images/icon/change2.png")));
        changePictureButton.setFont(b12);
        changePictureButton.setMargin(in);

        changePictureButton.addActionListener(e -> changeProfilePicture());
        btnPanelPic.add(changePictureButton);

        // Revert button
        revertPictureButton = new BtnRed("Revert", new ImageIcon(getClass().getResource("/images/icon/revert.png")));
        revertPictureButton.setFont(b12);
        revertPictureButton.setMargin(in);
        revertPictureButton.addActionListener(e -> revertProfilePicture());
        btnPanelPic.add(revertPictureButton);
        btnPanelPic.setPreferredSize(new Dimension(0, 50));
        picPanel.add(btnPanelPic, BorderLayout.SOUTH);
        westPanel.add(picPanel, BorderLayout.NORTH);
        mainPanel.add(westPanel, BorderLayout.WEST);

        JPanel innerPanel = new JPanel(new GridBagLayout());
        innerPanel.setOpaque(false);
        innerPanel.setBorder(BorderFactory.createEmptyBorder(10, 80, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 8, 7);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        // LABELS
        // Row 1: User ID
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        JLabel lblUserID = new JLabel("User ID:");
        lblUserID.setFont(p18);
        innerPanel.add(lblUserID, gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);
        // Row 2: Role
        gbc.gridy++;
        JLabel lblRole = new JLabel("Role:");
        lblRole.setFont(p18);
        innerPanel.add(lblRole, gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

        // Row 3: Status
        gbc.gridy++;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(p18);
        innerPanel.add(lblStatus, gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

        // Row 4: First Name
        gbc.gridy++;
        JLabel lblFName = new JLabel("First Name:");
        lblFName.setFont(p18);
        innerPanel.add(lblFName, gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

        // Row 5: Middle Name
        gbc.gridy++;
        JLabel lblMName = new JLabel("Middle Name:");
        lblMName.setFont(p18);
        innerPanel.add(lblMName, gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

        // Row 6: Last Name
        gbc.gridy++;
        JLabel lblLName = new JLabel("Last Name:");
        lblLName.setFont(p18);
        innerPanel.add(lblLName, gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

        // Row 7: Contact Number
        gbc.gridy++;
        JLabel lblCNumber = new JLabel("Contact Number:");
        lblCNumber.setFont(p18);
        innerPanel.add(lblCNumber, gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

        // Row 8: Email
        gbc.gridy++;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(p18);
        innerPanel.add(lblEmail, gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

        // Row 9: Username
        gbc.gridy++;
        JLabel lblUname = new JLabel("Username:");
        lblUname.setFont(p18);
        innerPanel.add(lblUname, gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

        // Row 10: Change Password Button
        gbc.gridy++;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(p18);
        innerPanel.add(lblPass, gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

        gbc.gridy = 1;
        innerPanel.add(new JLabel(""), gbc);
        // INPUTS
// ===== Row 1: User ID Field =====
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.weightx = 1;
        userIDField = new RTextField(30);
        userIDField.setFont(p18);
        userIDField.setEnabled(false);
        userIDField.setDisabledTextColor(Color.GRAY);
        innerPanel.add(userIDField, gbc);

        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

// ===== Row 2: Role Field + Error Label =====
        gbc.gridy++;
        roleField = new RTextField(30);
        roleField.setFont(p18);
        roleField.setEnabled(false);
        roleField.setDisabledTextColor(Color.GRAY);
        innerPanel.add(roleField, gbc);

        gbc.gridy++;
        JLabel errorRole = new JLabel();
        errorRole.setForeground(Color.RED);
        errorRole.setFont(p11);
        innerPanel.add(errorRole, gbc);

        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

// ===== Row 3: Status Field + Error Label =====
        gbc.gridy++;
        statusField = new RTextField(30);
        statusField.setFont(p18);
        statusField.setEnabled(false);
        statusField.setDisabledTextColor(Color.GRAY);
        innerPanel.add(statusField, gbc);

        gbc.gridy++;
        JLabel errorStatus = new JLabel();
        errorStatus.setForeground(Color.RED);
        errorStatus.setFont(p11);
        innerPanel.add(errorStatus, gbc);

        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

// ===== Row 4: First Name Field + Error Label =====
        gbc.gridy++;
        fNameField = new RTextField(30);
        fNameField.setFont(p18);
        fNameField.setEnabled(false);
        fNameField.setDisabledTextColor(Color.GRAY);
        innerPanel.add(fNameField, gbc);

        gbc.gridy++;
        JLabel errorFName = new JLabel();
        errorFName.setForeground(Color.RED);
        errorFName.setFont(p11);
        innerPanel.add(errorFName, gbc);

        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

// ===== Row 5: Middle Name Field + Error Label =====
        gbc.gridy++;
        mNameField = new RTextField(30);
        mNameField.setFont(p18);
        mNameField.setEnabled(false);
        mNameField.setDisabledTextColor(Color.GRAY);
        innerPanel.add(mNameField, gbc);

        gbc.gridy++;
        JLabel errorMName = new JLabel();
        errorMName.setForeground(Color.RED);
        errorMName.setFont(p11);
        innerPanel.add(errorMName, gbc);

        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

// ===== Row 6: Last Name Field + Error Label =====
        gbc.gridy++;
        lNameField = new RTextField(30);
        lNameField.setFont(p18);
        lNameField.setEnabled(false);
        lNameField.setDisabledTextColor(Color.GRAY);
        innerPanel.add(lNameField, gbc);

        gbc.gridy++;
        JLabel errorLName = new JLabel();
        errorLName.setForeground(Color.RED);
        errorLName.setFont(p11);
        innerPanel.add(errorLName, gbc);

        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

// ===== Row 7: Contact Number Field + Error Label =====
        gbc.gridy++;
        cNumberField = new RTextField(30);
        cNumberField.setFont(p18);
        cNumberField.setEnabled(false);
        cNumberField.setDisabledTextColor(Color.GRAY);
        innerPanel.add(cNumberField, gbc);

        gbc.gridy++;
        JLabel errorCNumber = new JLabel();
        errorCNumber.setForeground(Color.RED);
        errorCNumber.setFont(p11);
        innerPanel.add(errorCNumber, gbc);

        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

// ===== Row 8: Email Field + Error Label =====
        gbc.gridy++;
        emailField = new RTextField(30);
        emailField.setFont(p18);
        emailField.setEnabled(false);
        emailField.setDisabledTextColor(Color.GRAY);
        innerPanel.add(emailField, gbc);

        gbc.gridy++;
        JLabel errorEmail = new JLabel();
        errorEmail.setForeground(Color.RED);
        errorEmail.setFont(p11);
        innerPanel.add(errorEmail, gbc);

        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

// ===== Row 9: Username Field + Error Label =====
        gbc.gridy++;
        unameField = new RTextField(30);
        unameField.setFont(p18);
        unameField.setEnabled(false);
        unameField.setDisabledTextColor(Color.GRAY);
        innerPanel.add(unameField, gbc);

        gbc.gridy++;
        JLabel errorUName = new JLabel();
        errorUName.setForeground(Color.RED);
        errorUName.setFont(p11);
        innerPanel.add(errorUName, gbc);

        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

// ===== Row 10: Change Password Button =====
        gbc.gridy++;
        changePasswordButton = new BtnGreen("Change Password");
        changePasswordButton.setFont(b16);
        changePasswordButton.setEnabled(false);
        changePasswordButton.addActionListener(e -> executeAccessChangePassword());
        changePasswordButton.setPreferredSize(new Dimension(0, 40));
        innerPanel.add(changePasswordButton, gbc);

        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        innerPanel.add(new JLabel(""), gbc);

        mainPanel.add(innerPanel, BorderLayout.CENTER);

        // Edit button panel
        editButton = new BtnBlue("Edit");
        editButton.setPreferredSize(new Dimension(150, 40));
        editButton.addActionListener(e -> toggleEditMode());
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(editButton);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        // Load data
        loadUserData(userID);
    }

    private void loadUserData(int userID) {
        String query
                = "SELECT FName, MName, LName, CNumber, Email, Uname, Role, UStatus, ProfilePicture "
                + "FROM USER_ WHERE UserID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // populate text fields...
                    userIDField.setText(String.valueOf(userID));
                    fNameField.setText(rs.getString("FName"));
                    mNameField.setText(rs.getString("MName"));
                    lNameField.setText(rs.getString("LName"));
                    cNumberField.setText(rs.getString("CNumber"));
                    emailField.setText(rs.getString("Email"));
                    unameField.setText(rs.getString("Uname"));
                    roleField.setText(rs.getString("Role"));
                    statusField.setText(rs.getString("UStatus"));

                    // load the ProfilePicture BLOB
                    byte[] imgBytes = rs.getBytes("ProfilePicture");
                    if (imgBytes != null && imgBytes.length > 0) {
                        ImageIcon rawIcon = new ImageIcon(imgBytes);
                        Image scaled = rawIcon.getImage()
                                .getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                        profilePictureLabel.setIcon(new ImageIcon(scaled));
                        profilePictureLabel.setText("");  // remove the “No Image” text
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading user data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void reload() {
        loadUserData(currentUserID);
    }

    public JPanel getAccountDetailsGUI() {
        return mainPanel;
    }

    public JLabel getUpdatedProfile() {
        return profilePictureLabel;
    }

    private void toggleEditMode() {
        if (isEditMode) {
            // Save mode
            if (unameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Username cannot be empty.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (Database.isUsernameTaken(
                    Integer.parseInt(userIDField.getText()), unameField.getText().trim())) {
                JOptionPane.showMessageDialog(this,
                        "Username already exists. Please choose another.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            saveChanges();
            editButton.setText("Edit");
        } else {
            editButton.setText("Save");
        }

        isEditMode = !isEditMode;
        // Enable all except userID and role
        fNameField.setEnabled(isEditMode);
        mNameField.setEnabled(isEditMode);
        lNameField.setEnabled(isEditMode);
        cNumberField.setEnabled(isEditMode);
        emailField.setEnabled(isEditMode);
        unameField.setEnabled(isEditMode);
        statusField.setEnabled(false);
        roleField.setEnabled(false);
        changePasswordButton.setEnabled(isEditMode);
    }

    private void saveChanges() {
        String updateSql
                = "UPDATE USER_ SET FName = ?, MName = ?, LName = ?, CNumber = ?, Email = ?, Uname = ? WHERE UserID = ?";

        try (
                Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setString(1, fNameField.getText());
            stmt.setString(2, mNameField.getText());
            stmt.setString(3, lNameField.getText());
            stmt.setString(4, cNumberField.getText());
            stmt.setString(5, emailField.getText());
            stmt.setString(6, unameField.getText());
            stmt.setInt(7, Integer.parseInt(userIDField.getText()));
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Please relogin to apply the changes.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error saving changes: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changeProfilePicture() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            if (JOptionPane.showConfirmDialog(this,
                    "Submit this image?", "Confirm",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                try {
                    // Read file into byte array
                    byte[] imageBytes = Files.readAllBytes(file.toPath());

                    // Update database
                    String sql = "UPDATE USER_ SET ProfilePicture = ? WHERE UserID = ?";
                    try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                        stmt.setBytes(1, imageBytes);
                        stmt.setInt(2, currentUserID);  // make sure userID is available in this context
                        int rowsUpdated = stmt.executeUpdate();

                        if (rowsUpdated > 0) {
                            // Update image on GUI
                            ImageIcon icon = new ImageIcon(imageBytes);
                            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                            profilePictureLabel.setText("");
                            profilePictureLabel.setIcon(new ImageIcon(img));
                         JOptionPane.showMessageDialog(this, "Please relogin to apply the changes.");
                        } else {
                            JOptionPane.showMessageDialog(this, "User not found or image update failed.");
                        }
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to read image file: " + ex.getMessage(),
                            "File Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }

    private void revertProfilePicture() {
        // Load default ImageIcon once
        ImageIcon defaultIcon;
        try (InputStream in = getClass().getResourceAsStream(
                "/images/profilePicture/defaultProfile.png")) {
            if (in == null) {
                throw new IOException("Default image not found in classpath");
            }
            byte[] bytes = in.readAllBytes();
            defaultIcon = new ImageIcon(bytes);
            JOptionPane.showMessageDialog(this, "Please relogin to apply the changes.");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Could not load default image: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Scale a small icon for the dialog
        ImageIcon dialogIcon = new ImageIcon(
                defaultIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH)
        );

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to revert back to the default profile picture?",
                "Confirm Revert",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                dialogIcon
        );
        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        // Persist to database
        String updateSql = "UPDATE USER_ SET ProfilePicture = ? WHERE UserID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(updateSql); InputStream in2 = getClass().getResourceAsStream(
                "/images/profilePicture/defaultProfile.png"
        )) {

            byte[] defaultBytes = in2.readAllBytes();
            ps.setBytes(1, defaultBytes);
            ps.setInt(2, currentUserID);
            ps.executeUpdate();
        } catch (IOException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error reverting profile picture: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update UI with full‑sized image
        ImageIcon scaled = new ImageIcon(
                defaultIcon.getImage().getScaledInstance(
                        profilePictureLabel.getWidth(),
                        profilePictureLabel.getHeight(),
                        Image.SCALE_SMOOTH
                )
        );
        profilePictureLabel.setIcon(scaled);
        profilePictureLabel.setText("");
    }

    private void executeAccessChangePassword() {
        JDialog loginFrame = new JDialog((JFrame) null, "Change Password Access", true);
        loginFrame.setIconImage(Components.l);
        loginFrame.setTitle("Change Password Access");
        loginFrame.setResizable(false);
        loginFrame.setSize(450, 300);
        loginFrame.setIconImage(Components.l);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());

        // Background Panel
        JPanel backgroundPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
        backgroundPanel.setLayout(new BorderLayout());

        // Info Panel
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Labels and Inputs
        JLabel lblSecurityKey = new JLabel("Security Key");
        lblSecurityKey.setFont(Components.p18);

        JPasswordField txtSecurityKey = new RPasswordField(30);

        txtSecurityKey.setFont(Components.p18);
        txtSecurityKey.setPreferredSize(new Dimension(500, 40));

        JLabel errorSecurityKey = new JLabel();
        errorSecurityKey.setFont(Components.p11);
        errorSecurityKey.setForeground(Color.red);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Username Row
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(lblSecurityKey, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        // SecurityKey Row
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(txtSecurityKey, gbc);
        gbc.gridy++;
        infoPanel.add(errorSecurityKey, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);

        // SOUTH PANEL – Buttons
        JPanel southPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        southPanel.setOpaque(false);
        southPanel.setBorder(Components.b);

        JButton btnReturn = new BtnRed("Return", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
        JButton btnAccess = new BtnGreen("Access", new ImageIcon(getClass().getResource("/images/icon/login.png")));

        btnReturn.setPreferredSize(new Dimension(120, 30));
        btnAccess.setPreferredSize(new Dimension(120, 30));
        southPanel.add(btnReturn);
        southPanel.add(btnAccess);

        // NORTH PANEL – Title
        JPanel northPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/orange.jpg")).getImage());
        northPanel.setLayout(new BorderLayout());
        northPanel.setBorder(Components.b);

        JLabel titleLabel = new JLabel("Enter your Security Key", SwingConstants.CENTER);
        titleLabel.setFont(Components.b22);
        titleLabel.setForeground(Color.white);
        northPanel.add(titleLabel, BorderLayout.CENTER);

        // Final Assembly
        loginFrame.add(backgroundPanel, BorderLayout.CENTER);
        backgroundPanel.add(northPanel, BorderLayout.NORTH);
        backgroundPanel.add(infoPanel, BorderLayout.CENTER);
        backgroundPanel.add(southPanel, BorderLayout.SOUTH);

        // Event Handlers
        btnReturn.addActionListener(e -> loginFrame.dispose());
        btnAccess.addActionListener(e -> {
            String securityKey = new String(txtSecurityKey.getPassword()).trim();

            errorSecurityKey.setText("");
            txtSecurityKey.setForeground(Color.black);

            if (securityKey.isEmpty() || securityKey.equalsIgnoreCase("Security Key")) {
                errorSecurityKey.setText("Security Key is required.");
                txtSecurityKey.setForeground(Color.red);
                return;
            }

            // JDBC Verification
            String sql = "SELECT COUNT(*) FROM USER_ WHERE SecurityKey = ? AND Role = 'ADMIN' AND UserID = ?";

            try (Connection con = Database.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, securityKey);
                pst.setInt(2, currentUserID);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        executePasswordChange();
                        loginFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(loginFrame, "Invalid Security Key.", "Access Denied", JOptionPane.ERROR_MESSAGE);
                        errorSecurityKey.setText("Invalid Security Key");
                        txtSecurityKey.setForeground(Color.red);
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(loginFrame, "Database error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);

    }

    private void executePasswordChange() {
        JDialog passwordDialog = new JDialog((JFrame) null, "Update Password", true);
        passwordDialog.setSize(450, 300);
        passwordDialog.setResizable(false);
        passwordDialog.setLayout(new BorderLayout());
        passwordDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        passwordDialog.setIconImage(Components.l);

        // Background Panel
        JPanel passwordPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
        passwordPanel.setLayout(new BorderLayout());

        // Info Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPasswordField txtPassword = new RPasswordField(30);
        JPasswordField txtConfirmPassword = new RPasswordField(30);
        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(Components.p11);

        txtPassword.setFont(Components.p18);
        txtConfirmPassword.setFont(Components.p18);
        txtPassword.setPreferredSize(new Dimension(500, 40));
        txtConfirmPassword.setPreferredSize(new Dimension(500, 40));

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(10, 10, 5, 10);
        gbc2.anchor = GridBagConstraints.WEST;
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        // Password
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        inputPanel.add(new JLabel("Password"), gbc2);
        gbc2.gridx = 1;
        inputPanel.add(txtPassword, gbc2);

        // Confirm Password
        gbc2.gridx = 0;
        gbc2.gridy++;
        inputPanel.add(new JLabel("Confirm Password"), gbc2);
        gbc2.gridx = 1;
        inputPanel.add(txtConfirmPassword, gbc2);

        // Error Label
        gbc2.gridx = 1;
        gbc2.gridy++;
        inputPanel.add(errorLabel, gbc2);

        // Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        JButton btnCancel = new BtnRed("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
        JButton btnUpdate = new BtnGreen("Update", new ImageIcon(getClass().getResource("/images/icon/update.png")));
        bottomPanel.add(btnCancel);
        bottomPanel.add(btnUpdate);

        // Title Panel
        JPanel titlePanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/orange.jpg")).getImage());
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(Components.b);
        JLabel lblTitle = new JLabel("Update Admin Password", SwingConstants.CENTER);
        lblTitle.setFont(Components.b22);
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle, BorderLayout.CENTER);

        // Assemble
        passwordPanel.add(titlePanel, BorderLayout.NORTH);
        passwordPanel.add(inputPanel, BorderLayout.CENTER);
        passwordPanel.add(bottomPanel, BorderLayout.SOUTH);
        passwordDialog.add(passwordPanel);
        passwordDialog.setLocationRelativeTo(null);

        // Event Handling
        btnCancel.addActionListener(e -> passwordDialog.dispose());
        btnUpdate.addActionListener(e -> {
            String newPassword = new String(txtPassword.getPassword()).trim();
            String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();

            errorLabel.setText("");

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                errorLabel.setText("Both fields are required.");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                errorLabel.setText("Passwords do not match.");
                return;
            }

            // Update password in DB
            String updateSQL = "UPDATE USER_ SET Password = ? WHERE UserID = ?";

            try (Connection con = Database.getConnection(); PreparedStatement pst = con.prepareStatement(updateSQL)) {

                pst.setString(1, newPassword);
                pst.setInt(2, currentUserID);

                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(passwordDialog, "Password updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    passwordDialog.dispose();
                } else {
                    errorLabel.setText("Update failed. Please try again.");
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(passwordDialog, "Database error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        passwordDialog.setVisible(true);

    }

}
