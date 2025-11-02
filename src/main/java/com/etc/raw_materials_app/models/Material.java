package com.etc.raw_materials_app.models;

public class Material {
    private int materialId;
    private String materialName;
    private String itemCode ;

    public Material() {
    }

    public Material(int materialId, String materialName, String itemCode) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.itemCode = itemCode;
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
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    @Override
    public String toString() {
        return materialName;
    }
}
