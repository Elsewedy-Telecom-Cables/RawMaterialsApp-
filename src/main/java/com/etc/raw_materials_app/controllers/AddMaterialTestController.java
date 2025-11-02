
package com.etc.raw_materials_app.controllers;

import com.etc.raw_materials_app.dao.*;
import com.etc.raw_materials_app.models.*;
import com.etc.raw_materials_app.services.WindowUtils;
import com.etc.raw_materials_app.models.UserContext;
import com.etc.raw_materials_app.logging.Logging;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddMaterialTestController implements Initializable {

    @FXML private TextField accepted_quantity_textF;
    @FXML private Button clear_btn;
    @FXML private TextField comment_textF;
    @FXML private Label header_lbl;
    @FXML private ComboBox<Section> section_comb;
    @FXML private ComboBox<Supplier> supplier_comb;
    @FXML private ComboBox<Country> country_comb;
    @FXML private ComboBox<Material> material_comb;
    @FXML private ComboBox<MaterialDescription> material_description_comb;
    @FXML private TextField notes_textF;
    @FXML private TextField oracle_sample_textF;
    @FXML private TextField po_no_textF;
    @FXML private TextField receipt_textF;
    @FXML private TextField rejected_quantity_textF;
    @FXML private Button save_btn;
    @FXML private TextField spqr_textF;
    @FXML private TextField total_quantity_textF;

    private Stage stage;
    private int CURRENT_MATERIAL_TEST_ID = 0;
    private boolean update = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> header_lbl.requestFocus());

        clear_btn.setCursor(Cursor.HAND);
        save_btn.setCursor(Cursor.HAND);


        section_comb.setItems(SectionDao.getAllSections());
        material_comb.setItems(MaterialDao.getAllMaterials());
        supplier_comb.setItems(SupplierDao.getAllSuppliers());
        country_comb.setItems(FXCollections.observableArrayList());
        material_description_comb.setItems(MaterialDescriptionDao.getAllMaterialDescriptions());


        supplier_comb.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateSupplierCountries(newVal);
        });
    }


    private void updateSupplierCountries(Supplier supplier) {
        ObservableList<Country> filteredCountries = FXCollections.observableArrayList();
        if (supplier != null) {
            ObservableList<SupplierCountry> list = SupplierCountryDao.getAllSupplierCountries();
            for (SupplierCountry sc : list) {
                if (sc.getSupplierId() == supplier.getSupplierId()) {
                    Country c = new Country();
                    c.setCountryId(sc.getCountryId());
                    c.setCountryName(sc.getCountryName());
                    filteredCountries.add(c);
                }
            }
        }
        country_comb.setItems(filteredCountries);
        country_comb.getSelectionModel().clearSelection();
        if (!filteredCountries.isEmpty()) {
            country_comb.getSelectionModel().selectFirst();
        }
    }


    @FXML
    void clear(ActionEvent event) {
        clearHelp();
    }

    private void clearHelp() {
        po_no_textF.clear();
        receipt_textF.clear();
        total_quantity_textF.clear();
        accepted_quantity_textF.clear();
        rejected_quantity_textF.clear();
        oracle_sample_textF.clear();
        spqr_textF.clear();
        notes_textF.clear();
        comment_textF.clear();

        section_comb.getSelectionModel().clearSelection();
        supplier_comb.getSelectionModel().clearSelection();
        country_comb.getSelectionModel().clearSelection();
        material_comb.getSelectionModel().clearSelection();
        material_description_comb.getSelectionModel().clearSelection();
    }


    public void setMaterialTestData(int materialTestId, boolean update) {
        try {
            this.update = update;
            CURRENT_MATERIAL_TEST_ID = materialTestId;
            MaterialTest mt = MaterialTestDao.getMaterialTestById(materialTestId);

            assert mt != null;
            po_no_textF.setText(mt.getPoNo());
            receipt_textF.setText(mt.getReceipt());
            total_quantity_textF.setText(mt.getTotalQuantity());
            accepted_quantity_textF.setText(mt.getQuantityAccepted());
            rejected_quantity_textF.setText(mt.getQuantityRejected());
            oracle_sample_textF.setText(mt.getOracleSample());
            spqr_textF.setText(mt.getSpqr());
            notes_textF.setText(mt.getNotes());
            comment_textF.setText(mt.getComment());

            section_comb.getSelectionModel().select(SectionDao.getSectionById(mt.getSectionId()));
            material_comb.getSelectionModel().select(MaterialDao.getMaterialById(mt.getMaterialId()));
            material_description_comb.getSelectionModel().select(MaterialDescriptionDao.getMaterialDescriptionById(mt.getMaterialDescId()));

            Supplier supplier = SupplierDao.getSupplierById(mt.getSupplierId());
            supplier_comb.getSelectionModel().select(supplier);
            updateSupplierCountries(supplier);
            country_comb.getSelectionModel().select(CountryDao.getCountryById(mt.getCountryId()));

            clear_btn.setVisible(false);
        } catch (Exception e) {
            Logging.logException("ERROR", this.getClass().getName(), "setMaterialTestData", e);
            WindowUtils.ALERT("Error", "Exception during loading material test data.", WindowUtils.ALERT_ERROR);
        }
    }

    private void addMaterialTest() {
        try {

            String poNo = po_no_textF.getText();
            String receipt = receipt_textF.getText();
            String totalQty = total_quantity_textF.getText();
            String acceptedQty = accepted_quantity_textF.getText();
            String rejectedQty = rejected_quantity_textF.getText();
            String oracleSample = oracle_sample_textF.getText();
            String spqr = spqr_textF.getText();
            String notes = notes_textF.getText();
            String comment = comment_textF.getText();

            Section section = section_comb.getValue();
            Supplier supplier = supplier_comb.getValue();
            Country country = country_comb.getValue();
            Material material = material_comb.getValue();
            MaterialDescription materialDesc = material_description_comb.getValue();

            List<String> validationErrors = new ArrayList<>();

            if (section == null) validationErrors.add("Select a section.");
            if (supplier == null) validationErrors.add("Select a supplier.");
            if (country == null) validationErrors.add("Select a country.");
            if (material == null) validationErrors.add("Select a material.");
            if (materialDesc == null) validationErrors.add("Select a material description.");

            if (!validationErrors.isEmpty()) {
                WindowUtils.ALERT("Validation Error", String.join("\n", validationErrors), WindowUtils.ALERT_WARNING);
                return;
            }

            MaterialTest mt = new MaterialTest();
            mt.setSectionId(section.getSectionId());
            mt.setSupplierId(supplier.getSupplierId());
            mt.setCountryId(country.getCountryId());
            mt.setMaterialId(material.getMaterialId());
            mt.setMaterialDescId(materialDesc.getMaterialDescriptionId());
            mt.setUserId(UserContext.getCurrentUser().getUserId());
            mt.setCreationDate(LocalDateTime.now());
            mt.setPoNo(poNo);
            mt.setReceipt(receipt);
            mt.setTotalQuantity(totalQty);
            mt.setQuantityAccepted(acceptedQty);
            mt.setQuantityRejected(rejectedQty);
            mt.setOracleSample(oracleSample);
            mt.setSpqr(spqr);
            mt.setNotes(notes);
            mt.setComment(comment);

            boolean success = MaterialTestDao.insertMaterialTest(mt);
            if (success) {
                WindowUtils.ALERT("Success", "Material test saved successfully.", WindowUtils.ALERT_INFORMATION);
                clearHelp();

                // Refresh Table
                ViewMaterialTestsController viewController = ViewMaterialTestsController.getInstance();
                if (viewController != null) {
                    Platform.runLater(() -> {
                        viewController.refreshTable();
                    });
                }

                closeWindow();
            } else {
                WindowUtils.ALERT("Error", "Failed to save material test.", WindowUtils.ALERT_ERROR);
            }
        } catch (Exception e) {
            Logging.logException("ERROR", this.getClass().getName(), "addMaterialTest", e);
            WindowUtils.ALERT("Error", "Exception during saving.", WindowUtils.ALERT_ERROR);
        }
    }

    private boolean updateMaterialTest() {
        try {
            MaterialTest mt = new MaterialTest();
            mt.setMaterialTestId(CURRENT_MATERIAL_TEST_ID);
            mt.setSectionId(section_comb.getValue().getSectionId());
            mt.setSupplierId(supplier_comb.getValue().getSupplierId());
            mt.setCountryId(country_comb.getValue().getCountryId());
            mt.setMaterialId(material_comb.getValue().getMaterialId());
            mt.setMaterialDescId(material_description_comb.getValue().getMaterialDescriptionId());
            mt.setUserId(UserContext.getCurrentUser().getUserId());
            mt.setCreationDate(LocalDateTime.now());

            mt.setPoNo(po_no_textF.getText());
            mt.setReceipt(receipt_textF.getText());
            mt.setTotalQuantity(total_quantity_textF.getText());
            mt.setQuantityAccepted(accepted_quantity_textF.getText());
            mt.setQuantityRejected(rejected_quantity_textF.getText());
            mt.setOracleSample(oracle_sample_textF.getText());
            mt.setSpqr(spqr_textF.getText());
            mt.setNotes(notes_textF.getText());
            mt.setComment(comment_textF.getText());

            boolean success = MaterialTestDao.updateMaterialTest(mt);
            if (success) {
                WindowUtils.ALERT("Success", "Material test updated successfully.", WindowUtils.ALERT_INFORMATION);
                clearHelp();
            } else {
                WindowUtils.ALERT("Error", "Failed to update material test.", WindowUtils.ALERT_ERROR);
            }
            return success;
        } catch (Exception e) {
            Logging.logException("ERROR", this.getClass().getName(), "updateMaterialTest", e);
            WindowUtils.ALERT("Error", "Exception during updating.", WindowUtils.ALERT_ERROR);
            return false;
        }
    }

    @FXML
    void saveMaterialTest(ActionEvent event) {
        try {
            if (!update) {
                addMaterialTest();
            } else {
                boolean success = updateMaterialTest();
                if (success) {
                    closeWindow();
                }
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", this.getClass().getName(), "saveMaterialTest", ex);
            WindowUtils.ALERT("Error", "Exception during saving.", WindowUtils.ALERT_ERROR);
        }
    }

    public void closeWindow() {
        if (stage != null) {
            stage.close();
        }
    }

    public void setSaveButton() {
        save_btn.setText("Update");
    }
}

