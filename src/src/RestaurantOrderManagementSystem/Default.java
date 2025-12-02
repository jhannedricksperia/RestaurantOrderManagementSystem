package RestaurantOrderManagementSystem;
// Default JFrame class

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Default {

    public Default(String title) {

    }
}

class BtnDefault extends JButton implements MouseListener {

    protected ImageIcon image1;
    protected ImageIcon image2;
    ImageIcon currentImage;

    public BtnDefault(String text) {
        super(text);
        this.setFont(Components.b18);
        this.setForeground(new Color(240, 240, 240));
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        setBackgroundImages();
        this.addMouseListener(this);
    }

    public BtnDefault(String text, ImageIcon icon) {
        this(text);
        this.setIcon(icon);
    }

    public void setBackgroundImages() {
        this.image1 = new ImageIcon(getClass().getResource("/images/buttonBackground/orange.jpg"));
        this.image2 = new ImageIcon(getClass().getResource("/images/buttonBackground/orange2.jpg"));
        this.currentImage = image1;
        setBackgroundImage(currentImage);
    }

    protected void setBackgroundImage(ImageIcon backgroundImage) {
        this.currentImage = backgroundImage;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Make smoother edges
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded rectangle background image
        if (currentImage != null) {
            Shape clip = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20); // 30 = corner radius
            g2.setClip(clip);
            g2.drawImage(currentImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }

        g2.dispose();
        super.paintComponent(g); // Paint the text on top
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackgroundImage(image2);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBackgroundImage(image1);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}

class BtnMenuCategory extends JButton implements MouseListener {

    private boolean hovered = false;
    private boolean selected = false;

    public BtnMenuCategory(String text) {
        super(text);
        this.setFont(Components.p16);
        this.setForeground(Color.black);
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setOpaque(false);
        this.addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int borderInset = 1;
        Shape roundRect = new RoundRectangle2D.Float(borderInset, borderInset, getWidth() - 2 * borderInset, getHeight() - 2 * borderInset, 20, 20);

        if (hovered || selected) {
            g2.setColor(new Color(252, 218, 187)); // light orange
            g2.fill(roundRect);
            g2.setColor(new Color(255, 120, 0)); // dark orange
            g2.setStroke(new BasicStroke(1));
            g2.draw(roundRect);
            setFont(Components.b16);
            setForeground(new Color(232, 112, 5)); // orange text
        } else {
            setFont(Components.p16);
            setForeground(Color.BLACK);
        }

        g2.dispose();
        super.paintComponent(g);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        hovered = true;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        hovered = false;
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}

class BtnNav extends JButton implements MouseListener {

    protected ImageIcon image1; // Default image (transparent)
    protected ImageIcon image2; // Image when clicked
    ImageIcon currentImage;
    boolean isClicked = false; // Track whether the button was clicked

    private static BtnNav lastClickedButton = null;

    private final int cornerRadius = 30;

    public BtnNav(String text, ImageIcon icon) {
        super(text);
        this.setFont(Components.p16);
        this.setForeground(Color.BLACK);
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.image1 = null;
        this.image2 = new ImageIcon(getClass().getResource("/images/buttonBackground/btnnav.jpg"));
        this.currentImage = image1;
        setBackgroundImage(currentImage);

        this.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10)); // Top, Left, Bottom, Right
        this.setHorizontalAlignment(SwingConstants.LEFT);
        this.setHorizontalTextPosition(SwingConstants.RIGHT);
        this.setIcon(icon);
        this.setIconTextGap(10); // Space between icon and text
        this.addMouseListener(this);
    }

    protected void setBackgroundImage(ImageIcon backgroundImage) {
        this.currentImage = backgroundImage;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Enable antialiasing for smooth corners
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background image with rounded corners
        if (currentImage != null) {
            g2.setClip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
            g2.drawImage(currentImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Optionally paint a rounded border here if needed
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (!isClicked) {
            setBackgroundImage(image2);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (!isClicked) {
            setBackgroundImage(image1);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (lastClickedButton != null && lastClickedButton != this) {
            lastClickedButton.setBackgroundImage(lastClickedButton.image1);
            lastClickedButton.isClicked = false;
        }

        setBackgroundImage(image2);
        isClicked = true;
        lastClickedButton = this;
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
}


class BtnCustom extends JButton implements MouseListener {

    protected ImageIcon image1;
    protected ImageIcon image2;
    protected ImageIcon currentImage;

    public BtnCustom(ImageIcon image1, ImageIcon image2, String message) {
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setToolTipText(message);
        this.image1 = image1;
        this.image2 = image2;
        this.currentImage = image1;

        setBackgroundImage(currentImage); // Set initial image
        addMouseListener(this);
    }
    public BtnCustom(String title, ImageIcon image1, ImageIcon image2, String message) {
        this(image1, image2,message);
        setText(title);
        setFont(Components.p14);
        setForeground (Color.black);
    }
    

    // Set the background image
    protected void setBackgroundImage(ImageIcon backgroundImage) {
        this.currentImage = backgroundImage;
        setIcon(currentImage); // ✅ Important: Set the button's icon
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackgroundImage(image2);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBackgroundImage(image1);
    }

    // Unused but required
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}

class BtnRed extends BtnDefault {

    public BtnRed(String text) {
        super(text);
    }

    public BtnRed(String text, ImageIcon icon) {
        this(text);
        this.setIcon(icon);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackgroundImage(image2); // Change to image2 on hover
    }
    // Set the background images for mouse entered and exited

    @Override
    public void setBackgroundImages() {
        this.image1 = new ImageIcon(getClass().getResource("/images/buttonBackground/red.jpg"));
        this.image2 = new ImageIcon(getClass().getResource("/images/buttonBackground/red1.jpg"));
        this.currentImage = image1; // Default to image1
        setBackgroundImage(currentImage); // Set initial background image
    }
}

class BtnGreen extends BtnDefault {

    public BtnGreen(String text) {
        super(text);
    }

    public BtnGreen(String text, ImageIcon icon) {
        this(text);
        this.setIcon(icon);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackgroundImage(image2); // Change to image2 on hover
    }
    // Set the background images for mouse entered and exited

    @Override
    public void setBackgroundImages() {
        this.image1 = new ImageIcon(getClass().getResource("/images/buttonBackground/green.jpg"));
        this.image2 = new ImageIcon(getClass().getResource("/images/buttonBackground/green1.jpg"));
        this.currentImage = image1; // Default to image1
        setBackgroundImage(currentImage); // Set initial background image
    }
}

class BtnMagenta extends BtnDefault {

    public BtnMagenta(String text) {
        super(text);
    }

    public BtnMagenta(String text, ImageIcon icon) {
        this(text);
        this.setIcon(icon);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackgroundImage(image2); // Change to image2 on hover
    }
    // Set the background images for mouse entered and exited

    @Override
    public void setBackgroundImages() {
        this.image1 = new ImageIcon(getClass().getResource("/images/buttonBackground/magenta.jpg"));
        this.image2 = new ImageIcon(getClass().getResource("/images/buttonBackground/magenta1.jpg"));
        this.currentImage = image1; // Default to image1
        setBackgroundImage(currentImage); // Set initial background image
    }
}

class BtnBlue extends BtnDefault {

    public BtnBlue(String text) {
        super(text);
    }

    public BtnBlue(String text, ImageIcon icon) {
        this(text);
        this.setIcon(icon);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackgroundImage(image2); // Change to image2 on hover
    }
    // Set the background images for mouse entered and exited

    @Override
    public void setBackgroundImages() {
        this.image1 = new ImageIcon(getClass().getResource("/images/buttonBackground/blue2.jpg"));
        this.image2 = new ImageIcon(getClass().getResource("/images/buttonBackground/blue1.jpg"));
        this.currentImage = image1; // Default to image1
        setBackgroundImage(currentImage); // Set initial background image
    }
}

class MenuItemCustom extends JMenuItem {

    public MenuItemCustom(String text) {
        super(text);
        this.setFont(Components.p16);
        this.setBackground(Components.ashBlue);
        this.setForeground(Components.ashWhite);
    }
}

class Components extends JFrame {

    public static ImageIcon logoIcon;
    public static Image l;
    public static ImageIcon resizedIcon;
    public static JLabel logo;

    public static Image l2;
    public static ImageIcon resizedIcon2;
    public static JLabel logo2;
    public static Color lOrange = new Color(251, 133, 0);
    public static Color ashRed = new Color(204, 0, 0);
    public static Color gray = new Color(233, 236, 239);
    public static Color ashGreen = new Color(0, 153, 0);//Background
    public static Color black = new Color(0, 0, 0);//Background
    public static Color ashWhite = new Color(240, 240, 240);//Background
    public static Color ashWhite2 = new Color(235, 235, 235);//darker ash white
    public static Color ashBlue = new Color(0, 49, 122);//ashBlue
    public static Color orange = new Color(221, 110, 66);//Buttons
    public static Color whiteBlue = new Color(136, 162, 173);//White Blue
    public static Color lWhiteBlue = new Color(213, 224, 229);//Lighter White Blue
    public static Color ashMagenta = new Color(138, 40, 70);//Ash Magenta
    public static Color dGray = new Color(102, 102, 102);//Dark Gray
    public static Color lGray = new Color(220, 220, 220);//Light Gray
    public static Border b = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    public static Border bF = BorderFactory.createEmptyBorder(2, 2, 2, 2);
    public static Border borderLine = BorderFactory.createLineBorder(Color.black, 1);
    public static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
    public static SimpleDateFormat sdfMDY = new SimpleDateFormat("MM-dd-yyyy");
    public static SimpleDateFormat sdfT = new SimpleDateFormat("hh:mm a");

    public static Font p10 = new Font("Montserrat", Font.PLAIN, 10);
    public static Font b10 = new Font("Montserrat", Font.BOLD, 10);
    
    public static Font p11 = new Font("Montserrat", Font.PLAIN, 11);

    public static Font p12 = new Font("Montserrat", Font.PLAIN, 12);
    public static Font c12 = new Font("Consolas", Font.PLAIN, 12);
    public static Font b12 = new Font("Montserrat", Font.BOLD, 12);

    public static Font p13 = new Font("Montserrat", Font.PLAIN, 13);
    public static Font b13 = new Font("Montserrat", Font.BOLD, 13);

    public static Font p14 = new Font("Montserrat", Font.PLAIN, 14);
    public static Font b14 = new Font("Montserrat", Font.BOLD, 14);

    public static Font p16 = new Font("Montserrat", Font.PLAIN, 16);
    public static Font b16 = new Font("Montserrat", Font.BOLD, 16);

    public static Font p18 = new Font("Montserrat", Font.PLAIN, 18);
    public static Font b18 = new Font("Montserrat", Font.BOLD, 18);

    public static Font p20 = new Font("Montserrat", Font.PLAIN, 20);
    public static Font b20 = new Font("Montserrat", Font.BOLD, 20);

    public static Font p24 = new Font("Montserrat", Font.PLAIN, 24);
    public static Font b24 = new Font("Montserrat", Font.BOLD, 24);

    public static Font b22 = new Font("Montserrat", Font.BOLD, 22);
    public static Font p30 = new Font("Montserrat", Font.PLAIN, 30);
    public static Font b30 = new Font("Montserrat", Font.BOLD, 30);
    public static Font p26 = new Font("Montserrat", Font.PLAIN, 26);
    public static Font b26 = new Font("Montserrat", Font.BOLD, 26);

    public static Font p35 = new Font("Montserrat", Font.PLAIN, 35);
    public static Font b35 = new Font("Montserrat", Font.BOLD, 35);

    public static Font ca45 = new Font("Cal Sans", Font.BOLD, 45);
    public static Font ca50 = new Font("Cal Sans", Font.BOLD, 50);
    public static Font p45 = new Font("Montserrat", Font.PLAIN, 45);
    public static Font b45 = new Font("Montserrat", Font.BOLD, 45);

    public static Font b75 = new Font("Montserrat", Font.BOLD, 75);
    JLabel clockLabel; // Clock displayW

    public Components() {
        //CUSTOM HEADER
        logoIcon = new ImageIcon(getClass().getResource("/images/logo/logo.png")); // Load image
        l = logoIcon.getImage().getScaledInstance(120, 70, Image.SCALE_SMOOTH); // Resize to 100x100
        resizedIcon = new ImageIcon(l); // Convert back to ImageIcon
        logo = new JLabel(resizedIcon);

        l2 = logoIcon.getImage().getScaledInstance(230, 120, Image.SCALE_SMOOTH); // Resize to 100x100
        resizedIcon2 = new ImageIcon(l2); // Convert back to ImageIcon
        logo2 = new JLabel(resizedIcon2);

    }

    // Method to update clock display
    protected void updateClock() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - hh:mm:ss a"); // Date + Time
        clockLabel.setText(sdf.format(new Date())); // Update clock label
    }

    public static String convertLocalDateTimeToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM-dd-yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    public static String convertLocalDateToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM-dd-yyyy");
        return dateTime.format(formatter);
    }

    public static JTable updateTable(JTable table) {
        // Set custom header renderer
        table.getTableHeader().setDefaultRenderer(new CustomHeaderRenderer());
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);

        // Set custom row renderer
        DefaultTableCellRenderer rowRenderer = new CustomRowRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
        }

        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        return table;
    }

    public static JTable updateOrderTable(JTable table) {
        // Set custom header renderer
        CustomHeaderRenderer c = new CustomHeaderRenderer();
        c.setFont(p12);
        table.getTableHeader().setDefaultRenderer(c);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);

        // Set custom row renderer
        CustomRowRenderer rowRenderer = new CustomRowRenderer();
        rowRenderer.setFont(p12);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
        }

        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        return table;
    }

    public static JPanel setReportheader(String title, String dateFrom, String dateTo) {

        JPanel reportHeader = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("PAT'Z RESTAURANT - " + title, SwingConstants.CENTER);
        heading.setFont(b18);

        reportHeader.add(heading, BorderLayout.NORTH);

        LocalDateTime date = LocalDateTime.now();
        JLabel dateTimeLabel = new JLabel("Date Today: " + Components.convertLocalDateTimeToString(date));
        dateTimeLabel.setFont(p14);
        reportHeader.add(dateTimeLabel, BorderLayout.WEST);

        JLabel dateRangeLabel = new JLabel("Date Range:" + dateFrom + " - " + dateTo);
        dateRangeLabel.setFont(p14);
        reportHeader.add(dateRangeLabel, BorderLayout.EAST);

        reportHeader.setPreferredSize(new Dimension(0, 70));
        reportHeader.setBorder(b);
        return reportHeader;
    }

    public void displaySplash() {
        JProgressBar prog = new JProgressBar(0, 100);
        prog.setPreferredSize(new Dimension(200, 10));
        prog.setForeground(new Color(255, 105, 5));
        prog.setStringPainted(false);
        prog.setValue(0);

        JWindow splash = new JWindow();

        // Load splash background
        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/images/splash/splashBackground.png"));
        JPanel background = new BackgroundPanel(backgroundIcon.getImage());
        background.setLayout(new BorderLayout());

        splash.getContentPane().setLayout(new BorderLayout());
        splash.getContentPane().add(background, BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 0;

        // Determine content based on type
        JLabel contentLabel;

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/logo/logo.png")); // Adjust path as needed
        contentLabel = new JLabel(new ImageIcon(logoIcon.getImage().getScaledInstance(160, 95, Image.SCALE_SMOOTH)));

        panel.add(contentLabel, gbc);
        gbc.gridy = 1;
        panel.add(prog, gbc);
        background.add(panel, BorderLayout.CENTER);

        splash.setSize(400, 250);
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);

        for (int i = 0; i <= 100; i++) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException a) {
                a.printStackTrace();
            }
            prog.setValue(i);
        }

        try {
            Thread.sleep(70);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        splash.dispose();
    }
}

class BackgroundPanel extends JPanel {

    private Image backgroundImage;

    public BackgroundPanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image stretched to fill the panel
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}

class DisabledTableModel extends DefaultTableModel {

    public DisabledTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);

    }

    public DisabledTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);

    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // Prevents editing of all cells
    }
}

class CustomHeaderRenderer extends DefaultTableCellRenderer {

    public CustomHeaderRenderer() {
        setOpaque(true); // Important: allows background color to show
        setBackground(Color.GRAY); // Set background to gray
        setForeground(Color.WHITE); // Text color
        setHorizontalAlignment(CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setFont(Components.b14);
        return this;
    }
}

class CustomRowRenderer extends DefaultTableCellRenderer {

    private Color evenRowColor = Color.WHITE;
    private Color oddRowColor = new Color(240, 240, 240); // Light gray
    private Color selectedRowColor = new Color(62, 201, 20); // Light green

    public CustomRowRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        setFont(Components.p14);
        setForeground(Color.BLACK);

        if (isSelected) {
            setBackground(selectedRowColor);
            setFont(Components.b14);
            setForeground(Color.WHITE);
        } else {
            if (row % 2 == 0) {
                setBackground(evenRowColor);
            } else {
                setBackground(oddRowColor);
            }
        }

        // Add a single line (underline) under each cell
        setBorder(new MatteBorder(0, 0, 1, 0, new Color(200, 200, 200))); // light gray underline

        return this;
    }
}

class DateTimeSpinner extends JSpinner {

    public DateTimeSpinner() {
        super(new SpinnerDateModel());

        // Set the editor to display date and time in AM/PM format
        JSpinner.DateEditor editor = setEditor();
        this.setEditor(editor);

        // Change font to P18
        editor.getTextField().setFont(Components.p14);

        // Set default value to current date and time
        this.setValue(new Date());

        // Change arrow button color to orange
        for (Component component : this.getComponents()) {
            if (component instanceof JButton) {
                component.setBackground(Components.ashWhite);
            }
        }
    }

    public String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
        return sdf.format((Date) this.getValue());
    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format((Date) this.getValue());
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format((Date) this.getValue());
    }

    protected JSpinner.DateEditor setEditor() {
        return new JSpinner.DateEditor(this, "yyyy/MM/dd hh:mm a");
    }

//        public static void main(String[] args) {
//        JFrame frame = new JFrame("DateTime Spinner Test");
//        frame.setLayout(new FlowLayout());
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(300, 150);
//
//        DateTimeSpinner spinner = new DateSpinner();
//        frame.add(spinner);
//        
//        JButton b = new JButton();
//        frame.add(b);
//        b.addActionListener((ActionEvent e) -> {
//            System.out.println(spinner.getTime());
////        });
//            
//        
//        frame.setVisible(true);
//    }
}

class DateSpinner extends DateTimeSpinner {

    public DateSpinner() {
        super();
    }

    @Override
    public String getDateTime() {
        return null;
    }

    @Override
    public String getTime() {
        return null;
    }

    @Override
    protected JSpinner.DateEditor setEditor() {
        return new JSpinner.DateEditor(this, "yyyy/MM/dd");
    }
}

class TimeSpinner extends DateTimeSpinner {

    public TimeSpinner() {
        super();
    }

    @Override
    public String getDateTime() {
        return null;
    }

    @Override
    public String getDate() {
        return null;
    }

    @Override
    protected JSpinner.DateEditor setEditor() {
        return new JSpinner.DateEditor(this, "hh:mm a");
    }

}

class CustomComboBox<E> extends JComboBox<E> {

    public CustomComboBox(E[] items) {
        super(items);
        // Set default background color
        setBackground(Color.white);
        setFont(Components.p14);
    }

    public CustomComboBox() {
        super();
        // Set default background color
        setBackground(Color.white);
        setFont(Components.p14);
    }
}

class ReportDateForm extends Components {

    protected DateTimeSpinner dateFromSpinner;
    protected DateTimeSpinner dateToSpinner;
    protected JButton generateReportButton;

    public ReportDateForm(String title) {
        setTitle(title);
        setSize(400, 270);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(false);
        JPanel maxPanel = new JPanel(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(b);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // Title Label
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(b18);
        titleLabel.setForeground(ashWhite);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // Date From Label
        JPanel fr = new JPanel(new GridBagLayout());
        JLabel dateFromLabel = new JLabel("Date From:");
        dateFromLabel.setFont(p14);
        fr.add(dateFromLabel);
        mainPanel.add(fr, gbc);

        gbc.gridx = 1;
        dateFromSpinner = new DateSpinner();
        mainPanel.add(dateFromSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        // Date To Label
        JPanel to = new JPanel(new GridBagLayout());
        JLabel dateToLabel = new JLabel("Date To:");
        dateToLabel.setFont(p14);
        to.add(dateToLabel);
        mainPanel.add(to, gbc);

        gbc.gridx = 1;
        dateToSpinner = new DateSpinner();
        mainPanel.add(dateToSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        Dimension btnSize = new Dimension(120, 70);
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        btnPanel.setBorder(b);
        // Generate Report Button
        generateReportButton = new BtnDefault("RETRIEVE");
        generateReportButton.setPreferredSize(btnSize);
        btnPanel.add(generateReportButton);
        JButton cancelButton = new BtnDefault("CANCEL");
        cancelButton.setPreferredSize(btnSize);
        btnPanel.add(cancelButton);
        mainPanel.add(btnPanel, gbc);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(orange);

        header.setPreferredSize(new Dimension(0, 50));
        header.add(titleLabel, BorderLayout.CENTER);
        maxPanel.add(mainPanel, BorderLayout.CENTER);
        maxPanel.add(header, BorderLayout.NORTH);
        add(maxPanel, BorderLayout.CENTER);

        // Button Action Listener
        generateReportButton.addActionListener(e -> {
            generateReport();
        });

        cancelButton.addActionListener(e -> {
            dispose();
        });
        setVisible(true);
    }

    public void setAction() {

    }

    public void generateReport() {
        try {
            Date dateFrom = new Date(dateFromSpinner.getDate());
            Date dateTo = new Date(dateToSpinner.getDate());
            Date today = new Date();

            if (dateFrom.after(dateTo)) {
                JOptionPane.showMessageDialog(this, "Error: 'Date From' cannot be after 'Date To'.", "Invalid Date Range", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (dateFrom.after(today) || dateTo.after(today)) {
                JOptionPane.showMessageDialog(this, "Error: Dates cannot be in the future.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
                return;
            }

            setAction();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: Please select a valid date.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected String getDateFrom() {
        return dateFromSpinner.getDate();
    }

    protected String getDateTo() {
        return dateToSpinner.getDate();
    }

}

class CScrollPane extends JScrollPane {

    public CScrollPane() {
        super();
        setupScrollBars();
        forceScrollBarAsNeeded();
    }

    public CScrollPane(Component view) {
        super(view);
        setupScrollBars();
        forceScrollBarAsNeeded();
    }

    private void forceScrollBarAsNeeded() {
        super.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        super.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    @Override
    public void setHorizontalScrollBarPolicy(int policy) {
        super.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        super.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    @Override
    public void setVerticalScrollBarPolicy(int policy) {
        super.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        super.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    private void setupScrollBars() {
        Color thumb = Components.dGray;
        Color track = Components.lGray;

        JScrollBar vertical = getVerticalScrollBar();
        vertical.setUI(new BasicScrollBarUI() {

            @Override
            protected void configureScrollBarColors() {
                thumbColor = thumb;
                trackColor = track;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton(); // No arrow
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton(); // No arrow
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (!c.isEnabled() || thumbBounds.isEmpty()) {
                    return;
                }

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(thumbColor);

                int arc = 12; // Rounded corner radius
                int padding = 2; // Space between thumb and track

                // Shrink the bounds for visual padding
                int x = thumbBounds.x + padding;
                int y = thumbBounds.y + padding;
                int width = thumbBounds.width - padding * 2;
                int height = thumbBounds.height - padding * 2;

                g2.fillRoundRect(x, y, width, height, arc, arc);

                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(trackColor);
                g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize(JComponent c) {
                return new Dimension(12, Integer.MAX_VALUE);
            }
        });
    }

}

class CNavScrollPane extends JScrollPane {

    public CNavScrollPane() {
        super();
        setupScrollBars();
        forceScrollBarAsNeeded();
    }

    public CNavScrollPane(Component view) {
        super(view);
        setupScrollBars();
        forceScrollBarAsNeeded();
    }

    private void forceScrollBarAsNeeded() {
        super.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        super.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
    }

    @Override
    public void setVerticalScrollBarPolicy(int policy) {
        super.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        super.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
    }

    private void setupScrollBars() {
        Color thumb = Components.dGray;
        Color track = Components.lGray;

        JScrollBar vertical = getVerticalScrollBar();
        vertical.setUI(new BasicScrollBarUI() {

            @Override
            protected void configureScrollBarColors() {
                thumbColor = thumb;
                trackColor = track;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton(); // No arrow
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton(); // No arrow
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (!c.isEnabled() || thumbBounds.isEmpty()) {
                    return;
                }

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(thumbColor);

                int arc = 12; // Rounded corner radius
                int padding = 2; // Space between thumb and track

                // Shrink the bounds for visual padding
                int x = thumbBounds.x + padding;
                int y = thumbBounds.y + padding;
                int width = thumbBounds.width - padding * 2;
                int height = thumbBounds.height - padding * 2;

                g2.fillRoundRect(x, y, width, height, arc, arc);

                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(trackColor);
                g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize(JComponent c) {
                return new Dimension(12, Integer.MAX_VALUE);
            }
        });
    }

}

class PTextField extends JTextField {

    private String placeholder;

    public PTextField(String placeholder) {
        super(placeholder);
        this.placeholder = placeholder;

        // Set placeholder text as the default text
        setForeground(Color.GRAY);

        // FocusListener to clear placeholder text when clicked
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(placeholder)) {
                    setText("");
                    setForeground(Color.BLACK);  // Change text color when typing
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(placeholder);
                    setForeground(Color.GRAY);  // Change color back to gray for placeholder
                }
            }
        });

    }

    public PTextField() {
        super();
    }

    public void setDefault() {
        setText(placeholder);
        setForeground(Color.GRAY);
    }

    public void setPlaceHolder(String placeholder) {
        this.placeholder = placeholder;
    }
}

class COptionPane extends JOptionPane {

    public COptionPane() {
        // Set custom styling using Components
        UIManager.put("OptionPane.background", Components.ashWhite);
        UIManager.put("Panel.background", Components.ashWhite);
        UIManager.put("OptionPane.messageFont", Components.p12);
        UIManager.put("OptionPane.buttonFont", Components.b12);
        UIManager.put("Button.background", Components.ashBlue);
        UIManager.put("Button.foreground", Components.ashWhite);

        UIManager.put("ComboBox.background", Components.ashWhite);  // Dropdown background
        UIManager.put("ComboBox.foreground", Color.BLACK);  // Text color
        UIManager.put("ComboBox.font", Components.p12);             // Font
        UIManager.put("ComboBox.selectionBackground", Components.ashGreen); // When selecting
        UIManager.put("ComboBox.selectionForeground", Components.ashWhite); // Text when selecting
    }
//TEST
//    public static void main(String[] args) {
//      
//        // Apply custom styles by creating an instance
//        new COptionPane();
//
//        // Show a sample dialog to test appearance
//        JOptionPane.showInputDialog(
//            null,
//            "This is a custom styled COptionPane!",
//            "Test Dialog",
//            JOptionPane.INFORMATION_MESSAGE
//        );
//    }
}

class SeatCapacitySpinner extends JSpinner {

    public SeatCapacitySpinner() {
        // Set spinner model: initial value = 2, min = 2, max = 12, step = 2
        super(new SpinnerNumberModel(2, 2, 12, 2));

        // Set the editor to display numeric values
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(this, "#");
        this.setEditor(editor);

        // Change font to P14
        editor.getTextField().setFont(Components.p14);

        // Change arrow button color to orange (assuming ashWhite is an orange color in your palette)
        for (Component component : this.getComponents()) {
            if (component instanceof JButton) {
                component.setBackground(Components.ashWhite);
            }
        }
    }
}

class CTextField extends PTextField {

    public CTextField(String title) {
        super(title);
        setOpaque(false); // Make the background transparent
        setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK)); // Only bottom border
    }

}

class CPasswordField extends JPasswordField {

    private String placeholder;
    private boolean showingPlaceholder;

    public CPasswordField(String placeholder) {
        super();
        this.placeholder = placeholder;
        this.showingPlaceholder = true;

        setOpaque(false);
        setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
        setForeground(Color.GRAY);

        // Set placeholder initially
        setEchoChar((char) 0);
        setText(placeholder);

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder) {
                    setText("");
                    setForeground(Color.BLACK);
                    setEchoChar('•'); // typical password bullet
                    showingPlaceholder = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getPassword().length == 0) {
                    setText(placeholder);
                    setForeground(Color.GRAY);
                    setEchoChar((char) 0); // Show text instead of hiding it
                    showingPlaceholder = true;
                }
            }
        });
    }

    @Override
    public char[] getPassword() {
        if (showingPlaceholder) {
            return new char[0];
        }
        return super.getPassword();
    }
}

class RPasswordField extends CPasswordField {

    private final int arc;

    public RPasswordField(int arc) {
        super("");
        this.arc = arc;
        setOpaque(false); // Allow custom painting
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Optional: inner padding
    }

    public RPasswordField(String placeholder, int arc) {
        super(placeholder);
        this.arc = arc;
        setOpaque(false); // Allow custom painting
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Optional: inner padding
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background fill
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        super.paintComponent(g); // Paints the text
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.LIGHT_GRAY); // Light gray border
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

        g2.dispose();
    }
}

class PanelButton extends JPanel {

    private Color defaultColor = Color.white;
    private Color hoverColor = new Color(220, 220, 220);
    private Color pressedColor = new Color(200, 200, 200);
    private Color currentColor = defaultColor;
    private JTextArea label;
    private Runnable onClick;
    private double price;
    private int id, nServing;
    private String status;
    private final int arc = 20; // Rounded corners
    private String indicator;
    private JLabel picture;

    public PanelButton(String indicator, String category, JLabel picture, String text, int id, double price, String status, int nServing) {
        this.price = price;
        this.id = id;
        this.nServing = nServing;
        this.status = status;
        this.indicator = indicator;
        this.picture = picture;

        setLayout(new GridBagLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(180, 300));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(2, 2, 2, 2);

        gbc.weightx = 1.0;

        //Item Picture
        gbc.gridy = 0;
        JPanel pictureFrame = new JPanel(new BorderLayout());
        pictureFrame.setOpaque(false);
        pictureFrame.add(picture, BorderLayout.CENTER);
        pictureFrame.setPreferredSize(new Dimension(100, 100));
        add(pictureFrame, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Description Label
        gbc.gridy++;
        gbc.weighty = 0; // reset
        label = new JTextArea(text);
        label.setFont(Components.b14);
        label.setForeground(Color.DARK_GRAY);
        label.setWrapStyleWord(true);
        label.setLineWrap(true);
        label.setEditable(false);
        label.setOpaque(false);
        label.setFocusable(false);
        label.setHighlighter(null);
        label.setMargin(new Insets(2, 2, 2, 2));
        add(label, gbc);

        gbc.gridy++;
        add(new JLabel(""), gbc);

        gbc.gridy++;
        add(new JLabel(""), gbc);

        gbc.gridy++;
        add(new JLabel(""), gbc);
        // Price Label
        gbc.gridy++;
        JLabel priceLbl = new JLabel("P" + String.format("%.2f", price));
        priceLbl.setFont(Components.b13);
        priceLbl.setForeground(Components.lOrange);
        add(priceLbl, gbc);

        gbc.gridy++;
        add(new JLabel(""), gbc);
        gbc.gridy++;
        add(new JLabel(""), gbc);
        gbc.gridy++;
        add(new JLabel(""), gbc);

        // Status Label
        gbc.gridy++;
        JLabel statusLbl = new JLabel(status.toUpperCase());
        statusLbl.setFont(Components.b13);
        statusLbl.setForeground(status.equalsIgnoreCase("AVAILABLE") ? Components.ashGreen : Components.dGray);
        add(statusLbl, gbc);

        if (indicator.equalsIgnoreCase("search")) {

            gbc.gridy++;
            add(new JLabel(""), gbc);
            gbc.gridy++;
            add(new JLabel(""), gbc);
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.gridy++;
            JPanel categoryPanel = new RPanel(20);
            categoryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            categoryPanel.setPreferredSize(new Dimension(110, 30));
            categoryPanel.setBackground(new Color(255, 116, 0));
            categoryPanel.setLayout(new BorderLayout());
            JLabel categoryLbl = new JLabel(category, SwingConstants.CENTER);
            categoryLbl.setFont(Components.b14);
            categoryLbl.setForeground(Color.white);
            categoryPanel.add(categoryLbl, BorderLayout.CENTER);
            add(categoryPanel, gbc);
        }

        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                currentColor = hoverColor;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                currentColor = defaultColor;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                currentColor = pressedColor;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentColor = hoverColor;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClick != null) {
                    onClick.run();
                }
            }
        });
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    public void setButtonText(String text) {
        label.setText(text);
    }

    public String getButtonText() {
        return label.getText();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int width = getWidth();
        int height = getHeight();

        // Enable anti-aliasing for smooth corners
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw soft shadow (embossed look)
        g2.setColor(new Color(200, 200, 200, 100)); // light outer shadow
        g2.fillRoundRect(2, 2, width - 4, height - 4, arc, arc);

        // Draw main button shape
        g2.setColor(currentColor);
        g2.fillRoundRect(0, 0, width - 4, height - 4, arc, arc);

        g2.dispose();
        super.paintComponent(g); // Keep JLabel rendering
    }
}

class RTextField extends PTextField {

    private final int arc;

    public RTextField(int arc) {
        super();
        this.arc = arc;
        setOpaque(false); // Allow custom painting
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Optional: inner padding
    }

    public RTextField(String placeholder, int arc) {
        super(placeholder);
        this.arc = arc;
        setOpaque(false); // Allow custom painting
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Optional: inner padding
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background fill
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        super.paintComponent(g); // Paints the text
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.LIGHT_GRAY); // Light gray border
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

        g2.dispose();
    }

}

class RPanel extends JPanel {

    private final int arc;
    private Color border = Color.LIGHT_GRAY;

    public RPanel(int arc) {
        this.arc = arc;
        setOpaque(false); // Needed for transparency
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape round = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc);
        g2.setColor(getBackground());
        g2.fill(round);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(border); // Light gray border
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

        g2.dispose();
    }

    public void setBorderColor(Color color) {
        this.border = color;
    }
}
