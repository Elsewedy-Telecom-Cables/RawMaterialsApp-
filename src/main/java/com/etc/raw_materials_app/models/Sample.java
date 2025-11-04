package com.etc.raw_materials_app.models;

public class Sample {
    private int sampleId;
    private String sampleName;

    public Sample() {
    }

    public Sample(int sampleId, String sampleName) {
        this.sampleId = sampleId;
        this.sampleName = sampleName;
    }

    public int getSampleId() {
        return sampleId;
    }

    public void setSampleId(int sampleId) {
        this.sampleId = sampleId;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }
    @Override
    public String toString() {
        return sampleName ;
    }
}
