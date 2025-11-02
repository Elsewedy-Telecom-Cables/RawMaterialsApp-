package com.etc.raw_materials_app.models;

import java.time.LocalDateTime;

public class TestResult {
  private int testResultId;
  private int materialTestId;
  private int testNameId ;
  private int userId ;
  private String requirement;
  private String actual ;
  private LocalDateTime creationDate ;
  private Integer testSituation ;
  // Other Tables
  private MaterialTest materialTest ;
  private String testName ;
  private String userFullName ;

    public TestResult() {
    }

    public int getTestResultId() {
        return testResultId;
    }

    public void setTestResultId(int testResultId) {
        this.testResultId = testResultId;
    }

    public int getMaterialTestId() {
        return materialTestId;
    }

    public void setMaterialTestId(int materialTestId) {
        this.materialTestId = materialTestId;
    }

    public int getTestNameId() {
        return testNameId;
    }

    public void setTestNameId(int testNameId) {
        this.testNameId = testNameId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getTestSituation() {
        return testSituation;
    }

    public void setTestSituation(Integer testSituation) {
        this.testSituation = testSituation;
    }

    public MaterialTest getMaterialTest() {
        return materialTest;
    }

    public void setMaterialTest(MaterialTest materialTest) {
        this.materialTest = materialTest;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }
}
