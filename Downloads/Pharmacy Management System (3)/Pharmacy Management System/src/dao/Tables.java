package dao;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class Tables {

    public static void main(String[] args) {

        try {
            Connection con = ConnectionProvider.getCon();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("create table appuser (appuser_pk int AUTO_INCREMENT primary key,userRole varchar(200),name varchar(200), dob varchar(50),mobileNumber varchar(50),email varchar(200),username varchar(200),password varchar(200),address varchar(200))");
            stmt.executeUpdate("insert into appuser (userRole,name,dob,mobileNumber,email,username,password,address) values ('Admin','Admin','15-04-01','7735353911','admin@gmail.com','admin','admin','India')");
            stmt.executeUpdate("create table medicine (medicine_pk int AUTO_INCREMENT primary key,medicineName varchar(200),companyName varchar(200),SupplierName varchar(200),UnitOfMeasure varchar(200),MFG varchar(200),EXP varchar(200),PurchasePrice varchar(200),LendingRate varchar(200),MRP varchar(200),SalePrice varchar(200),taxtype varchar(200),taxPercentage varchar(200),quantity varchar(200))");
            JOptionPane.showMessageDialog(null, "Table Created Successfully.");

            // Create a table for suppliers
            stmt.executeUpdate("CREATE TABLE supplier ("
                    + "supplier_pk INT AUTO_INCREMENT PRIMARY KEY,"
                    + "supplierName VARCHAR(200),"
                    + "supplierType VARCHAR(200),"
                    + "address TEXT,"
                    + "mobileNumber VARCHAR(50),"
                    + "altMobile VARCHAR(50),"
                    + "email VARCHAR(200),"
                    + "otherDetails TEXT," // This is the correct column name
                    + "gstType VARCHAR(200),"
                    + "gstNumber VARCHAR(200),"
                    + "tinNumber VARCHAR(200),"
                    + "bankAccountNo VARCHAR(200),"
                    + "ifsc VARCHAR(200),"
                    + "conType VARCHAR(200)"
                    + ")");

            JOptionPane.showMessageDialog(null, "Supplier Table Created Successfully.");

            //Purchase table 
            stmt.executeUpdate("CREATE TABLE purchase ("
                    + "purchase_pk INT AUTO_INCREMENT PRIMARY KEY,"
                    + "purchaseDate DATE,"
                    + "invoiceNumber VARCHAR(200),"
                    + "purchaseName VARCHAR(50),"
                    + "purchasetype VARCHAR(50),"
                    + "address VARCHAR(50),"
                    + "ewayBillNo VARCHAR(200),"
                    + "medicineName VARCHAR(200),"
                    + "brandName VARCHAR(200),"
                    + "UOM VARCHAR(200),"
                    + "division VARCHAR(200),"
                    + "mfg VARCHAR(200),"
                    + "exp VARCHAR(200),"
                    + "purchaseRate VARCHAR(200),"
                    + "lendingRate VARCHAR(200),"
                    + "localorOut VARCHAR(200),"
                    + "MRP VARCHAR(200),"
                    + "SalePrice VARCHAR(200),"
                    + "Quantity VARCHAR(200),"
                    + "TaxType VARCHAR(200),"
                    + "Taxpercentage VARCHAR(200)"
                    + ")");

            JOptionPane.showMessageDialog(null, "Purchase Table Created Successfully.");
            //Customer Card

            stmt.executeUpdate("CREATE TABLE customerCard ("
                    + "card_pk INT AUTO_INCREMENT PRIMARY KEY,"
                    + "scanId VARCHAR(200),"
                    + "name VARCHAR(200),"
                    + "address varchar(200),"
                    + "mobile VARCHAR(50),"
                    + " email VARCHAR(50),"
                    + "relationship VARCHAR(200),"
                    + "discount varchar(200)" // This is the correct column name
                    + ")");
            JOptionPane.showMessageDialog(null, "Customer Card Table Created Successfully.");

            // Create a table for BankBook
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bankBook ("
                    + "sl_No INT AUTO_INCREMENT PRIMARY KEY,"
                    + "date DATE,"
                    + "paymentType VARCHAR(200),"
                    + "ref_No VARCHAR(200),"
                    + "bankName VARCHAR(200),"
                    + "supplierName VARCHAR(200),"
                    + "narration VARCHAR(200),"
                    + "payment VARCHAR(200),"
                    + "otherInfo VARCHAR(200)"
                    + ")");

            JOptionPane.showMessageDialog(null, "BankBook Table Created Successfully.");

            // Create a table for CashBook
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS cashBook ("
                    + "sl_No INT AUTO_INCREMENT PRIMARY KEY,"
                    + "date DATE,"
                    + "type VARCHAR(200),"
                    + "supplierName VARCHAR(200),"
                    + "paymentType VARCHAR(200),"
                    + "narration VARCHAR(200),"
                    + "payment VARCHAR(200),"
                    + "otherInfo VARCHAR(200)"
                    + ")");

            JOptionPane.showMessageDialog(null, "CashBook Table Created Successfully.");

            // Create a table for Narrations with date auto-update
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS narrationTable ("
                    + "sl_No INT AUTO_INCREMENT PRIMARY KEY,"
                    + "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "narration VARCHAR(200)"
                    + ")");
            JOptionPane.showMessageDialog(null, "Narration Table Created Successfully.");

            stmt.executeUpdate("CREATE TABLE Employee ("
                    + "empId INT AUTO_INCREMENT PRIMARY KEY,"
                    + "empName VARCHAR(255),"
                    + "designation VARCHAR(255),"
                    + "empType VARCHAR(255),"
                    + "empSex VARCHAR(10),"
                    + "empAccount VARCHAR(20),"
                    + "empAddress VARCHAR(255),"
                    + "mobile VARCHAR(15),"
                    + "email VARCHAR(255),"
                    + "accountno VARCHAR(20),"
                    + "ifsc VARCHAR(20),"
                    + "basicSalary DECIMAL(10, 2),"
                    + "allowance DECIMAL(10, 2),"
                    + "specialAllowance DECIMAL(10, 2),"
                    + "hra DECIMAL(10, 2),"
                    + "localConv DECIMAL(10, 2),"
                    + "basketAllowance DECIMAL(10, 2),"
                    + "additionalIns DECIMAL(10, 2),"
                    + "pf DECIMAL(10, 2),"
                    + "esi DECIMAL(10, 2),"
                    + "grossSalary DECIMAL(10, 2),"
                    + "perdaysalary DECIMAL(10, 2),"
                    + "joindate DATE,"
                    + "lastdate DATE"
                    + ")");

            JOptionPane.showMessageDialog(null, "Employee Table Created Successfully.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
}
