/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Testing;
import dao.ConnectionProvider;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Random;
import java.sql.PreparedStatement;

/**
 *
 * @author ESCJAVA04202228
 */
public class InsertRandomSuppliers {
    public static void main(String[] args) {
        try {
            Connection con = ConnectionProvider.getCon();
            Random random = new Random();
            
            // Create an array of supplier names for random selection
            String[] supplierNames = {"Supplier A", "Supplier B", "Supplier C", "Supplier D", "Supplier E", "Supplier F", "Supplier G", "Supplier H", "Supplier I", "Supplier J"};
            
            // Create an array of supplier types for random selection
            String[] supplierTypes = {"Sundry Debtors", "Sundry Creditors", "Analysis Account", "Trading Account", "Salary Account"};
            
            // Create an array of GST types for random selection
            String[] gstTypes = {"Consumer", "Tin (GST)"};
            
            PreparedStatement ps = con.prepareStatement("INSERT INTO supplier (supplierName, supplierType, address, mobileNumber, altMobile, email, otherDetails, gstType, gstNumber, tinNumber, bankAccountNo, ifsc, conType) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
            for (int i = 0; i < 10 + random.nextInt(11); i++) {
                String supplierName = supplierNames[random.nextInt(supplierNames.length)];
                String supplierType = supplierTypes[random.nextInt(supplierTypes.length)];
                String address = "Address " + (i + 1);
                String mobileNumber = "Mobile " + (i + 1);
                String altMobile = "Alt Mobile " + (i + 1);
                String email = "supplier" + (i + 1) + "@example.com";
                String otherDetails = "Other Details " + (i + 1);
                String gstType = gstTypes[random.nextInt(gstTypes.length)];
                String gstNumber = gstType.equals("Tin (GST)") ? "GST" + (i + 1) : "";
                String tinNumber = gstType.equals("Tin (GST)") ? "TIN" + (i + 1) : "";
                String bankAccountNo = "Account No " + (i + 1);
                String ifsc = "IFSC " + (i + 1);
                String conType = "Local"; // You can change this as needed
                
                ps.setString(1, supplierName);
                ps.setString(2, supplierType);
                ps.setString(3, address);
                ps.setString(4, mobileNumber);
                ps.setString(5, altMobile);
                ps.setString(6, email);
                ps.setString(7, otherDetails);
                ps.setString(8, gstType);
                ps.setString(9, gstNumber);
                ps.setString(10, tinNumber);
                ps.setString(11, bankAccountNo);
                ps.setString(12, ifsc);
                ps.setString(13, conType);
                
                ps.executeUpdate();
            }
            
            ps.close();
            con.close();
            
            System.out.println("Random suppliers inserted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
}
