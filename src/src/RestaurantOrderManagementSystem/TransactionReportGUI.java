package RestaurantOrderManagementSystem;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class TransactionReportGUI extends Components {

    //declaring variables 
    private String[] columnNames = {
        "OrderID",
        "Menu Item Code",
        "Menu Name",
        "Menu Category",
        "Item Price",
        "Quantity",
        "Total Amount",
        "Order Status",
        "Discount Type",
        "Total Amount"};
    private DateSpinner dateFromSpinner;
    private DateSpinner dateToSpinner;
    private JLabel dateFromToLabel, totalAmntLbl, totalNumLbl, mostPopular, leastPopular, todayLabel, mostCatPopular, leastCatPopular;
    private DefaultTableModel model = new DisabledTableModel(columnNames, 0);
    private JTable table = Components.updateTable(new JTable(model));
    private double totalNumOfTransaction = 0.00;
    private double totalAmount = 0.00;
    private double totalQuantity = 0.00;
    private JPanel mainPanel;
    private Date today = new Date();
    private JScrollPane scrollPane = new CNavScrollPane(table);

    public TransactionReportGUI() {
        // Other panels this holds the date from and date to 
        JPanel northWestPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        northWestPanel.setOpaque(false);
        // Labels and Spinners
        JLabel dateFromLabel = new JLabel("     Date From:  ");
        dateFromLabel.setFont(Components.b14);
        dateFromLabel.setForeground(Color.WHITE);
        JLabel dateToLabel = new JLabel("     Date To:  ");
        dateToLabel.setForeground(Color.WHITE);
        dateToLabel.setFont(Components.b14);

        dateFromSpinner = new DateSpinner();
        dateToSpinner = new DateSpinner();

        // Retrieve Button
        JButton retrieveButton = new BtnBlue("Retrieve", new ImageIcon(getClass().getResource("/images/icon/retrieve.png")));
        retrieveButton.setFont(Components.b14);
        // Retrieve Button 
        retrieveButton.addActionListener(e -> {
            // try catch for date
            try {
                if (!(dateFromSpinner.getModel() instanceof SpinnerDateModel)
                        || !(dateToSpinner.getModel() instanceof SpinnerDateModel)) {
                    throw new IllegalArgumentException("Invalid spinner model type.");
                }

                Date fromDate = ((SpinnerDateModel) dateFromSpinner.getModel()).getDate();
                Date toDate = ((SpinnerDateModel) dateToSpinner.getModel()).getDate();
                Date today = new Date();

                if (fromDate == null || toDate == null) {
                    JOptionPane.showMessageDialog(
                            null,
                            "One or both date fields are empty or invalid.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if (fromDate.after(toDate)) {
                    JOptionPane.showMessageDialog(
                            null,
                            "'Date From' cannot be after 'Date To'.",
                            "Invalid Date Range",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if (fromDate.after(today) || toDate.after(today)) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Dates cannot be in the future.",
                            "Invalid Date",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                // Update labels
                dateFromToLabel.setText("Date From: " + formatDate(fromDate) + " to " + formatDate(toDate));
                retrieveTransactionReportData();
            } catch (Exception ex) {
                ex.printStackTrace(); // Optional: log the actual error for debugging
                JOptionPane.showMessageDialog(
                        null,
                        "An error occurred while retrieving dates. Please check your inputs.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        //Center panel holds the Title for reports, the table and labels below
        JPanel centerPanel = new JPanel(new BorderLayout());
        //header panel holds the upper function of the report, the date from and date to and the retrieve button
        JPanel headerPanel = new JPanel(new BorderLayout());
        // This panel holds the fromto date panel
        JPanel dateFromToPanel = new JPanel();
        //this setting is to make the panel clear
        dateFromToPanel.setOpaque(false);
        //labels for the panel and setting
        dateFromToLabel = new JLabel("Date From:");
        dateFromToLabel.setBackground(Color.white);
        dateFromToLabel.setFont(Components.b16);

        // This panel holds the date now panel   
        JPanel dateNowPanel = new JPanel();

        dateNowPanel.setOpaque(false);
        //labels for the panel and setting
        todayLabel = new JLabel("Date Today:");
        todayLabel.setFont(Components.b16);
        todayLabel.setText("Today's Date: " + formatDate(today));

        // This panel holds the table now panel        
        JPanel tablePanel = new JPanel(new BorderLayout());
        //this setting is to make the panel clear
        tablePanel.setOpaque(false);
        //Setting for the border of the panel
        tablePanel.setBorder(Components.b);

        // this panel holds the middle section of the report      
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // declaring the panel plus adding color for design of the panel
        JPanel northPanel = new RPanel(30);
        northPanel.setBackground(Color.GRAY);

        //this panel holds the function for upper side plus the buttons needed
        JPanel northEastPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        northEastPanel.setOpaque(false); // make transparent if needed

        // this is the settings for the button referesh
        JButton refreshBtn = new BtnCustom(new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/refreshWhite.png")).getImage()), new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/refreshGreen.png")).getImage()), "Refresh");
        refreshBtn.addActionListener(e -> {
            retrieveTransactionReportData();
        });

        // Panel that holds the settings for the title of report
        JPanel titlePanel = new JPanel(new GridLayout(3, 1));

        titlePanel.setBackground(Color.orange);

        //settings and labels for the title report
        titlePanel.setOpaque(false);
        JLabel rName = new JLabel("Pat'z Restaurant", SwingConstants.CENTER);
        rName.setFont(Components.p16);
        JLabel aName = new JLabel("Guinhawa, City of Malolos, Bulacan", SwingConstants.CENTER);
        aName.setFont(Components.p12);
        JLabel cName = new JLabel("Transactions Report", SwingConstants.CENTER);
        cName.setFont(Components.b24);

        tablePanel.setPreferredSize(new Dimension(900, 0));

        JPanel centerSouthPanel = new JPanel();

        centerSouthPanel.setBorder(Components.b);

        // 2 rows, 2 columns, with 10px horizontal and vertical gaps:
        centerSouthPanel.setLayout(new GridLayout(2, 2, 20, 20));

        // Labels and settings for centersouth panel
        totalAmntLbl = new JLabel("Total Amount:", SwingConstants.LEFT);
        totalAmntLbl.setFont(Components.b13);

        mostPopular = new JLabel("Most Buyed Item:", SwingConstants.LEFT);
        mostPopular.setFont(Components.b13);

        leastPopular = new JLabel("Least Buyed Item:", SwingConstants.LEFT);
        leastPopular.setFont(Components.b13);

        mostCatPopular = new JLabel("Most Buyed Category:", SwingConstants.LEFT);
        mostCatPopular.setFont(Components.b13);

        leastCatPopular = new JLabel("Least Buyed Category:", SwingConstants.LEFT);
        leastCatPopular.setFont(Components.b13);

        totalNumLbl = new JLabel("Total No. of Transactions:", SwingConstants.LEFT);
        totalNumLbl.setFont(Components.b13);

        // adding panels 
        northPanel.setLayout(new BorderLayout());
        northPanel.add(northWestPanel, BorderLayout.WEST);
        northWestPanel.add(dateFromLabel);
        JPanel dateFromPanel = new RPanel(20);
        //setting for spinner
        dateFromPanel.setBackground(Color.white);
        dateFromSpinner.setBorder(null);
        dateFromPanel.add(dateFromSpinner);
        northWestPanel.add(dateFromPanel);
        northWestPanel.add(dateToLabel);
        //setting for spinner   
        JPanel dateToPanel = new RPanel(20);
        dateToPanel.setBackground(Color.white);
        dateToSpinner.setBorder(null);
        dateToPanel.add(dateToSpinner);
        northWestPanel.add(dateToPanel);
        northWestPanel.add(retrieveButton);
        northWestPanel.setBorder(Components.b);

        // this adds the button inside the panel                                
        northPanel.add(northEastPanel, BorderLayout.EAST);
        northEastPanel.add(refreshBtn);
        northEastPanel.setBorder(Components.b);

        // adding components in the panel              
        headerPanel.add(dateFromToPanel, BorderLayout.WEST);
        headerPanel.setBorder(Components.b);
        dateFromToPanel.add(dateFromToLabel);
        // adding components in the panel         
        headerPanel.add(dateNowPanel, BorderLayout.EAST);
        dateNowPanel.add(todayLabel);

        // adding components in the panel                         
        headerPanel.add(titlePanel, BorderLayout.NORTH);
        titlePanel.add(rName, BorderLayout.CENTER);
        titlePanel.add(aName, BorderLayout.CENTER);
        titlePanel.add(cName, BorderLayout.CENTER);

        // adding components in the panel                   
        centerPanel.add(headerPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        tablePanel.add(scrollPane);
        tablePanel.setBorder(Components.b);
        // setting for scroll pane         
        JScrollPane scrollMain = new CNavScrollPane();
        // adding components in the panel  
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(scrollMain);

        // adding components in the panel                  
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(centerSouthPanel, BorderLayout.SOUTH);
        centerSouthPanel.add(totalAmntLbl);
        centerSouthPanel.add(totalNumLbl);
        centerSouthPanel.add(mostPopular);
        centerSouthPanel.add(leastPopular);
        centerSouthPanel.add(mostCatPopular);
        centerSouthPanel.add(leastCatPopular);
    }

    public JPanel getTransactionReportGUI() {
        return mainPanel;
    }

    // Constructor for formating the date
    private String formatDate(Date date) {
        return new SimpleDateFormat("MM/dd/yyyy").format(date);
    }

    private void retrieveTransactionReportData() {
        // 1) build the SQL with two placeholders for the dates:
        String sql = """
        SELECT
          oi.OrderID AS [OrderID],
          oi.MenuItemID AS [Menu Item ID],
          m.MIName AS [Menu Item Name],
          mic.CatDes AS [Menu Category Name],
          m.UPrice AS [Unit Price],
          oi.Quantity AS [Quantity],
          oi.TotalPrice AS [Total Price],
          o.OStatus AS [OStatus],
          COALESCE(d.DCategory, 'NONE') AS [Discount Type],
          b.TotalAmount AS [Total Amount]
        FROM ORDERITEM oi
        JOIN MENUITEM m ON oi.MenuItemID = m.MenuItemID
        JOIN MENUITEMCATEGORY mic ON m.MICatID    = mic.MICatID
        JOIN ORDER_ o ON oi.OrderID    = o.OrderID
        JOIN BILL b ON o.OrderID      = b.OrderID
        LEFT JOIN BILLWITHDISCOUNT bw ON b.BillID = bw.BillID
        LEFT JOIN DISCOUNT d  ON bw.DiscountID   = d.DiscountID
        WHERE
          b.BStatus = 'PAID'
          AND CAST(o.DateTime AS DATE) BETWEEN ? AND ?
        ORDER BY
          oi.OrderID,
          oi.MenuItemID
    """;

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 2) parse the spinner strings ("yyyy/MM/dd") into java.sql.Date:
            String fromStr = dateFromSpinner.getDate();
            String toStr = dateToSpinner.getDate();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
            java.sql.Date sqlFrom, sqlTo;

            try {
                Date fromUtil = fmt.parse(fromStr);
                Date toUtil = fmt.parse(toStr);
                sqlFrom = new java.sql.Date(fromUtil.getTime());
                sqlTo = new java.sql.Date(toUtil.getTime());
            } catch (ParseException pe) {
                pe.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Invalid date format: " + pe.getMessage(),
                        "Date Parse Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3) bind dates to the two placeholders:
            stmt.setDate(1, sqlFrom);
            stmt.setDate(2, sqlTo);

            // 4) execute and rebuild your table model:
            try (ResultSet rs = stmt.executeQuery()) {

                DefaultTableModel tm = (DefaultTableModel) table.getModel();
                tm.setRowCount(0); // clear existing rows

                // renderers for alignment
                CustomRowRenderer centerRenderer = new CustomRowRenderer();
                centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
                CustomRowRenderer rightRenderer = new CustomRowRenderer();
                rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

                TableColumnModel cm = table.getColumnModel();
                tm.setRowCount(0);
                // center small integer/text columns:
                cm.getColumn(0).setCellRenderer(centerRenderer); // OrderID
                cm.getColumn(1).setCellRenderer(centerRenderer); // Menu Item ID
                cm.getColumn(2).setCellRenderer(centerRenderer); // Menu Name
                cm.getColumn(3).setCellRenderer(centerRenderer); // Menu Cat
                cm.getColumn(5).setCellRenderer(centerRenderer); // Quantity
                cm.getColumn(7).setCellRenderer(centerRenderer); // OStatus
                cm.getColumn(8).setCellRenderer(centerRenderer); // DType

                // right-align prices and amounts:
                cm.getColumn(4).setCellRenderer(rightRenderer); // Unit Price
                cm.getColumn(6).setCellRenderer(rightRenderer); // Total Price
                cm.getColumn(9).setCellRenderer(rightRenderer); // Total Amount

                while (rs.next()) {
                    tm.addRow(new Object[]{
                        rs.getInt("OrderID"),
                        rs.getInt("Menu Item ID"),
                        rs.getString("Menu Item Name"),
                        rs.getString("Menu Category Name"),
                        rs.getDouble("Unit Price"),
                        rs.getInt("Quantity"),
                        rs.getDouble("Total Price"),
                        rs.getString("OStatus"),
                        rs.getString("Discount Type"),
                        rs.getDouble("Total Amount")
                    });
                }
                fetchSouthSummaryData(sqlFrom, sqlTo);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error loading report: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private double fetchTotalAmount(java.sql.Date sqlFrom, java.sql.Date sqlTo) throws SQLException {
        String sql = """
        SELECT SUM(TotalAmount)
        FROM BILL
        WHERE BStatus = 'PAID'
          AND CAST(DateTime AS DATE) BETWEEN ? AND ?
    """;
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, sqlFrom);
            stmt.setDate(2, sqlTo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0.0;
            }
        }
    }

    private int fetchTotalTransaction(java.sql.Date sqlFrom, java.sql.Date sqlTo) throws SQLException {
        String sql = """
            SELECT COUNT(DISTINCT OrderID)
            FROM BILL
            WHERE BStatus = 'PAID'
              AND CAST(DateTime AS DATE) BETWEEN ? AND ?
        """;

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, sqlFrom);
            stmt.setDate(2, sqlTo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private String fetchExtremeItem(java.sql.Date sqlFrom, java.sql.Date sqlTo, boolean most) throws SQLException {
        String order;
        if (most) {
            order = "DESC";
        } else {
            order = "ASC";
        }

        String sql = String.format("""
            SELECT TOP 1 m.MIName
            FROM MENUITEM m
            WHERE m.MenuItemID IN (
                SELECT oi.MenuItemID
                FROM ORDERITEM oi
                WHERE oi.OrderID IN (
                    SELECT OrderID FROM BILL
                    WHERE BStatus = 'PAID'
                      AND CAST(DateTime AS DATE) BETWEEN ? AND ?
                )
            )
            GROUP BY m.MIName, m.MenuItemID
            ORDER BY (
                SELECT SUM(oi.Quantity)
                FROM ORDERITEM oi
                WHERE oi.MenuItemID = m.MenuItemID
            ) %s
        """, order);

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, sqlFrom);
            stmt.setDate(2, sqlTo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getString(1) : "N/A";
            }
        }
    }

    private String fetchExtremeCategory(java.sql.Date sqlFrom, java.sql.Date sqlTo, boolean most) throws SQLException {
        String order;
        if (most) {
            order = "DESC";
        } else {
            order = "ASC";
        }

        String sql = String.format("""
            SELECT TOP 1 mic.CatDes
            FROM MENUITEMCATEGORY mic
            WHERE mic.MICatID IN (
                SELECT m.MICatID
                FROM MENUITEM m
                WHERE m.MenuItemID IN (
                    SELECT oi.MenuItemID
                    FROM ORDERITEM oi
                    WHERE oi.OrderID IN (
                        SELECT OrderID FROM BILL
                        WHERE BStatus = 'PAID'
                          AND CAST(DateTime AS DATE) BETWEEN ? AND ?
                    )
                )
            )
            GROUP BY mic.CatDes, mic.MICatID
            ORDER BY (
                SELECT SUM(oi.Quantity)
                FROM ORDERITEM oi
                JOIN MENUITEM m ON oi.MenuItemID = m.MenuItemID
                WHERE m.MICatID = mic.MICatID
            ) %s
        """, order);

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, sqlFrom);
            stmt.setDate(2, sqlTo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getString(1) : "N/A";
            }
        }
    }

    private void fetchSouthSummaryData(java.sql.Date sqlFrom, java.sql.Date sqlTo) throws SQLException {
        totalAmntLbl.setText("Total Amount: ₱" + String.format("%.2f", fetchTotalAmount(sqlFrom, sqlTo)));
        totalNumLbl.setText("Total No. of Transaction " + fetchTotalTransaction(sqlFrom, sqlTo));

        mostPopular.setText("Most Buyed Item: " + fetchExtremeItem(sqlFrom, sqlTo, true));
        leastPopular.setText("Least Buyed Item: " + fetchExtremeItem(sqlFrom, sqlTo, false));

        mostCatPopular.setText("Most Buyed Category: " + fetchExtremeCategory(sqlFrom, sqlTo, true));
        leastCatPopular.setText("Least Buyed Category: " + fetchExtremeCategory(sqlFrom, sqlTo, false));
    }

// end of settings/code for report
}
