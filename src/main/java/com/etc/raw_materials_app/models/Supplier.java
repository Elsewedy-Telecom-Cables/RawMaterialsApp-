package com.etc.raw_materials_app.models;

public class Supplier {
    private int supplierId;
    private String supplierName;

    // Constructors
    public Supplier() {
    }

    public Supplier(String supplierName) {
        this.supplierName = supplierName;
    }

    public Supplier(int supplierId, String supplierName) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
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

