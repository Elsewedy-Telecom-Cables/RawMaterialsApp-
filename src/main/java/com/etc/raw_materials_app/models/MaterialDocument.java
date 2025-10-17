package com.etc.raw_materials_app.models;

public class MaterialDocument {
    private int materialDocumentId;
    private String materialDocumentName;
    private Material material;

    public MaterialDocument() {
    }

    public int getMaterialDocumentId() {
        return materialDocumentId;
    }

    public void setMaterialDocumentId(int materialDocumentId) {
        this.materialDocumentId = materialDocumentId;
    }

    public String getMaterialDocumentName() {
        return materialDocumentName;
    }

    public void setMaterialDocumentName(String materialDocumentName) {
        this.materialDocumentName = materialDocumentName;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return materialDocumentName + " (" + (material != null ? material.getMaterialName() : "") + ")";
    }
}
