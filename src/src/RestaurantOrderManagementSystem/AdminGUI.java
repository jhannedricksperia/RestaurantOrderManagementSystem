package RestaurantOrderManagementSystem;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AdminGUI extends ParentGUI {

    private final CardLayout centerCardLayout;
    private final JPanel centerCard;
    private DashboardGUI dashboardGUI;
    private int id;

    public AdminGUI(int id, String fName, String lName) {
        super(id, fName, lName);
        this.id = id;
        centerCardLayout = new CardLayout();
        centerCard = new JPanel(centerCardLayout);
        JPanel navigationButtons = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        dashboardGUI = new DashboardGUI();
        centerCard.add(summonDashBoard(), "Dashboard");
        eastPanel.add(centerCard, BorderLayout.CENTER);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10); // Top, Left, Bottom, Right padding
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.weighty = 0;

        // Create individual buttons
        JButton dashBoardButton = new BtnNav("Dashboard", new ImageIcon(getClass().getResource("/images/icon/dashboard.png")));
        dashBoardButton.addActionListener(e -> {
            cardTitle.setText("Dashboard");
            dashboardGUI.refresh();
            centerCard.add(dashboardGUI.getDashboardPanel(), "Dashboard");
            eastPanel.add(centerCard, BorderLayout.CENTER);
            centerCardLayout.show(centerCard, "Dashboard");
        });
        gbc.gridy = 0;
        navigationButtons.add(dashBoardButton, gbc);
        
        JButton accountDetailsButton = new BtnNav("Account Details", new ImageIcon(getClass().getResource("/images/icon/accountDetails.png")));
        accountDetailsButton.addActionListener(e -> {
            cardTitle.setText("Account Details");

            AccountDetailsGUI accountDetails = new AccountDetailsGUI(id);
            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);
            JPanel innerPanel = new JPanel(new BorderLayout());
            innerPanel.setOpaque(false);
            innerPanel.add(accountDetails.getAccountDetailsGUI(), BorderLayout.CENTER);
            JScrollPane sc = new CNavScrollPane(innerPanel);
            sc.setOpaque(false);
            sc.setBorder(null);
            sc.getViewport().setOpaque(false);
            panel.add(sc, BorderLayout.CENTER);
            centerCard.add(panel, "AccountDetails");
            centerCardLayout.show(centerCard, "AccountDetails");
            eastPanel.add(centerCard, BorderLayout.CENTER);
        });
        
        gbc.gridy++;
        navigationButtons.add(accountDetailsButton, gbc);
        
        JButton userButton = new BtnNav("User", new ImageIcon(getClass().getResource("/images/icon/user.png")));
        userButton.addActionListener(e -> {
            cardTitle.setText("User");
            UserGUI user = new UserGUI();
            centerCard.add(user.getUserPanel(), "User");
            centerCardLayout.show(centerCard, "User");
        });
        gbc.gridy++;
        navigationButtons.add(userButton, gbc);

        JButton customerButton = new BtnNav("Customer", new ImageIcon(getClass().getResource("/images/icon/customer.png")));
        customerButton.addActionListener(e -> {
            cardTitle.setText("Customer");

            CustomerGUI customer = new CustomerGUI();
            centerCard.add(customer.getCustomerPanel(), "Customer");
            centerCardLayout.show(centerCard, "Customer");
            eastPanel.add(centerCard, BorderLayout.CENTER);
        });
        gbc.gridy++;
        navigationButtons.add(customerButton, gbc);

        JButton orderItemButton = new BtnNav("Order Item", new ImageIcon(getClass().getResource("/images/icon/orderItem.png")));
        orderItemButton.addActionListener(e -> {
            cardTitle.setText("Order Item");

            OrderItemGUI orderItem = new OrderItemGUI();
            centerCard.add(orderItem.getOrderItemPanel(), "OrderItem");
            centerCardLayout.show(centerCard, "OrderItem");
            eastPanel.add(centerCard, BorderLayout.CENTER);
        });
        gbc.gridy++;
        navigationButtons.add(orderItemButton, gbc);

        JButton orderButton = new BtnNav("Order", new ImageIcon(getClass().getResource("/images/icon/order.png")));
        orderButton.addActionListener(e -> {
            cardTitle.setText("Order");

            OrderGUI order = new OrderGUI();
            centerCard.add(order.getOrderPanel(), "Order");
            centerCardLayout.show(centerCard, "Order");
            eastPanel.add(centerCard, BorderLayout.CENTER);
        });
        gbc.gridy++;
        navigationButtons.add(orderButton, gbc);

        JButton paymentButton = new BtnNav("Payment", new ImageIcon(getClass().getResource("/images/icon/payment.png")));
        paymentButton.addActionListener(e -> {
            cardTitle.setText("Payment");
            PaymentGUI payment = new PaymentGUI();
            centerCard.add(payment.getPaymentGUI(), "Payment");
            centerCardLayout.show(centerCard, "Payment");
            eastPanel.add(centerCard, BorderLayout.CENTER);
        });
        gbc.gridy++;
        navigationButtons.add(paymentButton, gbc);

        JButton cashPaymentButton = new BtnNav("Cash Payment", new ImageIcon(getClass().getResource("/images/icon/cashPayment.png")));
        cashPaymentButton.addActionListener(e -> {
            cardTitle.setText("Cash Payment");

            CashPaymentGUI cashPayment = new CashPaymentGUI();
            centerCard.add(cashPayment.getCashPaymentPanel(), "CashPayment");
            centerCardLayout.show(centerCard, "CashPayment");
            eastPanel.add(centerCard, BorderLayout.CENTER);
        });
        gbc.gridy++;
        navigationButtons.add(cashPaymentButton, gbc);

        JButton discountButton = new BtnNav("Discount", new ImageIcon(getClass().getResource("/images/icon/discount.png")));
        discountButton.addActionListener(e -> {
            cardTitle.setText("Discount");

            DiscountGUI discount = new DiscountGUI();
            centerCard.add(discount.getDiscountPanel(), "Discount");
            centerCardLayout.show(centerCard, "Discount");
            eastPanel.add(centerCard, BorderLayout.CENTER);
        });
        gbc.gridy++;
        navigationButtons.add(discountButton, gbc);

        JButton billButton = new BtnNav("Bill", new ImageIcon(getClass().getResource("/images/icon/bill.png")));
        billButton.addActionListener(e -> {
            cardTitle.setText("Bill");
            BillGUI bill = new BillGUI();
            centerCard.add(bill.getBillPanel(), "Bill");
            centerCardLayout.show(centerCard, "Bill");
            eastPanel.add(centerCard, BorderLayout.CENTER);
        });
        gbc.gridy++;
        navigationButtons.add(billButton, gbc);

        JButton billsWithDiscountButton = new BtnNav("Bills With Discount", new ImageIcon(getClass().getResource("/images/icon/billwithdiscount.png")));
        billsWithDiscountButton.addActionListener(e -> {
            cardTitle.setText("Bills with Discount");

            BillWithDiscountGUI bWDGUI = new BillWithDiscountGUI();
            centerCard.add(bWDGUI.getBillWithDiscountGUI(), "BillsWithDiscount");
            centerCardLayout.show(centerCard, "BillsWithDiscount");
            eastPanel.add(centerCard, BorderLayout.CENTER);
                });
        gbc.gridy++;
        navigationButtons.add(billsWithDiscountButton, gbc);

        JButton menuItemButton = new BtnNav("Menu Item", new ImageIcon(getClass().getResource("/images/icon/menuitem.png")));
        menuItemButton.addActionListener(e -> {
            cardTitle.setText("Menu Item");

            MenuItemGUI mItem = new MenuItemGUI();
            centerCard.add(mItem.getMenuItemPanel(), "MenuItem");
            centerCardLayout.show(centerCard, "MenuItem");
            eastPanel.add(centerCard, BorderLayout.CENTER);
        });
        gbc.gridy++;
        navigationButtons.add(menuItemButton, gbc);

        JButton menuItemCategoryButton = new BtnNav("Menu Item Category", new ImageIcon(getClass().getResource("/images/icon/menuitemcategory.png")));
        menuItemCategoryButton.addActionListener(e -> {
            cardTitle.setText("Menu Item Category");

            MenuItemCategoryGUI mItemCat = new MenuItemCategoryGUI();
            centerCard.add(mItemCat.getMenuItemCategoryPanel(), "MenuItemCategory");
            centerCardLayout.show(centerCard, "MenuItemCategory");
            eastPanel.add(centerCard, BorderLayout.CENTER);
        });
        gbc.gridy++;
        navigationButtons.add(menuItemCategoryButton, gbc);

        JButton reportsButton = new BtnNav("Reports+", new ImageIcon(getClass().getResource("/images/icon/report.png")));
        reportsButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "ReportsPanel");
            generalLabel.setText("        Reports");
        });
        gbc.gridy++;
        navigationButtons.add(reportsButton, gbc);
        
        JButton aboutUsButton = new BtnNav("About Us", new ImageIcon(getClass().getResource("/images/icon/aboutUs.png")));
        aboutUsButton.addActionListener(e -> {
            cardTitle.setText("About Us");

            AboutUsGUI aboutUsGUI = new AboutUsGUI();
            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);
            JPanel innerPanel = new JPanel(new BorderLayout());
            innerPanel.setOpaque(false);
            innerPanel.add(aboutUsGUI.getAboutUsGUI(), BorderLayout.CENTER);
            JScrollPane sc = new CNavScrollPane(innerPanel);
            sc.setOpaque(false);
            sc.setBorder(null);
            sc.getViewport().setOpaque(false);
            panel.add(sc, BorderLayout.CENTER);
            centerCard.add(panel, "AboutUs");
            centerCardLayout.show(centerCard, "AboutUs");
            eastPanel.add(centerCard, BorderLayout.CENTER);
        });
        gbc.gridy++;
        navigationButtons.add(aboutUsButton, gbc);

        gbc.gridy++;
        gbc.weighty = 1; // Make it absorb extra vertical space
        navigationButtons.add(Box.createVerticalGlue(), gbc);
        navigationButtons.setOpaque(false);

        // Same idea for the Report Panel
        JPanel reportPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcReport = new GridBagConstraints();
        gbcReport.gridx = 0;
        gbcReport.fill = GridBagConstraints.HORIZONTAL;
        gbcReport.insets = new Insets(5, 10, 5, 10);
        gbcReport.anchor = GridBagConstraints.NORTHWEST;
        gbcReport.weightx = 1;
        gbcReport.weighty = 0;

        
        gbcReport.gridy = 0;

        JButton salesReportButton = new BtnNav("Sales Report", new ImageIcon(getClass().getResource("/images/icon/salesReport.png")));
        salesReportButton.addActionListener(e -> {
            cardTitle.setText("Sales Report");
            SalesReportGUI sales = new SalesReportGUI();
            centerCard.add(sales.getSalesReportGUI(), "SalesReport");
            centerCardLayout.show(centerCard, "SalesReport");
            eastPanel.add(centerCard, BorderLayout.CENTER);
            
        });
        reportPanel.add(salesReportButton, gbcReport);

        JButton customerReportButton = new BtnNav("Cashier Report", new ImageIcon(getClass().getResource("/images/icon/cashierReport.png")));
        customerReportButton.addActionListener(e -> {
            cardTitle.setText("Customer Transaction Report");
            CashierReportGUI cashierReport = new CashierReportGUI();
            centerCard.add(cashierReport.getCashierReportGUI(), "CashierReport");
            centerCardLayout.show(centerCard, "CashierReport");
            eastPanel.add(centerCard, BorderLayout.CENTER);
        });
        gbcReport.gridy++;
        reportPanel.add(customerReportButton, gbcReport);

        JButton transactionReportButton = new BtnNav("Transaction Report", new ImageIcon(getClass().getResource("/images/icon/transactionReport.png")));
        transactionReportButton.addActionListener(e -> {
            cardTitle.setText("Transaction Report");
            TransactionReportGUI transactionReport = new TransactionReportGUI();
            centerCard.add(transactionReport.getTransactionReportGUI(), "TransactionReport");
            centerCardLayout.show(centerCard, "TransactionReport");
            eastPanel.add(centerCard, BorderLayout.CENTER);
        });
        gbcReport.gridy++;
        reportPanel.add(transactionReportButton, gbcReport);

        JButton returnButton = new BtnNav("Return", new ImageIcon(getClass().getResource("/images/icon/return1.png")));
        returnButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "NavigationPanel");
            generalLabel.setText("        General");
        });
        gbcReport.gridy++;
        reportPanel.add(returnButton, gbcReport);
        reportPanel.setOpaque(false);
        gbcReport.gridy++;
        gbcReport.weighty = 1; // Make it absorb extra vertical space
        reportPanel.add(Box.createVerticalGlue(), gbcReport);

        // Add both panels to the card layout panel
        cardPanel.add(navigationButtons, "NavigationPanel");
        cardPanel.add(reportPanel, "ReportsPanel");

        //Tip tools for each button
        dashBoardButton.setToolTipText("Go to Dashboard");
        userButton.setToolTipText("Manage Users");
        customerButton.setToolTipText("Manage Customers");
        orderItemButton.setToolTipText("Manage Order Items");
        orderButton.setToolTipText("Manage Orders");
        paymentButton.setToolTipText("Manage Payments");
        cashPaymentButton.setToolTipText("Handle Cash Payments");
        discountButton.setToolTipText("Manage Discounts");
        billButton.setToolTipText("View Bills");
        billsWithDiscountButton.setToolTipText("View Bills with Discounts");
        menuItemButton.setToolTipText("Manage Menu Items");
        menuItemCategoryButton.setToolTipText("Manage Menu Categories");
        reportsButton.setToolTipText("Open Reports Section");
        aboutUsButton.setToolTipText("Know More About the Developers");
        accountDetailsButton.setToolTipText("Update your Info");

        
        salesReportButton.setToolTipText("View Sales Report");
        customerReportButton.setToolTipText("View Cashier Transaction Report");
        transactionReportButton.setToolTipText("View Transaction Report");
        returnButton.setToolTipText("Return to Main Menu");

        //eastPanel.add(, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel summonDashBoard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(dashboardGUI.getDashboardPanel(), BorderLayout.CENTER);
        return panel;
    }

}
