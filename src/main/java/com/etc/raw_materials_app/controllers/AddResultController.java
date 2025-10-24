package com.etc.raw_materials_app.controllers;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddResultController implements Initializable {
    private int materialTestId;
    private String supplierName;
    private String materialName;
    private String materialDesName;
    private String poNo;
    private String oracleSample;
    private String itemCode;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        //   System.out.println("setStage called with stage: " + (stage != null ? stage.toString() : "null"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initData(int materialTestId, String supplierName, String materialName,
                         String materialDesName, String poNo, String oracleSample, String itemCode) {

    }
}
