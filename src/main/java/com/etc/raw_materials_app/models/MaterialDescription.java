package com.etc.raw_materials_app.models;

public class MaterialDescription {
    private int materialDescriptionId;
    private String materialDescriptionName;
    private Material material;

    public MaterialDescription() {
    }

    public int getMaterialDescriptionId() {
        return materialDescriptionId;
    }

    public void setMaterialDescriptionId(int materialDescriptionId) {
        this.materialDescriptionId = materialDescriptionId;
    }

    public String getMaterialDescriptionName() {
        return materialDescriptionName;
    }

    public void setMaterialDescriptionName(String materialDescriptionName) {
        this.materialDescriptionName = materialDescriptionName;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return materialDescriptionName + " (" + (material != null ? material.getMaterialName() : "") + ")";
    }
}
