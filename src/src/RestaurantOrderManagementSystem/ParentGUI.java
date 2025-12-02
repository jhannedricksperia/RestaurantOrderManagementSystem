
package RestaurantOrderManagementSystem;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class ParentGUI extends Components {

    protected int loggedInID;
    protected String name;
    protected JLabel profilePicture, cardTitle, generalLabel;
    protected JButton logout, exit;
    protected CardLayout cardLayout;
    protected JPanel mainPanel, navigationPanel, navHeadPanel, loggedUserPanel, namePanel, generalPanel, eastPanel, subHeadPanel, headCenterPanel, cardPanel, eastCenterPanel ;

    public ParentGUI(int id, String fName, String lName) {
        loggedInID = id;
        this.name = fName;
        profilePicture = Database.getProfile(loggedInID);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setTitle("");
        setIconImage(l);
        setSize(1600, 1000);
        setLocationRelativeTo(null);

        mainPanel = new BackgroundPanel(new ImageIcon(getClass().getResource("/images/background/mainBackground.png")).getImage());
        mainPanel.setLayout(new BorderLayout());
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);
        navPanel.setBorder(new EmptyBorder(10,10,10,0));
        navigationPanel = new RPanel (30);
        navigationPanel.setBackground(Color.white);
        navigationPanel.setPreferredSize(new Dimension(350, 0));
        navigationPanel.setLayout(new BorderLayout());
        this.profilePicture.setToolTipText("Logged-in user's profile picture");

        navHeadPanel = new JPanel(new BorderLayout());
        navHeadPanel.setOpaque(false);
        navHeadPanel.setPreferredSize(new Dimension(0, 150));

        loggedUserPanel = new JPanel();
        loggedUserPanel.setLayout(new BorderLayout());
        loggedUserPanel.setOpaque(false);
        loggedUserPanel.setPreferredSize(new Dimension(0, 110));

        namePanel = new JPanel(new GridLayout(3, 1, 0, 0));
        namePanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 20, 15));
        namePanel.setOpaque(false);

        JLabel profileName = new JLabel(name);
        profileName.setFont(b22);
        profileName.setToolTipText("Name of the Logged-in Admin");
        profileName.setForeground(Color.black);

        JLabel idLabel = new JLabel("User# " + id);
        idLabel.setToolTipText("User ID Number");
        idLabel.setFont(b12);
        idLabel.setForeground(Color.black);

        JLabel statusLabel = new JLabel("· ACTIVE");
        statusLabel.setToolTipText("Status of Logged-in Admin");
        statusLabel.setFont(b14);
        statusLabel.setForeground(new Color(23, 207, 72));

        namePanel.add(profileName);
        namePanel.add(idLabel);
        namePanel.add(statusLabel);

        loggedUserPanel.add(namePanel, BorderLayout.CENTER);
        loggedUserPanel.add(this.profilePicture, BorderLayout.WEST);
        loggedUserPanel.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 15));

        generalPanel = new JPanel(new BorderLayout());
        generalPanel.setPreferredSize(new Dimension(0, 50));
        generalPanel.setOpaque(false);
        generalPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 22, 84)));

        generalLabel = new JLabel("        General", SwingConstants.LEFT);
        generalLabel.setForeground(Color.BLACK);
        generalLabel.setFont(b18);
        generalPanel.add(generalLabel, BorderLayout.CENTER);

        eastCenterPanel = new JPanel();
        eastCenterPanel.setOpaque(false);
        eastCenterPanel.setLayout(new BorderLayout());


        subHeadPanel = new RPanel (30);
        subHeadPanel.setLayout(new BorderLayout());
        subHeadPanel.setBackground(Color.white);
        subHeadPanel.setPreferredSize(new Dimension(0, 50));
        cardTitle = new JLabel("Dashboard");
        cardTitle.setFont(b22);
        subHeadPanel.add(cardTitle, BorderLayout.WEST);

        subHeadPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 22, 84)), BorderFactory.createEmptyBorder(0, 14, 0, 0)));

        headCenterPanel = new JPanel(new BorderLayout());
        headCenterPanel.setOpaque(false);
        ImageIcon icon = new ImageIcon(new ImageIcon(getClass().getResource("/images/admin/headPanel/logoHeader.png")).getImage());
        JLabel logoHeading = new JLabel(icon);
        logoHeading.setToolTipText("Logo");
        headCenterPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        headCenterPanel.setPreferredSize(new Dimension (0, 70));
        headCenterPanel.add(logoHeading, BorderLayout.WEST);

        // Card panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

       logout = new  BtnCustom("Logout",new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/logout.png")).getImage()), new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/logoutBlue.png")).getImage()), "Logout");
       exit = new BtnCustom("Exit",new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/exit.png")).getImage()), new ImageIcon(new ImageIcon(getClass().getResource("/images/icon/exitBlue.png")).getImage()), "Exit");

        JPanel exitLogOutButtonPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        exitLogOutButtonPanel.setOpaque(false);
        exitLogOutButtonPanel.add(logout);
        exitLogOutButtonPanel.add(exit);
        headCenterPanel.add(exitLogOutButtonPanel, BorderLayout.EAST);

        // Logout Button Action
        logout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to log out?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                // Add logout logic here
                dispose();
                JOptionPane.showMessageDialog(this, "Logout Successfully", "Logout Successful", JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.invokeLater(() -> new LoginGUI());
            }
        });

        // Exit Button Action
        exit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to exit?",
                    "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);  // Prevent the default close action

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to exit?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);  // Exit the program when "Yes" is clicked
                }
                // If "No" is clicked, do nothing (the dialog closes, but the window remains open)
            }
        });
        JPanel eastOuterPanel = new JPanel (new BorderLayout());
        eastOuterPanel.setOpaque(false);
        eastOuterPanel.setBorder(b);
        JScrollPane cardPanelScroller = new CNavScrollPane(cardPanel);
        cardPanelScroller.setOpaque(false);
        cardPanelScroller.getViewport().setOpaque(false);
        cardPanelScroller.setBorder(null);
        mainPanel.add(navPanel, BorderLayout.WEST);
        navPanel.add(navigationPanel, BorderLayout.CENTER);
        navigationPanel.add(cardPanelScroller, BorderLayout.CENTER);
        navigationPanel.add(navHeadPanel, BorderLayout.NORTH);
        navHeadPanel.add(generalPanel, BorderLayout.SOUTH);
        navHeadPanel.add(loggedUserPanel, BorderLayout.CENTER);
        mainPanel.add(eastCenterPanel, BorderLayout.CENTER);
        eastCenterPanel.add(headCenterPanel, BorderLayout.NORTH);

        eastPanel = new RPanel (30);
        eastPanel.setBorder(b);
        eastPanel.setLayout(new BorderLayout());
        eastPanel.add (subHeadPanel, BorderLayout. NORTH) ;
        eastOuterPanel.add(eastPanel, BorderLayout.CENTER);
        eastCenterPanel.add(eastOuterPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

}
