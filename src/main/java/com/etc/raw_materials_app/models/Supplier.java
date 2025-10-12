package com.etc.raw_materials_app.models;

public class Supplier {
    private int supplierId;
    private String supplierName;
    private String supplierCode;

    // Constructors
    public Supplier() {
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    // Getters and Setters
    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    // toString for ComboBox or display
    @Override
    public String toString() {
        return supplierName;
    }
}

