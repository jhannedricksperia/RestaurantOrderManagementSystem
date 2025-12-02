package RestaurantOrderManagementSystem;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class AboutUsGUI extends Components {

    private JPanel aboutUsPanel;

    public AboutUsGUI() {
        aboutUsPanel = new JPanel();
        aboutUsPanel.setLayout(new BorderLayout());
        aboutUsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Restaurant Order Management System 2.0", SwingConstants.CENTER);
        titleLabel.setFont(b26);
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        aboutUsPanel.add(titleLabel, BorderLayout.NORTH);

        JTextPane descLabel = new JTextPane();
        descLabel.setText(
                "The Restaurant Order Management System (ROMS) is a school-based software project\n"
                + "designed to streamline the workflow of restaurant operations.\n\n"
                + "Initially developed using a Command-Line Interface (CLI), the system has evolved\n"
                + "into a full-featured Graphical User Interface (GUI) application built with Java\n"
                + "Swing and JDBC.\n\n"
                + "ROMS manages key functionalities such as customer handling,\n"
                + "order processing, order and sales tracking, and user management.\n\n"
                + "It aims to improve order accuracy, reduce service time,\n"
                + "and enhance customer satisfaction by digitizing the restaurant's day-to-day activities."
        );

        descLabel.setFont(p14); // Your custom font
        descLabel.setEditable(false);
        descLabel.setFocusable(false);
        descLabel.setOpaque(false);  // Transparent background
        descLabel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Apply centered alignment
        StyledDocument doc = descLabel.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        // Add to panel
        aboutUsPanel.add(descLabel, BorderLayout.CENTER);
        JPanel southPanel = new JPanel (new BorderLayout());
        southPanel.setOpaque(false);
        // "The Developers:" label
        JLabel developersLabel = new JLabel("The Developers:", SwingConstants.LEFT);
        developersLabel.setFont(b16); // Adjust the font style as needed
        developersLabel.setBorder(new EmptyBorder(0, 190, 0, 190));
        southPanel.add(developersLabel, BorderLayout.NORTH);  // Place the label before the teamPanel

        // Team members
        JPanel teamPanel = new JPanel();
        teamPanel.setLayout(new GridLayout(5, 1, 20, 20));
        teamPanel.setBorder(new EmptyBorder(0, 190, 0, 190));
        teamPanel.add(createMemberPanel("peria.jpg", "Jhann Edrick S. Peria", "Project Leader, Head GUI Designer, Programmer"));
        teamPanel.add(createMemberPanel("mj.jpg", "MJ Dela R. Peña", "Programmer, Assistant GUI Designer"));
        teamPanel.add(createMemberPanel("pat.jpg", "Maria Patrisha Estrella", "Programmer, Reports Management, Documentation, and Paperworks"));
        teamPanel.add(createMemberPanel("ian.jpg", "James Ian S. Antonio", "Programmer, Assistant GUI Designer"));
        teamPanel.add(createMemberPanel("lheriza.jpg", "Lheriza A. Miranda", "Programmer, Documentation, and Paperworks"));

        JScrollPane scrollPane = new CNavScrollPane(teamPanel);
        scrollPane.setBorder(null);
        southPanel.add(scrollPane, BorderLayout.SOUTH);
        aboutUsPanel.add(southPanel, BorderLayout.SOUTH);
    }

    private JPanel createMemberPanel(String imageName, String name, String role) {
        RPanel panel = new RPanel(30);
        panel.setLayout(new BorderLayout());
        panel.setMaximumSize(new Dimension(600, 100));
        panel.setBorder(new EmptyBorder(10, 0, 10, 0));
        panel.setBackground(Color.WHITE);

        // Load image
        Image img = null;
        try {
            img = new ImageIcon(getClass().getResource("/images/profilePicture/users/" + imageName)).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            System.err.println("Image not found: " + imageName);
        }

        JLabel imgLabel;
        if (img != null) {
            imgLabel = new JLabel(new ImageIcon(img));
        } else {
            imgLabel = new JLabel("No Image");
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imgLabel.setPreferredSize(new Dimension(80, 80));
        }

        imgLabel.setBorder(new EmptyBorder(0, 10, 0, 20));

        // Text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        textPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Labels
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(b16);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(p14);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        roleLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Add labels with vertical glue to center
        textPanel.add(Box.createVerticalGlue());
        textPanel.add(nameLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5))); // space between name and role
        textPanel.add(roleLabel);
        textPanel.add(Box.createVerticalGlue());

        panel.add(imgLabel, BorderLayout.WEST);
        panel.add(textPanel, BorderLayout.CENTER);

        return panel;
    }

    public JPanel getAboutUsGUI() {
        return aboutUsPanel;
    }

}
