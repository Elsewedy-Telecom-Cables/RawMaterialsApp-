package com.etc.raw_materials_app.models;

public class TestName {
    private int testNameId;
    private String testName;

    public TestName() {
    }

    public int getTestNameId() {
        return testNameId;
    }

    public void setTestNameId(int testNameId) {
        this.testNameId = testNameId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    @Override
    public String toString() {
        return testName ;
    }
}
