package RestaurantOrderManagementSystem;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;

public class CashierGUI extends ParentGUI {

    private JPanel menuCategoryPanel, menuCenterPanel, listPanel;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JTextField txtOrderNum, txtCustomer, txtBillID, txtVATAmount, txtCashier, txtSubTotal, txtDiscount, txtTotalAmount, txtVATSales;
    private JRadioButton rdbDineIn, rdbTakeOut;
    private ButtonGroup orderTypeGroup;
    private JButton changeQty, delete;
    private String fName, lName;
    private int id, nextOrderID, customerID;
    private BtnMenuCategory selectedCategoryBtn = null;
    private boolean isOrderSaved, isPaymentDone, isDiscountEnabled, isAuthorized;
    private double DISCOUNTRATE = 0.20;
    private PendingOrdersGUI pendingOrdersGUI;
    private AccountDetailsGUI accountDetailsGUI;
    private DashboardGUI dashboardGUI;
    private AboutUsGUI aboutUsGUI;
    private int paymentIDForReceipt;

    public CashierGUI(int id, String fName, String lName) {
        super(id, fName, lName);
        this.fName = fName;
        this.lName = lName;
        this.id = id;
        pendingOrdersGUI = new PendingOrdersGUI();
        accountDetailsGUI = new AccountDetailsGUI(id);
        dashboardGUI = new DashboardGUI();
        aboutUsGUI = new AboutUsGUI();
        JPanel navigationButtons = new JPanel(new GridBagLayout());
        navigationPanel.setPreferredSize(new Dimension(300, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0); // Top, Left, Bottom, Right padding
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.weighty = 0;

        // Create individual buttons
        JButton dashBoardButton = new BtnNav("Dashboard", new ImageIcon(getClass().getResource("/images/icon/dashboard.png")));
        gbc.gridy = 0;
        navigationButtons.add(dashBoardButton, gbc);

        JButton accountButton = new BtnNav("Account Details", new ImageIcon(getClass().getResource("/images/icon/accountDetails.png")));
        gbc.gridy++;
        navigationButtons.add(accountButton, gbc);

        JButton orderButton = new BtnNav("Order", new ImageIcon(getClass().getResource("/images/icon/order.png")));
        gbc.gridy++;
        navigationButtons.add(orderButton, gbc);

        JButton orderPendingButton = new BtnNav("Pending Orders", new ImageIcon(getClass().getResource("/images/icon/pending.png")));
        gbc.gridy++;
        navigationButtons.add(orderPendingButton, gbc);

        JButton aboutUsButton = new BtnNav("About Us", new ImageIcon(getClass().getResource("/images/icon/aboutUs.png")));
        gbc.gridy++;
        navigationButtons.add(aboutUsButton, gbc);

        gbc.gridy++;
        gbc.weighty = 1; // Make it absorb extra vertical space
        navigationButtons.add(Box.createVerticalGlue(), gbc);
        navigationButtons.setOpaque(false);

        cardPanel.add(navigationButtons, "NavigationPanel");

        dashBoardButton.setToolTipText("Go to Dashboard");
        accountButton.setToolTipText("Manage your profile");
        orderButton.setToolTipText("Manage Orders");
        orderPendingButton.setToolTipText("View Pending Orders");
        aboutUsButton.setToolTipText("Know more about the developers");

        //Main Center Panel
        CardLayout centerCard = new CardLayout();
        JPanel centerPanel = new JPanel(centerCard);
        centerPanel.setOpaque(false);

        // Create individual content panels
        JPanel dashboardPanel = createDashboardPanel();
        JPanel accountPanel = createAccountPanel();
        JPanel orderPanel = createOrderPanel();
        JPanel pendingOrderPanel = createPendingOrderPanel();
        JPanel aboutUsPanel = createAboutUsPanel();

        // Add them to centerPanel with card names
        centerPanel.add(dashboardPanel, "Dashboard");
        centerPanel.add(accountPanel, "Account Details");
        centerPanel.add(orderPanel, "Order");
        centerPanel.add(pendingOrderPanel, "Pending Orders");
        centerPanel.add(aboutUsPanel, "About Us");

        dashBoardButton.addActionListener(e -> {
            cardTitle.setText("Dashboard");
            dashboardGUI.refresh();
            centerCard.show(centerPanel, "Dashboard");
        });

        accountButton.addActionListener(e -> {
            cardTitle.setText("Account Details");
            accountDetailsGUI.reload();
            centerCard.show(centerPanel, "Account Details");
        });

        orderButton.addActionListener(e -> {
            cardTitle.setText("Order");
            centerCard.show(centerPanel, "Order");
        });

        orderPendingButton.addActionListener(e -> {
            cardTitle.setText("Pending Orders");
            pendingOrdersGUI.reload();
            centerCard.show(centerPanel, "Pending Orders");
        });

        aboutUsButton.addActionListener(e -> {
            cardTitle.setText("About Us");
            centerCard.show(centerPanel, "About Us");
        });

        eastPanel.add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.add(dashboardGUI.getDashboardPanel());
        return panel;
    }

    private JPanel createAccountPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.setOpaque(false);
        innerPanel.add(accountDetailsGUI.getAccountDetailsGUI(), BorderLayout.CENTER);
        JScrollPane sc = new CNavScrollPane(innerPanel);
        sc.setOpaque(false);
        sc.setBorder(null);
        sc.getViewport().setOpaque(false);
        panel.add(sc, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createOrderPanel() {
        isOrderSaved = false;
        isDiscountEnabled = false;
        isPaymentDone = false;
        customerID = -1;
        JPanel panel = new JPanel(new BorderLayout());
        JPanel outerListPanel = new JPanel(new BorderLayout());
        outerListPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        outerListPanel.setOpaque(false);
        panel.setOpaque(false);
        listPanel = new RPanel(30);
        listPanel.setLayout(new BorderLayout());
        listPanel.setBackground(Color.white);
        listPanel.setPreferredSize(new Dimension(400, 0));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        // Order Table Setup
        tableModel = new DisabledTableModel(new String[]{"ItemCode", "Item", "Price", "Qty", "Total Price"}, 0);
        orderTable = Components.updateOrderTable(new JTable(tableModel));

        orderTable.setBorder(new EmptyBorder(0, 10, 10, 10));
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(100);

        // Column 2 (index 1) and Column 3 (index 2) → center alignment
        DefaultTableCellRenderer centerRenderer = new CustomRowRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        orderTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        orderTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // Column 4 (index 3) → right alignment
        DefaultTableCellRenderer rightRenderer = new CustomRowRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        orderTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        JScrollPane tableScrollPane = new CNavScrollPane(orderTable);
        if (orderTable.getRowCount() == 0) {
            orderTable.setOpaque(true);
            orderTable.setBackground(Color.WHITE);
            tableScrollPane.getViewport().setBackground(Color.WHITE);
        } else {
            orderTable.setOpaque(false);
            tableScrollPane.getViewport().setOpaque(false);
        }
        JPanel tableScrollPanePanel = new JPanel(new BorderLayout());
        tableScrollPanePanel.setOpaque(false);
        tableScrollPanePanel.add(tableScrollPane);
        tableScrollPanePanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        // Hide the "ItemCode" column (index 0)
        TableColumn hiddenColumn = orderTable.getColumnModel().getColumn(0);
        orderTable.removeColumn(hiddenColumn);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 0, 10), BorderFactory.createMatteBorder(0, 0, 1, 0, Components.lGray)));
        infoPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 1, 2, 1); // Top, Left, Bottom, Right padding
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblTitle = new JLabel("Order Details");
        lblTitle.setFont(Components.b16);
        infoPanel.add(lblTitle, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        // --- Order Number ---
        JLabel lblOrderNum = new JLabel("Order Number");
        lblOrderNum.setFont(Components.p13);
        infoPanel.add(lblOrderNum, gbc);

        gbc.gridx = 1;
        gbc.weightx = 2;
        txtOrderNum = new JTextField();
        nextOrderID = Database.getNextID("ORDER_");
        if (nextOrderID != -1) {
            txtOrderNum.setText(String.valueOf(nextOrderID));
            txtOrderNum.setEnabled(false);
            txtOrderNum.setDisabledTextColor(Color.DARK_GRAY);
        }
        txtOrderNum.setFont(Components.b14);
        txtOrderNum.setOpaque(false);
        txtOrderNum.setBorder(new EmptyBorder(0, 0, 0, 0));
        txtOrderNum.setEditable(false);
        infoPanel.add(txtOrderNum, gbc);
        gbc.weightx = 1;
        // --- Customer ---
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblCustomer = new JLabel("Customer");
        lblCustomer.setFont(Components.p13);
        infoPanel.add(lblCustomer, gbc);

        gbc.gridx = 1;
        txtCustomer = new JTextField();
        txtCustomer.setFont(Components.b14);
        txtCustomer.setOpaque(false);
        txtCustomer.setBorder(new EmptyBorder(0, 0, 0, 0));
        txtCustomer.setEditable(false);
        infoPanel.add(txtCustomer, gbc);

        // --- Order Type ---
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblOrderType = new JLabel("Order Type");
        lblOrderType.setFont(Components.p13);
        infoPanel.add(lblOrderType, gbc);

        gbc.gridx = 1;
        JPanel orderTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        orderTypePanel.setOpaque(false);
        rdbDineIn = new JRadioButton("Dine-In", true); // default selected
        rdbDineIn.setFont(Components.p13);
        rdbDineIn.setOpaque(false);
        rdbTakeOut = new JRadioButton("Take-out");
        rdbTakeOut.setFont(Components.p13);
        rdbTakeOut.setOpaque(false);

        ButtonGroup orderTypeGroup = new ButtonGroup();
        orderTypeGroup.add(rdbDineIn);
        orderTypeGroup.add(rdbTakeOut);
        orderTypePanel.add(rdbDineIn);
        orderTypePanel.add(rdbTakeOut);
        infoPanel.add(orderTypePanel, gbc);

        // --- Cashier ---
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblCashier = new JLabel("Prepared by");
        lblCashier.setFont(Components.p13);
        infoPanel.add(lblCashier, gbc);

        gbc.gridx = 1;
        txtCashier = new JTextField(fName + " " + lName);
        txtCashier.setFont(Components.b14);
        txtCashier.setOpaque(false);
        txtCashier.setBorder(new EmptyBorder(0, 0, 0, 0));
        txtCashier.setEditable(false);
        infoPanel.add(txtCashier, gbc);
        // --- Bill ID ---
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblBillID = new JLabel("Bill ID");
        lblBillID.setFont(Components.p13);
        infoPanel.add(lblBillID, gbc);

        gbc.gridx = 1;
        txtBillID = new JTextField();
        txtBillID.setFont(Components.b14);
        txtBillID.setOpaque(false);
        txtBillID.setBorder(new EmptyBorder(0, 0, 0, 0));
        txtBillID.setEditable(false);
        infoPanel.add(txtBillID, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        JPanel summaryAllPanel = new JPanel(new BorderLayout());
        summaryAllPanel.setOpaque(false);
        JPanel summaryInfoPanel = new JPanel(new GridBagLayout());
        summaryInfoPanel.setOpaque(false);
        summaryInfoPanel.setPreferredSize(new Dimension(0, 110));
        summaryInfoPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.fill = GridBagConstraints.HORIZONTAL;
        gbc1.insets = new Insets(5, 10, 5, 10); // Top, Left, Bottom, Right padding
        gbc1.anchor = GridBagConstraints.NORTHWEST;
        gbc1.weightx = 1;
        gbc1.weighty = 0;

        // --- Sub Total Amount ---
        JLabel lblSubTotal = new JLabel("Sub Total Amount");
        lblSubTotal.setFont(Components.p13);
        summaryInfoPanel.add(lblSubTotal, gbc1);
        gbc1.gridx = 1;
        txtSubTotal = new JTextField("0.00");
        txtSubTotal.setHorizontalAlignment(JTextField.RIGHT);
        txtSubTotal.setFont(Components.b14);
        txtSubTotal.setOpaque(false);
        txtSubTotal.setBorder(new EmptyBorder(0, 0, 0, 0));
        txtSubTotal.setEditable(false);
        summaryInfoPanel.add(txtSubTotal, gbc1);

        // --- VatableSales ---
        gbc1.gridx = 0;
        gbc1.gridy++;
        JLabel lblVATSales = new JLabel("VATable Sales");
        lblVATSales.setFont(Components.p13);
        summaryInfoPanel.add(lblVATSales, gbc1);

        gbc1.gridx = 1;
        txtVATSales = new JTextField("0.00");
        txtVATSales.setHorizontalAlignment(JTextField.RIGHT);
        txtVATSales.setFont(Components.b14);
        txtVATSales.setOpaque(false);
        txtVATSales.setBorder(new EmptyBorder(0, 0, 0, 0));
        txtVATSales.setEditable(false);
        summaryInfoPanel.add(txtVATSales, gbc1);

        // --- VATAmount ---
        gbc1.gridx = 0;
        gbc1.gridy++;
        JLabel lblVATAmount = new JLabel("VAT Amount");
        lblVATAmount.setFont(Components.p13);
        summaryInfoPanel.add(lblVATAmount, gbc1);

        gbc1.gridx = 1;
        txtVATAmount = new JTextField("0.00");
        txtVATAmount.setHorizontalAlignment(JTextField.RIGHT);
        txtVATAmount.setFont(Components.b14);
        txtVATAmount.setOpaque(false);
        txtVATAmount.setBorder(new EmptyBorder(0, 0, 0, 0));
        txtVATAmount.setEditable(false);
        summaryInfoPanel.add(txtVATAmount, gbc1);

        // --- Discount ---
        gbc1.gridx = 0;
        gbc1.gridy++;
        JLabel lblDiscount = new JLabel("Discount");
        lblDiscount.setFont(Components.p13);
        summaryInfoPanel.add(lblDiscount, gbc1);

        gbc1.gridx = 1;
        txtDiscount = new JTextField("0.00");
        txtDiscount.setHorizontalAlignment(JTextField.RIGHT);
        txtDiscount.setFont(Components.b14);
        txtDiscount.setOpaque(false);
        txtDiscount.setBorder(new EmptyBorder(0, 0, 0, 0));
        txtDiscount.setEditable(false);
        summaryInfoPanel.add(txtDiscount, gbc1);

        gbc1.gridy++;
        gbc1.weighty = 1; // Make it absorb extra vertical space
        summaryInfoPanel.add(Box.createVerticalGlue(), gbc1);

        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setOpaque(false);
        totalPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new MatteBorder(1, 0, 0, 0, Color.gray)));
        // --- Total Amount ---
        JLabel lblTotal = new JLabel("Total Amount");
        lblTotal.setFont(Components.b20);

        txtTotalAmount = new JTextField("0.00");
        txtTotalAmount.setHorizontalAlignment(JTextField.RIGHT);
        txtTotalAmount.setFont(Components.b22);
        txtTotalAmount.setOpaque(false);
        txtTotalAmount.setBorder(new EmptyBorder(0, 0, 0, 0));
        txtTotalAmount.setEditable(false);

        totalPanel.add(lblTotal, BorderLayout.WEST);
        totalPanel.add(txtTotalAmount, BorderLayout.CENTER);

        summaryAllPanel.add(summaryInfoPanel, BorderLayout.CENTER);
        summaryAllPanel.add(totalPanel, BorderLayout.SOUTH);

        JPanel tablePanelNorth = new JPanel(new BorderLayout());
        tablePanelNorth.setOpaque(false);
        tablePanelNorth.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel tableButtons = new JPanel(new GridLayout(1, 2, 5, 5));
        tableButtons.setOpaque(false);
        changeQty = new BtnCustom(new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/change.png")).getImage()), new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/change1.png")).getImage()), "Change Quantity");
        delete = new BtnCustom(new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/deleteRecord1.png")).getImage()), new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/deleteRecord2.png")).getImage()), "Delete Item");
        JLabel titleTable = new JLabel("Order Item List", SwingConstants.LEFT);
        titleTable.setFont(Components.b16);
        titleTable.setForeground(Components.black);
        tableButtons.add(changeQty);
        tableButtons.add(delete);
        tablePanelNorth.add(tableButtons, BorderLayout.EAST);
        tablePanelNorth.add(titleTable, BorderLayout.WEST);
        tablePanelNorth.setPreferredSize(new Dimension(0, 40));

        JPanel finalButtonsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        finalButtonsPanel.setPreferredSize(new Dimension(0, 120));
        finalButtonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        finalButtonsPanel.setOpaque(false);
        JPanel buttonsNorth = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonsNorth.setOpaque(false);
        JButton paymentBtn = new BtnGreen("Pay", new ImageIcon(getClass().getResource("/images/icon/pay.png")));
        JButton saveBtn = new BtnDefault("Save", new ImageIcon(getClass().getResource("/images/icon/save.png")));

        saveBtn.setToolTipText("Save the current transaction");
        paymentBtn.setToolTipText("Proceed to payment");

        buttonsNorth.add(saveBtn);
        buttonsNorth.add(paymentBtn);

        JPanel buttonsSouth = new JPanel(new GridLayout(1, 5, 10, 10));
        buttonsSouth.setOpaque(false);

        JButton discountBtn = new BtnMagenta("Discount", new ImageIcon(getClass().getResource("/images/icon/discountWhite.png")));
        discountBtn.setFont(Components.b10);
        discountBtn.setMargin(new Insets(3, 5, 3, 5));
        discountBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        discountBtn.setHorizontalTextPosition(SwingConstants.CENTER);

        JButton newOrderBtn = new BtnBlue("Order", new ImageIcon(getClass().getResource("/images/icon/orderWhite.png")));
        newOrderBtn.setFont(Components.b10);
        newOrderBtn.setMargin(new Insets(3, 5, 3, 5));
        newOrderBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        newOrderBtn.setHorizontalTextPosition(SwingConstants.CENTER);

        JButton printReceiptBtn = new BtnBlue("Receipt", new ImageIcon(getClass().getResource("/images/icon/receipt.png")));
        printReceiptBtn.setFont(Components.b10);
        printReceiptBtn.setMargin(new Insets(3, 5, 3, 5));
        printReceiptBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        printReceiptBtn.setHorizontalTextPosition(SwingConstants.CENTER);

        JButton newCustomerBtn = new BtnBlue("Customer", new ImageIcon(getClass().getResource("/images/icon/customerAdd.png")));
        newCustomerBtn.setFont(Components.b10);
        newCustomerBtn.setMargin(new Insets(3, 5, 3, 5));
        newCustomerBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        newCustomerBtn.setHorizontalTextPosition(SwingConstants.CENTER);

        JButton voidBtn = new BtnMagenta("Void", new ImageIcon(getClass().getResource("/images/icon/void.png")));
        voidBtn.setFont(Components.b10);
        voidBtn.setMargin(new Insets(3, 5, 3, 5));
        voidBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        voidBtn.setHorizontalTextPosition(SwingConstants.CENTER);

        discountBtn.setToolTipText("Apply a discount to the order");
        newOrderBtn.setToolTipText("Start a new order");
        newCustomerBtn.setToolTipText("Register a new customer");
        voidBtn.setToolTipText("Cancel the current order.");

        buttonsSouth.add(discountBtn);
        buttonsSouth.add(voidBtn);
        buttonsSouth.add(printReceiptBtn);
        buttonsSouth.add(newOrderBtn);
        buttonsSouth.add(newCustomerBtn);

        finalButtonsPanel.add(buttonsNorth);
        finalButtonsPanel.add(buttonsSouth);

        listPanel.add(infoPanel, BorderLayout.NORTH);
        listPanel.add(tablePanel, BorderLayout.CENTER);
        tablePanel.add(tableScrollPanePanel, BorderLayout.CENTER);
        tablePanel.add(tablePanelNorth, BorderLayout.NORTH);
        listPanel.add(footer, BorderLayout.SOUTH);
        footer.add(summaryAllPanel, BorderLayout.CENTER);
        footer.add(finalButtonsPanel, BorderLayout.SOUTH);

        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setOpaque(false);
        menuPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        JPanel menuNorthPanel = new JPanel(new BorderLayout());
        menuNorthPanel.setOpaque(false);
        menuNorthPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

        JPanel searchBarPanel = new JPanel(new BorderLayout());
        searchBarPanel.setPreferredSize(new Dimension(0, 40));
        searchBarPanel.setOpaque(false);

        JTextField searchBarField = new RTextField("Search", 30);
        searchBarField.setFont(Components.p13);
        searchBarField.setForeground(Color.black);
        searchBarField.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel searchBarButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        searchBarButtons.setOpaque(false);

        JButton search = new BtnCustom(new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/search.png")).getImage()), new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/search1.png")).getImage()), "Filter");
        JButton refresh = new BtnCustom(new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/refresh2.png")).getImage()), new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/refresh1.png")).getImage()), "Filter");
        search.setToolTipText("Search a Menu Item");
        refresh.setToolTipText("Refresh the POS");
        searchBarPanel.add(searchBarField, BorderLayout.CENTER);
        searchBarPanel.add(searchBarButtons, BorderLayout.EAST);
        searchBarButtons.add(search);
        searchBarButtons.add(refresh);

        searchBarField.addActionListener(e -> searchMenuItems(searchBarField.getText()));
        search.addActionListener(e -> {
            String searchItem = searchBarField.getText();

            if (searchItem.isEmpty() || searchItem == null || searchItem.isBlank()) {
                JOptionPane.showMessageDialog(null, "No text inputted in the textfield", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                if (selectedCategoryBtn != null) {
                    selectedCategoryBtn.doClick(); // Reload current category
                }
                return;
            }
            searchMenuItems(searchBarField.getText());
        });

        refresh.addActionListener(e -> {
            if (selectedCategoryBtn != null) {
                selectedCategoryBtn.doClick(); // Reload current category
            }
        });

        JPanel exteriorMenuCategoryPanel = new JPanel(new BorderLayout());
        exteriorMenuCategoryPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        exteriorMenuCategoryPanel.setOpaque(false);
        menuCategoryPanel = new RPanel(30);

        JScrollPane exteriorMenuCategoryScrollPane = new CNavScrollPane(menuCategoryPanel);
        exteriorMenuCategoryScrollPane.setOpaque(false);
        exteriorMenuCategoryScrollPane.setBorder(null);
        exteriorMenuCategoryScrollPane.getViewport().setOpaque(false);
        exteriorMenuCategoryPanel.add(exteriorMenuCategoryScrollPane, BorderLayout.CENTER);
        menuCategoryPanel.setBackground(Color.white);
        menuCategoryPanel.setBackground(Color.white);
        menuCenterPanel = new JPanel(new BorderLayout());
        menuCenterPanel.setBackground(Color.white);
        menuCenterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane menuCenterScroller = new CNavScrollPane(menuCenterPanel);
        menuCenterScroller.setPreferredSize(new Dimension(120, 40));
        menuCenterScroller.setOpaque(false);
        menuCenterScroller.getViewport().setOpaque(false);
        menuCenterScroller.setBorder(null);

        panel.add(menuPanel, BorderLayout.CENTER);
        menuPanel.add(menuCenterScroller, BorderLayout.CENTER);
        menuPanel.add(menuNorthPanel, BorderLayout.NORTH);
        menuNorthPanel.add(searchBarPanel, BorderLayout.NORTH);
        menuNorthPanel.add(exteriorMenuCategoryPanel, BorderLayout.CENTER);

        panel.add(outerListPanel, BorderLayout.EAST);
        outerListPanel.add(listPanel, BorderLayout.CENTER);

        loadCategories();

        //Action listeners
        newOrderBtn.addActionListener(e -> {
            executeNewOrder();
        });

        paymentBtn.addActionListener(e -> {
            executePayment();
        });

        saveBtn.addActionListener(e -> {
            executeSaveOrder();
        });

        newCustomerBtn.addActionListener(e -> {
            executeNewCustomer();
        });

        discountBtn.addActionListener(e -> {
            executeDiscount();
        });

        printReceiptBtn.addActionListener(e -> {
            if (!isPaymentDone) {
                JOptionPane.showMessageDialog(null, "Receipt cannot be printed as payment is not done yet.", "Invalid Action", JOptionPane.ERROR_MESSAGE);
                return;
            }
            printReceipt(Integer.parseInt(txtBillID.getText()));
        });

        changeQty.addActionListener(e -> {
            changeQuantity();
        });

        delete.addActionListener(e -> {
            deleteItem();
        });

        voidBtn.addActionListener(e -> {
            executeVoid();
        });
        return panel;
    }

    private JPanel createPendingOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(pendingOrdersGUI.getPendingOrdersUI(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAboutUsPanel() {
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
        return panel;
    }

    private void changeQuantity() {
        int row = orderTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Please select an item to change quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newQtyStr = JOptionPane.showInputDialog("Enter new quantity:");
        try {
            int newQty = Integer.parseInt(newQtyStr);
            if (newQty <= 0) {
                JOptionPane.showMessageDialog(null, "Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double price = Double.parseDouble(tableModel.getValueAt(row, 2).toString());

            double newTotal = newQty * price;
            // Update the table model (UI only)
            tableModel.setValueAt(newQty, row, 3);
            tableModel.setValueAt(newTotal, row, 4);
            updateAmounts();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteItem() {
        int row = orderTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Please select an item to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int menuItemID = (int) tableModel.getValueAt(row, 0); // Assuming MenuItemID is in column 1

        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Remove from table model (UI)
            tableModel.removeRow(row);
            updateAmounts(); // Update UI total only
        }
    }

    private void executeNewOrder() {
        
        if (tableModel.getRowCount() == 0){
            JOptionPane.showMessageDialog(null, "Action cannot be completed. No transaction found.", "Invalid Action", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isOrderSaved) {
            int confirm = JOptionPane.showConfirmDialog(this, "Current order is not saved. Proceeding will clear it. Continue?", "Warning", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            
        }
        
        if (!isPaymentDone){
            JOptionPane.showMessageDialog(null, "The current transaction must be paid first or be voided.", "Invalid Action", JOptionPane.ERROR_MESSAGE);
            return;
        }
        tableModel.setRowCount(0); // Clear table

        nextOrderID = Database.getNextID("ORDER_");
        if (nextOrderID != -1) {
            txtOrderNum.setText(String.valueOf(nextOrderID));
            txtOrderNum.setEnabled(false);
            txtOrderNum.setDisabledTextColor(Color.DARK_GRAY);
        }
        customerID = -1;
        paymentIDForReceipt = 0;
        txtCustomer.setText("");
        txtBillID.setText("");
        txtSubTotal.setText("0.00");
        txtVATSales.setText("0.00");
        txtVATAmount.setText("0.00");
        txtDiscount.setText("0.00");
        txtTotalAmount.setText("0.00");
        isOrderSaved = false;
        isPaymentDone = false;
        isDiscountEnabled = false;
    }

    private void executeVoid() {
        if (tableModel.getRowCount() == 0){
            JOptionPane.showMessageDialog(null, "Action cannot be completed. No transaction found.", "Invalid Action", JOptionPane.ERROR_MESSAGE);
            return;
        }
        executeAccess();
        if (!isAuthorized) {
            isAuthorized = false;
            JOptionPane.showMessageDialog(null, "Access Denied", "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!isOrderSaved) {
            int confirm = JOptionPane.showConfirmDialog(this, "Current order is not saved. Proceeding will clear it. Continue?", "Warning", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                isAuthorized = false;
                executeNewOrder();
            } else {
                isAuthorized = false;
                return;
            }
        }
        else{
            isAuthorized = false;
            updateOrderStatusToCancelled();
            updateBillStatusToCancelled();
             tableModel.setRowCount(0); // Clear table

                nextOrderID = Database.getNextID("ORDER_");
                if (nextOrderID != -1) {
                    txtOrderNum.setText(String.valueOf(nextOrderID));
                    txtOrderNum.setEnabled(false);
                    txtOrderNum.setDisabledTextColor(Color.DARK_GRAY);
                }
                customerID = -1;
                paymentIDForReceipt = 0;
                txtCustomer.setText("");
                txtBillID.setText("");
                txtSubTotal.setText("0.00");
                txtVATSales.setText("0.00");
                txtVATAmount.setText("0.00");
                txtDiscount.setText("0.00");
                txtTotalAmount.setText("0.00");
                isOrderSaved = false;
                isPaymentDone = false;
                isDiscountEnabled = false;
        }
        if (isPaymentDone) {
            isAuthorized = false;
            JOptionPane.showMessageDialog(null, "Payment is already done. Cancelling the current order is not possible.", "Cancelling is not possible.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
    }

    private void executeDiscount() {
        executeAccess();
        if (!isAuthorized) {
            JOptionPane.showMessageDialog(null, "Access Denied", "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (isDiscountEnabled) {
            JOptionPane.showMessageDialog(null, "Discount has already been applied.", "Discount is already been applied.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isOrderSaved) {
            JOptionPane.showMessageDialog(null, "You cannot enter a discount since the Order is not saved.", "Action Not Allowed", JOptionPane.WARNING_MESSAGE);
            return;
        } else {
            isAuthorized = false;
            showDiscountDialog();
        }
    }

    private void executeNewCustomer() {
        if (customerID != -1) {
            JOptionPane.showMessageDialog(null, "Customer is already been added.", "Customer is already added.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Customer Dialog setup
        JDialog customerFrame = new JDialog((JFrame) null, "Add Customer", true);
        customerFrame.setResizable(false);
        customerFrame.setSize(450, 400);
        customerFrame.setIconImage(Components.l);
        customerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        customerFrame.setLayout(new BorderLayout(10, 10));
        //Holds the background
        JPanel backgroundPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
        backgroundPanel.setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(20, 10, 10, 10));

        // Labels
        JLabel lblFirstName = new JLabel("First Name:");
        JLabel lblMiddleName = new JLabel("Middle Name:");
        JLabel lblLastName = new JLabel("Last Name:");
        JLabel lblContactNumber = new JLabel("Contact Number:");

        // TextFields
        JTextField txtFirstName = new RTextField("Required", 30);
        JTextField txtMiddleName = new RTextField("Optional", 30);
        JTextField txtLastName = new RTextField("Required", 30);
        JTextField txtContactNumber = new RTextField("09 (Optional)", 30);

        lblFirstName.setFont(Components.p18);
        lblMiddleName.setFont(Components.p18);
        lblLastName.setFont(Components.p18);
        lblContactNumber.setFont(Components.p18);
        txtFirstName.setFont(Components.p18);
        txtMiddleName.setFont(Components.p18);
        txtLastName.setFont(Components.p18);
        txtContactNumber.setFont(Components.p18);

        Dimension fieldSize = new Dimension(500, 40);

        txtFirstName.setPreferredSize(fieldSize);
        txtMiddleName.setPreferredSize(fieldSize);
        txtLastName.setPreferredSize(fieldSize);
        txtContactNumber.setPreferredSize(fieldSize);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        // First Name
        gbc.gridx = 0;
        gbc.weightx = 0;

        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(lblFirstName, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        // Middle Name
        gbc.gridy++;
        infoPanel.add(lblMiddleName, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);

        // Last Name
        gbc.gridy++;
        infoPanel.add(lblLastName, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);

        // Contact Number
        gbc.gridy++;
        infoPanel.add(lblContactNumber, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        //First Name textfield
        infoPanel.add(txtFirstName, gbc);
        gbc.gridy++;
        JLabel errorFirstName = new JLabel();
        errorFirstName.setFont(Components.p11);
        errorFirstName.setForeground(Color.red);
        infoPanel.add(errorFirstName, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        //Middle Name textfield
        gbc.gridy++;
        infoPanel.add(txtMiddleName, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        //Last Name textfield    
        gbc.gridy++;
        infoPanel.add(txtLastName, gbc);

        gbc.gridy++;
        JLabel errorLastName = new JLabel();
        errorLastName.setFont(Components.p11);
        errorLastName.setForeground(Color.red);
        infoPanel.add(errorLastName, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);

        //Contact Number textfield   
        gbc.gridy++;
        infoPanel.add(txtContactNumber, gbc);
        gbc.gridy++;
        JLabel errorContactNumber = new JLabel();
        errorContactNumber.setFont(Components.p11);
        errorContactNumber.setForeground(Color.red);
        infoPanel.add(errorContactNumber, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);

        // SOUTH PANEL – Buttons
        JPanel southPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        southPanel.setOpaque(false);
        southPanel.setBorder(Components.b);
        Dimension d = new Dimension(120, 30);
        Insets margin = new Insets(2, 2, 2, 2);
        JButton btnCancel = new BtnRed("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
        JButton btnDone = new BtnGreen("Done", new ImageIcon(getClass().getResource("/images/icon/done.png")));

        btnCancel.setPreferredSize(d);
        btnCancel.setMargin(margin);
        btnDone.setPreferredSize(d);
        btnDone.setMargin(margin);
        southPanel.add(btnCancel);
        southPanel.add(btnDone);

        // NORTH title
        JPanel northPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/orange.jpg")).getImage());
        northPanel.setLayout(new BorderLayout());
        northPanel.setBorder(Components.b);

        JLabel titleLabel = new JLabel("Customer Information", SwingConstants.CENTER);
        titleLabel.setFont(Components.b22);
        titleLabel.setForeground(Color.white);

        northPanel.add(titleLabel, BorderLayout.CENTER);

        customerFrame.add(backgroundPanel, BorderLayout.CENTER);
        backgroundPanel.add(infoPanel, BorderLayout.CENTER);
        backgroundPanel.add(northPanel, BorderLayout.NORTH);
        backgroundPanel.add(southPanel, BorderLayout.SOUTH);

        // Button actions
        btnCancel.addActionListener(e -> customerFrame.dispose());

        btnDone.addActionListener(e -> {

            String firstName = txtFirstName.getText().trim();
            String middleName = txtMiddleName.getText().trim();
            String lastName = txtLastName.getText().trim();
            String cNumber = txtContactNumber.getText().trim();
            if (firstName.equalsIgnoreCase("Required")) {
                JOptionPane.showMessageDialog(customerFrame, "First name is required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                txtFirstName.setForeground(Color.red);
                errorFirstName.setText("First name is required.");
                return;
            }
            if (lastName.equalsIgnoreCase("Required")) {
                JOptionPane.showMessageDialog(customerFrame, "Last name is required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                txtLastName.setForeground(Color.red);
                errorLastName.setText("Last name is required.");
                return;
            }

            if (middleName.equalsIgnoreCase("Optional") || middleName.isBlank() || middleName.isEmpty() || middleName == null) {
                middleName = "NA";
            }

            if (!cNumber.isEmpty()) {
                if (cNumber.equals("09") || cNumber.equals("NA") || cNumber.equalsIgnoreCase("09 (Optional)")) {
                    cNumber = "NA";
                } else {
                    if (!Database.isValidContactNumberLength(cNumber)) {
                        JOptionPane.showMessageDialog(customerFrame, "Invalid contact number. It must be 11 digits long.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        txtContactNumber.setForeground(Color.red);
                        errorContactNumber.setText("Contact number must be 11 digits long.");
                        return;
                    }
                    if (!Database.isValidContactNumber(cNumber)) {
                        JOptionPane.showMessageDialog(customerFrame, "Invalid contact number. It must start with '09'.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        txtContactNumber.setForeground(Color.red);
                        errorContactNumber.setText("Contact number must start with 09.");
                        return;
                    }
                }
            }
            customerID = -1;
            Connection conn = null;

            try {
                conn = Database.getConnection();
                conn.setAutoCommit(false);  // Begin transaction

                // Check if customer exists first
                customerID = Database.getExistingCustomerID(firstName, middleName, lastName, cNumber);

                if (customerID != -1) {
                    txtCustomer.setText(firstName + " " + middleName + " " + lastName);
                    conn.commit();
                    JOptionPane.showMessageDialog(
                            customerFrame,
                            "Customer already exists (ID=" + customerID + ").",
                            "Info",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    // Customer doesn't exist, insert new
                    String insertSql = "INSERT INTO CUSTOMER (FName, MName, LName, CNumber) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                        ps.setString(1, firstName);
                        ps.setString(2, middleName);
                        ps.setString(3, lastName);
                        ps.setString(4, cNumber);

                        int rows = ps.executeUpdate();
                        if (rows == 0) {
                            throw new SQLException("Insert failed.");
                        }

                        try (ResultSet rs = ps.getGeneratedKeys()) {
                            if (rs.next()) {
                                customerID = rs.getInt(1);
                                txtCustomer.setText(firstName + " " + middleName + " " + lastName);
                            }
                        }
                    }

                    conn.commit();
                    JOptionPane.showMessageDialog(
                            customerFrame,
                            "Customer successfully added (ID=" + customerID + ").",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }

            } catch (SQLException ae) {
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException rbEx) {
                        rbEx.printStackTrace();
                    }
                }
                ae.printStackTrace();
                JOptionPane.showMessageDialog(
                        customerFrame,
                        "Database error: " + ae.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            } finally {
                if (conn != null) {
                    try {
                        conn.setAutoCommit(true);
                        conn.close();
                    } catch (SQLException closeEx) {
                        closeEx.printStackTrace();
                    }
                }
            }
            customerFrame.dispose();
        });

        customerFrame.setLocationRelativeTo(null);
        customerFrame.setVisible(true);
    }

    private void executeSaveOrder() {
        if (isOrderSaved) {
            JOptionPane.showMessageDialog(this, "Order#" + txtOrderNum.getText() + " has already been saved.", "Order already saved", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (customerID == -1) {
            JOptionPane.showMessageDialog(this, "Customer is not yet indicated. Please add a customer.", "No customer found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Order and bill values
        String orderType = rdbDineIn.isSelected() ? "DINE-IN" : "TAKE-OUT";
        String status = "PENDING";
        double subtotal = Double.parseDouble(txtSubTotal.getText());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        double vatAmount = Double.parseDouble(txtVATAmount.getText());
        double vatSales = Double.parseDouble(txtVATSales.getText());
        double totalAmount = Double.parseDouble(txtTotalAmount.getText());
        String billStatus = "UNPAID";

        Connection conn = null;

        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // 1. Insert into ORDER_ table
            String sqlOrder = "INSERT INTO ORDER_ (UserID, CustomerID, OType, SAmount, DateTime, OStatus) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, id);
                stmt.setInt(2, customerID);
                stmt.setString(3, orderType);
                stmt.setDouble(4, subtotal);
                stmt.setTimestamp(5, timestamp);
                stmt.setString(6, status);

                int rowsInserted = stmt.executeUpdate();

                if (rowsInserted == 0) {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "Order save failed!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Get generated OrderID for use in BILL
                ResultSet generatedOrderKeys = stmt.getGeneratedKeys();
                int orderID;
                if (generatedOrderKeys.next()) {
                    orderID = generatedOrderKeys.getInt(1);
                    txtOrderNum.setText(String.valueOf(orderID));
                } else {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "Failed to retrieve Order ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 2. Insert into BILL table
                String sqlBill = "INSERT INTO BILL (OrderID, DateTime, VATAmount, VATSales, TotalAmount, BStatus) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement billStmt = conn.prepareStatement(sqlBill, Statement.RETURN_GENERATED_KEYS)) {
                    billStmt.setInt(1, orderID);
                    billStmt.setTimestamp(2, timestamp);
                    billStmt.setDouble(3, vatAmount);
                    billStmt.setDouble(4, vatSales);
                    billStmt.setDouble(5, totalAmount);
                    billStmt.setString(6, billStatus);

                    int billRows = billStmt.executeUpdate();
                    if (billRows == 0) {
                        conn.rollback();
                        JOptionPane.showMessageDialog(this, "Bill save failed!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Get generated BillID
                    ResultSet generatedBillKeys = billStmt.getGeneratedKeys();
                    if (generatedBillKeys.next()) {
                        int billID = generatedBillKeys.getInt(1);
                        txtBillID.setText(String.valueOf(billID));
                    } else {
                        conn.rollback();
                        JOptionPane.showMessageDialog(this, "Failed to retrieve Bill ID.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // 3. Commit transaction if both inserts succeeded
                conn.commit();
                isOrderSaved = true;
                JOptionPane.showMessageDialog(this, "Order and Bill saved successfully!");

                // Save order items separately (can also be moved into this transaction)
                saveOrderItemsToDB();

            }

        } catch (SQLException ex) {
            // Handle rollback if anything fails
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Reset auto-commit and close connection
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

    private void executePayment() {
        if (isPaymentDone) {
            JOptionPane.showMessageDialog(null, "The payment has already been processed.", "Payment already done.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!isOrderSaved) {
            JOptionPane.showMessageDialog(this, "Order must be saved before proceeding with payment.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int billID = Integer.parseInt(txtBillID.getText());
            double totalAmount = Double.parseDouble(txtTotalAmount.getText());
            String paymentMode = "CASH";
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            // Ask for cash tendered
            String input = JOptionPane.showInputDialog(this, "Enter Cash Tendered:");
            if (input == null) {
                return; // Cancelled
            }
            double cashTendered = Double.parseDouble(input);

            if (cashTendered < totalAmount) {
                JOptionPane.showMessageDialog(this, "Cash is not enough to pay the total amount.", "Payment Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double change = cashTendered - totalAmount;

            String insertPaymentSQL = "INSERT INTO PAYMENT (BillID, DateTime, PaymentMode) VALUES (?, ?, ?)";
            String insertCashSQL = "INSERT INTO CASHPAYMENT (PaymentID, CashTendered, Change) VALUES (?, ?, ?)";

            Connection conn = null;

            try {
                conn = Database.getConnection();
                conn.setAutoCommit(false); // Begin transaction

                // Insert into PAYMENT table
                try (PreparedStatement paymentStmt = conn.prepareStatement(insertPaymentSQL, Statement.RETURN_GENERATED_KEYS)) {
                    paymentStmt.setInt(1, billID);
                    paymentStmt.setTimestamp(2, timestamp);
                    paymentStmt.setString(3, paymentMode);
                    int rowsInserted = paymentStmt.executeUpdate();

                    if (rowsInserted > 0) {
                        // Get the generated PaymentID
                        try (ResultSet generatedKeys = paymentStmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                int paymentID = generatedKeys.getInt(1);

                                // Insert into CASHPAYMENT table
                                try (PreparedStatement cashStmt = conn.prepareStatement(insertCashSQL)) {
                                    cashStmt.setInt(1, paymentID);
                                    cashStmt.setDouble(2, cashTendered);
                                    cashStmt.setDouble(3, change);
                                    int cashInserted = cashStmt.executeUpdate();

                                    if (cashInserted > 0) {
                                        // Commit the transaction if everything is successful
                                        conn.commit();

                                        // Update the Bill status to PAID
                                        updateBillStatusToPaid();

                                        // Print the receipt
                                        paymentIDForReceipt = paymentID;
                                        printReceipt(paymentIDForReceipt);

                                        // Notify the user
                                        JOptionPane.showMessageDialog(this, "Payment successful!\nChange: ₱" + String.format("%.2f", change));
                                        isPaymentDone = true;
                                    } else {
                                        conn.rollback();  // Rollback if cash payment insertion fails
                                        JOptionPane.showMessageDialog(this, "Failed to record cash payment.", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                } catch (SQLException cashEx) {
                                    conn.rollback();  // Rollback if an error occurs while inserting into CASHPAYMENT
                                    cashEx.printStackTrace();
                                    JOptionPane.showMessageDialog(this, "Database error: " + cashEx.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } catch (SQLException genEx) {
                            conn.rollback();  // Rollback if an error occurs while fetching generated keys
                            genEx.printStackTrace();
                            JOptionPane.showMessageDialog(this, "Database error: " + genEx.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        conn.rollback();  // Rollback if payment insertion fails
                        JOptionPane.showMessageDialog(this, "Failed to record payment.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException paymentEx) {
                    conn.rollback();  // Rollback in case of any SQL exception during payment insert
                    paymentEx.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Database error: " + paymentEx.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input or empty field.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                // Reset auto-commit and close the connection
                if (conn != null) {
                    try {
                        conn.setAutoCommit(true);
                        conn.close();
                    } catch (SQLException closeEx) {
                        closeEx.printStackTrace();
                    }
                }
            }
        } catch (Exception ae) {
            ae.printStackTrace();
        }
    }

//======================FOR ORDER=============================
    private void loadCategories() {
        menuCategoryPanel.removeAll(); // Clear existing buttons
        menuCategoryPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 10, 10)); // Horizontal and vertical gaps
        selectedCategoryBtn = null; // Reset selection

        String query = "SELECT MICatID, CatDes FROM MENUITEMCATEGORY";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            boolean firstButton = true;

            while (rs.next()) {
                int id = rs.getInt("MICatID");
                String name = rs.getString("CatDes");

                BtnMenuCategory catButton = new BtnMenuCategory(name);

                catButton.addActionListener(e -> {
                    if (selectedCategoryBtn != null) {
                        selectedCategoryBtn.setSelected(false); // Deselect previous
                    }
                    catButton.setSelected(true); // Select this
                    selectedCategoryBtn = catButton;

                    loadMenuItems(id); // Your existing logic
                });

                menuCategoryPanel.add(catButton);

                // Select the first button by triggering its action
                if (firstButton) {
                    catButton.doClick(); // Triggers actionListener
                    firstButton = false;
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        menuCategoryPanel.revalidate();
        menuCategoryPanel.repaint();
    }

    private void loadMenuItems(int categoryID) {
        menuCenterPanel.removeAll();
        menuCenterPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 5, 5)); // Horizontal and vertical gaps
        menuCenterPanel.setOpaque(false);
        String query = "SELECT MenuItemID, MIName, UPrice, NServing, MIStatus, MICatID,ItemPicture FROM MENUITEM WHERE MICatID = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, categoryID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int itemId = rs.getInt("MenuItemID");
                    String name = rs.getString("MIName");
                    double price = rs.getDouble("UPrice");
                    int nServing = rs.getInt("NServing");
                    String status = rs.getString("MIStatus");
                    int catId = rs.getInt("MICatID");
                    MenuItem item = new MenuItem(itemId, name, price, nServing, status, catId);

                    // Retrieve the item picture as a byte array
                    byte[] imgBytes = rs.getBytes("ItemPicture");
                    ImageIcon itemPicture = null;
                    if (imgBytes != null) {
                        // Convert byte array to ImageIcon
                        itemPicture = new ImageIcon(imgBytes);
                    }
                    Image image = itemPicture.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    ImageIcon resizedIcon = new ImageIcon(image);
                    JLabel picture = new JLabel(resizedIcon);

                    PanelButton itemButton = new PanelButton("normal", null, picture, name, itemId, price, status, nServing);
                    itemButton.setOnClick(() -> addItemToOrder(item));
                      
                    if (status.equalsIgnoreCase("UNAVAILABLE")) {
                        itemButton.setEnabled(false);
                        itemButton.setBackground(Components.dGray);
                        itemButton.setOnClick(() -> {
                        JOptionPane.showMessageDialog(null, "Menu Item is Unavailable", "Menu Item is Unavailable", JOptionPane.ERROR_MESSAGE);
                        }
                        );
                    }
                    else{
                        itemButton.setOnClick(() -> addItemToOrder(item));
                    }

                    menuCenterPanel.add(itemButton);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace(); // Or show a styled dialog
        }

        menuCenterPanel.revalidate();
        menuCenterPanel.repaint();
    }

    private void addItemToOrder(MenuItem item) {
        JTable table = orderTable;
        DefaultTableModel tableModel = (DefaultTableModel) orderTable.getModel();

        // Ask user for quantity
        String qtyStr = JOptionPane.showInputDialog(null, "Enter quantity for " + item.getName() + ":");
        if (qtyStr == null) {
            return; // Cancelled
        }

        int qty;
        try {
            qty = Integer.parseInt(qtyStr);
            if (qty <= 0) {
                JOptionPane.showMessageDialog(null, "Invalid quantity entered.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number entered.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean itemExistsInTable = false;

        // Check if item already exists in table
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (Integer.parseInt(tableModel.getValueAt(i, 0).toString()) == item.getMenuItemID()) {
                int currentQty = (int) tableModel.getValueAt(i, 3);
                int newQty = currentQty + qty;

                tableModel.setValueAt(newQty, i, 3); // Update quantity
                tableModel.setValueAt(String.format("%.2f", newQty * item.getUnitPrice()), i, 4);
                itemExistsInTable = true;
                updateAmounts();
                return;
            }
        }

        // If item is new (not in table yet)
        if (!itemExistsInTable) {
            double total = qty * item.getUnitPrice();

            tableModel.addRow(new Object[]{
                item.getMenuItemID(),
                item.getName(),
                String.format("%.2f", item.getUnitPrice()), // formatted unit price
                qty,
                String.format("%.2f", total) // formatted total
            });

            updateAmounts();
        }
    }

    private void updateAmounts() {
        updateSubTotalAmountUI();
        updateVATSalesUI();
        updateVATUI();
        updateTotalAmountUI();
    }

    private void updateTotalAmountUI() {
        txtTotalAmount.setText(String.format(txtSubTotal.getText()));

    }

    private void updateVATSalesUI() {
        double subTotalAmount = Double.parseDouble(txtSubTotal.getText());
        txtVATSales.setText(String.format("%.2f", subTotalAmount / 1.12));
    }

    private void updateVATUI() {
        double vatSales = Double.parseDouble(txtVATSales.getText());
        txtVATAmount.setText(String.format("%.2f", vatSales * 0.12));
    }

    private void updateSubTotalAmountUI() {
        double subtotal = 0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            subtotal += Double.parseDouble(tableModel.getValueAt(i, 4).toString()); // Total Price
        }

        txtSubTotal.setText(String.format("%.2f", subtotal));
    }

    private void searchMenuItems(String keyword) {
        menuCenterPanel.removeAll();
        menuCenterPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 10, 10));
        menuCenterPanel.setOpaque(false);

        String query = "SELECT m.MenuItemID, m.MIName, m.UPrice, m.NServing, m.MIStatus,m.MICatID, m.ItemPicture, c.CatDes FROM MENUITEM m JOIN MENUITEMCATEGORY c ON m.MICatID = c.MICatID WHERE m.MIName LIKE ?";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    int itemId = rs.getInt("MenuItemID");
                    String name = rs.getString("MIName");
                    double price = rs.getDouble("UPrice");
                    int nServing = rs.getInt("NServing");
                    String status = rs.getString("MIStatus");
                    int catId = rs.getInt("MICatID");
                    String category = rs.getString("CatDes");

                    MenuItem item = new MenuItem(itemId, name, price, nServing, status, catId);

                    // Retrieve the item picture as a byte array
                    byte[] imgBytes = rs.getBytes("ItemPicture");
                    ImageIcon itemPicture = null;
                    if (imgBytes != null) {
                        // Convert byte array to ImageIcon
                        itemPicture = new ImageIcon(imgBytes);
                    }
                    Image image = itemPicture.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    ImageIcon resizedIcon = new ImageIcon(image);
                    JLabel picture = new JLabel(resizedIcon);

                    PanelButton itemButton = new PanelButton("search", category, picture, name, itemId, price, status, nServing);
                    itemButton.setOnClick(() -> addItemToOrder(item));
  
                    if (status.equalsIgnoreCase("UNAVAILABLE")) {
                        itemButton.setEnabled(false);
                        itemButton.setBackground(Components.dGray);
                        itemButton.setOnClick(() -> {
                        JOptionPane.showMessageDialog(null, "Menu Item is Unavailable", "Menu Item is Unavailable", JOptionPane.ERROR_MESSAGE);
                        }
                        );
                    }
                    else{
                        itemButton.setOnClick(() -> addItemToOrder(item));
                    }

                    menuCenterPanel.add(itemButton);
                }

                if (!found) {
                    JLabel error = new JLabel("No items found.");
                    error.setFont(Components.b16);
                    error.setForeground(Color.black);
                    menuCenterPanel.add(error);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        menuCenterPanel.revalidate();
        menuCenterPanel.repaint();
    }

    private void showDiscountDialog() {
        JDialog discountFrame = new JDialog((JFrame) null, "Add Discount", true);
        discountFrame.setResizable(false);
        discountFrame.setSize(700, 600);
        discountFrame.setIconImage(Components.l);
        discountFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        discountFrame.setLayout(new BorderLayout());

        JPanel backgroundPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/whiteBackground.png")).getImage());
        backgroundPanel.setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(20, 10, 10, 10));

        // Labels
        JLabel lblBillID = new JLabel("Bill ID");
        JLabel lblDiscountID = new JLabel("Discount ID");
        JLabel lblFirstName = new JLabel("First Name");
        JLabel lblMiddleName = new JLabel("Middle Name");
        JLabel lblLastName = new JLabel("Last Name");
        JLabel lblEffectiveDate = new JLabel("Expiry Date");
        JLabel lblCategory = new JLabel("Discount Category");
        JLabel lblMunicipality = new JLabel("Municipality");
        JLabel lblProvince = new JLabel("Province");

        // Inputs
        JTextField txtDiscountBillID = new RTextField(txtBillID.getText(), 30);
        txtDiscountBillID.setEnabled(false);
        txtDiscountBillID.setDisabledTextColor(Color.black);
        JTextField txtDiscountID = new RTextField("Required", 30);
        JTextField txtFirstName = new RTextField("Required", 30);
        JTextField txtMiddleName = new RTextField("Optional", 30);
        JTextField txtLastName = new RTextField("Required", 30);
        DateTimeSpinner dateSpinner = new DateSpinner();
        RPanel spinnerPanel = new RPanel(30);
        spinnerPanel.setBackground(Color.WHITE);
        spinnerPanel.setBorder(b);
        spinnerPanel.setLayout(new BorderLayout());
        dateSpinner.setBorder(null);
        spinnerPanel.add(dateSpinner, BorderLayout.CENTER);
        JComboBox<String> cmbCategory = new CustomComboBox<>(new String[]{"SELECT", "SENIOR CITIZEN", "PWD"});
        RPanel catPanel = new RPanel(30);
        catPanel.setBackground(Color.WHITE);
        catPanel.setBorder(b);
        catPanel.setLayout(new BorderLayout());
        cmbCategory.setBorder(null);
        catPanel.add(cmbCategory, BorderLayout.CENTER);
        JTextField txtMunicipality = new RTextField("Required", 30);
        JTextField txtProvince = new RTextField("Required", 30);

        lblBillID.setFont(Components.p14);
        lblDiscountID.setFont(Components.p14);
        lblFirstName.setFont(Components.p14);
        lblMiddleName.setFont(Components.p14);
        lblLastName.setFont(Components.p14);
        lblEffectiveDate.setFont(Components.p14);
        lblCategory.setFont(Components.p14);
        lblMunicipality.setFont(Components.p14);
        lblProvince.setFont(Components.p14);

        txtDiscountBillID.setFont(Components.p14);
        txtDiscountID.setFont(Components.p14);
        txtFirstName.setFont(Components.p14);
        txtMiddleName.setFont(Components.p14);
        txtLastName.setFont(Components.p14);
        txtMunicipality.setFont(Components.p14);
        txtProvince.setFont(Components.p14);
        cmbCategory.setFont(Components.p14);
        dateSpinner.setFont(Components.p14);

        Dimension fieldSize = new Dimension(500, 40);
        txtDiscountBillID.setPreferredSize(fieldSize);
        txtDiscountID.setPreferredSize(fieldSize);
        txtFirstName.setPreferredSize(fieldSize);
        txtMiddleName.setPreferredSize(fieldSize);
        txtLastName.setPreferredSize(fieldSize);
        txtMunicipality.setPreferredSize(fieldSize);
        txtProvince.setPreferredSize(fieldSize);
        cmbCategory.setPreferredSize(fieldSize);
        dateSpinner.setPreferredSize(fieldSize);

        // Layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels - Left Column
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.gridy = 0;

        infoPanel.add(lblBillID, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(lblDiscountID, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(lblFirstName, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(lblMiddleName, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(lblLastName, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(lblMunicipality, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(lblProvince, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(lblCategory, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(lblEffectiveDate, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);

        // Inputs - Right Column
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;

        infoPanel.add(txtDiscountBillID, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(txtDiscountID, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        JLabel errorDiscountID = new JLabel();
        errorDiscountID.setFont(Components.p11);
        errorDiscountID.setForeground(Color.red);
        infoPanel.add(errorDiscountID, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(txtFirstName, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        JLabel errorFirstName = new JLabel();
        errorFirstName.setFont(Components.p11);
        errorFirstName.setForeground(Color.red);
        infoPanel.add(errorFirstName, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);

        gbc.gridy++;
        infoPanel.add(txtMiddleName, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(txtLastName, gbc);
        gbc.gridy++;
        JLabel errorLastName = new JLabel();
        errorLastName.setFont(Components.p11);
        errorLastName.setForeground(Color.red);
        infoPanel.add(errorLastName, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(txtMunicipality, gbc);
        gbc.gridy++;
        JLabel errorMunicipality = new JLabel();
        errorMunicipality.setFont(Components.p11);
        errorMunicipality.setForeground(Color.red);
        infoPanel.add(errorMunicipality, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(txtProvince, gbc);
        gbc.gridy++;
        JLabel errorProvince = new JLabel();
        errorProvince.setFont(Components.p11);
        errorProvince.setForeground(Color.red);
        infoPanel.add(errorProvince, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(catPanel, gbc);
        gbc.gridy++;
        JLabel errorCategory = new JLabel();
        errorCategory.setFont(Components.p11);
        errorCategory.setForeground(Color.red);
        infoPanel.add(errorCategory, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);
        gbc.gridy++;
        infoPanel.add(spinnerPanel, gbc);
        gbc.gridy++;
        JLabel errorDate = new JLabel();
        errorDate.setFont(Components.p11);
        errorDate.setForeground(Color.red);
        infoPanel.add(errorDate, gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel(""), gbc);

        // SOUTH PANEL – Buttons
        JPanel southPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        southPanel.setOpaque(false);
        southPanel.setBorder(Components.b);
        Dimension d = new Dimension(120, 30);
        Insets margin = new Insets(2, 2, 2, 2);

        JButton btnCancel = new BtnRed("Cancel", new ImageIcon(getClass().getResource("/images/icon/cancel.png")));
        JButton btnDone = new BtnGreen("Done", new ImageIcon(getClass().getResource("/images/icon/done.png")));

        btnCancel.setPreferredSize(d);
        btnCancel.setMargin(margin);
        btnDone.setPreferredSize(d);
        btnDone.setMargin(margin);
        southPanel.add(btnCancel);
        southPanel.add(btnDone);

        // NORTH PANEL – Title
        JPanel northPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/buttonBackground/orange.jpg")).getImage());
        northPanel.setLayout(new BorderLayout());
        northPanel.setBorder(Components.b);

        JLabel titleLabel = new JLabel("Discount Information", SwingConstants.CENTER);
        titleLabel.setFont(Components.b22);
        titleLabel.setForeground(Color.white);
        northPanel.add(titleLabel, BorderLayout.CENTER);

        JScrollPane backgroundScrollPane = new CNavScrollPane(backgroundPanel);
        backgroundScrollPane.setPreferredSize(new Dimension(500, 600));
        backgroundScrollPane.setOpaque(false);
        backgroundScrollPane.setBorder(null);
        backgroundScrollPane.getViewport().setOpaque(false);
        // Final Frame Composition
        discountFrame.add(backgroundScrollPane, BorderLayout.CENTER);
        backgroundPanel.add(infoPanel, BorderLayout.CENTER);
        backgroundPanel.add(northPanel, BorderLayout.NORTH);
        backgroundPanel.add(southPanel, BorderLayout.SOUTH);

        // Cancel button
        btnCancel.addActionListener(e -> discountFrame.dispose());
        btnDone.addActionListener(e -> {
            try {
                String billID = txtDiscountBillID.getText();
                String discountID = txtDiscountID.getText();
                String firstName = txtFirstName.getText();
                String mName = txtMiddleName.getText();
                String lastName = txtLastName.getText();
                String eDate = dateSpinner.getDate();
                String dCategory = cmbCategory.getSelectedItem().toString();
                String mun = txtMunicipality.getText();
                String prov = txtProvince.getText();

                // Reset foregrounds to black
                txtDiscountBillID.setForeground(Color.black);
                txtDiscountID.setForeground(Color.black);
                txtFirstName.setForeground(Color.black);
                txtMiddleName.setForeground(Color.black);
                txtLastName.setForeground(Color.black);
                txtMunicipality.setForeground(Color.black);
                txtProvince.setForeground(Color.black);
                cmbCategory.setForeground(Color.black);
                dateSpinner.getEditor().getComponent(0).setForeground(Color.black);

                // Clear error labels
                errorDiscountID.setText("");
                errorFirstName.setText("");
                errorLastName.setText("");
                errorMunicipality.setText("");
                errorProvince.setText("");
                errorCategory.setText("");
                errorDate.setText("");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

                try {
                    java.util.Date selectedDate = sdf.parse(eDate);
                    java.util.Date currentDate = sdf.parse(sdf.format(new java.util.Date())); // Strip time

                    if (selectedDate.before(currentDate)) {
                        JOptionPane.showMessageDialog(null, "Date cannot be before today.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
                        errorDate.setText("Invalid Date. Date cannot be before today.");
                        dateSpinner.getEditor().getComponent(0).setForeground(Color.red);
                        return;
                    }
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid date format.", "Date Error", JOptionPane.ERROR_MESSAGE);
                    errorDate.setText("Invalid Date. Date cannot be before today.");
                    dateSpinner.getEditor().getComponent(0).setForeground(Color.red);

                }

                // Check required fields
                if (billID.isEmpty() || billID.equalsIgnoreCase("Required")
                        || discountID.isEmpty() || discountID.equalsIgnoreCase("Required")
                        || firstName.isEmpty() || firstName.equalsIgnoreCase("Required")
                        || lastName.isEmpty() || lastName.equalsIgnoreCase("Required")
                        || dCategory.isEmpty() || dCategory.equalsIgnoreCase("SELECT")
                        || mun.isEmpty() || mun.equalsIgnoreCase("Required")
                        || prov.isEmpty() || prov.equalsIgnoreCase("Required")) {

                    JOptionPane.showMessageDialog(null, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);

                    if (billID.isEmpty() || billID.equalsIgnoreCase("Required")) {
                        txtDiscountBillID.setForeground(Color.red);

                    }
                    
                    if (mName.equals("Optional")) {
                        mName = "NULL";
                    }
                    if (discountID.isEmpty() || discountID.equalsIgnoreCase("Required")) {
                        errorDiscountID.setText("Discount ID is required.");
                        txtDiscountID.setForeground(Color.red);

                    }
                    if (discountID.length() > 20) {
                        errorDiscountID.setText("Invalid discount ID length.");
                        txtDiscountID.setForeground(Color.red);

                    }
                    if (firstName.isEmpty() || firstName.equalsIgnoreCase("Required")) {
                        errorFirstName.setText("First Name is required.");
                        txtFirstName.setForeground(Color.red);

                    }
                    if (lastName.isEmpty() || lastName.equalsIgnoreCase("Required")) {
                        errorLastName.setText("Last Name is required.");
                        txtLastName.setForeground(Color.red);

                    }
                    if (mun.isEmpty() || mun.equalsIgnoreCase("Required")) {
                        errorMunicipality.setText("Municipality is required.");
                        txtMunicipality.setForeground(Color.red);

                    }
                    if (prov.isEmpty() || prov.equalsIgnoreCase("Required")) {
                        errorProvince.setText("Province is required.");
                        txtProvince.setForeground(Color.red);

                    }
                    if (dCategory.isEmpty() || dCategory.equalsIgnoreCase("SELECT")) {
                        errorCategory.setText("Category is required.");
                        cmbCategory.setForeground(Color.red);

                    }

                    return; // Stop further execution
                }

                double subtotalAmount = Double.parseDouble(txtSubTotal.getText());
                double discountRate = DISCOUNTRATE;

                double vatableSales = subtotalAmount / 1.12;
                double lessVAT = subtotalAmount - vatableSales;
                double totalNetOfVAT = subtotalAmount - lessVAT;
                double discount = totalNetOfVAT * (discountRate / 100.0);
                double newTotalAmount = subtotalAmount - (lessVAT + discount);
                txtTotalAmount.setText(String.format("%.2f", newTotalAmount));
                txtVATAmount.setText("0.00");
                txtVATSales.setText(String.format("%.2f", vatableSales));
                txtDiscount.setText(String.format("%.2f", discount + lessVAT));

                if (!Database.discountExists(discountID)) {
                    insertDiscountToDB(discountID, firstName, mName, lastName, eDate, dCategory, mun, prov);
                }

                insertBillWithDiscountToDB(billID, discountID, lessVAT, discount);

                discountFrame.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(discountFrame, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        discountFrame.setLocationRelativeTo(null);
        discountFrame.setVisible(true);

    }

    private void insertDiscountToDB(String discountID, String fName, String mName, String lName, String eDate, String dCategory, String mun, String prov) {
        // SQL command to insert a new discount record into the DISCOUNT table
        String insertDiscountSQL = "INSERT INTO DISCOUNT (DiscountID, FName, MName, LName, EDate, DCategory, Mun, Prov) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // Declare connection outside try-with-resources to access it in catch/finally blocks
        Connection conn = null;

        try {
            // Get a connection from the Database class
            conn = Database.getConnection();

            // Disable auto-commit to manually manage transaction
            conn.setAutoCommit(false);

            // Prepare the INSERT statement
            try (PreparedStatement discountStmt = conn.prepareStatement(insertDiscountSQL)) {

                // Set the values in the prepared statement with trimmed inputs
                discountStmt.setString(1, discountID.trim());
                discountStmt.setString(2, fName.trim());
                discountStmt.setString(3, mName.trim());
                discountStmt.setString(4, lName.trim());
                discountStmt.setString(5, eDate.trim());  // Format as needed for SQL
                discountStmt.setString(6, dCategory.trim());
                discountStmt.setString(7, mun.trim());
                discountStmt.setString(8, prov.trim());

                // Execute the INSERT and get number of rows affected
                int rowsInserted = discountStmt.executeUpdate();

                if (rowsInserted > 0) {
                    // If at least one row is inserted, commit the transaction
                    conn.commit();
                    JOptionPane.showMessageDialog(null, "Discount information has been saved to the database.");
                } else {
                    // If nothing was inserted, roll back the transaction
                    conn.rollback();
                    JOptionPane.showMessageDialog(null, "No discount record was inserted.", "Insert Failed", JOptionPane.WARNING_MESSAGE);
                }

            }

        } catch (SQLException e) {
            // If any SQL error occurs, attempt to roll back the transaction
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace(); // Log rollback failure
                }
            }

            // Print stack trace and show error message
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            // Always try to reset auto-commit to true to avoid side effects
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close(); // Close connection to avoid leaks
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void insertBillWithDiscountToDB(String billID, String discountID, double lessVat, double discountAmount) {

        // SQL command to insert a new record into the BILLWITHDISCOUNT table
        String insertBillWithDiscountSQL = "INSERT INTO BILLWITHDISCOUNT (BillID, DiscountID, LessVat, DiscountAmount) VALUES (?, ?, ?, ?)";

        Connection conn = null; // Declare connection outside try block for rollback/finally access

        try {
            // Get a connection to the database
            conn = Database.getConnection();

            // Disable auto-commit to manually control the transaction
            conn.setAutoCommit(false);

            // Prepare the SQL insert statement
            try (PreparedStatement billWithDiscountStmt = conn.prepareStatement(insertBillWithDiscountSQL)) {

                // Set the parameters for the prepared statement
                billWithDiscountStmt.setInt(1, Integer.parseInt(billID.trim())); // Convert and trim billID
                billWithDiscountStmt.setString(2, discountID.trim());
                billWithDiscountStmt.setDouble(3, lessVat);
                billWithDiscountStmt.setDouble(4, discountAmount);

                // Execute the insert operation
                int rowsInserted = billWithDiscountStmt.executeUpdate();

                if (rowsInserted > 0) {
                    // Commit the transaction if insert was successful
                    conn.commit();
                    JOptionPane.showMessageDialog(null, "Bill with discount information has been saved to the database.");
                    isDiscountEnabled = true;
                } else {
                    // Rollback if no rows were inserted
                    conn.rollback();
                    JOptionPane.showMessageDialog(null, "Insert failed: No data was saved.", "Insert Failed", JOptionPane.WARNING_MESSAGE);
                }
            }

        } catch (SQLException e) {
            // Rollback the transaction in case of any exception
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            // Show the error message to the user
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            // Always reset auto-commit and close the connection
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restore default behavior
                    conn.close(); // Prevent resource leaks
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void saveOrderItemsToDB() {
        int orderID = Integer.parseInt(txtOrderNum.getText());
        String insertSQL = "INSERT INTO ORDERITEM (OrderID, MenuItemID, Quantity, TotalPrice) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int menuItemID = Integer.parseInt(tableModel.getValueAt(i, 0).toString());   // MID
                int quantity = Integer.parseInt(tableModel.getValueAt(i, 3).toString());     // Quantity
                double totalPrice = Double.parseDouble(tableModel.getValueAt(i, 4).toString()); // Total Price

                pstmt.setInt(1, orderID);
                pstmt.setInt(2, menuItemID);
                pstmt.setInt(3, quantity);
                pstmt.setDouble(4, totalPrice);

                pstmt.addBatch();
            }

            pstmt.executeBatch();
            JOptionPane.showMessageDialog(null, "Order items successfully saved to the database.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number format in order table.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void printReceipt(int paymentID) {
        JTextArea receiptArea = new JTextArea();
        receiptArea.setFont(Components.c12);
        receiptArea.setEditable(false);
        String query = """
                SELECT B.BillID, B.DateTime AS BillDate, O.OrderID, O.OType, U.FName AS CashierFName, U.LName AS CashierLName,
                       C.FName AS CustomerFName, C.LName AS CustomerLName, C.CNumber,
                       P.PaymentID, CP.CashTendered, CP.Change, P.PaymentMode,
                       B.TotalAmount, B.VATAmount, B.VATSales,
                       O.SAmount AS SubTotal,
                       D.FName AS DiscountFName, D.LName AS DiscountLName, D.DiscountID, D.DCategory, D.Mun, D.Prov, D.EDate,
                       BD.DiscountAmount, BD.LessVAT
                FROM BILL B
                JOIN ORDER_ O ON B.OrderID = O.OrderID
                JOIN USER_ U ON O.UserID = U.UserID
                JOIN CUSTOMER C ON O.CustomerID = C.CustomerID
                JOIN PAYMENT P ON B.BillID = P.BillID
                JOIN CASHPAYMENT CP ON P.PaymentID = CP.PaymentID
                LEFT JOIN BILLWITHDISCOUNT BD ON B.BillID = BD.BillID
                LEFT JOIN DISCOUNT D ON BD.DiscountID = D.DiscountID
                WHERE P.PaymentID = ?
                """;
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(query);) {

            stmt.setInt(1, paymentID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                StringBuilder receipt = new StringBuilder();

                String cashier = rs.getString("CashierFName") + " " + rs.getString("CashierLName");
                String customer = rs.getString("CustomerFName") + " " + rs.getString("CustomerLName");
                String cNumber = rs.getString("CNumber");
                String orderType = rs.getString("OType");
                int billID = rs.getInt("BillID");
                double cashTendered = rs.getDouble("CashTendered");
                double change = rs.getDouble("Change");
                double totalAmount = rs.getDouble("TotalAmount");
                double vat = rs.getDouble("VATAmount");
                double vatSales = rs.getDouble("VATSales");
                double subTotal = rs.getDouble("SubTotal");
                double lessVAT = rs.getObject("LessVAT") != null ? rs.getDouble("LessVAT") : 0.0;
                double discountAmount = rs.getDouble("DiscountAmount");
                String paymentMode = rs.getString("PaymentMode");

                // Discount Info
                String discountID = rs.getString("DiscountID");
                // Build Receipt Text
                receipt.append("\n");
                receipt.append("\t                 PAT’Z RESTAURANT\n");
                receipt.append("\t             PAT’S FOODS CORPORATION\n");
                receipt.append("\t         GUINHAWA, CITY OF MALOLOS, BULACAN\n");
                receipt.append("\t           VAT Reg TIN: 000-111-222-333\n");
                receipt.append("\t              MIN#1111222233334444\n\n");

                receipt.append("\tCashier: ").append(cashier).append("\n");
                receipt.append("\t===================================================\n");
                receipt.append(String.format("\t%s  Bill#: %s OR#%s\n", rs.getTimestamp("BillDate"), billID, billID));
                receipt.append("\t---------------------------------------------------\n");
                receipt.append(String.format("\t\t               %s\n", orderType.toUpperCase()));
                receipt.append("\t---------------------------------------------------\n\n");
                receipt.append(String.format("\t%-19s %9s %7s %12s\n", "Item", "UPrice", "Qty", "Total"));

                // Get Order Items
                String itemsQuery = """
                    SELECT MI.MIName, MI.UPrice, OI.Quantity, OI.TotalPrice
                    FROM ORDERITEM OI
                    JOIN MENUITEM MI ON OI.MenuItemID = MI.MenuItemID
                    WHERE OI.OrderID = ?
                """;
                PreparedStatement itemsStmt = conn.prepareStatement(itemsQuery);
                itemsStmt.setInt(1, rs.getInt("OrderID"));
                ResultSet itemsRs = itemsStmt.executeQuery();

                int itemCount = 0;
                while (itemsRs.next()) {
                    String itemName = itemsRs.getString("MIName");
                    double uPrice = itemsRs.getDouble("UPrice");
                    int qty = itemsRs.getInt("Quantity");
                    double total = itemsRs.getDouble("TotalPrice");
                    receipt.append(String.format("\t%-19s %9.2f %6dx %12.2f\n", itemName, uPrice, qty, total));
                    itemCount += qty;
                }

                receipt.append("\t***************** NOTHING FOLLOWS *****************\n\n");

                receipt.append(String.format("\t%-25s %24.2f\n", "Subtotal Amount", subTotal));
                receipt.append(String.format("\t%-25s %24.2f\n", "Cash Tendered", cashTendered));
                receipt.append(String.format("\t%-25s %24.2f\n", "Change", change));
                receipt.append(String.format("\t%-25s %24.2f\n", "Vatable Sales", vatSales));
                receipt.append(String.format("\t%-25s %24.2f\n", "Less VAT", lessVAT));
                receipt.append(String.format("\t%-25s %24.2f\n", "VAT Amount (12%)", vat));
                receipt.append(String.format("\t%-25s %24.2f\n", "Discount Amount", discountAmount));
                receipt.append(String.format("\t%-25s %24.2f\n", "Total Amount", totalAmount));
                receipt.append(String.format("\n\tNo. of Items: %30d items\n", itemCount));

                receipt.append("\n\t--------------------- PAYMENT ---------------------\n");
                receipt.append(String.format("\t%-25s %24s\n", "Payment ID", paymentID));
                receipt.append(String.format("\t%-25s %24s\n", "Payment Mode", paymentMode));

                receipt.append("\t---------------------------------------------------\n");
                receipt.append("\t                     CUSTOMER\n");
                receipt.append(String.format("\t%-25s %24s\n", "Customer Name", customer));
                receipt.append(String.format("\t%-25s %24s\n", "Contact Number", cNumber));
                receipt.append("\t---------------------------------------------------\n");

                // If Discount Applied
                if (discountID != null) {
                    receipt.append("\t                      DISCOUNT\n");
                    receipt.append(String.format("\t%-25s %24s\n", "Name", rs.getString("DiscountFName") + " " + rs.getString("DiscountLName")));
                    receipt.append(String.format("\t%-25s %24s\n", "ID#", discountID));
                    receipt.append(String.format("\t%-25s %24s\n", "Type", rs.getString("DCategory")));
                    receipt.append(String.format("\t%-25s %24s\n", "City/Province", rs.getString("Mun") + ", " + rs.getString("Prov")));
                    receipt.append(String.format("\t%-25s %24s\n", "Expiry Date", rs.getDate("EDate")));
                    receipt.append("\n\n\tSignature : _____________________________________\n");
                    receipt.append("\t---------------------------------------------------\n");
                }

                receipt.append("\t          For any feedback, contact us at\n");
                receipt.append("\t                    044-111-1111\n\n");
                receipt.append("\t\t        Toll fees may apply\n");
                receipt.append("\t\t     Thank you for eating with us!\n\t\t            Come again!\n");
                receipt.append("\t==================== THANK YOU ====================");

                receiptArea.setText(receipt.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error generating receipt: " + e.getMessage());
            return;
        }

        // Now Show Popup
        JDialog dialog = new JDialog();
        dialog.setTitle("Receipt Preview");
        dialog.setModal(true);
        dialog.setSize(490, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JLabel titleLabel = new JLabel("Receipt", SwingConstants.CENTER);
        titleLabel.setFont(Components.b22);
        titleLabel.setBackground(Components.orange);
        titleLabel.setForeground(Components.ashWhite);
        dialog.add(titleLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new CNavScrollPane(receiptArea);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton printButton = new BtnDefault("PRINT");
        JButton doneButton = new BtnGreen("DONE");

        buttonPanel.add(printButton);
        buttonPanel.add(doneButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        printButton.addActionListener(e -> {
            try {
                receiptArea.print();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        doneButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void applyDiscountToBill() {
        // Retrieve the updated amounts from the form fields
        double newTotalAmount = Double.parseDouble(txtTotalAmount.getText().trim());
        double vatableSales = Double.parseDouble(txtVATSales.getText().trim());
        double lessVAT = Double.parseDouble(txtVATAmount.getText().trim());
        int billID = Integer.parseInt(txtBillID.getText());

        String updateSql = "UPDATE BILL SET TotalAmount = ?, VATAmount = ?, VATSales = ? WHERE BillID = ?";

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setDouble(1, newTotalAmount);
                ps.setDouble(2, lessVAT);        // Setting VATAmount
                ps.setDouble(3, vatableSales);   // Setting VATSales
                ps.setInt(4, billID);            // Bill identifier

                int rowsUpdated = ps.executeUpdate();

                if (rowsUpdated > 0) {
                    conn.commit();
                    JOptionPane.showMessageDialog(null, "Bill details updated successfully!", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    conn.rollback(); // Rollback if nothing was updated
                    JOptionPane.showMessageDialog(null, "No matching bill found to update.", "Update Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                conn.rollback(); // Rollback on exception
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database error occurred while updating bill.", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                conn.setAutoCommit(true); // Reset auto-commit to default
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

// Method to update the BStatus to "PAID" for a given BillID
    private boolean updateBillStatusToPaid() {
        int billID = Integer.parseInt(txtBillID.getText());
        String sql = "UPDATE BILL SET BStatus = ? WHERE BillID = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // Start transaction

            try {
                pstmt.setString(1, "PAID");
                pstmt.setInt(2, billID);

                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    conn.commit(); // Commit changes
                    return true;
                } else {
                    conn.rollback(); // Nothing updated, rollback
                    return false;
                }

            } catch (SQLException e) {
                conn.rollback(); // Rollback on failure
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true); // Reset auto-commit
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to update the BStatus to "CANCELLED" for a given BillID
    private boolean updateBillStatusToCancelled() {
        int billID = Integer.parseInt(txtBillID.getText());
        String sql = "UPDATE BILL SET BStatus = ? WHERE BillID = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // Begin transaction

            try {
                pstmt.setString(1, "CANCELLED");
                pstmt.setInt(2, billID);

                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    conn.commit(); // Commit transaction
                    return true;
                } else {
                    conn.rollback(); // No rows updated, rollback
                    return false;
                }

            } catch (SQLException e) {
                conn.rollback(); // Rollback on error
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true); // Restore default auto-commit behavior
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

// Method to update the OStatus to "PAID" for a given OrderID
    private boolean updateOrderStatusToDone() {
        int orderID = Integer.parseInt(txtOrderNum.getText());
        String sql = "UPDATE ORDER_ SET OStatus = ? WHERE OrderID = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // Start transaction

            try {
                pstmt.setString(1, "PAID");
                pstmt.setInt(2, orderID);

                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    conn.commit(); // Commit transaction
                    return true;
                } else {
                    conn.rollback(); // Nothing updated, rollback
                    return false;
                }

            } catch (SQLException e) {
                conn.rollback(); // Rollback on error
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true); // Reset
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateOrderStatusToCancelled() {
        int orderID = Integer.parseInt(txtOrderNum.getText());
        String sql = "UPDATE ORDER_ SET OStatus = ? WHERE OrderID = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // Begin transaction

            try {
                pstmt.setString(1, "CANCELLED");
                pstmt.setInt(2, orderID);

                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    conn.commit(); // Commit transaction
                    return true;
                } else {
                    conn.rollback(); // Rollback if no rows were updated
                    return false;
                }

            } catch (SQLException e) {
                conn.rollback(); // Rollback on error
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true); // Restore default behavior
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void executeAccess() {
        JDialog loginFrame = new JDialog((JFrame) null, "Admin Access", true);
        loginFrame.setIconImage(Components.l);
        loginFrame.setTitle("Admin Access");
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
        JLabel lblSecurityKey = new JLabel("Admin Security Key");
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

        JLabel titleLabel = new JLabel("Authorized Access", SwingConstants.CENTER);
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
            String sql = "SELECT COUNT(*) FROM USER_ WHERE SecurityKey = ? AND Role = 'ADMIN'";

            try (Connection con = Database.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, securityKey);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        isAuthorized = true;
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
