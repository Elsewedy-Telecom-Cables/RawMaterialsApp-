package com.etc.raw_materials_app.models;

import java.time.LocalDateTime;

public class MaterialTest {
    private int materialTestId;
    private int sectionId;
    private String sectionName;
    private int supplierId;
    private String supplierName;
    private String supplierCode;
    private int countryId;
    private String countryName;
    private int materialId;
    private String materialName;
    private int userId;
    private String userFullName;
    private String poNo;
    private String receipt;
    private String totalQuantity;
    private String quantityAccepted;
    private String quantityRejected;
    private LocalDateTime creationDate;
    private String oracleSample;
    private String itemCode;
    private String spqr;
    private String notes;
    private String comment;

    public MaterialTest() {
    }


    public int getMaterialTestId() {
        return materialTestId;
    }

    public void setMaterialTestId(int materialTestId) {
        this.materialTestId = materialTestId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

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
    public String getSupplierCode() {
        return supplierCode;
    }
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getQuantityAccepted() {
        return quantityAccepted;
    }

    public void setQuantityAccepted(String quantityAccepted) {
        this.quantityAccepted = quantityAccepted;
    }

    public String getQuantityRejected() {
        return quantityRejected;
    }

    public void setQuantityRejected(String quantityRejected) {
        this.quantityRejected = quantityRejected;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getOracleSample() {
        return oracleSample;
    }

    public void setOracleSample(String oracleSample) {
        this.oracleSample = oracleSample;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getSpqr() {
        return spqr;
    }

    public void setSpqr(String spqr) {
        this.spqr = spqr;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "MaterialTest{" +
                "materialTestId=" + materialTestId +
                ", sectionName='" + sectionName + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", countryName='" + countryName + '\'' +
                ", materialName='" + materialName + '\'' +
                ", userFullName='" + userFullName + '\'' +
                '}';
    }
}