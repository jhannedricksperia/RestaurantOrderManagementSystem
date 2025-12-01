# RestaurantOrderManagementSystem
PAT’Z RESTAURANT: Restaurant Order Management System with GUI and Database

Roles:
- PERIA, JHANN EDRICK S. – Project Leader/Programmer
- ANTONIO, JAMES IAN S. – Programmer
- DELA PENA, MJ - GUI Designer
- ESTRELLA, MARIA PATRISHA - Programmer
- MIRANDA, LHERIZA A. – Programmer

1 - Project Title  
	PAT’Z RESTAURANT: Restaurant Order Management System with GUI and Database

2 - Project Description  
	This system allows users to create and manage records for customers and staff, record food orders, generate bills, accept payments, and store transactions in a database. It provides a responsive GUI and integrates Java with SQL Server for seamless order and record management.

3 - Group Members  
	- Peria, Jhann Edrick S. – Project Leader, Head GUI Designer, Programmer  
	- Antonio, James Ian S. – Programmer, Assistant GUI Designer  
	- Dela Pena, MJ R. – Assistant GUI Designer  
	- Estrella, Maria Patrisha – Reports Management, Documentation  
	- Miranda, Lheriza A. – Programmer, Documentation  

4 - Features & Functionalities  
	1. Login System
	2. Order Management and Transaction System
	3. Order Monitoring System
	4. Billing System
	5. Payment Processing
	6. Discount System
	7. Transaction Records
	8. Data Retrieval System
	9. Menu Management System
	10. CRUD Operations
	11. Report Generation
	12. GUI-Based Interface
	13. Java Application with SQL Integration

5 - Technologies Used  
	- Java JDK 23  
	- Java Swing  
	- Java AWT  
	- Microsoft SQL Server 2022  
	- JDBC for database connection  
	- Apache NetBeans IDE 2023  

6 - Installation & Setup Instructions  
	1. Install JDK 23 or later.  
	2. Install Microsoft SQL Server 2022.  
		- Enable SQL Server Authentication.  
		- Allow TCP/IP connections and open port 1433 in firewall.  
	3. Create a user login in SQL Server named `admin` with password `password`.  
	4. Download the `RestaurantOrderManagementSystem` project folder.  
		- Ensure it contains the `nbproject` and `Restaurant Management System` folders.  
	5. Inside the `Restaurant Management System` folder, verify these subfolders:  
		- `database`  
		- `RestaurantOrderManagementSystem`  
	6. Open SQL Server Management Studio (SSMS):  
		- Connect using your admin credentials.  
		- Navigate to the `Database` folder in the project.  
		- Open and run `RMS.sql` file statement by statement (F5) to create the database and insert sample data. 
	7. Go to src/fonts folder
	8. Select all the fonts or CTRL + A, then right click and click install. This will install the necessary fonts for the program. 
	9. Open the project in NetBeans 2023.  
	10. Import the project folder.  
	11. Add the JDBC Driver:  
		- Right-click Libraries > Add JAR/Folder > Select your JDBC driver.  
	12. Run `Main.java` to launch the system.


7 - Database Configuration  
	7 - Database Configuration  
	- Database Name: `RestaurantOrderManagementSystem`

	- Table Structures:
		• USER_
			- UserID (PK), FName, MName, LName, CNumber, Email, Uname (unique), Password, Role, SecurityKey (unique), UStatus, ProfilePicture
		• CUSTOMER
			- CustomerID (PK), FName, MName, LName, CNumber
		• MENUITEMCATEGORY
			- MICatID (PK), CatDes
		• ORDER_
			- OrderID (PK), UserID (FK), CustomerID (FK), OType, SAmount, DateTime, OStatus
		• MENUITEM
			- MenuItemID (PK), MICatID (FK), MIName, UPrice, NServing, MIStatus, ItemPicture
		• BILL
			- BillID (PK), OrderID (FK, unique), DateTime, VATAmount, VATSales, TotalAmount, BStatus
		• ORDERITEM
			- Composite PK (OrderID, MenuItemID), Quantity, TotalPrice, OrderID (FK), MenuItemID (FK)
		• PAYMENT
			- PaymentID (PK), BillID (FK, unique), DateTime, PaymentMode
		• CASHPAYMENT
			- C_PaymentID (PK), PaymentID (FK, unique), CashTendered, Change
		• DISCOUNT
			- DiscountID (PK), FName, MName, LName, EDate, DCategory, Mun, Prov
		• BILLWITHDISCOUNT
			- Composite PK (BillID, DiscountID), LessVAT, DiscountAmount, BillID (FK), DiscountID (FK)

	- How to Import:
		1. Open SQL Server Management Studio (SSMS).
		2. Log in using your configured `admin` user and `password`.
		3. Open the `RMS.sql` file located in the `Database` folder of the project.
		4. Execute the script line by line (or block by block) using the F5 key.
		5. This will create the database `RestaurantOrderManagementSystem` and all required tables.


8 - Usage Guide  
	- Login with predefined credentials
		* Default user for admin:
			Username: admin
			Password: password
			Security Key: 123  
  
	- Navigate the GUI to:  
		- Add or search customers  
		- Take and save orders  
		- Apply discounts and process payments  
		- Generate printable bills  
		- View and manage transactions  

	- To perform a transaction:
		1. Log in as a cashier user.
		2. Add your Menu Categories and Menu Items first (in Admin mode) to enable ordering.
		3. Create a customer record.
		4. Click the menu items you want to order.
		5. Click the Save button to finalize the order.
		6. Apply a discount if necessary.
		7. Proceed to payment.
		8. A receipt will automatically pop up.
		9. To start a new transaction, click the order icon in the lower right corner.

	- Exit the system via the logout button or the exit button on the upper-right corner of the program 

9 - Known Issues & Future Improvements  
	- Known Issues:  
		• Delays occur on low-end computers.  
		• Java-SQL Server connection setup may be difficult for beginners.  

	- Future Improvements:  
		1. Add two-factor authentication for enhanced login security.   
		2. Add advanced filtering/search tools for CRUD tables.  
		3. Addition of the table and the reservation management system.
		4. Add other payment modes such as Electronic Wallet and Bank Transaction.
		

10 - Acknowledgments & References  

	Cañete E. (2020, May 25). How to Compute Senior Citizen's Discount. Retrieved May 10, 2025, from https://youtu.be/ucmod3iGMfY?si=rf-JcVGCGMOqfTcK  

	Coderanch. (n.d.). Border with rounded JTextField. Retrieved May 10, 2025, from https://coderanch.com/t/336048/java/Border-rounded-JTextField

	Coolors. (n.d.). The super fast color palettes generator! Retrieved May 10, 2025, from https://coolors.co/

	Dribbble. (n.d.). UI design inspiration. Retrieved May 10, 2025, from https://dribbble.com/tags/ui-inspiration

	Flaticon. (n.d.). Vector icons and stickers. Retrieved May 10, 2025, from https://www.flaticon.com/
	
	Google. (n.d.). Material Symbols and Icons. Retrieved May 10, 2025, from https://fonts.google.com/icons

	Ha Minh, N. (2019, July 4). How to add file filter for JFileChooser dialog in Java. CodeJava. Retrieved May 10, 2025, from https://www.codejava.net/java-se/swing/add-file-filter-for-jfilechooser-dialog

	Panlasang Pinoy. (n.d.). Home. Retrieved May 10, 2025, from https://panlasangpinoy.com/

	Roberts, D. (2008, November 6). Wrap layout. Tips4Java. Retrieved May 10, 2025, from https://tips4java.wordpress.com/2008/11/06/wrap-layout/

	Serious Eats. (n.d.). Serious Eats. Retrieved May 10, 2025, from https://www.seriouseats.com/

	Stack Overflow. (2011, November 4). Trying to create JTable with proper row header. Retrieved May 10, 2025, 		from https://stackoverflow.com/questions/8002445/trying-to-create-jtable-with-proper-row-header

	Stack Overflow. (2013, October 2). Simplest way to set image as JPanel background. Retrieved May 10, 2025, 		from https://stackoverflow.com/questions/19125707/simplest-way-to-set-image-as-jpanel-background

	The Kitchn. (n.d.). We’re the secret ingredient for home cooks. Retrieved May 10, 2025, from https://www.thekitchn.com/         

	Walter R. (2024, November 11). How To Compute 12% VAT | VATable Sales And VAT Amount. Retrieved May 10, 2025, from https://youtu.be/QLQ_QGmdrXA?si=LOyhIO_ArIUNrl_z 

	W3Schools. (n.d.). SQL Server COALESCE() function. Retrieved May 10, 2025, from https://www.w3schools.com/sql/func_sqlserver_coalesce.asp

	W3Schools. (n.d.). SQL TOP, LIMIT, FETCH FIRST or ROWNUM clause. Retrieved May 10, 2025, from https://www.w3schools.com/sql/sql_top.asp

	W3Schools. (n.d.). SQL LEFT JOIN keyword. Retrieved May 10, 2025, from https://www.w3schools.com/sql/sql_join_left.asp

