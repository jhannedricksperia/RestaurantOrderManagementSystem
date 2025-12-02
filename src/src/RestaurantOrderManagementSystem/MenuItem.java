//Package Name
package RestaurantOrderManagementSystem;

/*
This class describes the menu items that will be ordered by the customers.
This references the OrderItem.
 */
public class MenuItem {

    private int menuItemID;
    private String name;
    private double unitPrice;
    private int nServing;
    private String status;
    private int menuItemCategoryID;

    public MenuItem(int menuItemID, String name, double unitPrice, int nServing, String status, int menuItemCategoryID) {
        this.menuItemID = menuItemID;
        this.name = name;
        this.unitPrice = unitPrice;
        this.nServing = nServing;
        this.status = status;
        this.menuItemCategoryID = menuItemCategoryID;
    }

    // Getters
    public int getMenuItemID() {
        return menuItemID;
    }

    public String getName() {
        return name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getnServing() {
        return nServing;
    }

    public String getStatus() {
        return status;
    }

    public int getMenuItemCategoryID() {
        return menuItemCategoryID;
    }

    // Setters (if needed)
    public void setName(String name) {
        this.name = name;
    }


    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setnServing(int nServing) {
        this.nServing = nServing;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMenuItemCategoryID(int menuItemCategoryID) {
        this.menuItemCategoryID = menuItemCategoryID;
    }

    @Override
    public String toString() {
        return name + " - ₱" + String.format("%.2f", unitPrice) + " (" + status + ")";
    }
}
