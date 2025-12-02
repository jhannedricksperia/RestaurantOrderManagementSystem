package RestaurantOrderManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class DashboardGUI extends Components {

    private JPanel mainPanel;
    private BackgroundPanel backgroundPanel;
    private JPanel statsPanel;

    // Labels to allow refreshing
    private JLabel ordersCountLabel;
    private JLabel menuItemsSoldLabel;
    private JLabel cashiersCountLabel;
    private JLabel totalRevenueLabel;

    public DashboardGUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10,0,0,0));
        // Center panel - custom background panel
        backgroundPanel = new BackgroundPanel(
                new ImageIcon(getClass().getResource("/images/dashboardBackground/dashboard.png")).getImage()
        );
        backgroundPanel.setLayout(new BorderLayout());

        // East side - stats
        statsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        statsPanel.setPreferredSize(new Dimension(300, 0));
        statsPanel.setOpaque(false);

        // Create and add stat panels with labels
        ordersCountLabel = new JLabel(getOrdersCount(), SwingConstants.CENTER);
        statsPanel.add(createStatPanel("Orders Done", ordersCountLabel));

        menuItemsSoldLabel = new JLabel(getMenuItemsSold(), SwingConstants.CENTER);
        statsPanel.add(createStatPanel("Menu Items Sold", menuItemsSoldLabel));

        cashiersCountLabel = new JLabel(getEmployeeCount(), SwingConstants.CENTER);
        statsPanel.add(createStatPanel("Cashiers", cashiersCountLabel));

        totalRevenueLabel = new JLabel(getTotalRevenue(), SwingConstants.CENTER);
        statsPanel.add(createStatPanel("Total Sales", totalRevenueLabel));
        JPanel opaquePanel = new JPanel(new BorderLayout());
        opaquePanel.setOpaque(false);
        opaquePanel.add(statsPanel, BorderLayout.EAST);
        opaquePanel.setBorder(b);
        statsPanel.setPreferredSize(new Dimension(300, 0));
        backgroundPanel.add(opaquePanel, BorderLayout.CENTER);
        mainPanel.add(backgroundPanel, BorderLayout.CENTER);
    }

    public JPanel getDashboardPanel() {
        return mainPanel;
    }

    //Call this method to refresh all stats from the database.

    public void refresh() {
        ordersCountLabel.setText(getOrdersCount());
        menuItemsSoldLabel.setText(getMenuItemsSold());
        cashiersCountLabel.setText(getEmployeeCount());
        totalRevenueLabel.setText(getTotalRevenue());
        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private JPanel createStatPanel(String titleS, JLabel valueLabel) {
        JPanel panel = new RPanel(30);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.white);
        JLabel title = new JLabel(titleS);
        title.setFont(b18);
        valueLabel.setFont(b35);
        panel.add(valueLabel, BorderLayout.CENTER);
        panel.add(title, BorderLayout.NORTH);

        return panel;
    }

    // --- Database Methods ---
    private String getOrdersCount() {
        String sql = "SELECT COUNT(*) FROM ORDER_ WHERE OStatus = 'DONE'";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return String.valueOf(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0";
    }

    private String getMenuItemsSold() {
        String sql = "SELECT SUM(Quantity) FROM ORDERITEM";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return String.valueOf(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0";
    }

    private String getEmployeeCount() {
        String sql = "SELECT COUNT(*) FROM USER_ WHERE Role = 'Cashier'";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return String.valueOf(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0";
    }

    private String getTotalRevenue() {
        String sql = "SELECT SUM(TotalAmount) FROM BILL";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                double value = rs.getDouble(1);
                return "₱ " + String.format("%,.2f", value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "₱ 0.00";
    }
}
