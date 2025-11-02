package com.etc.raw_materials_app.models;

public class MaterialDocument {
    private int materialDocumentId;
    private String materialDocumentName;

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

    @Override
    public String toString() {
        return materialDocumentName ;
    }
}
