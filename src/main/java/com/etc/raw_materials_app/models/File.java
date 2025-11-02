package com.etc.raw_materials_app.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class File {

    private int fileId;
    private int materialDocumentId ;
    private Integer userId;
    private LocalDateTime creationDate;
    private String filePath;
    private String comment;
     // Other Tables
    private String  materialDocumentName ;
    private String  materialName;
    private String  supplierName;
    private String userFullName ;
    private MaterialTest materialTest ;

    public File() {
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getMaterialDocumentId() {
        return materialDocumentId;
    }

    public void setMaterialDocumentId(int materialDocumentId) {
        this.materialDocumentId = materialDocumentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMaterialDocumentName() {
        return materialDocumentName;
    }

    public void setMaterialDocumentName(String materialDocumentName) {
        this.materialDocumentName = materialDocumentName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public MaterialTest getMaterialTest() {
        return materialTest;
    }

    public void setMaterialTest(MaterialTest materialTest) {
        this.materialTest = materialTest;
    }
}