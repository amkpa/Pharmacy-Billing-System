/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Testing;

import java.util.List;

/**
 *
 * @author ESCJAVA04202228
 */
public class SupplierData {
     private int totalCount;
    private List<String[]> supplierList;

    public SupplierData(int totalCount, List<String[]> supplierList) {
        this.totalCount = totalCount;
        this.supplierList = supplierList;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<String[]> getSupplierList() {
        return supplierList;
    }
}
