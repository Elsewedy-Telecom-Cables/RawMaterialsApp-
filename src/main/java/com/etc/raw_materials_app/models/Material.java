package com.etc.raw_materials_app.models;

public class Material {
    private int materialId;
    private String materialName;

    // Constructors
    public Material() {
    }

    public Material(String materialName) {
        this.materialName = materialName;
    }

    public Material(int materialId, String materialName) {
        this.materialId = materialId;
        this.materialName = materialName;
    }

    // Getters and Setters
    public int getmaterialId() {
        return materialId;
    }

    public void setmaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getmaterialName() {
        return materialName;
    }

    public void setmaterialName(String materialName) {
        this.materialName = materialName;
    }

    @Override
    public String toString() {
        return materialName;
    }
}
