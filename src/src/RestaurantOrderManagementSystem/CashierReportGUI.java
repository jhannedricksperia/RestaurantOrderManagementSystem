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

public class CashierReportGUI extends Components {

    //declaring variables 
    //Table name
    private String[] columnNames = {
        "User ID",
        "First Name",
        "Last Name",
        "No. of Successful Orders",
        "No. of Cancelled Orders",
        "Revenue"
    };
    private DateSpinner dateFromSpinner;
    private DateSpinner dateToSpinner;
    private JLabel dateFromToLabel, totalNumCLbl, totalNumLbl, totalAmntLbl, totalNumCanLbl, mostPerformed, leastPerformed, todayLabel;
    private DefaultTableModel model = new DisabledTableModel(columnNames, 0);
    private JTable table = Components.updateTable(new JTable(model));
    private double numberOfCashier = 0.00;
    private double totalNumOfTransaction = 0.00;
    private double totalAmount = 0.00;
    private double totalQuantity = 0.00;
    private JPanel mainPanel;
    private JScrollPane scrollPane = new CNavScrollPane(table);

    private Date today = new Date();

    // additional settings table and adding a scroll pane to the panel
    public CashierReportGUI() {
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
                retrieveCashierReportData();
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
            retrieveCashierReportData();
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
        JLabel cName = new JLabel("Cashiers Report", SwingConstants.CENTER);
        cName.setFont(Components.b24);

        tablePanel.setPreferredSize(new Dimension(900, 0));

        JPanel centerSouthPanel = new JPanel();

        centerSouthPanel.setBorder(Components.b);

        // 2 rows, 2 columns, with 10px horizontal and vertical gaps:
        centerSouthPanel.setLayout(new GridLayout(2, 3, 20, 20));

        // Labels and settings for centersouth panel
        totalNumCLbl = new JLabel("Total Number of Cashier:", SwingConstants.LEFT);
        totalNumCLbl.setFont(Components.b13);

        totalNumLbl = new JLabel("Total Number of Successful Orders:", SwingConstants.LEFT);
        totalNumLbl.setFont(Components.b13);

        totalAmntLbl = new JLabel("Total Sales:", SwingConstants.LEFT);
        totalAmntLbl.setFont(Components.b13);

        totalNumCanLbl = new JLabel("Total Number of Cancelled Orders:", SwingConstants.LEFT);
        totalNumCanLbl.setFont(Components.b13);

        mostPerformed = new JLabel("Most Highly Performed Cashier:", SwingConstants.LEFT);
        mostPerformed.setFont(Components.b13);

        leastPerformed = new JLabel("Least Performed Cashier:", SwingConstants.LEFT);
        leastPerformed.setFont(Components.b13);

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
        centerSouthPanel.add(totalNumCLbl);
        centerSouthPanel.add(totalNumLbl);
        centerSouthPanel.add(totalNumCanLbl);
        centerSouthPanel.add(totalAmntLbl);
        centerSouthPanel.add(mostPerformed);
        centerSouthPanel.add(leastPerformed);

    }

    public JPanel getCashierReportGUI() {
        return mainPanel;
    }

    // Constructor for formating the date
    private String formatDate(Date date) {
        return new SimpleDateFormat("MM/dd/yyyy").format(date);
    }

    private void retrieveCashierReportData() {
        String sql = """
        SELECT
          u.UserID AS [UserID],
          u.FName AS [FName],
          u.LName AS [LName],
          (
            SELECT COUNT(*)
            FROM ORDER_ o2
            JOIN BILL    b2 ON o2.OrderID = b2.OrderID
            WHERE o2.UserID   = u.UserID
              AND o2.OStatus  = 'DONE'
              AND CAST(b2.DateTime AS DATE) BETWEEN ? AND ?
          )                                                   AS [DoneOrders],
          (
            SELECT COUNT(*)
            FROM ORDER_ o3
            JOIN BILL    b3 ON o3.OrderID = b3.OrderID
            WHERE o3.UserID   = u.UserID
              AND o3.OStatus  = 'CANCELLED'
              AND CAST(b3.DateTime AS DATE) BETWEEN ? AND ?
          )                                                   AS [CancelledOrders],
          (
            SELECT SUM(b4.TotalAmount)
            FROM ORDER_ o4
            JOIN BILL    b4 ON o4.OrderID = b4.OrderID
            WHERE o4.UserID    = u.UserID
              AND b4.BStatus   = 'PAID'
              AND CAST(b4.DateTime AS DATE) BETWEEN ? AND ?
          )                                                   AS [TotalRevenue]
        FROM USER_ u
        WHERE u.Role = 'CASHIER'
        ORDER BY [DoneOrders] DESC
        """;

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // parse the spinner strings ("yyyy/MM/dd") into java.sql.Date:
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
            // subquery 1
            stmt.setDate(1, sqlFrom);
            stmt.setDate(2, sqlTo);
            // subquery 2
            stmt.setDate(3, sqlFrom);
            stmt.setDate(4, sqlTo);
            // subquery 3
            stmt.setDate(5, sqlFrom);
            stmt.setDate(6, sqlTo);

            try (ResultSet rs = stmt.executeQuery()) {
                DisabledTableModel tm = (DisabledTableModel) table.getModel();
                tm.setRowCount(0);
                // alignment: IDs & names centered, numbers right‐aligned
                CustomRowRenderer center = new CustomRowRenderer();
                center.setHorizontalAlignment(SwingConstants.CENTER);
                CustomRowRenderer right = new CustomRowRenderer();
                right.setHorizontalAlignment(SwingConstants.RIGHT);

                TableColumnModel cm = table.getColumnModel();
                cm.getColumn(0).setCellRenderer(center);
                cm.getColumn(1).setCellRenderer(center);
                cm.getColumn(2).setCellRenderer(center);
                cm.getColumn(3).setCellRenderer(center);
                cm.getColumn(4).setCellRenderer(center);
                cm.getColumn(5).setCellRenderer(right);

                while (rs.next()) {
                    tm.addRow(new Object[]{
                        rs.getInt("UserID"),
                        rs.getString("FName"),
                        rs.getString("LName"),
                        rs.getInt("DoneOrders"),
                        rs.getInt("CancelledOrders"),
                        rs.getDouble("TotalRevenue")
                    });
                }
                fetchSouthSummaryData(sqlFrom, sqlTo);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error loading cashier report: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private int fetchTotalCashiers() throws SQLException {
        String sql = "SELECT COUNT(*) FROM USER_ WHERE Role = 'CASHIER'";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private int fetchTotalCancelledOrders(java.sql.Date sqlFrom, java.sql.Date sqlTo) throws SQLException {
        String sql = """
        SELECT COUNT(*)
        FROM ORDER_ o
        JOIN BILL b ON o.OrderID = b.OrderID
        WHERE o.OStatus = 'CANCELLED'
          AND CAST(b.DateTime AS DATE) BETWEEN ? AND ?
    """;
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, sqlFrom);
            stmt.setDate(2, sqlTo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private int fetchTotalDoneOrders(java.sql.Date sqlFrom, java.sql.Date sqlTo) throws SQLException {
        String sql = """
        SELECT COUNT(*)
        FROM ORDER_ o
        JOIN BILL b ON o.OrderID = b.OrderID
        WHERE o.OStatus = 'DONE'
          AND CAST(b.DateTime AS DATE) BETWEEN ? AND ?
    """;
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, sqlFrom);
            stmt.setDate(2, sqlTo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private double fetchTotalSales(java.sql.Date sqlFrom, java.sql.Date sqlTo) throws SQLException {
        String sql = """
        SELECT SUM(b.TotalAmount)
        FROM ORDER_ o
        JOIN BILL b ON o.OrderID = b.OrderID
        WHERE b.BStatus = 'PAID'
          AND CAST(b.DateTime AS DATE) BETWEEN ? AND ?
    """;
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, sqlFrom);
            stmt.setDate(2, sqlTo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0.0;
            }
        }
    }

    private String fetchExtremeCashier(java.sql.Date sqlFrom, java.sql.Date sqlTo, boolean most) throws SQLException {
        String order;
        if (most) {
            order = "DESC";
        } else {
            order = "ASC";
        }

        String sql = String.format("""
        SELECT TOP 1 u.FName + ' ' + u.LName
        FROM USER_ u
        WHERE u.Role = 'CASHIER'
        ORDER BY (
            SELECT COUNT(*)
            FROM ORDER_ o
            JOIN BILL b ON o.OrderID = b.OrderID
            WHERE o.UserID = u.UserID
              AND o.OStatus = 'DONE'
              AND CAST(b.DateTime AS DATE) BETWEEN ? AND ?
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
        // Assume sqlFrom and sqlTo are java.sql.Date types
        totalNumCLbl.setText("Total Number of Cashier: " + fetchTotalCashiers());
        totalNumLbl.setText("Total Number of Successful Orders: " + fetchTotalDoneOrders(sqlFrom, sqlTo));
        totalNumCanLbl.setText("Total Number of Cancelled Orders: " + fetchTotalCancelledOrders(sqlFrom, sqlTo));
        totalAmntLbl.setText(String.format("Total Sales: ₱%.2f", fetchTotalSales(sqlFrom, sqlTo)));
        mostPerformed.setText("Most Highly Performed Cashier: " + fetchExtremeCashier(sqlFrom, sqlTo, true));
        leastPerformed.setText("Least Performed Cashier: " + fetchExtremeCashier(sqlFrom, sqlTo, false));

    }

}
