/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pharmacy.management.system;

import dao.ConnectionProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

import java.awt.Graphics;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
import javax.swing.JOptionPane; // For using JOptionPane for dialog boxes
import javax.swing.table.DefaultTableModel; // For working with JTable's DefaultTableModel
import java.sql.Connection; // For working with database connections
import java.sql.PreparedStatement; // For preparing SQL statements
import java.sql.ResultSet; // For handling result sets from database queries
import java.sql.SQLException; // For handling SQL exceptions
import java.text.SimpleDateFormat; // For date formatting


/**
 *
 * @author mbish
 */
public class CashBook extends javax.swing.JFrame {
    
       private DefaultTableModel tableModel;

    /**
     * Creates new form CashBook
     */
    public CashBook() {
        initComponents();
        
       populateSupplierNames();
        populateNarration();
       tableModel = (DefaultTableModel) jTable1getData.getModel();
     getDataAndPopulateTable();
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
    
    private void saveNarration() {
    String narration = jTextField3.getText().trim(); // Get the narration text from the text field

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
    
    
private void getDataAndPopulateTable() {
    try {
        Connection con = ConnectionProvider.getCon();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM cashBook");

        tableModel.setRowCount(0);

        while (rs.next()) {
            int slNo = rs.getInt("sl_No");
            String date = rs.getString("date");
            String paymentType = rs.getString("paymentType");
            String supplierName = rs.getString("supplierName");
            String narration = rs.getString("narration");
            String payment = rs.getString("payment");
            String otherInfo = rs.getString("otherInfo");

            tableModel.addRow(new Object[]{slNo, date, paymentType, supplierName, narration, payment, otherInfo});
        }

        rs.close();
        stmt.close();
        con.close();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error fetching data: " + ex.getMessage());
    }
}

    
    
    
    
    private void saveCashBook() {
    try {
        Connection con = ConnectionProvider.getCon();

        // Prepare the INSERT statement for the cashBook table
        PreparedStatement ps = con.prepareStatement("INSERT INTO cashBook (date, paymentType, supplierName, narration, payment, otherInfo) VALUES (?, ?, ?, ?, ?, ?)");

        // Get data from GUI components
        java.util.Date date = jDateChooser1.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(date);
        String paymentType = (String) jComboBox1.getSelectedItem();
        String supplierName = (String) jComboBox4SupplierName.getSelectedItem();
        String narration = (String) jComboBox3Narration.getSelectedItem();
        String payment = jTextField2.getText();
        String otherInfo = jTextArea1.getText();

        // Set values in the prepared statement
        ps.setString(1, strDate);
        ps.setString(2, paymentType);
        ps.setString(3, supplierName);
        ps.setString(4, narration);
        ps.setString(5, payment);
        ps.setString(6, otherInfo);

        int rowsAffected = ps.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "CashBook entry saved successfully.");
            // Clear all input fields
            clearFields();
            // Refresh the table data
           getDataAndPopulateTable();
            
           // calculateAndDisplayTotalPayment();
            
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save CashBook entry.");
        }

        ps.close();
        con.close();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
    }
}
    
    private void clearFields() {
    // Clear all input fields here
    jDateChooser1.setDate(null);  // Clear date field
    jComboBox1.setSelectedIndex(0);  // Reset combo box selection
    jComboBox4SupplierName.setSelectedIndex(0);  // Reset combo box selection
    jComboBox3Narration.setSelectedIndex(0);  // Reset combo box selection
  //  jTextField1.setText("");  // Clear text field
    jTextField2.setText("");  // Clear text field
    jTextArea1.setText("");  // Clear text area
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
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jComboBox3Narration = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        btnSaveCashBookData = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1getData = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        btnSaveNarration = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jComboBox4SupplierName = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Cash Book");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 20, 223, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Date");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, -1, 22));
        getContentPane().add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 190, -1));

        jLabel3.setText("   Type :");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, -1, 22));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Recipt", "Payment", " " }));
        getContentPane().add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 190, -1));

        jLabel4.setText("Payment Type");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, -1, 22));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", " " }));
        getContentPane().add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 170, 190, -1));

        jLabel5.setText("Supplier Name :");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, -1, -1));

        jLabel6.setText("Payment");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 270, -1, -1));
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 270, 189, -1));

        jLabel7.setText("Narration");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 240, -1, -1));

        jComboBox3Narration.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Being Cash Deposited....", "Being Cash Paid...." }));
        getContentPane().add(jComboBox3Narration, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 240, 189, -1));

        jLabel8.setText("Other Info. : ");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 310, -1, -1));

        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        jScrollPane1.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 310, 190, 90));

        btnSaveCashBookData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/save.png"))); // NOI18N
        btnSaveCashBookData.setText("Save");
        btnSaveCashBookData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveCashBookDataActionPerformed(evt);
            }
        });
        getContentPane().add(btnSaveCashBookData, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 420, -1, -1));

        jTable1getData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL NO.", "Type ", "Payment Method", "Supplier Name", "Narration", "Payment", "Other Info"
            }
        ));
        jScrollPane2.setViewportView(jTable1getData);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 100, 940, -1));

        jButton4.setText("Modify");
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 540, -1, -1));

        jButton5.setText("Delete");
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 540, -1, -1));

        jLabel9.setText("Total Amount");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 540, 80, 20));
        getContentPane().add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 540, 150, -1));

        jLabel10.setText("Supplier Name");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 540, 90, 20));
        getContentPane().add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 540, 160, -1));

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search Button.png"))); // NOI18N
        jButton6.setText("Search");
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 537, 90, 30));

        jLabel11.setText("Narration");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 570, 50, 20));
        getContentPane().add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 570, 170, -1));

        btnSaveNarration.setText("Save");
        btnSaveNarration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveNarrationActionPerformed(evt);
            }
        });
        getContentPane().add(btnSaveNarration, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 610, -1, -1));

        jButton7.setText("Print");
        getContentPane().add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 660, -1, -1));

        jButton8.setText("Excel");
        getContentPane().add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 620, -1, -1));

        jButton9.setText("PDF");
        getContentPane().add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 620, -1, -1));

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/close.png"))); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1310, 10, -1, -1));

        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jComboBox4SupplierName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Supplier"  }));
        getContentPane().add(jComboBox4SupplierName, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 190, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        setVisible(false);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void btnSaveCashBookDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveCashBookDataActionPerformed
         saveCashBook();
    }//GEN-LAST:event_btnSaveCashBookDataActionPerformed

    private void btnSaveNarrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveNarrationActionPerformed
        saveNarration() ;
    }//GEN-LAST:event_btnSaveNarrationActionPerformed

    
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
            java.util.logging.Logger.getLogger(CashBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CashBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CashBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CashBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CashBook().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSaveCashBookData;
    private javax.swing.JButton btnSaveNarration;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
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
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
