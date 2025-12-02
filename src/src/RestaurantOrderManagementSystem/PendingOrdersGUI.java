package RestaurantOrderManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PendingOrdersGUI extends Components {

    private JPanel cardPanel, mainPanel;
    private CardLayout cardLayout;
    private int currentPage = 0;
    private ArrayList<JPanel> pages = new ArrayList<>();

    public PendingOrdersGUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        cardPanel.setBorder(b);
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        loadOrders();
        setupNavigation();
    }

    public JPanel getPendingOrdersUI() {
        return mainPanel;
    }

    private void setupNavigation() {
        JPanel navPanel = new JPanel();
        navPanel.setOpaque(false);
        navPanel.setBorder(b);
        Dimension btnDimension = new Dimension(200, 40);
        JButton prev = new BtnDefault("Previous", new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/previous.png")).getImage()));
        JButton next = new BtnDefault("Next", new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/next.png")).getImage()));
        next.setHorizontalTextPosition(SwingConstants.LEFT);
        prev.setPreferredSize(btnDimension);
        next.setPreferredSize(btnDimension);

        prev.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                cardLayout.show(cardPanel, String.valueOf(currentPage));
                cardPanel.revalidate();
                cardPanel.repaint();
            }
        });

        next.addActionListener(e -> {
            if (currentPage < pages.size() - 1) {
                currentPage++;
                cardLayout.show(cardPanel, String.valueOf(currentPage));
                cardPanel.revalidate();
                cardPanel.repaint();
            }
        });
        navPanel.add(prev);
        navPanel.add(next);
        mainPanel.add(navPanel, BorderLayout.SOUTH);
    }

    private void loadOrders() {
        String query = """
            SELECT o.OrderID, c.FName, c.LName, oi.MenuItemID, m.MIName, cat.CatDes, oi.Quantity, oi.TotalPrice
            FROM ORDER_ o
            JOIN CUSTOMER c ON o.CustomerID = c.CustomerID
            JOIN ORDERITEM oi ON o.OrderID = oi.OrderID
            JOIN MENUITEM m ON oi.MenuItemID = m.MenuItemID
            JOIN MENUITEMCATEGORY cat ON m.MICatID = cat.MICatID
            WHERE o.OStatus = 'PENDING'
            ORDER BY o.OrderID
        """;

        try (
                Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            ArrayList<OrderItemRow> allItems = new ArrayList<>();

            while (rs.next()) {
                int orderId = rs.getInt("OrderID");
                String customerName = rs.getString("FName") + " " + rs.getString("LName");
                int menuItemId = rs.getInt("MenuItemID");
                String menuItemName = rs.getString("MIName");
                String category = rs.getString("CatDes");
                int quantity = rs.getInt("Quantity");
                double totalPrice = rs.getDouble("TotalPrice");

                allItems.add(new OrderItemRow(orderId, menuItemId, menuItemName, category, quantity, totalPrice, customerName));
            }

            ArrayList<Integer> processedOrderIds = new ArrayList<>();
            ArrayList<JPanel> currentBatch = new ArrayList<>();

            for (OrderItemRow item : allItems) {
                if (!processedOrderIds.contains(item.orderId)) {
                    DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Category", "Menu Item", "Qty"}, 0);

                    for (OrderItemRow row : allItems) {
                        if (row.orderId == item.orderId) {
                            tableModel.addRow(new Object[]{row.category, row.menuItemName, row.quantity});
                        }
                    }

                    JTable orderTable = Components.updateOrderTable(new JTable(tableModel));
                    orderTable.setBackground(Color.WHITE);
                    DefaultTableCellRenderer centerRenderer = new CustomRowRenderer();
                    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

                    // Apply the renderer to all columns in the table
                    for (int i = 0; i < orderTable.getColumnCount(); i++) {
                        orderTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    }
                    JScrollPane scrollPane = new CNavScrollPane(orderTable);

                    JLabel orderLabel = new JLabel("Order: " + item.orderId, SwingConstants.CENTER);
                    orderLabel.setFont(b16);
                    JLabel customerLabel = new JLabel("Customer: " + item.customerName);
                    customerLabel.setFont(p14);

                    JPanel labelsPanel = new JPanel(new GridLayout(2, 1));
                    labelsPanel.setOpaque(false);
                    labelsPanel.setBorder(b);
                    labelsPanel.add(orderLabel);
                    labelsPanel.add(customerLabel);

                    JButton doneBtn = new BtnGreen("Done", new ImageIcon(
                            new ImageIcon(getClass().getResource("/images/icon/done.png")).getImage())
                    );
                    JPanel buttonPanel = new JPanel(new BorderLayout());
                    buttonPanel.setOpaque(false);
                    buttonPanel.setBorder(b);
                    buttonPanel.add(doneBtn, BorderLayout.CENTER);

                    JPanel panel = new RPanel(30);
                    panel.setBorder(b);
                    panel.setBackground(Color.white);
                    panel.setLayout(new BorderLayout());
                    panel.add(labelsPanel, BorderLayout.NORTH);
                    panel.add(scrollPane, BorderLayout.CENTER);
                    panel.add(buttonPanel, BorderLayout.SOUTH);

                    int finalOrderId = item.orderId;
                    doneBtn.addActionListener(e -> {
                        try (
                                Connection updateConn = Database.getConnection(); PreparedStatement update = updateConn.prepareStatement(
                                "UPDATE ORDER_ SET OStatus = 'DONE' WHERE OrderID = ?")) {
                            update.setInt(1, finalOrderId);
                            update.executeUpdate();
                            refreshUI();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    });

                    currentBatch.add(panel);
                    processedOrderIds.add(item.orderId);

                    if (currentBatch.size() == 6) {
                        addPage(currentBatch);
                        currentBatch = new ArrayList<>();
                    }
                }
            }

            if (!currentBatch.isEmpty()) {
                addPage(currentBatch);
            }
            if (!pages.isEmpty()) {
                cardLayout.show(cardPanel, "0");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addPage(ArrayList<JPanel> panels) {
        JPanel page = new JPanel(new GridLayout(2, 3, 10, 10));
        page.setOpaque(false);

        for (JPanel panel : panels) {
            page.add(panel);
        }

        // Add empty transparent panels to fill remaining slots
        int remaining = 6 - panels.size();
        for (int i = 0; i < remaining; i++) {
            JPanel empty = new JPanel();
            empty.setOpaque(false); // Keeps it invisible but preserves layout space
            page.add(empty);
        }

        cardPanel.add(page, String.valueOf(pages.size()));
        pages.add(page);
    }

    private void refreshUI() {
        cardPanel.removeAll();
        pages.clear();
        currentPage = 0;
        loadOrders();
        cardPanel.revalidate();
        cardPanel.repaint();

    }

    public void reload() {
        refreshUI();
    }
}

class OrderItemRow {

    int orderId;
    int menuItemId;
    String menuItemName;
    String category;
    int quantity;
    double totalPrice;
    String customerName;

    public OrderItemRow(int orderId, int menuItemId, String menuItemName, String category,
            int quantity, double totalPrice, String customerName) {
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.category = category;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.customerName = customerName;
    }
}
