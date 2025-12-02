package RestaurantOrderManagementSystem;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        new COptionPane();
        Components c = new Components();
        c.displaySplash();
        SwingUtilities.invokeLater(() -> new LoginGUI());
    }
}



 





