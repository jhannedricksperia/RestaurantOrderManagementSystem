package RestaurantOrderManagementSystem;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Database {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=RestaurantOrderManagementSystem;encrypt=true;trustServerCertificate=true";
        String username = "admin";
        String password = "password";
        return DriverManager.getConnection(url, username, password);
    }

    // Checks if the contact number starts with 09
    public static boolean isValidContactNumber(String phone) {
        return phone.matches("^09\\d{9}$");
    }
    // Checks if the contact number is exactly 11 digits long

    public static boolean isValidContactNumberLength(String phone) {
        return phone != null && phone.length() == 11;
    }

    public static boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }

    public static boolean isUsernameValid(String username, String table) {
        String sql = "SELECT * FROM " + table + " WHERE username = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return !rs.next(); // true if username does not exist
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // In case of error, treat as invalid
        }
    }

    public static int getNextID(String table) {
        int nextID = -1;
        String sql = "SELECT IDENT_CURRENT(?) + IDENT_INCR(?) AS NextID";

        try (
                Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Set the table name as a parameter (this is typically not allowed for table names in SQL queries).
            // This still leaves the SQL Injection risk because we cannot parameterize table names in SQL queries directly.
            // A more robust solution would involve validating or sanitizing the table name thoroughly.
            stmt.setString(1, table);
            stmt.setString(2, table);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nextID = rs.getInt("NextID");

                    // Adjust if the table is empty
                    if (isTableEmpty(table)) {
                        nextID -= 1;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return nextID;
    }

    public static boolean isTableEmpty(String table) {
        // Basic table name validation (you can extend this to a more robust check)
        if (table == null || table.trim().isEmpty()) {
            return true; // Assume empty if table name is invalid
        }

        String sql = "SELECT COUNT(*) FROM " + table;  // No alias needed here

        try (
                Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) == 0;  // Access the first column directly (COUNT(*) result)
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return true; // Assume empty if error occurs
    }

    public static MenuItem getMenuItem(int menuItemID) {
        MenuItem menuItem = null;
        String query = "SELECT * FROM MENUITEM WHERE MenuItemID = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the menuItemID to the prepared statement
            stmt.setInt(1, menuItemID);

            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) {
                // Check if the result set contains any records
                if (rs.next()) {
                    // Map the result set to a MenuItem object
                    String menuItemName = rs.getString("MIName");
                    double unitPrice = rs.getDouble("UPrice");
                    int nServing = rs.getInt("NServing");
                    String status = rs.getString("MIStatus");
                    int menuItemCategoryID = rs.getInt("MICaTID");

                    // Create and return the MenuItem object
                    menuItem = new MenuItem(menuItemID, menuItemName, unitPrice, nServing, status, menuItemCategoryID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log the error for debugging purposes
        }

        return menuItem;  // Return null if no item was found
    }

    public static int getCategoryCount() {
        String query = "SELECT COUNT(*) FROM MENUITEMCATEGORY"; // Adjust table name as needed
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getInt(1); // Get the count of categories
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Or log this exception depending on your logging strategy
            return 0; // If error occurs, return 0
        }
        return 0; // If no categories, return 0
    }

    public static boolean isUsernameTaken(int userID, String username) {
        String query = "SELECT COUNT(*) FROM USER_ WHERE Uname = ? AND UserID != ?";
        try (
                Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setInt(2, userID); // Exclude current user ID
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error checking username uniqueness.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public static JLabel getProfile(int userID) {
        String sql = "SELECT ProfilePicture FROM USER_ WHERE UserID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                byte[] imgBytes = rs.getBytes("ProfilePicture");
                if (imgBytes != null) {
                    ImageIcon profilePicture = new ImageIcon(imgBytes);
                    Image image = profilePicture.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                    ImageIcon resizedIcon = new ImageIcon(image);
                    return new JLabel(resizedIcon);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error occurred with the database", JOptionPane.ERROR_MESSAGE);
        }

        return new JLabel("No Image Found");
    }

    public static void insertDefaultUsers() {
        String countQuery = "SELECT COUNT(*) FROM USER_ WHERE Role = 'ADMIN' ";
        String insertSQL = "INSERT INTO USER_ (FName, MName, LName, CNumber, Email, Uname, Password, Role, SecurityKey, UStatus, ProfilePicture) VALUES (?, NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        URL defaultImageLink = Database.class.getResource("/images/profilePicture/defaultProfile.png");

        Object[][] defaultUsers = {
            {"ADMIN", "DEFAULT", "NULL", "admin@patz.com", "admin", "password", "ADMIN", "123", "ACTIVE", defaultImageLink}
        };

        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(countQuery)) {

            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    for (Object[] user : defaultUsers) {
                        pstmt.setString(1, (String) user[0]); // FName
                        pstmt.setString(2, (String) user[1]); // LName
                        pstmt.setString(3, (String) user[2]); // CNumber
                        pstmt.setString(4, (String) user[3]); // Email
                        pstmt.setString(5, (String) user[4]); // Uname
                        pstmt.setString(6, (String) user[5]); // Password
                        pstmt.setString(7, (String) user[6]); // Role
                        pstmt.setString(8, (String) user[7]); // SecurityKey
                        pstmt.setString(9, (String) user[8]); // UStatus

                        try (InputStream fis1 = ((URL) user[9]).openStream()) {
                            pstmt.setBinaryStream(10, fis1, fis1.available());
                            pstmt.executeUpdate();
                        } catch (IOException ex) {
                            System.err.println("Error reading image: " + ex.getMessage());
                        }
                    }
                    // System.out.println("Default ADMIN and CASHIER inserted.");
                }
            } else {
                // System.out.println("USER_ table is not empty - no default users inserted.");
            }

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
    }

    public static boolean discountExists(String discountID) {
        String checkSQL = "SELECT * FROM DISCOUNT WHERE DiscountID = ?";
        Connection conn = null;

        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);  // Start manual transaction

            try (PreparedStatement stmt = conn.prepareStatement(checkSQL)) {
                stmt.setString(1, discountID.trim());

                try (ResultSet rs = stmt.executeQuery()) {
                    boolean exists = rs.next();
                    conn.commit();  // Commit if no issues
                    return exists;
                }
            }

        } catch (SQLException e) {
            // Rollback on error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();  // Log rollback error
                }
            }

            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error while checking discount: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;

        } finally {
            // Always reset auto-commit and close connection
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static int getExistingCustomerID(String fName, String mName, String lName, String cNumber) throws SQLException {
        String selectSql = "SELECT CustomerID FROM CUSTOMER WHERE FName = ? AND MName = ? AND LName = ? AND CNumber = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(selectSql)) {

            ps.setString(1, fName);
            ps.setString(2, mName);
            ps.setString(3, lName);
            ps.setString(4, cNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("CustomerID");
                }
            }
        }
        return -1; // Not found
    }

}
