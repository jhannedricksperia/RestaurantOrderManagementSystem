package RestaurantOrderManagementSystem;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class LoginGUI extends JFrame {

    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JLabel usernameError, passwordError, allError;
    private int passwordAttemptCount = 0;
    private CardLayout eastCardLayout;
    private JPanel eastCardPanel;
    private String sKey;

    public LoginGUI() {
        setTitle("Welcome to Pat'z Restaurant");
        setSize(800, 500);
        setIconImage(Components.l);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Inserts default uses to the system
        Database.insertDefaultUsers();
        JPanel loginBackground = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/loginBackground.png")).getImage());
        loginBackground.setLayout(new BorderLayout());
        JPanel westPanel = new JPanel(new BorderLayout());
        JPanel westInnerPanel = new JPanel(new GridBagLayout());
        westInnerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // 1. Hello! label
        JLabel helloLabel = new JLabel("Hello!");
        helloLabel.setFont(Components.b24);
        helloLabel.setForeground(Color.white);
        westInnerPanel.add(helloLabel, gbc);

        // 2. Dynamic Greeting label
        gbc.gridy++;
        JLabel goodLabel = new JLabel("Good");
        goodLabel.setFont(Components.ca50);
        goodLabel.setForeground(Color.white);
        westInnerPanel.add(goodLabel, gbc);
        // 2. Dynamic Greeting label
        gbc.gridy++;
        JLabel greetingLabel = new JLabel(getGreeting());
        greetingLabel.setFont(Components.ca50);
        greetingLabel.setForeground(Color.white);
        westInnerPanel.add(greetingLabel, gbc);

        westInnerPanel.setOpaque(false);
        JPanel dummyPanel = new JPanel();
        dummyPanel.setOpaque(false);
        dummyPanel.setPreferredSize(new Dimension(60, 0));
        westPanel.add(westInnerPanel, BorderLayout.CENTER);
        westPanel.add(dummyPanel, BorderLayout.WEST);
        eastCardLayout = new CardLayout();
        eastCardPanel = new JPanel(eastCardLayout);

        JPanel eastPanel = new JPanel(new GridBagLayout());
        eastPanel.setOpaque(false);
        westPanel.setOpaque(false);
        eastCardPanel.setOpaque(false);

        eastPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbcEast = new GridBagConstraints();
        gbcEast.insets = new Insets(5, 5, 5, 5); // spacing between components
        gbcEast.gridx = 0;
        gbcEast.fill = GridBagConstraints.HORIZONTAL;

        // 1. Log In title
        JLabel titleLabel = new JLabel("Log In", SwingConstants.CENTER);
        titleLabel.setFont(Components.b30);

        gbcEast.gridy = 0;
        gbcEast.gridwidth = 2; // center the title across two columns if needed
        gbcEast.anchor = GridBagConstraints.CENTER;
        eastPanel.add(titleLabel, gbcEast);

        gbcEast.gridy++;
        gbcEast.gridwidth = 1;
        eastPanel.add(new JLabel(""), gbcEast);
        gbcEast.gridy++;
        gbcEast.gridwidth = 1;
        eastPanel.add(new JLabel(""), gbcEast);

        // 3. Username text field
        gbcEast.gridy++;
        usernameTextField = new CTextField("Username");
        usernameTextField.setFont(Components.p16);
        eastPanel.add(usernameTextField, gbcEast);
        gbcEast.gridy++;
        gbcEast.gridwidth = 1;
        usernameError = new JLabel("");
        eastPanel.add(usernameError, gbcEast);
        usernameError.setFont(Components.p12);
        usernameError.setForeground(Components.ashRed);
        gbcEast.gridy++;
        gbcEast.gridwidth = 1;
        eastPanel.add(new JLabel(""), gbcEast);
        // 5. Password field
        gbcEast.gridy++;
        passwordField = new CPasswordField("Password");
        passwordField.setFont(Components.p16);
        eastPanel.add(passwordField, gbcEast);

        gbcEast.gridy++;
        gbcEast.gridwidth = 1;
        passwordError = new JLabel("");
        eastPanel.add(passwordError, gbcEast);
        passwordError.setFont(Components.p12);
        passwordError.setForeground(Components.ashRed);

        gbcEast.gridy++;
        gbcEast.gridwidth = 1;
        eastPanel.add(new JLabel(""), gbcEast);
        gbcEast.gridy++;
        gbcEast.gridwidth = 1;
        allError = new JLabel("");
        allError.setFont(Components.p12);
        allError.setForeground(Components.ashRed);
        eastPanel.add(allError, gbcEast);
        gbcEast.gridy++;
        gbcEast.gridwidth = 1;
        eastPanel.add(new JLabel(""), gbcEast);

        // 6. Login button
        gbcEast.gridy++;
        gbcEast.anchor = GridBagConstraints.CENTER;
        JButton loginBtn = new BtnDefault("Login");
        loginBtn.setPreferredSize(new Dimension(200, 40));
        eastPanel.add(loginBtn, gbcEast);

        gbcEast.gridy++;
        eastPanel.add(new JLabel(""), gbcEast);
        gbcEast.gridy++;
        eastPanel.add(new JLabel(""), gbcEast);
        gbcEast.gridy++;
        // Use HTML to underline the text
        JButton forgotPasswordBtn = new JButton("Forgot Password?");
        forgotPasswordBtn.setFont(Components.p12);
        forgotPasswordBtn.setForeground(Color.GRAY);
        forgotPasswordBtn.setHorizontalAlignment(SwingConstants.LEFT);
        forgotPasswordBtn.setPreferredSize(new Dimension(200, 40));

        // Make it look like a transparent hyperlink
        forgotPasswordBtn.setOpaque(false);
        forgotPasswordBtn.setContentAreaFilled(false);
        forgotPasswordBtn.setBorderPainted(false);
        forgotPasswordBtn.setFocusPainted(false);
        forgotPasswordBtn.setMargin(new Insets(2, 2, 2, 2));

        // Hover effect to change color
        forgotPasswordBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                forgotPasswordBtn.setForeground(Components.ashGreen); // your custom color
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                forgotPasswordBtn.setForeground(Color.GRAY);
            }
        });

        eastPanel.add(forgotPasswordBtn, gbcEast);

        loginBtn.addActionListener(e -> {
            authenticate();
        });

        forgotPasswordBtn.addActionListener(e -> {
            executeAccess();
        });

        usernameTextField.setPreferredSize(new Dimension(200, 35));
        passwordField.setPreferredSize(new Dimension(200, 35));

        //For the forgot password
        JPanel resetPasswordPanel = new JPanel(new GridBagLayout());
        resetPasswordPanel.setOpaque(false);

        GridBagConstraints gbcReset = new GridBagConstraints();
        gbcReset.insets = new Insets(5, 5, 5, 5);
        gbcReset.gridx = 0;
        gbcReset.fill = GridBagConstraints.HORIZONTAL;

        JLabel newPassLabel = new JLabel("New Password");
        newPassLabel.setFont(Components.p14);
        JPasswordField newPasswordField = new CPasswordField("");
        newPasswordField.setPreferredSize(new Dimension(200, 35));
        newPasswordField.setFont(Components.p16);
        
        gbcReset.gridy = 0;
        resetPasswordPanel.add(newPassLabel, gbcReset);
        gbcReset.gridy++;
        resetPasswordPanel.add(newPasswordField, gbcReset);
        gbcReset.gridy++;
        JLabel errorNewPass = new JLabel ("");
        errorNewPass.setFont(Components.p11);
        errorNewPass.setForeground(Color.red);
        resetPasswordPanel.add(errorNewPass, gbcReset);
        gbcReset.gridy++;
        resetPasswordPanel.add(new JLabel(""), gbcReset);
        gbcReset.gridy++;
        resetPasswordPanel.add(new JLabel(""), gbcReset);
        gbcReset.gridy++;
        
        JLabel confirmPassLabel = new JLabel("Confirm Password");
        confirmPassLabel.setFont(Components.p14);
        JPasswordField confirmPasswordField = new CPasswordField("");
        confirmPasswordField.setPreferredSize(new Dimension(200, 35));
        confirmPasswordField.setFont(Components.p16);
        resetPasswordPanel.add(confirmPassLabel, gbcReset);
        gbcReset.gridy++;
        resetPasswordPanel.add(confirmPasswordField, gbcReset);
        gbcReset.gridy++;
        JLabel errorConfirmPass = new JLabel ("");
        errorConfirmPass.setFont(Components.p11);
        errorConfirmPass.setForeground(Color.red);
        resetPasswordPanel.add(errorConfirmPass, gbcReset);

        // Back and Save Buttons
        gbcReset.gridy++;
        resetPasswordPanel.add(new JLabel(""), gbcReset);
        gbcReset.gridy++;
        resetPasswordPanel.add(new JLabel(""), gbcReset);
        gbcReset.gridy++;
        resetPasswordPanel.add(new JLabel(""), gbcReset);
        gbcReset.gridy++;
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JButton backBtn = new BtnRed("Back");
        JButton saveBtn = new BtnGreen("Save");

        // Back button logic
        backBtn.addActionListener(e -> {
            // Replace with actual logic to go back to the previous panel
            eastCardLayout.show(eastCardPanel, "loginPanel");
            sKey = "";
        });

        // Save button logic
        saveBtn.addActionListener(e -> {
            newPasswordField.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
            confirmPasswordField.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
            newPasswordField.setForeground(Color.BLACK);
            confirmPasswordField.setForeground(Color.BLACK);
            errorConfirmPass.setText("");
            errorNewPass.setText("");
            String newPass = String.valueOf(newPasswordField.getPassword()).trim();
            String confirmPass = String.valueOf(confirmPasswordField.getPassword()).trim();

            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                errorNewPass.setText("Please fill out all fields");
                errorConfirmPass.setText("Please fill out all fields");
                newPasswordField.setBorder(new MatteBorder(0, 0, 2, 0, Components.ashRed));
                confirmPasswordField.setBorder(new MatteBorder(0, 0, 2, 0, Components.ashRed));
                JOptionPane.showMessageDialog(null, "Please fill out all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!newPass.equals(confirmPass)) {
                errorNewPass.setText("Passwords do not match.");
                newPasswordField.setBorder(new MatteBorder(0, 0, 2, 0, Components.ashRed));
                confirmPasswordField.setBorder(new MatteBorder(0, 0, 2, 0, Components.ashRed));
                JOptionPane.showMessageDialog(null, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (newPass.length() < 8) {
                errorNewPass.setText("Password must be at least 8 characters");
                errorConfirmPass.setText("Password must be at least 8 characters");
                newPasswordField.setBorder(new MatteBorder(0, 0, 2, 0, Components.ashRed));
                confirmPasswordField.setBorder(new MatteBorder(0, 0, 2, 0, Components.ashRed));
                JOptionPane.showMessageDialog(null, "Password must be at least 8 characters", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }   
            if (!newPass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d])[a-zA-Z\\d[^\\s]]+$")) {
                errorNewPass.setText("Password must be alphanumeric with symbols");
                errorConfirmPass.setText("Password must be alphanumeric with symbols");
                newPasswordField.setBorder(new MatteBorder(0, 0, 2, 0, Components.ashRed));
                confirmPasswordField.setBorder(new MatteBorder(0, 0, 2, 0, Components.ashRed));
                JOptionPane.showMessageDialog(null, "Password must be alphanumeric with symbols", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // JDBC password update with try-with-resources
            try (Connection conn = Database.getConnection(); PreparedStatement pstmt = conn.prepareStatement("UPDATE USER_ SET Password = ?,UStatus = 'ACTIVE' WHERE SecurityKey = ?")) {

                pstmt.setString(1, newPass);
                pstmt.setString(2, sKey); // make sure 'skey' is accessible here

                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(null, "Password updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    sKey = "";
                    // Navigate back or close dialog
                    allError.setText("");
                    eastCardLayout.show(eastCardPanel, "loginPanel");
                } else {
                    JOptionPane.showMessageDialog(null, "Update failed. Key not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(backBtn);
        buttonPanel.add(saveBtn);
        resetPasswordPanel.add(buttonPanel, gbcReset);
        // Add reset panel to card layout

        loginBackground.add(eastCardPanel, BorderLayout.CENTER);
        eastCardPanel.add(eastPanel, "loginPanel");
        eastCardPanel.add(resetPasswordPanel, "resetPanel");
        westPanel.setPreferredSize(new Dimension(480, 0));
        loginBackground.add(westPanel, BorderLayout.WEST);
        add(loginBackground);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private static String getGreeting() {
        int hour = LocalTime.now().getHour();
        if (hour >= 5 && hour < 12) {
            return "Morning!";
        } else if (hour >= 12 && hour < 18) {
            return "Afternoon!";
        } else {
            return "Evening!";
        }
    }

    private void authenticate() {
        String username;
        String password;
        allError.setText("");
        passwordError.setText("");
        usernameError.setText("");
        username = usernameTextField.getText().trim();
        password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            clearErrors();
            allError.setText("Please fill all the text fields");
            usernameTextField.setBorder(new MatteBorder(0, 0, 2, 0, Components.ashRed));
            passwordField.setBorder(new MatteBorder(0, 0, 2, 0, Components.ashRed));
            return;
        }

        String sql = "SELECT UserID, FName, LName, Password, Role, UStatus FROM USER_ WHERE Uname = ?";

        try (
                Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                clearErrors();
                usernameError.setText("Username not found");
                usernameTextField.setBorder(new MatteBorder(0, 0, 2, 0, Components.ashRed));
            } else {
                String dbPassword = rs.getString("Password");
                String dbRole = rs.getString("Role");
                if (!dbPassword.equals(password)) {
                    passwordAttemptCount++;

                    if (passwordAttemptCount >= 3) {
                        // Update user status to BLOCKED
                        String updateSql = "UPDATE USER_ SET UStatus = 'BLOCKED' WHERE Uname = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setString(1, username);
                            updateStmt.executeUpdate();
                        }

                        clearErrors();
                        allError.setText("Account blocked due to 3 failed attempts.");
                        return;
                    }

                    clearErrors();
                    passwordError.setText("Invalid password. Attempts left: " + (3 - passwordAttemptCount));
                    passwordField.setBorder(new MatteBorder(0, 0, 2, 0, Components.ashRed));
                } else {
                    String status = rs.getString("UStatus");
                    if (!status.equalsIgnoreCase("ACTIVE")) {
                        clearErrors();
                        allError.setText("This account is " + status + " and cannot log in.");
                        return;
                    }

                    // Reset attempts on successful login
                    passwordAttemptCount = 0;

                    int userId = rs.getInt("UserID");
                    String fName = rs.getString("FName");
                    String lName = rs.getString("LName");
                    dispose();
                    if (dbRole.equalsIgnoreCase("ADMIN")) {
                        SwingUtilities.invokeLater(() -> new AdminGUI(userId, fName, lName));
                    } else if (dbRole.equalsIgnoreCase("CASHIER")) {
                        SwingUtilities.invokeLater(() -> new CashierGUI(userId, fName, lName));
                    }
                }

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error occured with the database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearErrors() {
        allError.setText("");
        usernameError.setText("");
        passwordError.setText("");
        passwordField.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
        usernameTextField.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
    }

    private void executeAccess() {
        JDialog loginFrame = new JDialog((JFrame) null, "Authentication", true);
        loginFrame.setIconImage(Components.l);
        loginFrame.setTitle("Authentication");
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
        JLabel lblSecurityKey = new JLabel("Enter Security Key");
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

        JLabel titleLabel = new JLabel("Authentication", SwingConstants.CENTER);
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
            String sql = "SELECT COUNT(*) FROM USER_ WHERE SecurityKey = ?";

            try (Connection con = Database.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, securityKey);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        sKey = securityKey;
                        eastCardLayout.show(eastCardPanel, "resetPanel");
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
}
