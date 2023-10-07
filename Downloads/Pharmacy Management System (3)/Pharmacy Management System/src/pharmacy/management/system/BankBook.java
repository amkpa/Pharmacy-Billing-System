/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pharmacy.management.system;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import dao.ConnectionProvider;
import java.awt.Desktop;
import java.awt.print.PrinterJob;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Statement;
import javax.swing.JOptionPane; 
import javax.swing.table.DefaultTableModel; 
import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.text.SimpleDateFormat; 



public class BankBook extends javax.swing.JFrame {
    
    private DefaultTableModel tableModel;
    

    public BankBook() {
        initComponents();
        populateSupplierNames();
        populateNarration();
        tableModel = (DefaultTableModel) jTable1getData.getModel();
        getDataAndPopulateTable();
        TotalAmount.setEditable(false);
    }

    
 
      private void getDataAndPopulateTable() {
        try {
            Connection con = ConnectionProvider.getCon();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM bankBook");

            tableModel.setRowCount(0);

            while (rs.next()) {
                int slNo = rs.getInt("sl_No");
                String date = rs.getString("date");
                String paymentType = rs.getString("paymentType");
                String refNo = rs.getString("ref_No");
                String bankName = rs.getString("bankName");
                String supplierName = rs.getString("supplierName");
                String narration = rs.getString("narration");
                String payment = rs.getString("payment");
                String otherInfo = rs.getString("otherInfo");

                tableModel.addRow(new Object[]{slNo, date, paymentType, refNo, bankName, supplierName, narration, payment, otherInfo});
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error fetching data: " + ex.getMessage());
        }
    }



    
    

    private void populateSupplierNames() {
        try {
            Connection con = ConnectionProvider.getCon();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT supplierName FROM supplier");

            while (rs.next()) {
                String supplierName = rs.getString("supplierName");
                jComboBox4SupplierName.addItem(supplierName);
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error populating supplier names: " + ex.getMessage());
        }
    }

    private void populateNarration() {
        try {
            Connection con = ConnectionProvider.getCon();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT narration FROM narrationTable");

            while (rs.next()) {
                String narration = rs.getString("narration");
                jComboBox3Narration.addItem(narration);
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error populating narration names: " + ex.getMessage());
        }
    }    
    
//private void saveBankBook() {
//    try {
//        Connection con = ConnectionProvider.getCon();
//
//        // Check if a record with the same ref_No already exists
//        String refNo = jTextField1.getText();
//        PreparedStatement checkDuplicate = con.prepareStatement("SELECT COUNT(*) FROM bankBook WHERE ref_No = ?");
//        checkDuplicate.setString(1, refNo);
//        ResultSet resultSet = checkDuplicate.executeQuery();
//        resultSet.next();
//        int rowCount = resultSet.getInt(1);
//
//        if (rowCount > 0) {
//            JOptionPane.showMessageDialog(this, "A record with the same Ref. No. already exists.");
//            return;
//        }
//
//        // If no duplicate found, proceed with insertion
//        PreparedStatement ps = con.prepareStatement("INSERT INTO bankBook (date, paymentType, ref_No, bankName, supplierName, narration, payment, otherInfo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
//
//        // Get data from GUI components
//        java.util.Date date = jDateChooser1.getDate();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String strDate = sdf.format(date);
//        String paymentType = (String) jComboBox1.getSelectedItem();
//        String bankName = (String) jComboBox2.getSelectedItem();
//        String supplierName = (String) jComboBox4SupplierName.getSelectedItem(); // Get selected supplier name
//        String narration = (String) jComboBox3Narration.getSelectedItem();
//        String payment = jTextField2.getText();
//        String otherInfo = jTextArea1.getText();
//
//        // Validate and check for required fields
//        if (strDate.isEmpty() || paymentType.isEmpty() || refNo.isEmpty() || bankName.isEmpty() || supplierName.isEmpty() || narration.isEmpty() || payment.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "All fields are required.");
//            return; // Stop processing if any field is empty
//        }
//
//        // Set values in the prepared statement
//        ps.setString(1, strDate);
//        ps.setString(2, paymentType);
//        ps.setString(3, refNo);
//        ps.setString(4, bankName);
//        ps.setString(5, supplierName);
//        ps.setString(6, narration);
//        ps.setString(7, payment);
//        ps.setString(8, otherInfo);
//
//        int rowsAffected = ps.executeUpdate();
//
//        if (rowsAffected > 0) {
//            JOptionPane.showMessageDialog(this, "Bank Book entry saved successfully.");
//            // Clear all input fields
//            clearFields();
//
//            // Refresh the table data
//            getDataAndPopulateTable();
//        } else {
//            JOptionPane.showMessageDialog(this, "Failed to save Bank Book entry.");
//        }
//
//        ps.close();
//        con.close();
//    } catch (Exception ex) {
//        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
//    }
//}

    private void saveBankBook() {
    try {
        Connection con = ConnectionProvider.getCon();

        // Prepare the INSERT statement
        PreparedStatement ps = con.prepareStatement("INSERT INTO bankBook (date, paymentType, ref_No, bankName, supplierName, narration, payment, otherInfo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

        // Get data from GUI components
        java.util.Date date = jDateChooser1.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(date);
        String paymentType = (String) jComboBox1.getSelectedItem();
        String bankName = (String) jComboBox2.getSelectedItem();
        String supplierName = (String) jComboBox4SupplierName.getSelectedItem();
        String narration = (String) jComboBox3Narration.getSelectedItem();
        String refNo = jTextField1.getText();
        String payment = jTextField2.getText();
        String otherInfo = jTextArea1.getText();

        // Set values in the prepared statement
        ps.setString(1, strDate);
        ps.setString(2, paymentType);
        ps.setString(3, refNo);
        ps.setString(4, bankName);
        ps.setString(5, supplierName);
        ps.setString(6, narration);
        ps.setString(7, payment);
        ps.setString(8, otherInfo);

        int rowsAffected = ps.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Bank Book entry saved successfully.");
            // Clear all input fields
            clearFields();
            // Refresh the table data
            getDataAndPopulateTable();
            
            calculateAndDisplayTotalPayment();
            
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save Bank Book entry.");
        }

        ps.close();
        con.close();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
    }
}


// Method to clear all input fields
private void clearFields() {
    jDateChooser1.setDate(null);
    jComboBox1.setSelectedIndex(0);
    jTextField1.setText("");
    jComboBox2.setSelectedIndex(0);
    jComboBox4SupplierName.setSelectedIndex(0);
    jComboBox3Narration.setSelectedIndex(0);
    jTextField2.setText("");
    jTextArea1.setText("");
    txtSupplierNameSearch.setText("");
}





private void saveNarration() {
    String narration = jTextField4.getText().trim(); // Get the narration text from the text field

    if (narration.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Narration name is required.");
    } else {
        try {
            Connection con = ConnectionProvider.getCon();

            // Check if the narration already exists
            PreparedStatement checkDuplicate = con.prepareStatement("SELECT COUNT(*) FROM narrationTable WHERE narration = ?");
            checkDuplicate.setString(1, narration);
            ResultSet resultSet = checkDuplicate.executeQuery();
            resultSet.next();
            int rowCount = resultSet.getInt(1);

            if (rowCount > 0) {
                JOptionPane.showMessageDialog(this, "Narration with the same name already exists.");
                return;
            }

            // If no duplicate found, proceed with insertion
            PreparedStatement ps = con.prepareStatement("INSERT INTO narrationTable (narration) VALUES (?)");
            ps.setString(1, narration);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Narration saved successfully.");
                jTextField4.setText(""); // Clear the narration text field after saving

                // Refresh the bank book narration dropdown
                populateNarration();
                
                

                // You can add any additional logic here after successful insertion.
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save narration.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
}



private void updateData() {
    int selectedRowIndex = jTable1getData.getSelectedRow();

    if (selectedRowIndex == -1) {
        JOptionPane.showMessageDialog(this, "Please select a row to update.");
        return;
    }

    // Get the existing data from the selected row in the JTable
   int slNo = Integer.parseInt(jTable1getData.getValueAt(selectedRowIndex, 0).toString());

    String existingDate = jTable1getData.getValueAt(selectedRowIndex, 1).toString();
    String existingPaymentType = jTable1getData.getValueAt(selectedRowIndex, 2).toString();
    String existingRefNo = jTable1getData.getValueAt(selectedRowIndex, 3).toString();
    String existingBankName = jTable1getData.getValueAt(selectedRowIndex, 4).toString();
    String existingSupplierName = jTable1getData.getValueAt(selectedRowIndex, 5).toString();
    String existingNarration = jTable1getData.getValueAt(selectedRowIndex, 6).toString();
    String existingPayment = jTable1getData.getValueAt(selectedRowIndex, 7).toString();
    String existingOtherInfo = jTable1getData.getValueAt(selectedRowIndex, 8).toString();

    // Get updated data from the GUI components
    String updatedDate = null;
    String updatedPaymentType = null;
    String updatedRefNo = null;
    String updatedBankName = null;
    String updatedSupplierName = null;
    String updatedNarration = null;
    String updatedPayment = null;
    String updatedOtherInfo = null;

    if (jDateChooser1.getDate() != null) {
        java.util.Date updatedDateUtil = jDateChooser1.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        updatedDate = sdf.format(updatedDateUtil);
    }

    if (jComboBox1.getSelectedItem() != null) {
        updatedPaymentType = (String) jComboBox1.getSelectedItem();
    }

    if (!jTextField1.getText().isEmpty()) {
        updatedRefNo = jTextField1.getText();
    }

    if (jComboBox2.getSelectedItem() != null) {
        updatedBankName = (String) jComboBox2.getSelectedItem();
    }

    if (jComboBox4SupplierName.getSelectedItem() != null) {
        updatedSupplierName = (String) jComboBox4SupplierName.getSelectedItem();
    }

    if (jComboBox3Narration.getSelectedItem() != null) {
        updatedNarration = (String) jComboBox3Narration.getSelectedItem();
    }

    if (!jTextField2.getText().isEmpty()) {
        updatedPayment = jTextField2.getText();
    }

    if (!jTextArea1.getText().isEmpty()) {
        updatedOtherInfo = jTextArea1.getText();
    }

    try {
        Connection con = ConnectionProvider.getCon();
        PreparedStatement ps = con.prepareStatement("UPDATE bankBook SET date=?, paymentType=?, ref_No=?, bankName=?, supplierName=?, narration=?, payment=?, otherInfo=? WHERE sl_No=?");

        // Check each field for changes and update accordingly
        if (updatedDate != null && !updatedDate.equals(existingDate)) {
            ps.setString(1, updatedDate);
        } else {
            ps.setString(1, existingDate);
        }

        if (updatedPaymentType != null && !updatedPaymentType.equals(existingPaymentType)) {
            ps.setString(2, updatedPaymentType);
        } else {
            ps.setString(2, existingPaymentType);
        }

        if (updatedRefNo != null && !updatedRefNo.equals(existingRefNo)) {
            ps.setString(3, updatedRefNo);
        } else {
            ps.setString(3, existingRefNo);
        }

        if (updatedBankName != null && !updatedBankName.equals(existingBankName)) {
            ps.setString(4, updatedBankName);
        } else {
            ps.setString(4, existingBankName);
        }

        if (updatedSupplierName != null && !updatedSupplierName.equals(existingSupplierName)) {
            ps.setString(5, updatedSupplierName);
        } else {
            ps.setString(5, existingSupplierName);
        }

        if (updatedNarration != null && !updatedNarration.equals(existingNarration)) {
            ps.setString(6, updatedNarration);
        } else {
            ps.setString(6, existingNarration);
        }

        if (updatedPayment != null && !updatedPayment.equals(existingPayment)) {
            ps.setString(7, updatedPayment);
        } else {
            ps.setString(7, existingPayment);
        }

        if (updatedOtherInfo != null && !updatedOtherInfo.equals(existingOtherInfo)) {
            ps.setString(8, updatedOtherInfo);
        } else {
            ps.setString(8, existingOtherInfo);
        }

        ps.setInt(9, slNo);

        int rowsAffected = ps.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Data updated successfully.");

            // Update the data in the jTable1 only for the changed fields
            if (updatedDate != null && !updatedDate.equals(existingDate)) {
                jTable1getData.setValueAt(updatedDate, selectedRowIndex, 1);
            }
            if (updatedPaymentType != null && !updatedPaymentType.equals(existingPaymentType)) {
                jTable1getData.setValueAt(updatedPaymentType, selectedRowIndex, 2);
            }
            if (updatedRefNo != null && !updatedRefNo.equals(existingRefNo)) {
                jTable1getData.setValueAt(updatedRefNo, selectedRowIndex, 3);
            }
            if (updatedBankName != null && !updatedBankName.equals(existingBankName)) {
                jTable1getData.setValueAt(updatedBankName, selectedRowIndex, 4);
            }
            if (updatedSupplierName != null && !updatedSupplierName.equals(existingSupplierName)) {
                jTable1getData.setValueAt(updatedSupplierName, selectedRowIndex, 5);
            }
            if (updatedNarration != null && !updatedNarration.equals(existingNarration)) {
                jTable1getData.setValueAt(updatedNarration, selectedRowIndex, 6);
            }
            if (updatedPayment != null && !updatedPayment.equals(existingPayment)) {
                jTable1getData.setValueAt(updatedPayment, selectedRowIndex, 7);
            }
            if (updatedOtherInfo != null && !updatedOtherInfo.equals(existingOtherInfo)) {
                jTable1getData.setValueAt(updatedOtherInfo, selectedRowIndex, 8);
            }
            
                      // Call calculateAndDisplayTotalPayment method here
            calculateAndDisplayTotalPayment();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update data.");
        }

        ps.close();
        con.close();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
    }
}







































    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jComboBox3Narration = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        saveBankBookData = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1getData = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        saveNarration = new javax.swing.JButton();
        updateData = new javax.swing.JButton();
        deletaData = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        TotalAmount = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtSupplierNameSearch = new javax.swing.JTextField();
        SupplierNameSearch = new javax.swing.JButton();
        printAllDataBtn = new javax.swing.JButton();
        convertExcel = new javax.swing.JButton();
        PdfConveter = new javax.swing.JButton();
        btnConvertPdf = new javax.swing.JLabel();
        jComboBox4SupplierName = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setText("Bank Book");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 20, -1, -1));

        jLabel2.setText("Date");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 30, 22));
        getContentPane().add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 110, 190, -1));

        jLabel3.setText("Payment Type");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 80, 20));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cheque", "UPI" }));
        getContentPane().add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, 190, -1));

        jLabel4.setText("Ref. No.");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, -1, -1));
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, 190, -1));

        jLabel5.setText("Bank Name");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 200, -1, -1));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SBI", "HDFC", "Union", "Kotak", "PNB", "BOI", "CBI" }));
        getContentPane().add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 200, 190, -1));

        jLabel6.setText("Payment");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 290, -1, -1));
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 290, 189, -1));

        jLabel7.setText("Narration");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 250, 50, 40));

        jComboBox3Narration.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Being Bank Deposited....", "Being Cheque Deposited....", "Being UPI Deposited...." }));
        jComboBox3Narration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3NarrationActionPerformed(evt);
            }
        });
        getContentPane().add(jComboBox3Narration, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 260, 189, -1));

        jLabel8.setText("Other Info. : ");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 320, 70, -1));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 320, 190, 90));

        saveBankBookData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/save.png"))); // NOI18N
        saveBankBookData.setText("Save");
        saveBankBookData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBankBookDataActionPerformed(evt);
            }
        });
        getContentPane().add(saveBankBookData, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 430, -1, -1));

        jLabel9.setText("Supplier Name");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 230, 80, 30));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/close.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1320, 10, -1, -1));

        jTable1getData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Sl No.", "Date", "Payment Type", "Ref. No.", "Bank Name", "Supplier Name", "Narration", "Payment", "Other Info."
            }
        ));
        jScrollPane2.setViewportView(jTable1getData);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 100, 850, 440));

        jLabel11.setText("Narration");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 570, 50, 20));
        getContentPane().add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 570, 170, -1));

        saveNarration.setText("Save");
        saveNarration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveNarrationActionPerformed(evt);
            }
        });
        getContentPane().add(saveNarration, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 610, -1, -1));

        updateData.setText("Modify");
        updateData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateDataActionPerformed(evt);
            }
        });
        getContentPane().add(updateData, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 550, -1, -1));

        deletaData.setText("Delete");
        deletaData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletaDataActionPerformed(evt);
            }
        });
        getContentPane().add(deletaData, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 550, -1, -1));

        jLabel10.setText("Total Amount");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 550, 80, 20));

        TotalAmount.setText("               0.0");
        TotalAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TotalAmountActionPerformed(evt);
            }
        });
        getContentPane().add(TotalAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 550, 150, -1));

        jLabel12.setText("Supplier Name");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 550, 90, 20));

        txtSupplierNameSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupplierNameSearchActionPerformed(evt);
            }
        });
        getContentPane().add(txtSupplierNameSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 550, 160, -1));

        SupplierNameSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search Button.png"))); // NOI18N
        SupplierNameSearch.setText("Search");
        SupplierNameSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SupplierNameSearchActionPerformed(evt);
            }
        });
        getContentPane().add(SupplierNameSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 550, 100, 30));

        printAllDataBtn.setText("Print");
        printAllDataBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printAllDataBtnActionPerformed(evt);
            }
        });
        getContentPane().add(printAllDataBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 670, -1, -1));

        convertExcel.setText("Excel");
        convertExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                convertExcelActionPerformed(evt);
            }
        });
        getContentPane().add(convertExcel, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 630, -1, -1));

        PdfConveter.setText("PDF");
        PdfConveter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PdfConveterActionPerformed(evt);
            }
        });
        getContentPane().add(PdfConveter, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 630, -1, -1));

        btnConvertPdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Dashboard Background.jpeg"))); // NOI18N
        getContentPane().add(btnConvertPdf, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, -40, -1, -1));

        jComboBox4SupplierName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Supplier" }));
        jComboBox4SupplierName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4SupplierNameActionPerformed(evt);
            }
        });
        getContentPane().add(jComboBox4SupplierName, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 230, 190, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        setVisible(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void SupplierNameSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SupplierNameSearchActionPerformed
        String searchCriteria = txtSupplierNameSearch.getText().trim();

        if (searchCriteria.isEmpty()) {

            getDataAndPopulateTable();
          
        } else {

            searchAndDisplayBankBookResults(searchCriteria);

        }
    }//GEN-LAST:event_SupplierNameSearchActionPerformed

private void searchAndDisplayBankBookResults(String searchCriteria) {
    DefaultTableModel model = (DefaultTableModel) jTable1getData.getModel();
    model.setRowCount(0); // Clear existing data in the table

    // Clear other fields
    clearFields();

    try {
        Connection con = ConnectionProvider.getCon();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM bankBook WHERE supplierName LIKE ?");
        ps.setString(1, "%" + searchCriteria + "%"); // Search for supplier names containing the entered criteria
        ResultSet rs = ps.executeQuery();

        boolean hasData = false; // Flag to check if there is any data

        while (rs.next()) {
            int slno = rs.getInt("sl_No");
            String date = rs.getString("date");
            String paymentType = rs.getString("paymentType");
            String refNo = rs.getString("ref_No");
            String bankName = rs.getString("bankName");
            String supplierName = rs.getString("supplierName");
            String narration = rs.getString("narration");
            String payment = rs.getString("payment");
            String otherInfo = rs.getString("otherInfo");

            model.addRow(new Object[]{
                slno,
                date,
                paymentType,
                refNo,
                bankName,
                supplierName,
                narration,
                payment,
                otherInfo
            });

            hasData = true; // Data is found
        }

        rs.close();
        ps.close();
        con.close();

        if (!hasData) {
            // Display a message in the table if no data is found
            model.addRow(new Object[]{"No data found", "", "", "", "", "", "", "", ""});
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
    }
}








    
    
    private void jComboBox3NarrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3NarrationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3NarrationActionPerformed

    private void saveNarrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveNarrationActionPerformed
       saveNarration();
    }//GEN-LAST:event_saveNarrationActionPerformed

    private void saveBankBookDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBankBookDataActionPerformed
        saveBankBook();
    }//GEN-LAST:event_saveBankBookDataActionPerformed

    private void updateDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateDataActionPerformed
      updateData() ;
    }//GEN-LAST:event_updateDataActionPerformed

    private void deletaDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletaDataActionPerformed
      
    int selectedRowIndex = jTable1getData.getSelectedRow();

    if (selectedRowIndex == -1) {
        JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        return;
    }

    // Get the unique identifier (e.g., sl_No) from the selected row
    int slNo = Integer.parseInt(jTable1getData.getValueAt(selectedRowIndex, 0).toString());

    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("DELETE FROM bankBook WHERE sl_No = ?");

            // Set the sl_No parameter for the DELETE query
            ps.setInt(1, slNo);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Record deleted successfully.");
                // Remove the selected row from the JTable
                tableModel.removeRow(selectedRowIndex);
                
                
                  calculateAndDisplayTotalPayment();
                
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete record.");
            }

            ps.close();
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }


    }//GEN-LAST:event_deletaDataActionPerformed

    private void TotalAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TotalAmountActionPerformed
        calculateAndDisplayTotalPayment();
    }//GEN-LAST:event_TotalAmountActionPerformed

    private void PdfConveterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PdfConveterActionPerformed
         Document document = new Document(PageSize.A4);
    try {
        // Provide the path where you want to save the PDF file
        String pdfFilePath = "bank_book.pdf";
        PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
        
        document.open();
        
        // Add a title to the PDF
        document.add(new Paragraph("Bank Book Data"));

        // Create a PDF table
        com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(9); // Number of columns
        
        // Add table headers
        pdfTable.addCell("Sl No.");
        pdfTable.addCell("Date");
        pdfTable.addCell("Payment Type");
        pdfTable.addCell("Ref. No.");
        pdfTable.addCell("Bank Name");
        pdfTable.addCell("Supplier Name");
        pdfTable.addCell("Narration");
        pdfTable.addCell("Payment");
        pdfTable.addCell("Other Info.");

        // Loop through your tableModel to add data to the PDF table
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                pdfTable.addCell(tableModel.getValueAt(row, col).toString());
            }
        }

        document.add(pdfTable);
        document.close();

        // Auto-download the PDF file
        Desktop.getDesktop().open(new File(pdfFilePath));
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error creating PDF: " + e.getMessage());
    }
    }//GEN-LAST:event_PdfConveterActionPerformed

    private void convertExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convertExcelActionPerformed
        Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Bank Book Data");

    // Create a header row
    Row headerRow = sheet.createRow(0);
    String[] headers = {"Sl No.", "Date", "Payment Type", "Ref. No.", "Bank Name", "Supplier Name", "Narration", "Payment", "Other Info."};

    for (int col = 0; col < headers.length; col++) {
        Cell cell = headerRow.createCell(col);
        cell.setCellValue(headers[col]);
    }

    // Loop through your tableModel to add data to the Excel sheet
    for (int row = 0; row < tableModel.getRowCount(); row++) {
        Row dataRow = sheet.createRow(row + 1);

        for (int col = 0; col < tableModel.getColumnCount(); col++) {
            Cell cell = dataRow.createCell(col);
            cell.setCellValue(tableModel.getValueAt(row, col).toString());
        }
    }

    try {
        // Provide the path where you want to save the Excel file
        String excelFilePath = "bank_book.xlsx";
        FileOutputStream outputStream = new FileOutputStream(excelFilePath);
        workbook.write(outputStream);
        outputStream.close();

        // Auto-download the Excel file
        Desktop.getDesktop().open(new File(excelFilePath));
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error creating Excel file: " + e.getMessage());
    }
    }//GEN-LAST:event_convertExcelActionPerformed

    private void printAllDataBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printAllDataBtnActionPerformed
            // Create a PrinterJob
    PrinterJob job = PrinterJob.getPrinterJob();
    
    // Create a PageFormat
    PageFormat pf = job.pageDialog(job.defaultPage());
    
    // Set the Printable to your table
    job.setPrintable(new Printable() {
        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex > 0) {
                return NO_SUCH_PAGE;
            }
            
            // Create a Graphics2D object
            Graphics2D g2d = (Graphics2D) graphics;
            
            // Translate and scale to fit the entire table on the page
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2d.scale(0.75, 0.75); // You may need to adjust the scaling
            
            // Call the print method of your JTable
            jTable1getData.print(g2d);
            
            return PAGE_EXISTS;
        }
    }, pf);
    
    // Show the print dialog
    if (job.printDialog()) {
        try {
            job.print();
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Error printing: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_printAllDataBtnActionPerformed

    private void txtSupplierNameSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupplierNameSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupplierNameSearchActionPerformed

    private void jComboBox4SupplierNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4SupplierNameActionPerformed

    }//GEN-LAST:event_jComboBox4SupplierNameActionPerformed

    private void calculateAndDisplayTotalPayment() {
    double totalPayment = 0.0;

    // Iterate through the rows in the JTable and calculate the total payment
    for (int i = 0; i < jTable1getData.getRowCount(); i++) {
        String paymentStr = jTable1getData.getValueAt(i, 7).toString();
        double payment = Double.parseDouble(paymentStr);
        totalPayment += payment;
    }

    // Display the calculated total payment in the TotalAmount field
    TotalAmount.setText(String.format("%.2f", totalPayment));
}

    
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BankBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BankBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BankBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BankBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BankBook().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton PdfConveter;
    private javax.swing.JButton SupplierNameSearch;
    private javax.swing.JTextField TotalAmount;
    private javax.swing.JLabel btnConvertPdf;
    private javax.swing.JButton convertExcel;
    private javax.swing.JButton deletaData;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3Narration;
    private javax.swing.JComboBox<String> jComboBox4SupplierName;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1getData;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JButton printAllDataBtn;
    private javax.swing.JButton saveBankBookData;
    private javax.swing.JButton saveNarration;
    private javax.swing.JTextField txtSupplierNameSearch;
    private javax.swing.JButton updateData;
    // End of variables declaration//GEN-END:variables
}
